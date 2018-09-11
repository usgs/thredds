package thredds.server.wfs;

public class WFSFeatureAttribute {

    private final String name;
    private final String type;

    public String getName() {
        return name;
    }
    public String getType() { return type; }

    public WFSFeatureAttribute(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
