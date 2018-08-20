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
	
	public Point setupPoint(NetcdfDataset set, Variable vari, int index)
	{
		// Points are much simpler, node_count is used multigeometries so it's a bit different
		// No need for the cat here, unless there is a multipoint
		Array xPts = null;
		Array yPts = null;
		Integer ind = (int)index;
		Variable node_counts = null;
		boolean multi = false;
		SimpleGeometryIndexFinder indexFinder = null;

		List<CoordinateAxis> axes = set.getCoordinateAxes();
		CoordinateAxis x = null; CoordinateAxis y = null;
		
		String[] node_coords = vari.findAttributeIgnoreCase(CF.NODE_COORDINATES).getStringValue().split(" ");
		
		// Look for x and y
		
		for(CoordinateAxis ax : axes){
			
			if(ax.getFullName().equals(node_coords[0])) x = ax;
			if(ax.getFullName().equals(node_coords[1])) y = ax;
		}
		
		// Node count is used very differently in points
		// Similar use to part_node_count in other geometries
		String node_c_str = vari.findAttValueIgnoreCase(CF.NODE_COUNT, "");
		
		if(!node_c_str.equals("")) {
			node_counts = set.findVariable(node_c_str);
			indexFinder = new SimpleGeometryIndexFinder(node_counts);
			multi = true;
		}
		
		try {
			
			//
			if(multi)
			{
				xPts = x.read( indexFinder.getBeginning(index) + ":" + indexFinder.getEnd(index) ).reduce();
				yPts = y.read( indexFinder.getBeginning(index) + ":" + indexFinder.getEnd(index) ).reduce();
			}
			
			else
			{
				xPts = x.read( ind.toString() ).reduce();
				yPts = y.read( ind.toString() ).reduce();
				this.x = xPts.getDouble(0);
				this.y = yPts.getDouble(0);
			}
		
			// Set points
			if(!multi) {
				this.x = xPts.getDouble(0);
				this.y = yPts.getDouble(0);
				this.data = vari.read(":," + index).reduce();
			}
		
			else {
				IndexIterator itr_x = xPts.getIndexIterator();
				IndexIterator itr_y = yPts.getIndexIterator();
				this.next = null;
				this.prev = null;
				
				CFPoint point = this;
		
				// x and y should have the same shape (size), will add some handling on this
				while(itr_x.hasNext()) {
					point.x = itr_x.getDoubleNext();
					point.y = itr_y.getDoubleNext();
					point.data = vari.read(":," + index).reduce();
					point.next = new CFPoint(-1, -1, point, null, null); // -1 is a default value, it gets assigned eventually
					point = point.getNext();
				}
				
				// Clean up the last point since it will be invalid
				point = point.getPrev();
				point.next = null;
			}
		
		} catch (IOException e) {

			return null;
		
		} catch (InvalidRangeException e) {
			
			e.printStackTrace();
		}
		
		return this;
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
