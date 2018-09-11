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
        fileOutput += "<wfs:WFS_FeatureType xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://opengis.net/gml\" xmlns:fes=\"http://www.opengis.net/fes/2.0\" xmlns:ogc=\"http://www.opengis.net/ogc\""
                + " xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:wfs=\"http://opengis.net/wfs/2.0\" xmlns=\"http://www.opengis.net/wfs/2.0\" version=\"2.0.0\">";
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
            fileOutput += "<element name =\"" + feat.getName() + "\" type=\"" +feat.getType() + "\">";
        }
    }

    public void finishXML() {
        fileOutput += "</wfs:WFS_FeatureType>";
        this.response.append(fileOutput);
        response = null;
        fileOutput = null;
    }
}
