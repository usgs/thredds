package thredds.server.wfs;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WFSDescribeFeatureTypeWriter {

    private PrintWriter response;
    private String fileOutput;
    private String server;
    private List<WFSFeature> featureList;

    public WFSDescribeFeatureTypeWriter(PrintWriter response) {
        this.response = response;
        this.fileOutput = "";
        this.server = null;
        this.featureList = new ArrayList<WFSFeature>();
    }

    public void startXML() {
        fileOutput += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        fileOutput += "<xsd:schema xmlns:tds=\"" + server + "\" " +
                "xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns=\"http://www.w3.org/2001/XMLSchema\" xmlns:gml=\"http://www.opengis.net/gml\" " +
                "targetNamespace=\"" + server + "\" elementFormDefault=\"qualified\" " +
                "version=\"0.1\">";
        fileOutput += "<xsd:import namespace=\"http://www.opengis.net/gml\" " +
                "schemaLocation=\"http://schemas.opengis.net/gml/2.1.2/feature.xsd\"/>";
        writeFeatures();
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void addFeature(WFSFeature feature) {

        featureList.add(feature);
    }

    public void writeFeatures() {

        for (WFSFeature feat : featureList) {
            fileOutput += "<xsd:complexType name=\"" + feat.getTitle() + "\">";
            fileOutput += "<xsd:complexContent>";
            fileOutput += "<xsd:extension base=\"gml:" + feat.getType() + "\">";
            fileOutput += "<xsd:sequence>";

            for (WFSFeatureAttribute attribute : feat.getAttributes()) {
                fileOutput += "<xsd:element name =\"" + attribute.getName() + "\" type=\"" + attribute.getType() + "\"/>";
            }

            fileOutput += "</xsd:sequence>";
            fileOutput += "</xsd:extension>";
            fileOutput += "</xsd:complexContent>";
            fileOutput += "</xsd:complexType>";
            fileOutput += "<xsd:element name =\"" + feat.getName() + "\" type=\"tds:" + feat.getTitle() + "\"/>";

        }
    }

    public void finishXML() {
        fileOutput += "</xsd:schema>";
        this.response.append(fileOutput);
        response = null;
        fileOutput = null;
    }
}
