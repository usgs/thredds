package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.ma2.Array;

/**
 * An interface to interact with
 * CF 1.8 Compliant Simple Geometries,
 * and an Enum for the different
 * geometries
 *
 * @author Katie
 *
 */


public interface SimpleGeometry {

    public void setData(Array data);
    public Array getData();




}
