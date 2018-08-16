package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * A class which given a dataset, will read from the dataset one of the simple geometry types. Those being polygon, line, or point.
 * 
 * @author wchen@usgs.gov
 *
 */
public class SimpleGeometryReader {
	
	NetcdfDataset ds;
	String conv;
	
	/**
	 * Returns a Polygon given a variable name and the geometric index. If the Polygon is not found it will return null. If the Polygon is a part of the Multi-Polygon, it will return the head
	 * (the first Polygon in the series which constitutes the Multi-Polygon).
	 * 
	 * @param name of the variable which holds the Polygon
	 * @param index of the Polygon within the variable
	 * @return a new polygon with all associated information
	 */
	public Polygon readPolygon(String name, int index) {
		
		Variable polyvar = ds.findVariable(name);
		if(polyvar == null) return null;
		
		// create a blank cf Polygon
		Polygon poly = new CFPolygon();
		
		// Check for convention to see which convention to use, later
		return poly.setupPolygon(ds, polyvar, index);
	}
	
	/**
	 * Returns a Line given a variable name and the geometric index. If the Line is not found it will return null. If the Line is a part of the Multi-Line, it will return the head
	 * (the first Line in the series which constitutes the Multi-Line).
	 * 
	 * @param name of the variable which holds the Line
	 * @param index of the Line within the variable
	 * @return a new line with all associated information
	 */
	public Line readLine(String name, int index) {
	
		Variable linevar = ds.findVariable(name);
		if(linevar == null) return null;
		
		return null;
	}
	
	/**
	 * Returns a Point given a variable name and the geometric index. If the Point is not found it will return null. If the Point is a part of the Multi-Point, it will return the head
	 * (the first Point in the series which constitutes the Multi-Point).
	 * 
	 * @param name of the variable which holds the Point
	 * @param index of the Point within the variable
	 * @return a new Point with all associated information
	 */
	public Point readPoint(String name, int index) {

		Variable pointvar = ds.findVariable(name);
		if(pointvar == null) return null;
		
		return null;
	}
	
	/**
	 * Constructs a new Simple Geometry Reader over the specified dataset.
	 * Assumes the CF Convention.
	 * 
	 * @param ds - the specified dataset
	 */
	public SimpleGeometryReader(NetcdfDataset ds) {
		this.ds = ds;
	}
	
	/**
	 * Constructs a new Simple Geometry Reader over the specified dataset with a specified convention.
	 * 
	 * @param ds - the specified dataset
	 * @param convention - the convention the reader should follow
	 */
	public SimpleGeometryReader(NetcdfDataset ds, String convention) {
		this.ds = ds;
		this.conv = convention;
	}
}