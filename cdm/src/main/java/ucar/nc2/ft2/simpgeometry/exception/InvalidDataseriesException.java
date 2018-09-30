package ucar.nc2.ft2.simpgeometry.exception;

/**
 * An exception thrown for invalid dataseries in CF-SimpleGeometry Features
 * 
 * @author wchen@usgs.gov
 *
 */
public class InvalidDataseriesException extends Exception {
	
	// Predefined Error Messages
	public static final String RANK_MISMATCH = "Error: Only dataseries of rank 1 or 2 are allowed for simple geometry.";
	
	// Error associated with this exception
	public String error;
	
	/**
	 * Construct a new instance of this exception
	 * 
	 * @param error associated error message.
	 */
	public InvalidDataseriesException(String error) {
		this.error = error;
	}
	
	/**
	 * Returns the error message associated with the exception.
	 */
	public String toString() {
		return this.error;
	}
}
