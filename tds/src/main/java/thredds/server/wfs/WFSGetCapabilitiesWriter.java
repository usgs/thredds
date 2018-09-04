package thredds.server.wfs;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import thredds.server.config.ThreddsConfig;

/**
 * A writer for a WFS compliant Geospatial XML given a print writer.
 * Specifically writes the XML for WFS GetCapabilities requests.
 * 
 * @author wchen@usgs.gov
 *
 */
public class WFSGetCapabilitiesWriter {
	
	private PrintWriter response;
	private String fileOutput;
	private String server;
	private List<WFSRequestType> operationList;
	private List<WFSFeature> featureList;
	
	/**
	 * Initiate the response with an XML file with an XML header and the WFS_Capabilities tag.
	 */
	public void startXML() {
		fileOutput += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		fileOutput += "<WFS_Capabilities xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://opengis.net/gml\" xmlns:fes=\"http://www.opengis.net/fes/2.0\" xmlns:ogc=\"http://www.opengis.net/ogc\""
				+ " xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:wfs=\"http://opengis.net/wfs/2.0\" xmlns=\"http://www.opengis.net/wfs/2.0\" version=\"2.0.0\">";
		writeServiceInfo();
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
	}
	
	/**
	 * Writes the two service sections
	 */
	private void writeServiceInfo() {
		fileOutput += "<ows:ServiceIdentification> "
				+ "<ows:Title>WFS Server on THREDDS</ows:Title> "
				+ "<ows:Abstract>ncWFS uses the NetCDF Java Library to handle WFS requests</ows:Abstract> "
				+ "<ows:ServiceType codeSpace=\"OGC\">WFS</ows:ServiceType> "
				+ "<ows:ServiceTypeVersion>2.0.0</ows:ServiceTypeVersion> "
				+ "<ows:Fees/> "
				+ "<ows:AccessConstraints/> "
		+ "</ows:ServiceIdentification> ";
		
		fileOutput += "<ows:ServiceProvider> "
				+ "<ows:ProviderName>" + ThreddsConfig.get("serverInformation.hostInstitution.name", "hostInstitution") + "</ows:ProviderName> "
				+ "<ows:ProviderSite xlink:href=\"" + ThreddsConfig.get("serverInformation.hostInstitution.webSite", "") +  "\" xlink:type=\"simple\"/> "
				+ "<ows:ServiceContact/> "
				+ "</ows:ServiceProvider> ";
	}
	
	/**
	 * Given the parameter operation name, add the operation
	 * to the operations metadata section.
	 */
	private void writeOperation(WFSRequestType rt) {
		
		fileOutput += "<ows:Operation name=\"" + rt.toString() + "\"> "
				+ "<ows:DCP> "
				+ "<ows:HTTP> "
				+ "<ows:Get xlink:href=\"" + server + "?\"/> "
				+ "<ows:Post xlink:href=\"" + server + "\"/> "
				+ "</ows:HTTP> "
				+ "</ows:DCP>";
		fileOutput += "</ows:Operation> ";
	}
	
	/**
	 * Writes a constraint OWS element out.
	 * 
	 * @param name of the constraint
	 * @boolean isImplemented or not
	 */
	private void writeAConstraint(String name, boolean isImplemented) {
		
		String defValue;
		
		if(isImplemented) defValue = "TRUE"; else defValue = "FALSE";
		
		fileOutput += "<ows:Constraint name=\"" + name + "\"> "
				+ "<ows:NoValues/> "
				+ "<ows:DefaultValue>" + defValue +"</ows:DefaultValue> "
						+ "</ows:Constraint>";
	}
	
	/**
	 * Given the parameter operation name, add the operation
	 * to the operations metadata section.
	 */
	public void addOperation(WFSRequestType rt) {
		
		this.operationList.add(rt);
	}
	
	/**
	 * Takes all added operations and writes an operations metadata section.
	 */
	public void writeOperations() {
		fileOutput += "<ows:OperationsMetadata> ";
		for(WFSRequestType rt : operationList) {
			writeOperation(rt);
		}
		
		writeAConstraint("ImplementsBasicWFS", true);
		writeAConstraint("ImplementsTransactionalWFS", false);
		writeAConstraint("ImplementsLockingWFS", false);
		writeAConstraint("KVPEncoding", false);
		writeAConstraint("XMLEncoding", true);
		writeAConstraint("SOAPEncoding", false);
		writeAConstraint("ImplementsInheritance", false);
		writeAConstraint("ImplementsRemoteResolve", false);
		writeAConstraint("ImplementsResultPaging", false);
		writeAConstraint("ImplementsStandardJoins", false);
		writeAConstraint("ImplementsSpatialJoins", false);
		writeAConstraint("ImplementsTemporalJoins", false);
		writeAConstraint("ImplementsFeatureVersioning", false);
		writeAConstraint("ManageStoredQueries", false);
		writeAConstraint("PagingIsTransactionSafe", false);
		writeAConstraint("QueryExpressions", false);
		
		fileOutput += "</ows:OperationsMetadata>";
	}
	
	/**
	 * 
	 */
	public void writeFeatureTypes() {
		fileOutput += "<FeatureTypeList> ";
				
			for(WFSFeature wf : featureList) {
				fileOutput +=
						"<FeatureType> "
					+ 	"<Name>" + wf.getName() + "</Name>"
					+ 	"<Title>" + wf.getTitle() + "</Title>"
					+	"</FeatureType> ";
			}
		
		fileOutput += "</FeatureTypeList> ";
	}
	
	/**
	 * Add a feature to the writer's feature list.
	 * 
	 * @param feature to add
	 */
	public void addFeature(WFSFeature feature) {
		this.featureList.add(feature);
	}
	
	/**
	 * Opens a WFSDataWriter, writes to the HttpResponse given.
	 * 
	 * @param response to write to
	 */
	public WFSGetCapabilitiesWriter(PrintWriter response){
		this.response = response;
		this.fileOutput = "";
		this.server = null;
		this.operationList = new ArrayList<WFSRequestType>();
		this.featureList = new ArrayList<WFSFeature>();
	}

	public void setServer(String server) {
		this.server = server;
	}
}
