//required import statements
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@SuppressWarnings("serial")
public class BattleshipAnimation extends JPanel {

	// Fills a grid with battleships.
	public static void fillGridShips(boolean[][] grid, ArrayList<Battleship> ships) {
		// Checks each ship
		for (int i = 0; i < ships.size(); i++) {
			
			// Checks if it's face down.
			if (ships.get(i).getFaceDown()) {
				for (int j = 0; j < ships.get(i).getSize(); j++) {
					// sets the grid position of the ship piece into the 2d array.
					grid[ships.get(i).getY() + j][ships.get(i).getX()] = true;
				}
			}
			
			// If it's not face down,
			else if(!ships.get(i).getFaceDown()) {
				for (int j = 0; j < ships.get(i).getSize(); j++) {
					// sets the grid position of the ship piece into the 2d array.
					grid[ships.get(i).getY()][ships.get(i).getX() + j] = true;
				}
			}
		}
	}
	
	// Checks if an ArrayList of ships has been destroyed
	public boolean groupDestroyed(ArrayList<Battleship> ships) {
		for (int i = 0; i < ships.size(); i++) {
			if (!ships.get(i).destroyed()) {
				return false;
			}
		}
		return true;
	}
	// Set initial height and width
	private static final int WIDTH = 800;
	private static final int HEIGHT = 800;

	//required global variables
	private int turn = 0; // Speaks for itself, int to say what turn the game is on.
	private boolean turnover = false; // boolean for if the turn has ended.
	public boolean showShips = true; // Boolean that was made to make logic for turn order. Looking back on it, I should've just done this differently to make it less complicated.
	private BufferedImage image;
	private Graphics g;
	private Timer timer;
	private int players = 0; // Variable to state how many players there are. This was going to be used for changing small pieces of code.
	ArrayList<Battleship> playerShips = new ArrayList<Battleship>(); // Arraylist for playerships since there were plans to make the amount of ships changeable
	ArrayList<Battleship> enemyShips = new ArrayList<Battleship>(); // Arraylist for enemyships since there were plans to make the amount of ships changeable
	ArrayList<Tile> playerMisses = new ArrayList<Tile>(); // Arraylist for miss squares
	ArrayList<Tile> enemyMissOrHit = new ArrayList<Tile>(); // Arraylist for miss squares
	private boolean playerGridShips[][] = new boolean[10][10]; // 2d array for where the ships are
	private boolean playerGridHits[][] = new boolean[10][10]; // 2d array for what grid spots have been hit
	private boolean enemyGridShips[][] = new boolean[10][10]; // 2d array for where the ships are
	private boolean enemyGridHits[][] = new boolean[10][10]; // 2d array for what grid spots have been hit
	 
	// Constructor required by BufferedImage
	public BattleshipAnimation() {
		// set up Buffered Image and Graphics objects
		image =  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();

		// Fill the array list with 5 ships, 1 5 size, 1 4 size, 2 3 size, 1 2 size
		playerShips.add(new Battleship(2, Math.random() < .5));
		playerShips.add(new Battleship(3, Math.random() < .5));
		playerShips.add(new Battleship(3, Math.random() < .5));
		playerShips.add(new Battleship(4, Math.random() < .5));
		playerShips.add(new Battleship(5, Math.random() < .5));
		
		
		// Make sure there are no ships overlapping.
		for (int i = 0; i < playerShips.size(); i++) {
			for (int j = 0; j < playerShips.size(); j++) {
				
				// If a ship is overlapping, make a new ship in a different location then restart the for loop.
				if (playerShips.get(i).overlapWith(playerShips.get(j)) && i != j) {
					playerShips.set(i, new Battleship(playerShips.get(i).getSize(), Math.random() < .5));
					i = 0;
					break;
				}
			}
		}
		
		// Fill the second Array List with 5 ships, 1 5 size, 1 4 size, 2 3 size, 1 2 size.
		enemyShips.add(new Battleship(2, Math.random() < .5));
		enemyShips.add(new Battleship(3, Math.random() < .5));
		enemyShips.add(new Battleship(3, Math.random() < .5));
		enemyShips.add(new Battleship(4, Math.random() < .5));
		enemyShips.add(new Battleship(5, Math.random() < .5));
		
		// Make sure the second set of ships have no overlapping.
		for (int i = 0; i < enemyShips.size(); i++) {
			for (int j = 0; j < enemyShips.size(); j++) {
				
				// If one of the ships is overlapping, make a new ship in a different location then restart the for loop.
				if (enemyShips.get(i).overlapWith(enemyShips.get(j)) && i != j) {
					enemyShips.set(i, new Battleship(enemyShips.get(i).getSize(), Math.random() < .5));
					i = 0;
					break;
				}
			}
		}
		
		// Fill 2d array with PlayerShips
		fillGridShips(playerGridShips, playerShips);
		fillGridShips(enemyGridShips, enemyShips);
		
		//set up and start the Timer
		timer = new Timer(10, new TimerListener());
		timer.start();
		addMouseListener(new Mouse());
		addMouseWheelListener(new MouseWheelActions());
		addKeyListener(new Key());
		setFocusable(true);
	}
	
	private class Mouse implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			// Turns where the mouse clicked into a grid position from  0-9
			int xGridClick = e.getX() / (WIDTH / 10);
			int yGridClick = e.getY() / (HEIGHT / 10);
			
			// Makes sure you haven't already clicked, and that it's your turn to click.
			if (turn == 2 && !turnover) {
				
				// Checks to make sure you haven't clicked there before.
				if (!enemyGridHits[yGridClick][xGridClick]) {
					
					// Checks the grid to say that you have now clicked there.
					enemyGridHits[yGridClick][xGridClick] = true;
					
					// adds a new tile.
					enemyMissOrHit.add(new Tile(xGridClick, yGridClick));
					
					// if there was a ship there, set the tile to be red and give another turn.
					if (enemyGridShips[yGridClick][xGridClick]) {
						enemyMissOrHit.get(enemyMissOrHit.size() - 1).setHit(true); 
						
						// Checks each ship
						for (int i = 0; i < enemyShips.size(); i++) {
							
							// Checks each piece of the ship
							for (int j = 0; j < enemyShips.get(i).getSize(); j++) {
								
								// Checks if face down
								if (enemyShips.get(i).getFaceDown()) {
									
									// Checks the position of the ship and each piece of the ship.
									// If it was shot in one of it's positions, set ship to be hit in that spot.
									if (enemyShips.get(i).getX() == xGridClick && enemyShips.get(i).getY() + j == yGridClick) {
										enemyShips.get(i).setHit(true, j);
									}
									
								}
								
								//checks if not face down
								else if(!enemyShips.get(i).getFaceDown()) {
									
									// Checks the position of the ship and each piece of the ship.
									// If it was shot in one of it's positions, set ship to be hit in that spot.
									if (enemyShips.get(i).getX() + j == xGridClick && enemyShips.get(i).getY() == yGridClick) {
										enemyShips.get(i).setHit(true, j);
									}
								}
							}
						}
						if (groupDestroyed(enemyShips)) {
							turn = 3;
						}
					}
					// else, end the turn
					else {
						turnover = true;
					}
				}
			}
			
			// Added this for 2 player version, unfortunately doesn't work (though it hasn't been able to be tested yet due to the game not starting).
			else if (players == 2 && turn == 1 && !showShips) {
	
				// Makes sure you haven't already clicked, and that it's your turn to click.
				if (turn == 1 && !turnover) {
					
					// Checks to make sure you haven't clicked there before.
					if (!playerGridHits[yGridClick][xGridClick]) {
						
						// Checks the grid to say that you have now clicked there.
						playerGridHits[yGridClick][xGridClick] = true;
						
						// adds a new tile.
						playerMisses.add(new Tile(xGridClick, yGridClick));
						
						// if there was a ship there, set the tile to be red and give another turn.
						if (playerGridShips[yGridClick][xGridClick]) {
							playerMisses.get(playerMisses.size() - 1).setHit(true); 
							
							// Checks each ship
							for (int i = 0; i < playerShips.size(); i++) {
								
								// Checks each piece of the ship
								for (int j = 0; j < playerShips.get(i).getSize(); j++) {
									
									// Checks if face down
									if (playerShips.get(i).getFaceDown()) {
										
										// Checks the position of the ship and each piece of the ship.
										// If it was shot in one of it's positions, set ship to be hit in that spot.
										if (playerShips.get(i).getX() == xGridClick && playerShips.get(i).getY() + j == yGridClick) {
											playerShips.get(i).setHit(true, j);
										}
										
									}
									
									//checks if not face down
									else if(!playerShips.get(i).getFaceDown()) {
										
										// Checks the position of the ship and each piece of the ship.
										// If it was shot in one of it's positions, set ship to be hit in that spot.
										if (playerShips.get(i).getX() + j == xGridClick && playerShips.get(i).getY() == yGridClick) {
											playerShips.get(i).setHit(true, j);
										}
									}
								}
							}
							if (groupDestroyed(playerShips)) {
								turn = 4;
							}
						}
						// else, end the turn
						else {
							turnover = true;
						}
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class MouseWheelActions implements MouseWheelListener {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class Key implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
			// Player selection code.
			if (e.getKeyCode() == KeyEvent.VK_1) {
				if (turn == 0) {
					players = 1;
					turnover = true;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_2) {
				if (turn == 0) {
					players = 2;
					turnover = true;
				}
			}
			// Starting turn, changes the board to the enemy board.
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (turn == 0 && turnover && players == 1) {
					turn = 2;
					turnover = false;
				}
				
				// If it was the opponents turn, change it to your turn.
				else if (turnover && players == 1) {
					
					
					if (turn == 1 ) {
						turn++;
						turnover = false;
					}
					// If it was your turn that just ended:
					else while(turn == 2 && players == 1) {
						
						// choose a random X and Y position on the grid.
						int chosenXPos = (int)(Math.random() * 10);
						int chosenYPos = (int)(Math.random() * 10);
						
						// If that chosen X and Y position was already chosen, get a new position
						while (playerGridHits[chosenYPos][chosenXPos]) {
							chosenXPos = (int)(Math.random() * 10);
							chosenYPos = (int)(Math.random() * 10);
						}
						
						// If there was no ship there, add a white tile to the position.
						if (!playerGridShips[chosenYPos][chosenXPos]) {
							playerMisses.add(new Tile(chosenXPos, chosenYPos));
							playerGridHits[chosenYPos][chosenXPos] = true;
							turn--; 
							turnover = true;
						}
						else {
							
							// Checks each ship
							for (int i = 0; i < playerShips.size(); i++) {
								
								// Checks each piece of the ship
								for (int j = 0; j < playerShips.get(i).getSize(); j++) {
									
									// Checks if the ship is face down
									if (playerShips.get(i).getFaceDown()) {
										
										// Checks the position of the ship and each piece of the ship.
										// If it was shot in one of it's positions, set ship to be hit in that spot.
										if (playerShips.get(i).getX() == chosenXPos && playerShips.get(i).getY() + j == chosenYPos) {
											playerShips.get(i).setHit(true, j);
										}
										
									}
									
									// checks if not face down
									else if(!playerShips.get(i).getFaceDown()) {
										
										// Checks the position of the ship and each piece of the ship.
										// If it was shot in one of it's positions, set ship to be hit in that spot.
										if (playerShips.get(i).getX() + j == chosenXPos && playerShips.get(i).getY() == chosenYPos) {
											playerShips.get(i).setHit(true, j);
										}
									}
								}
							}
							
							// Checks if all playerShips were destroyed. If so end the game.
							if (groupDestroyed(playerShips)) {
								turn = 4;
							}
						}
						
					}
					
				}
				else if (turn == 0 && turnover && players == 2) {
					turn = 1;
					showShips = true;
					turnover = true;
				}
				if (turn == 5){
					turn = 2;
				}
				
				else if (turn == 6) {
					turn = 1;
				}
				else if (turn == 1 && players == 2 && !showShips) {
					turn = 5;
				}
				else if(players == 2 && turn == 2 && showShips) {
					showShips = false;
					turn = 1;
				}
				else if (players == 2 && turn == 2) {
					showShips = false;
					turn = 6;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	//TimerListener class that is called repeatedly by the timer
	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Draw background
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			// Draw gridlines
			g.setColor(Color.GRAY.darker());
			for (int i = 0; i < 10; i++) {
				g.drawLine(1 * WIDTH / 10 * i, 0, 1 * WIDTH / 10 * i, HEIGHT);
			}
			for (int i = 0; i < 10; i++) {
				g.drawLine(0, 1 * HEIGHT / 10 * i, WIDTH, 1 * WIDTH / 10 * i);
			}
			
			// Beginning player amount selection
			if (turn == 0) {
				g.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
				g.setColor(Color.BLUE.darker());
				g.drawString("Press 1 for 1 player, and 2 for 2 player.", WIDTH /4,(int) (HEIGHT / 1.2));
				g.drawString(players + " Players selected", WIDTH /4,(int) (HEIGHT / 1.2) + 40);
			}
			
			// Draw player1 Ships and tile misses
			else if (turn == 1 && players == 1) {
				for (int i = 0; i < playerShips.size(); i++) {
					playerShips.get(i).draw(g, WIDTH, HEIGHT);
					
				}
				for (int i = 0; i < playerMisses.size(); i++) {
					playerMisses.get(i).draw(g, WIDTH, HEIGHT);
				}
			}
			else if (turn == 1 && players == 2 && showShips) {
				for (int i = 0; i < playerShips.size(); i++) {
					playerShips.get(i).draw(g, WIDTH, HEIGHT);
					
				}
				for (int i = 0; i < playerMisses.size(); i++) {
					playerMisses.get(i).draw(g, WIDTH, HEIGHT);
				}
			}
			else if (turn == 1 && players == 2) {
				for (int i = 0; i < playerMisses.size(); i++) {
					playerMisses.get(i).draw(g, WIDTH, HEIGHT);
				}
			}
			
			// Draw enemy side of board.
			else if (turn  == 2 && players == 1) {
				for (int i = 0; i < enemyMissOrHit.size(); i++) {
					enemyMissOrHit.get(i).draw(g, WIDTH, HEIGHT);
				}
			}
			
			
			else if (turn == 2 && players == 2) {
				for (int i = 0; i < playerShips.size(); i++) {
					enemyShips.get(i).draw(g, WIDTH, HEIGHT);
				}
				for (int i = 0; i < enemyMissOrHit.size(); i++) {
					enemyMissOrHit.get(i).draw(g, WIDTH, HEIGHT);
				}
			}
			
			// Draw enemy side of board and say that player1 is the winner
			else if (turn == 3) {
				for (int i = 0; i < playerShips.size(); i++) {
					enemyShips.get(i).draw(g, WIDTH, HEIGHT);
				}
				for (int i = 0; i < enemyMissOrHit.size(); i++) {
					enemyMissOrHit.get(i).draw(g, WIDTH, HEIGHT);
				}
				g.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
				g.setColor(Color.BLUE.darker());
				g.drawString("Player1 Wins!!", WIDTH /3,(int) (HEIGHT / 3));
			}
			
			// Draw player side of the board and say that the enemy is the winner.
			else if (turn == 4) {
				for (int i = 0; i < playerShips.size(); i++) {
					playerShips.get(i).draw(g, WIDTH, HEIGHT);
				}
				for (int i = 0; i < playerMisses.size(); i++) {
					playerMisses.get(i).draw(g, WIDTH, HEIGHT);
				}
				g.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
				g.setColor(Color.BLUE.darker());
				g.drawString("Player2 Wins!!", WIDTH /3,(int) (HEIGHT / 3));
				
			}
			else if (turn == 5) {
				g.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
				g.setColor(Color.BLUE.darker());
				g.drawString("Pass the computer to player 2", WIDTH /3,(int) (HEIGHT / 3));
			}
			else if (turn == 6) {
				g.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
				g.setColor(Color.BLUE.darker());
				g.drawString("Pass the computer to player 1", WIDTH /3,(int) (HEIGHT / 3));
			}
			
			
			
			repaint(); //leave this alone, it MUST  be the last thing in this method
		}
		
	}

	//do not modify this
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	//main method with standard graphics code
	public static void main(String[] args) {
		JFrame frame = new JFrame("Animation Shell");
		frame.setSize(WIDTH + 18, HEIGHT + 47);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new BattleshipAnimation()); //TODO: Change this to the name of your class!
		frame.setVisible(true);
	}

}
