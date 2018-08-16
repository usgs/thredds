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

/**
 * A CF 1.8 compliant Line
 * for use with Simple Geometries. Can also
 * represent Multilines.
 * 
 * @author wchen@usgs.gov
 *
 */
public class CFLine implements Line {

	private List<CFPoint> points;	// a list of the constitutent points of the Line, connected in ascending order as in the CF convention
	private CFLine next;	// if non-null, next refers to the next line part of a multi-line
	private CFLine prev;	// if non-null, prev refers to the previous line part of a multi-line	
	private Array data;		// data associated with the line

	/**
	 * Add a point to the end of the line. 
	 *
	 */
	public void addPoint(double x, double y) {
		CFPoint pt_prev = null;
		
		if(points.size() > 0) {
			pt_prev = points.get(points.size() - 1);
		}
		
		this.points.add(new CFPoint(x, y, pt_prev, null));
	}
	
	/**
	 * Returns the list of points which make up this line
	 * 
	 * @return points - the collection of points that make up this line
	 */
	public List<CFPoint> getPoints() {
		return points;
	}
	
	/**
	 * Get the data associated with this line
	 * 
	 * @return data
	 */
	public Array getData() {
		return data;
	}
	
	/**
	 * If part of a multiline, returns the next line within that line
	 * if it is present.
	 * 
	 * @return next line if present, null if not
	 */
	public CFLine getNext() {
		return next;
	}
	
	/**
	 * If part of a multiline, returns the previous line within that line
	 * if it is present
	 * 
	 * @return previous line if present, null if not
	 */
	public CFLine getPrev() {
		return prev;
	}

	/**
	 * Set the data associated with this Line
	 * 
	 * @param data - array of data to set to
	 */
	public void setData(Array data) {
		this.data = data;
	}
	
	/**
	 * Sets the next line which make up the multiline which this line is a part of.
	 * Automatically connects the other line to this line as well.
	 */
	public void setNext(CFLine next) {
		this.next = next;
		
		if(next != null) {
			next.setPrevOnce(this);
		}
	}
	
	private void setNextOnce(CFLine next) {
		this.next = next;
	}
	

	/**
	 * Sets the previous line which makes up the multiline which this line is a part of.
	 * Automatically connect the other line to this line as well.
	 */
	public void setPrev(CFLine prev) {
		this.prev = prev;
		
		if(prev != null) {
			prev.setNextOnce(this);
		}
	}
	
	private void setPrevOnce(CFLine prev) {
		this.prev = prev;
	}
	
	/**
	 *  Constructs an "empty" line with no members using an ArrayList to implement the point list.
	 * 
	 */
	public CFLine() {
		this.points = new ArrayList<CFPoint>();
		this.next = null;
		this.prev = null;
		this.data = null;
	}
	
	/**
	 * From a given list of points, construct a line
	 * 
	 * @param new_pt The list of points which will constitute the new line
	 */
	public CFLine(List<CFPoint> new_pt) {
		this.points = new_pt;
		this.next = null;
		this.data = null;
	}
	
	/**
	 * Given a dataset, variable, and index, automatically populates this Line and
	 * returns it.
	 * 
	 * @param dataset which the variable is a part of
	 * @param var the variable which has a geometry attribute
	 * @param index of the line within the variable
	 * @return return a line
	 */
	public Line setupLine(NetcdfDataset dataset, Variable var, int index)
	{
		this.points = new ArrayList<CFPoint>();
		Array xPts = null;
		Array yPts = null;
		Variable node_counts;

		List<CoordinateAxis> axes = dataset.getCoordinateAxes();
		CoordinateAxis x = null; CoordinateAxis y = null;
		
		// Look for x and y
		
		for(CoordinateAxis ax : axes){
			
			if(ax.getAxisType() == AxisType.GeoX) x = ax;
			if(ax.getAxisType() == AxisType.GeoY) y = ax;
		}
		
		String node_c_str = var.findAttValueIgnoreCase(CF.NODE_COUNT, "");
		
		if(!node_c_str.equals("")) {
			node_counts = dataset.findVariable(node_c_str);
		}
		
		else return null;
		
		SimpleGeometryKitten kitty = new SimpleGeometryKitten(node_counts);
		
		try {
			xPts = x.read( kitty.getBeginning(index) + ":" + kitty.getEnd(index) ).reduce();
			yPts = y.read( kitty.getBeginning(index) + ":" + kitty.getEnd(index) ).reduce();
		
		} catch (IOException e) {

				return null;
			
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IndexIterator itr_x = xPts.getIndexIterator();
		IndexIterator itr_y = yPts.getIndexIterator();
		
		// x and y should have the same shape, will add some handling on this
		while(itr_x.hasNext()) {
			this.addPoint(itr_x.getDoubleNext(), itr_y.getDoubleNext());
		}
				
				
		// Now set the Data
		try {
				this.setData(var.read(":," + index).reduce());
					
		} catch (IOException | InvalidRangeException e) {

			return null;
					
		}
		
		return this;
	}
}
