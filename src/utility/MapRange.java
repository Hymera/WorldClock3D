package utility;

/**
 *	Re-maps a number from one range to another
 *	@author Patrick Kasek
 *	@version 1.0
*	@param value  Number: the incoming value to be converted
 *	@param start1 Number: lower bound of the value's current range
 *	@param stop1  Number: upper bound of the value's current range
 *	@param start2 Number: lower bound of the value's target range
 *	@param stop2  Number: upper bound of the value's target range
 *	@return double: remapped number
 */
public class MapRange {

	public MapRange() {
	}
	
	public static double map(double value, double start1, double stop1, double start2, double stop2) {
		return start2 + ((value - start1) * (stop2 - start2)) / (stop1 - start1);
	}
}