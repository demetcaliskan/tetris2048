import java.awt.Color;
import java.util.Random;

public class Box {
	/**
	 * This is our box class in order to do tetronomial. We used a different approach so each shapes
	 * consist for different box. It has color, number, half length, x and y coordinates data fields.
	 * @author Kaan Koc, Arif Dikkanat, Demet Çalışkan
	 *
	 */

	Color color;
	int number;
	double halfLength;
	double x;
	double y;
	int rotate = 0;
	/**
	 * This is constructor for creating box. Half length, x and y coordinates are necessary in order to create a box.
	 * @param halfLength, double half of the length of a box.
	 * @param x, double x coordinate of the box.
	 * @param y, double y coordinate of the box.
	 */
	Box(double halfLength, double x, double y) {
		number = generateNumber();
		if(number == 2)
			color = Color.white;
		else if(number == 4)
			color = Color.yellow;
		this.halfLength = halfLength;
		this.x = x;
		this.y = y;
	}
	/**
	 * This method generates either 2 or 4 for the box value.
	 * @return integer number of the box.
	 */
	int generateNumber() {
		Random rn = new Random();
		int random = rn.nextInt(1000000);
		if(random % 2 == 0)
			return 2;
		else {
			return 4;
		}
	}

}
