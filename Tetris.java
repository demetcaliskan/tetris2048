import java.awt.Color;
import java.awt.Font;
import java.lang.System;
import java.util.Arrays;
import java.util.Random;

/**
 * This program is the combination of Tetris and 2048 games. 
 * @author Kaan Koc, Arif Dikkanat, Demet Çalışkan
 *
 */

/**
 * This is our Tetris class that contains the main method and other 
 * necessary methods that we used in our program. 
 */
public class Tetris {
	/**
	 * main method of the Tetris class 
	 */
	public static void main(String[] args) {
		tetris();
		gameOver();
	}
	/**
	 * the game starts and ends in this method, everything is happening here
	 */
	public static void tetris() {
		drawMap();
		int[][] coordinates = new int[10][10];//tetris map including each coordinate of the shapes(boxes)
		Box[][] shapes = shapes();
		Box[][] randomShapes = randomShapes();
		Random rn = new Random();
		int[] score=new int[1];
		score[0]=0;
		int counter = 0;
		int randomcounter=0;
		int[] random1 = new int[2];
		while(mapChecker(coordinates)==true){
			//In order to show the next shape making necessary loops
			if (randomcounter!=0) {
				for (int j = 0; j < randomShapes[random1[0]].length; j++) {
					deleteBox(randomShapes[random1[1]][j]);
					StdDraw.setPenColor(Color.gray);
					StdDraw.filledSquare(randomShapes[random1[1]][j].x, randomShapes[random1[1]][j].y, 0.25);
				}
			}
			Box[] shape = new Box[4]; //our tetronimo
			if (randomcounter==0) {
				random1[0]=rn.nextInt(7);//now
				random1[1]=rn.nextInt(7);//next
				for (int j = 0; j < randomShapes[random1[0]].length; j++) {
					shape[j] = randomShapes[random1[1]][j];
				}
			}
			if(randomcounter != 0) {
				random1[0]=random1[1];//now
				for (int j = 0; j < randomShapes[random1[0]].length; j++) {
					shape[j] = randomShapes[random1[1]][j];
				}
				random1[1]=rn.nextInt(7);//next
			}
			for (int j = 0; j < shapes[random1[0]].length; j++) {
				shapes[random1[0]][j].number = shape[j].number;
				generateColor(shapes[random1[0]][j], shapes[random1[0]][j].number);
				createBox(shapes[random1[0]][j]);
			}
			for (int j = 0; j < randomShapes[random1[0]].length; j++) {
				createBox(randomShapes[random1[1]][j]);
			}
			double check = findMinY(shapes[random1[0]]);

			while(check-1!=-0.5) {//In order to control the map boundaries for shapes using this while loop
				if(random1[0] != 0) {				
					if(lineChecker2(coordinates, shapes[random1[0]])==true) {
						StdDraw.show(500);
						keyBoardPressed(shapes[random1[0]],random1[0],coordinates);							
					}
				}
				else {				
					if(lineChecker2(coordinates,shapes[random1[0]])==true) {
						if(findMinY(shapes[random1[0]])==check) {
							System.out.println(counter);
							StdDraw.show(500);
							keyBoardPressed(shapes[random1[0]],random1[0],coordinates);							
						}
					}
					else if(findMinY(shapes[random1[0]])!=check) {
						for (double z = findMinY(shapes[random1[0]]); z >= 3.5; z = z - 1.0) {
							if(lineChecker2(coordinates,shapes[random1[0]])==true) {
								StdDraw.show(500);
								keyBoardPressed(shapes[random1[0]],random1[0],coordinates);						
							}
						}
					}
				}
				check--;
			}
			//filling my coordinates at necessary points in our map
			coordinates[(int) (shapes[random1[0]][0].y-0.5)][(int) (shapes[random1[0]][0].x-0.5)]=shapes[random1[0]][0].number;
			coordinates[(int) (shapes[random1[0]][1].y-0.5)][(int) (shapes[random1[0]][1].x-0.5)]=shapes[random1[0]][1].number;
			coordinates[(int) (shapes[random1[0]][2].y-0.5)][(int) (shapes[random1[0]][2].x-0.5)]=shapes[random1[0]][2].number;
			coordinates[(int) (shapes[random1[0]][3].y-0.5)][(int) (shapes[random1[0]][3].x-0.5)]=shapes[random1[0]][3].number;
			merge3(shapes, coordinates, random1[0],score);
			for (int i = 0; i < 10; i++) {
				System.out.println(Arrays.toString(coordinates[i]));
			}
			counter++;
			shapes = shapes();
			randomcounter++;
			StdDraw.show(200);
			while(isFull(coordinates) == true) {
				StdDraw.show(200);
				getDown(coordinates,score);
			}	
		}
	}
	/**
	 * This method just writes Game Over on the screen when the game finishes
	 */
	public static void gameOver() {
		Font font = new Font("Arial", Font.BOLD, 60);
		StdDraw.setFont(font);
		StdDraw.setPenColor(Color.black);
		StdDraw.text(4.0, 5.0, "Game Over");
		StdDraw.show();
	}
	/**
	 * This method deletes the given box by over write it with default map's square.
	 * @param box, the box wanted to be delete
	 */
	public static void deleteBox(Box box) {
		StdDraw.setPenColor(187, 173, 160);
		StdDraw.filledSquare(box.x, box.y, box.halfLength);
		StdDraw.setPenColor(Color.gray);
		StdDraw.square(box.x, box.y, box.halfLength);
	}
	/**
	 * This method finds the minimum X value given a shape array which has a type box.
	 * @param shapes, box array that minimum X value wanted to be found
	 * @return value of the X which is double
	 */
	public static double findMinX(Box[] shapes) {
		double minX = 10.0;
		for (int i = 0; i < shapes.length; i++) {
			if(shapes[i].x < minX)
				minX = shapes[i].x;
		}
		return minX;
	}
	/**
	 * This method finds the maximum X value given a shape array.
	 * 
	 * @param shapes, box array that maximum X value wanted to be found
	 * @return value of the X which is double
	 */
	public static double findMaxX(Box[] shapes) {
		double maxX = 0.0;
		for (int i = 0; i < shapes.length; i++) {
			if(shapes[i].x > maxX) {
				maxX = shapes[i].x;
			}
		}
		return maxX;
	}
	/**
	 * This method shifts the given shape array with changing the x and y values of the boxes and creates them again.
	 * User has four choices first one is the shift left, method checks if it is out of bounds of the map and if there is an another box using coordinates double dimension array
	 * Then shifts to the left or not otherwise. Same case goes for the right and down option. Shift clock wise was the tricky part since all of the shapes are different from each other
	 * I had to right individual case for them also for 90, 180, 270 degrees.
	 * 
	 * @param shapes, shape array that type of box.
	 * @param random, is the integer number represents different shapes from the tetronomial.
	 * @param coordinates, double dimension integer array that represents the coordinates of the array.
	 */
	public static void keyBoardPressed(Box[] shapes, int random, int[][] coordinates) {
		if (StdDraw.isKeyPressed(65) || StdDraw.isKeyPressed(37)) {
			System.out.println("Shift left is pressed!");
			// Implement shift left

			final int x = (int) (findMinX(shapes) - 0.50);
			final int y = (int) (findMaxY(shapes) - 1.50);
			final int y1 = (int) (findMinY(shapes) - 1.50);

			if(x < 0) {
				moveALine(shapes);
			}
			else {

				if(findMinX(shapes) - 1.0 >= 0.50 && coordinates[y][x] == 0 && coordinates[y1][x] == 0 && findMinY(shapes) - 1.0 >= 0.5) {
					for (int i = 0; i < shapes.length; i++) {
						deleteBox(shapes[i]);
						shapes[i].y--;
						shapes[i].x--;
						createBox(shapes[0]);
						createBox(shapes[1]);
						createBox(shapes[2]);
						createBox(shapes[3]);

					}
				}
				else {
					moveALine(shapes);
					System.out.println("It should not move");

				}
			}
		}
		else if(StdDraw.isKeyPressed(68) || StdDraw.isKeyPressed(39)) {
			System.out.println("Shift right is pressed!");
			final int x = (int) (findMaxX(shapes) + 0.50);
			final int x1 = (int) (findMinX(shapes) + 0.50);
			final int y = (int) (findMinY(shapes) - 1.50);
			System.out.println("shift right x is " + x);
			System.out.println("shift right y  is " + y);
			if(findMaxX(shapes) + 1.0 < 8.50 && coordinates[y][x] == 0 && coordinates[y][x1] == 0 && findMinY(shapes) - 1.0 >= 0.5) {
				for (int i = 0; i < shapes.length; i++) {
					deleteBox(shapes[i]);
					shapes[i].y--;
					shapes[i].x++;
					createBox(shapes[0]);
					createBox(shapes[1]);
					createBox(shapes[2]);
					createBox(shapes[3]);
				}
			}
			else {
				moveALine(shapes);
			}		
		}
		else if(StdDraw.isKeyPressed(87) || StdDraw.isKeyPressed(38)) {
			System.out.println("Shift clock wise!");
			if(random == 0) {

				double maxX = findMaxX(shapes);
				double maxY = findMaxY(shapes);

				final int x = (int) (maxX - 2.50);
				final int y = (int) (maxY - 2.50);
				if(x == 0 || y == 0) {
					return;
				}

				if(shapes[0].rotate == 0) {
					if(maxX - 3.0 >= 0.5 && maxY - 3.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							shapes[i].y = shapes[i].y - i;
							shapes[i].x = shapes[i].x - i;
							createBoxes(shapes);


						}
						shapes[0].rotate = 90;

					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 90) {
					if(findMaxX(shapes) - 3.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0)
								shapes[0].x -= 3.0;
							else if(i == 1) {
								shapes[1].x -= 2.0;
								shapes[1].y -= 1.0;
							}
							else if(i == 2) {
								shapes[2].y -= 2.0;
								shapes[2].x -= 1.0;
							}
							else if(i == 3) {
								shapes[3].y -= 3.0;
							}
							createBoxes(shapes);


						}
						shapes[0].rotate = 180;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 180) {
					if(findMinX(shapes) + 3.0 < 8.50 && findMaxY(shapes) - 3.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 1) {
								shapes[1].y--;
								shapes[1].x++;
							}
							else if(i == 2) {
								shapes[2].y -= 2.0;
								shapes[2].x += 2.0;
							}
							else if(i == 3) {
								shapes[3].y -= 3.0;
								shapes[3].x += 3.0;
							}
							createBoxes(shapes);

						}
						shapes[0].rotate = 0;

					}
					else {
						moveALine(shapes);
					}
				}
			}
			else if(random == 1) {
				if(shapes[0].rotate == 0) {
					if(findMaxY(shapes) - 2.0 >= 0.5 && findMaxX(shapes) - 1.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0)
								shapes[0].y++;
							else if(i == 1)
								shapes[1].x--;
							else if(i == 2) {
								shapes[2].y--;
							}
							else if(i == 3) {
								shapes[3].x--;
								shapes[3].y = shapes[3].y - 2;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 90;
					}
				}
				else if(shapes[0].rotate == 90) {
					if(findMinX(shapes) + 2.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].x += 2.0;
							}
							else if(i == 1) {
								shapes[1].x += 1.0;
								shapes[1].y += 1.0;
							}
							else if(i == 3) {
								shapes[3].x -= 1.0;
								shapes[3].y += 1.0;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 180;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 180) {
					if(findMaxY(shapes) - 2.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].x--;
								shapes[0].y -= 2.0;
							}
							else if(i == 1) {
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].x--;
							}
							else if(i == 3) {
								shapes[3].y++;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 0;
					}
					else {
						moveALine(shapes);
					}
				}
			}
			else if(random == 2) {
				if(shapes[0].rotate == 0) {
					if(findMinY(shapes) - 1.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].x++;
							}
							else if(i == 1) {
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].x--;
							}
							else if(i == 3) {
								shapes[3].x -= 2.0;
								shapes[3].y--;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 90;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 90) {
					if(findMaxX(shapes) + 1.0 <= 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].y -= 2.0;
								shapes[0].x++;
							}
							else if(i == 1) {
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].x++;

							}
							else if(i == 3) {
								shapes[3].y++;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 180;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 180) {
					if(findMinY(shapes) - 1.0 >= 0.5 && findMaxX(shapes) + 1.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].y--;
								shapes[0].x -= 2.0;
							}
							else if(i == 1) {
								shapes[1].x--;
							}
							else if(i == 2) {
								shapes[2].y--;
							}
							else if(i == 3) {
								shapes[3].x++;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 0;
					}
					else {
						moveALine(shapes);
					}
				}
			}
			else if(random == 3) {
				if(shapes[0].rotate == 0) {
					if(findMaxY(shapes) - 2.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].x++;
							}
							else if(i == 1) {
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].y -= 2.0;
								shapes[2].x--;
							}
							else if(i == 3) {
								shapes[3].x -= 2.0;
								shapes[3].y--;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 90;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 90) {
					if(findMaxX(shapes) + 1.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].y -= 2.0;
								shapes[0].x++;
							}
							else if(i == 1) {
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].x--;
							}
							else if(i == 3) {
								shapes[3].y++;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 180;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 180) {
					if(findMinY(shapes) - 1.0 >= 0.5 && findMaxX(shapes) + 1.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].y--;
								shapes[0].x -= 2.0;
							}
							else if(i == 1) {
								shapes[1].x--;
							}
							else if(i == 2) {
								shapes[2].y++;
							}
							else if(i == 3) {
								shapes[3].x++;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 0;
					}
					else {
						moveALine(shapes);
					}
				}

			}
			else if(random == 4) {
				if(shapes[0].rotate == 0) {
					if(findMaxX(shapes) + 3.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							System.out.println("Anaın amına koyayım");
							if(i == 0) {
								shapes[0].x += 2.0;
							}
							else if(i == 1) {
								shapes[1].x += 1.0;
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].y -= 2.0;

							}
							else if(i == 3) {
								shapes[3].y++;
								shapes[3].x += 1.0;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 90;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 90) {
					if(findMaxX(shapes) + 2.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].x += 1.0;
								shapes[0].y -= 3.0;
							}
							else if(i == 1) {
								shapes[1].y -= 2.0;

							}
							else if(i == 2) {
								shapes[2].y--;
								shapes[2].x--;
							}
							else if(i == 3)  {
								shapes[3].y -= 2.0;
								shapes[3].x += 2.0;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 180;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 180) {
					if(findMaxX(shapes) + 1.0 < 8.50 && findMinX(shapes) - 1.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].y--;
								shapes[0].x -= 2.0;
							}
							else if(i == 1) {
								shapes[1].y--;
								shapes[1].x -= 2.0;
							}
							else if(i == 2) {
								shapes[2].x--;
							}
							else if(i == 3) {
								shapes[3].x -= 3.0;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 0;
					}
					else {
						moveALine(shapes);
					}
				}
			}
			else if(random == 5) {

				if(shapes[0].rotate == 0) {	
					for (int i = 0; i < shapes.length; i++) {
						deleteBox(shapes[i]);
						if(i == 0)
							shapes[0].x++;
						else if(i == 1)
							shapes[1].y--;
						else if(i == 2)
							shapes[2].y++;
						else if(i == 3)
							shapes[3].x--;

						createBoxes(shapes);

					}
					moveALine(shapes);
					shapes[0].rotate = 90;
				}
				else if(shapes[0].rotate == 90) {
					for (int i = 0; i < shapes.length; i++) {
						deleteBox(shapes[i]);
						if(i == 0)
							shapes[0].y--;
						else if(i == 1)
							shapes[1].x--;
						else if(i == 2)
							shapes[2].x++;
						else if(i == 3)
							shapes[3].y++;

						createBoxes(shapes);

					}
					moveALine(shapes);
					shapes[0].rotate = 180;
				}
				else if(shapes[0].rotate == 180) {
					for (int i = 0; i < shapes.length; i++) {
						deleteBox(shapes[i]);
						if(i == 0)
							shapes[0].x--;
						else if(i == 1)
							shapes[1].y++;
						else if(i == 2)
							shapes[2].y--;
						else if(i == 3)
							shapes[3].x++;

						createBoxes(shapes);
					}
					moveALine(shapes);
					shapes[0].rotate = 0;
				}
			}
			else if(random == 6) {
				if(shapes[0].rotate == 0) {
					if(findMaxY(shapes) - 2.0 >= 0.5) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].x++;
							}
							else if(i == 1) {
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].y -= 2.0;
								shapes[2].x--;
							}
							else if(i == 3) {
								shapes[3].x--;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 90;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 90) {
					if(findMaxY(shapes) - 2.0 >= 0.5 && findMaxX(shapes) + 1.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].y -= 2.0;
								shapes[0].x++;
							}
							else if(i == 1) {
								shapes[1].y--;
							}
							else if(i == 2) {
								shapes[2].x--;
							}
							else if(i == 3) {
								shapes[3].x++;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 180;
					}
					else {
						moveALine(shapes);
					}
				}
				else if(shapes[0].rotate == 180) {
					if(findMaxY(shapes) - 2.0 >= 0.5 && findMaxX(shapes) + 1.0 < 8.50) {
						for (int i = 0; i < shapes.length; i++) {
							deleteBox(shapes[i]);
							if(i == 0) {
								shapes[0].y--;
								shapes[0].x -= 2.0;
							}
							else if(i == 1) {
								shapes[1].x--;
							}
							else if(i == 2) {
								shapes[2].y++;

							}
							else if(i == 3) {
								shapes[3].y--;
							}
							createBoxes(shapes);
						}
						shapes[0].rotate = 0;
					}
					else {
						moveALine(shapes);
					}
				}
			}
		}
		else {
			moveALine(shapes);
		}
	}
	/**
	 * This method creates the shapes using individual boxes. This method implemented this way because it is not creating as same as writing inside the loop.
	 * @param shapes, shape array the type of box.
	 */
	public static void createBoxes(Box[] shapes) {
		createBox(shapes[0]);
		createBox(shapes[1]);
		createBox(shapes[2]);
		createBox(shapes[3]);
	}
	/**
	 * This method moves the given box array named shapes by using create box method.
	 * @param shapes, box array
	 */
	public static void moveALine(Box[] shapes) {
		// The reason i did not use i for creating boxes when i write on the loop it creates new boxes through each iteration and it does not seems smooth as this version on the game.
		for (int i = 0; i < shapes.length; i++) {
			deleteBox(shapes[i]);
			shapes[i].y--;
			createBox(shapes[0]);
			createBox(shapes[1]);
			createBox(shapes[2]);
			createBox(shapes[3]);
		}

	}
	/**
	 * This method creates our tetrominoes.Creating each tetromino inside of a box array and returning a 2D array which includes each tetromino box array.
	 * @return 2D array which includes each tetromino box array.
	 */
	public static Box[][] shapes() {
		Box I[] = {new Box(0.50, 0.50, 9.50), new Box(0.50, 1.50, 9.50), new Box(0.50, 2.50, 9.50), new Box(0.50, 3.50, 9.50)};
		Box S[] = {new Box(0.50, 0.50, 8.50), new Box(0.50, 1.50, 8.50), new Box(0.50, 1.50, 9.50), new Box(0.50, 2.50, 9.50)};
		Box Z[] = {new Box(0.50, 0.50, 9.50), new Box(0.50, 1.50, 9.50), new Box(0.50, 1.50, 8.50), new Box(0.50, 2.50, 8.50)};
		Box J[] = {new Box(0.50, 1.5, 9.5),  new Box(0.50, 2.5, 9.5), new Box(0.50, 3.5, 9.5), new Box(0.50, 3.5, 8.5)};
		Box L[] = {new Box(0.50, 1.5, 9.5), new Box(0.50, 2.5, 9.5), new Box(0.50, 3.5, 9.5), new Box(0.50, 1.5, 8.5)};
		Box O[] = {new Box(0.50, 1.5, 9.5), new Box(0.50, 2.5, 9.5), new Box(0.50, 1.5, 8.5), new Box(0.50, 2.5, 8.5)};
		Box T[] = {new Box(0.50, 0.5, 9.5), new Box(0.50, 1.50, 9.50), new Box(0.50, 2.50, 9.50), new Box(0.50, 1.50, 8.50)};
		Box[][] shapes = {I, S, Z, J, L, O, T};
		return shapes;
	}
	/**
	 * This method does exactly same thing as shapes method.However this method creates the smaller size boxes in order to use this to show users the next step box.
	 * @return 2D array which includes each tetromino box array.
	 */
	public static Box[][] randomShapes() {
		Box I[] = {new Box(0.25, 8.25, 0.75), new Box(0.25, 8.75, 0.75), new Box(0.25, 9.25, 0.75), new Box(0.25, 9.75, 0.75)};
		Box S[] = {new Box(0.25, 8.25, 0.75), new Box(0.25, 8.75, 0.75), new Box(0.25, 8.75, 1.25), new Box(0.25, 9.25, 1.25)};
		Box Z[] = {new Box(0.25, 8.25, 1.25), new Box(0.25, 8.75, 0.75), new Box(0.25, 8.75, 1.25), new Box(0.25, 9.25, 0.75)};
		Box J[] = {new Box(0.25, 8.25, 0.75), new Box(0.25, 8.75, 0.75), new Box(0.25, 9.25, 0.75), new Box(0.25, 9.25, 0.25)};
		Box L[] = {new Box(0.25, 8.75, 0.25), new Box(0.25, 8.75, 0.75), new Box(0.25, 9.25, 0.75), new Box(0.25, 9.75, 0.75)};
		Box O[] = {new Box(0.25, 9.25, 0.75), new Box(0.25, 8.75, 0.75), new Box(0.25, 8.75, 1.25), new Box(0.25, 9.25, 1.25)};
		Box T[] = {new Box(0.25, 8.25, 0.75), new Box(0.25, 8.75, 0.75), new Box(0.25, 9.25, 0.75), new Box(0.25, 8.75, 0.25)};
		Box[][] shapes = {I, S, Z, J, L, O, T};
		return shapes;
	}
	/**
	 * This method draws the map, it creates squares three times first is for background, second is for map grids, third is for score field. I used filled square in order to
	 * look like background.
	 */
	public static void drawMap() {
		StdDraw.setCanvasSize(500, 500);
		double maxScale = 10.0;
		StdDraw.setXscale(0.0, maxScale);
		StdDraw.setYscale(0.0, maxScale);
		StdDraw.setPenColor(187, 173, 160);
		double halfLength = 0.5;
		double length = halfLength * 2;
		double x = halfLength;
		double y = halfLength;
		for(double i = x; i <= 8.50; i = i + length) {
			for(double j = y; j <= maxScale; j = j + length) {
				StdDraw.filledSquare(i, j, halfLength);
			}
		}
		StdDraw.setPenColor(Color.gray);
		for(double i = x; i <= 8.50; i = i + length) {
			for(double j = y; j <= maxScale; j = j + length) {
				StdDraw.square(i, j, halfLength);
			}
		}
		for(double i = 8.50; i <= 10.0; i = i + length) {
			for(double j = y; j <= maxScale; j = j + length) {
				StdDraw.filledSquare(i, j, halfLength);
			}
		}
		Font font = new Font("Arial", Font.BOLD, 30);
		StdDraw.setFont(font);
		StdDraw.setPenColor(Color.white);
		StdDraw.text(9.0, 9.5, "Score");
		Font font2 = new Font("Arial", Font.BOLD, 30);
		StdDraw.setFont(font2);
		StdDraw.setPenColor(Color.white);
		StdDraw.text(9.0, 2.5, "Next");
	}
	/**
	 * This method checks the tetris(coordinates array) map if the map is available for next step returns true else returns false.
	 * @param array The coordinates array which is the tetris map
	 * @return if the map is available for next step returns true else returns false.
	 */
	public static boolean mapChecker(int[][] array) {
		for (int i = 0; i < array[0].length; i++) {
			if(array[9][i]!=0) {
				gameOver();	
				return false;
			}
		}
		return true;
	}
	/**
	 * This method checks the map for our tetrominoes while they are getting down whether they can do the next stop for going down or not.
	 * @param coordinates the tetris map
	 * @param box our tetromino
	 * @return if returns true then it means tetromino can go down else cant.
	 */
	public static boolean lineChecker2(int[][] coordinates,Box[] box) {
		int counter1=0;
		int[] counter = new int[4];
		//looking each box from tetomino in order to find out 
		for (int i = 0; i < box.length; i++) {	
			counter[0]=4;
			counter[1]=4;
			counter[2]=4;
			counter[3]=4;
			System.out.println(box[i].y);
			if(coordinates[(int) (box[i].y-1.5)][(int) (box[i].x-0.5)]!=0) {
				counter[i]=i;
				counter1++;
			}
		}
		System.out.println(counter1);
		int counter2=0;
		for (int i = 0; i < box.length; i++) {
			if(counter[i]!=4) {
				if(coordinates[(int) (box[i].y-1.5)][(int) (box[i].x-0.5)]==0) {
					counter2++;
				}
			}
		}
		if(counter1==counter2) {
			return true;
		}
		return false;
	}
	/**
	 * This method merges the necessary boxes.
	 * @param shapes, double dimensional array which contains the shapes. 
	 * @param coordinates, the tetris map.
	 * @param random, the shape number which selected in the main method.
	 * @param score, the initial score of the player
	 */
	public static void merge3(Box[][] shapes, int[][] coordinates, int random, int[] score) {
		while(mergeBoolean(coordinates)==true)	{
			StdDraw.show(180);
			//starting to check from bottom to top if there is any mergeable boxes in the map if there is merge happens 
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 10; j++) {
					if(coordinates[i][j]==coordinates[i+1][j]&&coordinates[i][j]!=0) {
						int number = coordinates[i+1][j];
						deleteBox(new Box(0.5,j+0.5,i+1.5));
						coordinates[i+1][j]=0;
						int finalNumber = number*2;
						Box newBox = new Box(0.50,j+0.5,i+0.5);
						newBox.number=finalNumber;
						coordinates[i][j]=finalNumber;
						generateColor(newBox, finalNumber);
						createBox(newBox);
						scoreHolder(coordinates,finalNumber,score);
						while(mergeBoolean2(coordinates)==false) {
							StdDraw.show(180);
							//starting top to bottom checking if there are boxes flying on the map making them get down as much as they can
							for (int i1 = 9; i1 >0; i1--) {
								for (int j1 =9 ; j1 >=0 ; j1--) {
									if(coordinates[i1][j1]!=0 && coordinates[i1-1][j1]==0) {
										int number1 = coordinates[i1][j1];
										deleteBox(new Box(0.50,j1+0.5,i1+0.5));
										coordinates[i1][j1]=0;
										Box newBox1 = new Box(0.5,j1+0.5,i1-0.5);
										newBox1.number=number1;
										coordinates[i1-1][j1]=number1;
										generateColor(newBox1, number1);
										createBox(newBox1);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * This method removes the boxes in a horizontal line. 
	 * @param coordinates, the tetris map.
	 * @param score, the initial score of the player
	 */
	public static void lineDestroyer(int[][] coordinates, int score[]) {
		int add = 0;
		for (int i = 0; i < coordinates[0].length-2; i++) {
			Box box = new Box(0.5, i+0.5, 0.5);
			add=add+box.number;
			deleteBox(box);	
			coordinates[0][i] = 0;
		}
		scoreHolder(coordinates, add, score);
	}
	/**
	 * This method checks if there are any mergeable boxes in the map
	 * @param coordinates, tetris map.
	 * @return true if there is possible merges on the map
	 */
	public static boolean mergeBoolean(int[][]coordinates) {
		int counter=0;
		//from bottom to top checking the map for possible merges
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				if(coordinates[i][j]==coordinates[i+1][j]&&coordinates[i][j]!=0&&coordinates[i+1][j]!=0) {
					counter++;
				}
			}
		}
		if(counter==0) {
			return false;
		}
		else
			return true;
	}
	/**
	 * This method checks if there is any boxes flying on the map rather than getting down after merge
	 * @param coordinates, tetris map.
	 * @return false if there is any necessary changes for the map for the boxes flying on the map
	 */
	public static boolean mergeBoolean2(int[][] coordinates) {
		int counter = 0;
		for (int i1 = 9; i1 >0; i1--) {
			for (int j1 =9; j1 >=0 ; j1--) {
				if(coordinates[i1][j1]!=0 && coordinates[i1-1][j1]==0) {
					counter++;
				}
			}
		}
		if(counter==0) {
			return true;
		}
		else
			return false;
	}
	/**
	 * This method moves the boxes in the up to the bottom in a horizontal line. 
	 * @param coordinates, the tetris map.
	 * @param score, the initial score of the player
	 */
	public static void getDown(int[][] coordinates,int[] score) {
		lineDestroyer(coordinates,score);
		double y = 1.5;
		Box box = new Box(0.5, 0.5, y);
		while(y<coordinates.length) {
			for(int j=0; j<coordinates[(int) Math.abs(y)].length; j++) {
				if(coordinates[(int) Math.abs(y)][j] != 0) {
					box.y = y;
					box.x = j+0.5;
					int number = coordinates[(int) Math.abs(y)][j];
					box.number = number;
					generateColor(box, number);
					deleteBox(box);
					coordinates[(int) Math.abs(y)][j] = 0;
					box.y--;
					coordinates[(int) Math.abs(y-1)][j] = number;
					createBox(box);
					//box.y++;
				}
			}
			y++;
		}
	}
	/**
	 * This method checks whether a horizontal line in the map is full with boxes or not. 
	 * @param coordinates, the tetris map.
	 * @return true if the horizontal line is full.
	 */
	public static boolean isFull(int[][] coordinates) {
		int counter = 0;
		for (int i = 0; i < coordinates[0].length; i++) {
			if(coordinates[0][i]!=0) {
				counter++;
			}	
		}
		if(counter == 8) 
			return true;
		else
			return false;
	}
	/**
	 * This method changes the color of box depending on its number.
	 * @param box, our tetromino.
	 * @param number, the number of the tetromino (box).
	 */
	public static void generateColor(Box box, int number) {
		if(number ==2)
			box.color= StdDraw.WHITE;
		else if(number == 4) 
			box.color = StdDraw.YELLOW;
		else if(number == 8)
			box.color = StdDraw.ORANGE;
		else if(number == 16)
			box.color = StdDraw.PRINCETON_ORANGE;
		else if(number == 32)
			box.color = StdDraw.RED;
		else if(number == 64)
			box.color = StdDraw.BOOK_RED;
		else if(number == 128)
			box.color = StdDraw.GREEN;
		else if(number == 256)
			box.color = StdDraw.BOOK_BLUE;
		else if(number == 512)
			box.color = StdDraw.BLUE;
		else if(number == 1024)
			box.color = StdDraw.PINK;
		else if(number == 2048)
			box.color = StdDraw.CYAN;
	}
	
	/**
	 * This method updates the score and shows it on the screen.
	 * @param coordinates, the tetris map.
	 * @param add, the new score of the player which will be added to the initial score
	 * @param score, the initial score of the player
	 */
	public static void scoreHolder(int[][] coordinates, int add, int[] score) {
		Font font = new Font("Arial", Font.BOLD, 30);
		StdDraw.setFont(font);
		score[0] = score[0] + add;
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledSquare(9.0, 7.5, 0.5);
		StdDraw.setPenColor(Color.white);
		if(Integer.toString(score[0]).length()>=4) {
			Font newFont = new Font("Arial", Font.BOLD, 23);
			StdDraw.setFont(newFont);
			}
		StdDraw.text(9.0, 7.5, Integer.toString(score[0]));
		System.out.println("score"+score);		
	}
	/**
	 * This method creates boxes on the map for given box.
	 * @param box, input box for draw.
	 */
	public static void createBox(Box box) {
		Font font = new Font("Arial", Font.BOLD, 30);
		StdDraw.setFont(font);
		StdDraw.setPenColor(box.color);
		StdDraw.filledSquare(box.x, box.y, box.halfLength);		
		StdDraw.setPenColor(Color.gray);
		StdDraw.text(box.x, box.y, Integer.toString(box.number));
		StdDraw.setPenColor(Color.gray);
		StdDraw.square(box.x, box.y, box.halfLength);
	}
	/**
	 * This method finds the minimum Y value in a box array.
	 * @param shapes, box array
	 * @return double Y value
	 */
	public static double findMinY(Box[] shapes) {
		double minY = 10.0;
		for (int i = 0; i < shapes.length; i++) {
			if(shapes[i].y < minY) {
				minY = shapes[i].y;
			}
		}
		return minY;
	}
	/**
	 * This method finds the max Y value in a box array.
	 * @param shapes, box array.
	 * @return double X value.
	 */
	public static double findMaxY(Box[] shapes) {
		double maxY = 0.0;
		for (int i = 0; i < shapes.length; i++) {
			if(shapes[i].y > maxY)
				maxY = shapes[i].y;
		}
		return maxY;
	}
}