package thredds.server.wfs;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class WFSExceptionWriter {
	private final String text;
	private final String ExceptionCode;
	private final String locator;
	private final HttpServletResponse hsr;
	
	/**
	 * Given the information on construction, writes the necessary exception information.
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException{
		PrintWriter xmlResponse = hsr.getWriter();
		
		xmlResponse.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlResponse.append("<ows:ExceptionReport xml:lang=\"en-US\" xsi:schemaLocation=\"http://www.opengis.net/ows/1.1"
				+ " http://schemas.opengis.net/ows/1.1.0/owsExceptionReport.xsd\" version=\"2.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		xmlResponse.append("<ows:Exception locator=\"" + locator + "\" exceptionCode=\"" + ExceptionCode + "\">");
		xmlResponse.append("<ows:ExceptionText>" + text + "</ows:ExceptionText>");
		xmlResponse.append("</ows:Exception>");
		xmlResponse.append("</ows:ExceptionReport>");
	}
	
	/**
	 * Creates a new OWS Exception Report based on a text and an exception code.
	 * 
	 * @param text the text associated with the exception
	 * @param locator the locator associated with the exception
	 * @param ExceptionCode the standardized exception code
	 * @param hsr http response to write to
	 */
	public WFSExceptionWriter(String text, String locator, String ExceptionCode, HttpServletResponse hsr) {
		this.text = text;
		this.locator = locator;
		this.ExceptionCode = ExceptionCode;
		this.hsr = hsr;
	}
}
