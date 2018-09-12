package thredds.server.wfs;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * A writer for a WFS compliant Feature Collection GML file.
 * Answers to GetFeature requests.
 * 
 * @author wchen@usgs.gov
 *
 */
public class WFSGetFeatureWriter {
	
	private PrintWriter response;
	private String fileOutput;
	
	/**
	 * Initiate the response with an XML file with an XML header and the FeatureCollection tag.
	 */
	public void startXML() {
		fileOutput += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		fileOutput += "<wfs:FeatureCollection xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://opengis.net/gml\" xmlns:fes=\"http://www.opengis.net/fes/2.0\" xmlns:ogc=\"http://www.opengis.net/ogc\""
				+ " xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:wfs=\"http://opengis.net/wfs/2.0\" xmlns=\"http://www.opengis.net/wfs/2.0\" version=\"2.0.0\">";
	}
	
	/**
	 * Finish writing the XML file, write the end tag for FeatureCollection and append it all to the PrintWriter.
	 * 
	 * Once a XML is finished, the WFSDataWriter is no longer usable.
	 */
	public void finishXML() {
		fileOutput += "</wfs:FeatureCollection>";
		response.append(fileOutput);
		fileOutput = null; response = null;
	}
	
	/**
	 * Opens a WFSGetFeatureWriter, writes to the response given.
	 * 
	 * @param response to write to
	 * @throws IOException 
	 */
	public WFSGetFeatureWriter(PrintWriter response) {
		this.fileOutput = "";
		this.response = response;
	}
}
