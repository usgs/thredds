package thredds.server.wfs;

import java.io.PrintWriter;
import java.util.List;

/**
 * A writer for a WFS compliant Geospatial XML given a print writer.
 * 
 * @author wchen@usgs.gov
 *
 */
public class WFSDataWriter {
	
	private PrintWriter response;
	private String fileOutput;
	private WFSRequestType requestType;
	private List<WFSRequestType> supportedOperations;
	private List<GeometryType> supportedFTs;
	
	/**
	 * Initiate the response with an XML file with an XML header and the WFS_Capabilities tag.
	 */
	public void startXML() {
		fileOutput += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		fileOutput += "<WFS_Capabilities xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://opengis.net/gml\" xmlns:fes=\"http://www.opengis.net/fes/2.0\" xmlns:ogc=\"http://www.opengis.net/ogc\""
				+ " xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:wfs=\"http://opengis.net/wfs/2.0\" xmlns=\"http://www.opengis.net/wfs/2.0\" version=\"2.0.0\">";
	}
	
	public void setWFSRequestType(WFSRequestType rqt) {
		requestType = rqt;
	}
	
	/**
	 * Finish writing the XML file, write the end tag for WFS_Capabilities and append it all to the PrintWriter.
	 * 
	 * Once a XML is finished, the WFSDataWriter is no longer usable.
	 */
	public void finishXML() {
		fileOutput += "</WFS_Capabilities>";
		this.response.append(fileOutput);
		response = null;
		fileOutput = null;
		requestType = null;
	}
	
	// GetCapabilties specific
	
	/**
	 * Starts writing an operations metadata section/
	 */
	public void startOperationsMetadata(){
		fileOutput += "<ows:OperationsMetadata>";
	}
	
	/**
	 * Given the parameter operation name, add the operation
	 * to the operations metadata section.
	 */
	public void addOperation(WFSRequestType rt) {
		
		fileOutput += "<ows:Operation name=\"" + rt.toString() + "\">";;
	}
	
	/**
	 * Write the ending tag on operations metadata and append it to the PrintWriter
	 */
	public void finishOperationsMetadata(){
		fileOutput += "</ows:OperationsMetadata>";
	}
	
	
	
	
	/**
	 * Opens a WFSDataWriter, writes to the HttpResponse given.
	 * 
	 * @param response to write to
	 */
	public WFSDataWriter(PrintWriter response){
		this.response = response;
		fileOutput = "";
		requestType = null;
	}
}
