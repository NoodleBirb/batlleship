import java.awt.Color;
import java.awt.Graphics;

public class Tile {
	
	private int xGrid;
	private int yGrid;
	private boolean hit;
	
	public Tile(int x, int y) {
		xGrid = x;
		yGrid = y;
		hit = false;
	}

	public int getxGrid() {
		return xGrid;
	}

	public void setxGrid(int xGrid) {
		this.xGrid = xGrid;
	}

	public int getyGrid() {
		return yGrid;
	}

	public void setyGrid(int yGrid) {
		this.yGrid = yGrid;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
	
	public void draw(Graphics g, int width, int height) {
		// Picks between red and white color based on a boolean.
		if (hit == true) {
			g.setColor(Color.RED);
		}
		else {
			g.setColor(Color.WHITE);
		}
		
		// Draws the tile the size of a grid square.
		g.fillRect(xGrid * width/10, yGrid * height/10, width/10, height/10);
		g.setColor(Color.GRAY.darker());
		g.drawRect(xGrid * width/10, yGrid * height/10, width/10, height/10);
	}
}
