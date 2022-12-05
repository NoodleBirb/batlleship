import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Battleship {

	private boolean[] spotHit;
	private boolean faceDown;
	private int xGrid;
	private int yGrid;
	private int size;
	
	public Battleship() {
		size  = (int)(Math.random()* 4 + 2);
		spotHit = new boolean[size];
		for (int i = 0; i < spotHit.length; i++) {
			spotHit[i] = false;
		}
		faceDown = false;
		
		xGrid = (int)(Math.random() * 10);
		if (xGrid + size > 9) {
			while (xGrid + size > 9) {
				xGrid--;
			}
		}
		yGrid = (int)(Math.random() * 10);
		if (yGrid + size > 9) {
			while (yGrid + size > 9) {
				yGrid--;
			}
		}
	}
	
	public Battleship(int length) {
		size  = length;
		spotHit = new boolean[size];
		for (int i = 0; i < spotHit.length; i++) {
			spotHit[i] = false;
		}
		faceDown = false;
		
		xGrid = (int)(Math.random() * 10);
		if (!faceDown) {
			if (xGrid + size > 9) {
				while (xGrid + size > 9) {
					xGrid--;
				}
			}
		}
		yGrid = (int)(Math.random() * 10);
		if (faceDown) {
			if (yGrid + size > 9) {
				while (yGrid + size > 9) {
					yGrid--;
				}
			}
		}
	}
	public Battleship(int length, boolean face) {
		size  = length;
		spotHit = new boolean[size];
		for (int i = 0; i < spotHit.length; i++) {
			spotHit[i] = false;
		}
		faceDown = face;
		
		xGrid = (int)(Math.random() * 10);
		if (!faceDown) {
			if (xGrid + size > 9) {
				while (xGrid + size > 9) {
					xGrid--;
				}
			}
		}
		yGrid = (int)(Math.random() * 10);
		if (faceDown) {
			if (yGrid + size > 9) {
				while (yGrid + size > 9) {
					yGrid--;
				}
			}
		}
	}
	
	public Battleship(int x, int y) {
		size  = (int)(Math.random()* 4 + 2);
		spotHit = new boolean[size];
		for (int i = 0; i < spotHit.length; i++) {
			spotHit[i] = false;
		}
		faceDown = false;
		
		xGrid = x;
		if (!faceDown) {
			if (xGrid + size > 9) {
				while (xGrid + size > 9) {
					xGrid--;
				}
			}
		}
		yGrid = y;
		if (faceDown) {
			if (yGrid + size > 9) {
				while (yGrid + size > 9) {
					yGrid--;
				}
			}
		}
		
	}
	public Battleship(int x, int y, int shipSize) {
		size = shipSize;
		spotHit = new boolean[size];
		for (int i = 0; i < spotHit.length; i++) {
			spotHit[i] = false;
		}
		faceDown = false;
		xGrid = x;
		
		if (xGrid + size > 10) {
			
			while (xGrid + size > 10) {
				
				xGrid -= 1;
			}
		}
		yGrid = y;
		if (yGrid + size > 10) {
			while (yGrid + size > 10) {
				yGrid-= 1;
			}
		}
	}
	
	public void draw(Graphics g, int width, int height) {
		if (faceDown) {
			for (int i = 0; i < spotHit.length; i++) {
				if (spotHit[i] == true) {
					g.setColor(Color.RED);
				}
				else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(xGrid * width/10, yGrid * height/10 + height/10 * i, width/10, height/10);
			}
		}
		else if(!faceDown) {
			for (int i = 0; i < spotHit.length; i++) {
				if (spotHit[i] == true) {
					g.setColor(Color.RED);
				}
				else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(xGrid * width/10 + height/10 * i, yGrid * height/10, width/10, height/10);
			}
		}
	}
	
	public boolean[] getSpotsHit() {
		return spotHit;
	}
	public void setHit(boolean hit, int position) {
		if (position < spotHit.length) {
			spotHit[position] = hit;
		}
	}
	public boolean getFaceDown() {
		return faceDown;
	}
	public void setFaceDown(boolean upOrDown) {
		if (upOrDown) {
			if (yGrid + size <= 9) {
				faceDown = upOrDown;
			}
		}
		else if (!upOrDown) {
			if (xGrid + size <= 9) {
				faceDown = upOrDown;
			}
		}
	}
	
	public boolean destroyed() {
		for (int i = 0; i < spotHit.length; i++) {
			if (spotHit[i] == false) {
				return false;
			}
		}
		return true;
	}
	
	public void setY(int yPos) {
		yGrid = yPos;
	}
	
	public int getY() {
		return yGrid;
	}
	
	public void setX(int xPos) {
		xGrid = xPos;
	}
	
	public int getX() {
		return xGrid;
	}
	
	public int getSize() {
		return size;
	}
	
	// Checks every situation to see if the ship overlaps with a designated ship.
	public boolean overlapWith(Battleship ship2) {
		// First two situations, ship 2 face down.
		if (ship2.getFaceDown()) {
			
			// First situation, ship1 face down as well.
			if (faceDown) {
				
				// Checks if their x is the same. X must be the same for them to overlap when they are both face down.
				if (ship2.getX() == xGrid) {
					
					// Checks if Ship2 has a y value greater than ship1
					if (ship2.getY() >= yGrid) {
						
						// Checks if ship1 + size is greater then ship2 y
						if (yGrid + size >= ship2.getY()) {
							return true;
						}
						
						// Returns false if none of this is true.
						else {
							return false;
						}
					}
					// Check if ship1 has a greater y position.
					else if(yGrid > ship2.getY()) {
						
						// Check if ship2 + it's size is greater than yGrid.
						if (ship2.getY() + ship2.getSize() > yGrid) {
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return false;
					}
				}	
			}
			
			// Situation 2: ship1 face straight
			// Checks if ship2 has a Y position less than or equal to ship1 Y.
			// Also checks if ship2 Y + size is greater than or equal to yGrid.
			else if (ship2.getY() <= yGrid && ship2.getY() + ship2.getSize() >= yGrid){
				
				// Checks if ship2's X position is greater than xGrid.
				if (ship2.getX() >= xGrid) {
					
					// Check if ship1 + size is greater than X.
					if (xGrid + size >= ship2.getX()) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return true;
				}
			}
			else {
				return false;
			}
		}
		
		// Situation 3: Ship1 facedown but Ship2 is facing straight.
		if(faceDown) {
			
			// First check if Ship2 has a Y position in the boundaries of Ship1's size
			if (ship2.getY() >= yGrid && ship2.getY() <= yGrid + size){
				
				// Check if ship1's x position is greater than Ship2's
				if (xGrid >= ship2.getX()) {
					
					// Check if ship2's X + size is greater than ship1
					// If this is true, they overlap
					if (ship2.getX() + ship2.getSize() >= xGrid) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return true;
				}
			}
			else {
				return false;
			}
		}
		// Last two situations, both are straight. Checks if they are on the same y level
		else if(ship2.getY() == yGrid) {
			
			// First of last two situations, ship1 is further away
			if (xGrid >= ship2.getX()) {
				
				// If ship2 + it's size is greater than xGrid, they must overlap.
				if (ship2.getX() + ship2.getSize() >= xGrid) {
					return true;
				}
				else {
					return false;
				}
			}
			
			// Last situation, ship2 is further away than ship1.
			else if(ship2.getX() >= xGrid) {
				
				// If ship1 + it's size is greater than ship2, they must overlap.
				if (xGrid + size >= ship2.getX()) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}
		return false;
	}
	
	
}
