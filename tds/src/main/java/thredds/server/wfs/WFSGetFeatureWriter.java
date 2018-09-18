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
	 * Initiate the response with an XML file with an XML header and the FeatureCollection tag. Write bounding box information.
	 */
	public void startXML() {
		fileOutput += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		fileOutput += "<wfs:FeatureCollection xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://opengis.net/gml/3.2\" xmlns:fes=\"http://www.opengis.net/fes/2.0\" xmlns:ogc=\"http://www.opengis.net/ogc\""
				+ " xmlns:wfs=\"http://opengis.net/wfs/2.0\" xmlns:tds=\"http://localhost:8080/thredds/wfs/results/tds\" xmlns=\"http://www.opengis.net/wfs/2.0\" version=\"2.0.0\" numberMatched=\"1\" numberReturned=\"1\">"
		
		   // WFS Bounding Box
			+ "<wfs:boundedBy>"
			+ "<wfs:Envelope srsName=" + "\"urn:ogc:def:crs:EPSG::4326\"" + ">"
					+ "<wfs:lowerCorner>" + "-180 -90" + "</wfs:lowerCorner>"
					+ "<wfs:upperCorner>" + "180 90" + "</wfs:upperCorner>"
			+ "</wfs:Envelope>"
			+ "</wfs:boundedBy>";
	}
	
	public void writeMembers() {
		//for(SimpleGeometry geometry : geometryList) {
			fileOutput
				   += "<wfs:member>" 
					
				   // GML Bounding Box
					+ "<gml:boundedBy>"
					+ "<gml:Envelope srsName=" + "\"urn:ogc:def:crs:EPSG::4326\"" + ">"
							+ "<gml:lowerCorner>" + "-180 -90" + "</gml:lowerCorner>"
							+ "<gml:upperCorner>" + "180 90" + "</gml:upperCorner>"
					+ "</gml:Envelope>"
					+ "</gml:boundedBy>"
					
					// Write Geometry Information
					+ "<tds:hru_soil_moist gml:id=\"hru_soil_moist.1\">"
					+ "<tds:catchments_geometry_container>"
					+ "<gml:Point srsName=\"http://www.opengis.net/gml/srs/epsg.xml@900913\" srsDimension=\"2\">"
					+ "<gml:pos>50.0 50.0</gml:pos>"
					+ "</gml:Point>"
					
					// Cap off headers
					+ "</tds:catchments_geometry_container>"
					+ "</tds:hru_soil_moist>"
					+ "</wfs:member>";
		//}
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
