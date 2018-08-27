package ucar.nc2.ft2.coverage.simpgeometry;

enum CFGEOMETRY {
    CFPOINT("Point"), CFLINE("Line"), CFPOLYGON("Polygon");

    private String description;

    public String getDescription() {
        return this.description;
    }

    CFGEOMETRY(String description) {
        this.description = description;
    }
}
