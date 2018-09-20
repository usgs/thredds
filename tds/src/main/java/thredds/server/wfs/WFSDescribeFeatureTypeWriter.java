package thredds.server.wfs;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WFSDescribeFeatureTypeWriter {

    private PrintWriter response;
    private String fileOutput;
    private final String server;
    private final String namespace;
    private List<WFSFeature> featureList;

    public WFSDescribeFeatureTypeWriter(PrintWriter response, String server, String namespace) {
        this.response = response;
        this.fileOutput = "";
        this.server = server;
        this.namespace = namespace;
        this.featureList = new ArrayList<WFSFeature>();
    }

    public void startXML() {
        fileOutput += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        fileOutput += "<schema " + "xmlns:" + WFSController.TDSNAMESPACE + "="  + WFSXMLHelper.encQuotes(namespace) + " " +
                "xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns=\"http://www.w3.org/2001/XMLSchema\" xmlns:gml=\"http://www.opengis.net/gml\" " +
                "targetNamespace=\"" + server + "\" elementFormDefault=\"qualified\" " +
                "version=\"0.1\">";
        fileOutput += "<import namespace=\"http://www.opengis.net/gml\" " +
                "schemaLocation=\"http://schemas.opengis.net/gml/2.1.2/feature.xsd\"/>";
        writeFeatures();
    }

    public void addFeature(WFSFeature feature) {

        featureList.add(feature);
    }

    public void writeFeatures() {

        for (WFSFeature feat : featureList) {
            fileOutput += "<element name =\"" + feat.getName() + "\" type=\"" + feat.getTitle() + "\"/>";
            fileOutput += "<complexType name=\"" + feat.getTitle() + "\">";
            fileOutput += "<complexContent>";
            fileOutput += "<extension base=\"gml:" + feat.getType() + "\">";
            fileOutput += "<sequence>";

            for (WFSFeatureAttribute attribute : feat.getAttributes()) {
                fileOutput += "<element name =\"" + attribute.getName() + "\" type=\"" + attribute.getType() + "\"/>";
            }

            fileOutput += "</sequence>";
            fileOutput += "</extension>";
            fileOutput += "</complexContent>";
            fileOutput += "</complexType>";

        }
    }
    
    public void finishXML() {
        fileOutput += "</schema>";
        this.response.append(fileOutput);
        response = null;
        fileOutput = null;
    }
}
