package ucar.nc2.ft2.coverage.simpgeometry;

import ucar.nc2.Variable;
import ucar.nc2.constants.CF;

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
	
	public int getBeing(int index) {
		
	}
	
	public int getEnd(int index) {
		
	}
	
	/**
	 * Call up a new Kitten, the Kitten must be given a Variable though.
	 * 
	 * @param variable Variable to give it
	 */
	public SimpleGeometryKitten(Variable variable) {
		cat_toy = variable;
		cat_toy.findAttribute(CF.GEOMETRY);
	}
}
