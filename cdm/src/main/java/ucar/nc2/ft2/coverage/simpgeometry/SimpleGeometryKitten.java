package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.nc2.Variable;
import ucar.nc2.constants.CF;
import ucar.nc2.ft2.coverage.simpgeometry.*;

/**
 * A cat (the animal) trained in finding Simple Geometry.
 * 
 * A Simple Geometry Kitten can go find the beginning and end indicies of
 * a simple geometry X and Y within a variable. But first the Kitten needs a Variable to find it in.
 * The kitten remembers previous adventures so if the kitten is tasked to find a Simple Geometry close to one before it
 * will find it faster.
 * 
 * @author wchen
 *
 */
public class SimpleGeometryKitten {
	
	Variable cat_toy;
	Variable node_count;
	Variable part_node_count;
	String geometry_type;
	int past_index;
	int previous_end, new_end;
	int previous_begin, new_being;
	
	
	public int getBeginning(int index) {
		
		//Test if the last end is the new beginning
		if(index == (past_index + 1 ))
		{
			return previous_end + 1;
		}
		
		int new_beginning = -1;
		
		// Otherwise, find it!
		
		past_index = index;
		return new_beginning;
	}
	
	public int getEnd(int index) {

		// Test if the last beginning is the new end
		if(index == (past_index - 1))
		{
			return previous_begin - 1;
		}
		
		// Otherwise find it
		
		return new_end;
	}
	
	/**
	 * Call up a new Kitten, the Kitten must be given a Variable though.
	 * 
	 * @param variable Variable to give it
	 */
	public SimpleGeometryKitten(Variable variable) {
		cat_toy = variable;
		
		if(variable.findAttribute(CF.GEOMETRY_TYPE) != null)
		{
			geometry_type = variable.findAttribute(CF.GEOMETRY_TYPE).getStringValue();
		}
		
		past_index = -3;
		previous_end = -1;
	}
}
