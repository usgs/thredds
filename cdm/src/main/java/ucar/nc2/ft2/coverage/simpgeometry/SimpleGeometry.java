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
enum CFGEOMETRY {
	CFPOINT, CFLINE, CFPOLYGON;
}

public interface SimpleGeometry{
	
	public CFGEOMETRY getGeometryType(); //need to add to CFLINE, POLY, POINT
		
	public Array getData();

}
	