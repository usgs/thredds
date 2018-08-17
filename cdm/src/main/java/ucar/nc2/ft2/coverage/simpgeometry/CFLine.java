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
		
		this.points.add(new CFPoint(x, y, (CFPoint) pt_prev, null));
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
		this.points.clear();
		Array xPts = null;
		Array yPts = null;
		Variable node_counts = null;
		Variable part_node_counts = null;

		List<CoordinateAxis> axes = dataset.getCoordinateAxes();
		CoordinateAxis x = null; CoordinateAxis y = null;
		
		String[] node_coords = var.findAttributeIgnoreCase(CF.NODE_COORDINATES).getStringValue().split(" ");
		
		// Look for x and y
		
		for(CoordinateAxis ax : axes){
			
			if(ax.getFullName().equals(node_coords[0])) x = ax;
			if(ax.getFullName().equals(node_coords[1])) y = ax;
		}
		
		// Affirm node counts
		String node_c_str = var.findAttValueIgnoreCase(CF.NODE_COUNT, "");
		
		if(!node_c_str.equals("")) {
			node_counts = dataset.findVariable(node_c_str);
		}
		
		else return null;
		
		// Affirm part node counts
		String p_node_c_str = var.findAttValueIgnoreCase(CF.PART_NODE_COUNT, "");
		
		if(!p_node_c_str.equals("")) {
			part_node_counts = dataset.findVariable(p_node_c_str);
		}
		
		SimpleGeometryKitten kitty = new SimpleGeometryKitten(node_counts);
		
		//Get beginning and ending indicies for this polygon
		int lower = kitty.getBeginning(index);
		int upper = kitty.getEnd(index);

		
		try {
			
			xPts = x.read( lower + ":" + upper ).reduce();
			yPts = y.read( lower + ":" + upper ).reduce(); 

			IndexIterator itr_x = xPts.getIndexIterator();
			IndexIterator itr_y = yPts.getIndexIterator();
			
			// No multipolygons just read in the whole thing
			if(part_node_counts == null) {
				
				this.next = null;
				this.prev = null;
				
				// x and y should have the same shape, will add some handling on this
				while(itr_x.hasNext()) {
					this.addPoint(itr_x.getDoubleNext(), itr_y.getDoubleNext());
				}
	
				this.setData(var.read(":," + index).reduce());
			}
			
			// If there are multipolygons then take the upper and lower of it and divy it up
			else {
				
				CFLine tail = this;
				Array pnc = part_node_counts.read();
				IndexIterator pnc_itr = pnc.getIndexIterator();
				
				// In part node count search for the right index to begin looking for "part node counts"
				int pnc_ind = 0;
				int pnc_end = 0;
				while(pnc_end < lower)
				{
					pnc_end += pnc_itr.getIntNext();
					pnc_ind++;
				}
				
				// Now the index is found, use part node count and the index to find each part node count of each individual part
				while(lower < upper) {
					
					int smaller = pnc.getInt(pnc_ind);
					
					while(smaller > 0) {
						tail.addPoint(itr_x.getDoubleNext(), itr_y.getDoubleNext());
						smaller--;
					}
					
					// Set data of each
					tail.setData(var.read(":," + index));
					lower += tail.getPoints().size();
					pnc_ind++;
					tail.setNext(new CFLine());
					tail = tail.getNext();
				}
				
				//Clean up
				tail = tail.getPrev();
				if(tail != null) tail.setNext(null);
			}
		}
		
		catch (IOException e) {

			return null;
		
		} catch (InvalidRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;
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
	
}
