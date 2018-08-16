package ucar.nc2.ft2.coverage.simpgeometry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.constants.CF;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ft2.coverage.simpgeometry.Point;

/**
 * A CF 1.8 compliant Point
 * for use with Simple Geometries.
 * Can also represent multipoints.
 * 
 * @author wchen@usgs.gov
 *
 */
public class CFPoint implements Point{

	private double x;	// x coordinate
	private double y;	// y coordinate
	private CFPoint next;	// next element in a multipoint
	private CFPoint prev;	// previous element in a multipoint
	private Array data;	// data of the point
	
	/**
	 * Get the data associated with this point
	 * 
	 * @return data
	 */
	public Array getData() {
		return data;
	}
	
	/**
	 * Return the x coordinate for the point.
	 * 
	 * @return x of the point
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Return the y coordinate for the point
	 * 
	 * @ return y of the point
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Retrieves the next point within a multipoint if any
	 * 
	 * @return next point if it exists, null if not
	 */
	public CFPoint getNext() {
		return next;
	}
	
	/**
	 * Retrieves the previous point within a multipoint if any
	 * 
	 * @return previous point if it exists null if not
	 */
	public CFPoint getPrev() {
		return prev;
	}
	
	
	/**
	 *  Sets the next point in a multipoint
	 * 
	 */
	protected void setNext(CFPoint next) {
		this.next = next;
	}
	
	/**
	 * 
	 *  Set the previous point in a multipoint
	 * 
	 */
	protected void setPrev(CFPoint prev) {
		this.prev = prev;
	}
	
	/**
	 * Construct a new IMMUTABLE point from specified parameters
	 * The construction will automatically connect in related parts of a Multipoint - just specify any constituents
	 * of a multipoint as next or prev.
	 * 
	 * Assumes that data is null.
	 * 
	 * @param x - the x coordinate of the point
	 * @param y - the y coordinate of the point
	 * @param prev - previous point if part of a multipoint
	 * @param next - next point if part of a multipoint
	 */
	public CFPoint(double x, double y, CFPoint prev, CFPoint next) {
		this.next = next;
		this.prev = prev;
		
		// Create links automatically
		if(next != null) {
			next.setPrev(this);
		}
		
		if(prev != null) {
			prev.setNext(this);
		}
		
		this.x = x;
		this.y = y;
	}
	
	public Point setupPoint(NetcdfDataset set, Variable vari, int index)
	{
		// Points are much simpler, node_count is used multigeometries so it's a bit different
		// No need for the cat here
		Array xPts = null;
		Array yPts = null;
		Integer ind = (int)index;

		List<CoordinateAxis> axes = set.getCoordinateAxes();
		CoordinateAxis x = null; CoordinateAxis y = null;
		
		// Look for x and y
		
		for(CoordinateAxis ax : axes){
			
			if(ax.getAxisType() == AxisType.GeoX) x = ax;
			if(ax.getAxisType() == AxisType.GeoY) y = ax;
		}
		
		try {
			xPts = x.read( ind.toString() ).reduce();
			yPts = y.read( ind.toString() ).reduce();
		
		} catch (IOException e) {

				return null;
			
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Now set the Data
		try {
			this.data = vari.read(":," + index).reduce();
			
		} catch (IOException | InvalidRangeException e) {

			return null;
			
		}
		
		// still things to set
		this.x = xPts.getDouble(0);
		this.y = yPts.getDouble(0);
		this.next = null;
		this.prev = null;
		
		return this;
	}
	
	/**
	 * Construct a new IMMUTABLE point from specified parameters
	 * The construction will automatically connect in related parts of a Multipoint - just specify any constituents
	 * of a multipoint as next or prev.
	 * 
	 * @param x - the x coordinate of the point
	 * @param y - the y coordinate of the point
	 * @param prev - previous point if part of a multipoint
	 * @param next - next point if part of a multipoint
	 * @param data - data associated with the point
	 */
	public CFPoint(double x, double y, CFPoint prev, CFPoint next, Array data) {
		this.next = next;
		this.prev = prev;
		
		// Create links automatically
		if(next != null) {
			next.setPrev(this);
		}
		
		if(prev != null) {
			prev.setNext(this);
		}
		
		this.x = x;
		this.y = y;
		this.data = data;
	}
}
