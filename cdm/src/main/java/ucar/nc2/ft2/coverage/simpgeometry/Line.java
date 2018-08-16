package ucar.nc2.ft2.coverage.simpgeometry;

import java.util.List;
import ucar.ma2.Array;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Generic interface for a Simple Geometry line.
 * 
 * @author wchen@usgs.gov
 *
 */
public interface Line extends SimpleGeometry{
	/**
	 * Add a point to the end of the line. 
	 *
	 */
	public void addPoint(double x, double y);
	
	/**
	 * Returns the list of points which make up this line
	 * 
	 * @return points - the collection of points that make up this line
	 */
	public List<CFPoint> getPoints();
	
	/**
	 * Get the data associated with this line
	 * 
	 * @return data
	 */
	public Array getData();
	
	/**
	 * If part of a multiline, returns the next line within that line
	 * if it is present.
	 * 
	 * @return next line if present, null if not
	 */
	public CFLine getNext();
	
	/**
	 * If part of a multiline, returns the previous line within that line
	 * if it is present
	 * 
	 * @return previous line if present, null if not
	 */
	public CFLine getPrev();
	
	/**
	 * Set the data associated with this Line
	 * 
	 * @param data - array of data to set to
	 */
	public void setData(Array data);
	
	/**
	 * Sets the next line which make up the multiline which this line is a part of.
	 * Automatically connects the other line to this line as well.
	 */
	public void setNext(CFLine next);
	

	/**
	 * Sets the previous line which makes up the multiline which this line is a part of.
	 * Automatically connect the other line to this line as well.
	 */
	public void setPrev(CFLine prev);
	
	/**
	 * Given a dataset, construct a line from the variable which holds lines
	 * and the index as given.
	 * 
	 * @param dataset Where the line variable resides
	 * @param variable Which holds polygon information
	 * @param index for Indexing within the polygon variable
	 * 
	 * @return the constructed Line with associated data
	 */
	public Line setupLine(NetcdfDataset dataset, Variable variable, int index);
	
}
