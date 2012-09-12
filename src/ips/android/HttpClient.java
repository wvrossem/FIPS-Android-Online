package ips.android;

import ips.algorithm.PositioningResult;
import ips.algorithm.knn.NNResults;
import ips.data.serialization.Serializer;
import ips.server.DataUploadRequest;
import ips.server.IPSServlet;
import ips.server.PositioningRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * A client for sending positioning requests to the server
 * 
 * @author Wouter Van Rossem
 * 
 */
public class HttpClient {

	private static DefaultHttpClient mClient = new DefaultHttpClient();

	// private static final String PATH =
	// "http://localhost:8080/ipsserver_tomcat/PositioningServlet";

	private static final String PATH = "http://wilma.vub.ac.be:9191/ipsserver_tomcat/";//PositioningServlet";

	private HttpClient() {
		mClient = new DefaultHttpClient();
	}

	/**
	 * 
	 * @param servlet
	 * @param xmlContent
	 * @return
	 */
	private static String sendRequest(IPSServlet servlet, String xmlContent) {
		HttpPost httppost = new HttpPost(PATH + servlet.getServletPath());
		HttpResponse response;

		httppost.setHeader("content-type", "text/xml");

		HttpEntity xmlEntity;

		try {
			xmlEntity = new StringEntity(xmlContent);

			httppost.setEntity(xmlEntity);

			response = mClient.execute(httppost);
			HttpEntity entity = response.getEntity();

			byte buffer[] = new byte[16384];
			InputStream is = entity.getContent();
			int numBytes = is.read(buffer);
			is.close();

			String entityContents = new String(buffer, 0, numBytes);

			return entityContents;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Sends a positioning request to the servlet on the server
	 * 
	 * @param mm
	 *            The measurement of the client
	 * @return A PositioningResult that contains the result of the algorithm
	 */
	public static PositioningResult calculatePosition(PositioningRequest request) {

		/**
		 * Transform the measurement to XML
		 */

		// Outputstream on which the XML will be written to
		OutputStream os = new ByteArrayOutputStream();

		try {
			Serializer.getInstance().serializeToXML(request, os);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String xmlResults = os.toString();

		// Escape the string for http
		// xmlResults = StringEscapeUtils.escapeXml(xmlResults);

		//System.out.println(xmlResults);

		String entityContents = sendRequest(IPSServlet.PositioningServlet, xmlResults);

		System.out.println(entityContents);

		try {
			switch (request.algorithm) {
			case NearestNeighbors:
				NNResults pos = (NNResults) Serializer.getInstance().deserialize(
						NNResults.class, entityContents);
				return pos;
			case BayesPositioning:
				PositioningResult res = (PositioningResult) Serializer
						.getInstance().deserialize(PositioningResult.class,
								entityContents);
				return res;
			}

			return null;
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * @param request
	 */
	public static void uploadData(DataUploadRequest request) {
		/**
		 * Transform the data upload request to XML
		 */
		// Outputstream on which the XML will be written to
		OutputStream os = new ByteArrayOutputStream();

		try {
			Serializer.getInstance().serializeToXML(request, os);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String xmlResults = os.toString();
		
		System.out.println(xmlResults);

		String entityContents = sendRequest(IPSServlet.DataUploadServlet, xmlResults);

		System.out.println(entityContents);
	}
}