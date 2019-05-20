package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.Properties;
import javax.swing.JPanel;

/**
 * 
 * @author rouzoo
 *
 */
public class BoardPanel extends JPanel {
	
	private static final long serialVersionUID = 2496223275366024540L;
	/*
	 * ������� ����
	 */
	public static final int COL_COUNT = 20; 
	public static final int ROW_COUNT = 20;
	/*
	 * ������ ������ ������
	 */
	public static final int TILE_SIZE = 32;
		
	/**
	 * ����� � ��� �����
	 */
	private static final Font FONT = new Font("Tahoma", Font.BOLD, 25);
		
	
	private SnakeGame game;
	
	/*
	 * ������ ������
	 */
	private TileType[] tiles;
		
	/*
	 * �������� �������� ����
	 */
	public BoardPanel(SnakeGame game) {
		this.game = game;
		this.tiles = new TileType[ROW_COUNT * COL_COUNT];
		setPreferredSize(new Dimension(COL_COUNT * TILE_SIZE, ROW_COUNT * TILE_SIZE));
		setBackground(new Color(166, 4, 184));
	}
	
	public void clearBoard() {
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = null;
		}
	}
	
	/*
	 * ������ ��� ������
	 */
	public void setTile(Point point, TileType type) {
		setTile(point.x, point.y, type);
	}
	
	public void setTile(int x, int y, TileType type) {
		tiles[y * ROW_COUNT + x] = type;
	}
	
	/*
	 * ������ ��� ������
	 */
	public TileType getTile(int x, int y) {
		return tiles[y * ROW_COUNT + x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/*
		 * ���������� ������
		 */
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				TileType type = getTile(x, y);
				if(type != null) {
					drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
				}
			}
		}
		
		/*
		 * ������ �����
		 */
		g.setColor(Color.black);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
				g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
			}
		}		
		
		/*
		 * ����� ���������
		 */
		if(game.isGameOver() || game.isNewGame() || game.isPaused()) {
			g.setColor(Color.WHITE);
			
			/*
			 * ����� ����
			 */
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			
			String largeMessage = null;
			String smallMessage = null;
			if(game.isNewGame()) {
				largeMessage = "Snake Game!";
				smallMessage = "Press Enter to play!";
			} else if(game.isGameOver()) {
				largeMessage = "Game Over!";
				smallMessage = "Press Enter to play again!";
			} else if(game.isPaused()) {
				largeMessage = "Пауза";
				smallMessage = "Press P to continue!";
			}
			
			/*
			 * ������������ ������ ����� �� ������
			 */
			g.setFont(FONT);
			g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 50);
			g.drawString(smallMessage, centerX - g.getFontMetrics().stringWidth(smallMessage) / 2, centerY + 50);
		}
	}
	
	/*
	 * ������ ������ ��� ������� ������
	 */
	private void drawTile(int x, int y, TileType type, Graphics g) {
	
		Properties p = System.getProperties();
		switch(type) {
		
		case Fruit:
			String fruitFilePath; 
				fruitFilePath =  p.getProperty("user.dir") +  File.separator +"res" + File.separator + "cherries.png";

				Image img3 = Toolkit.getDefaultToolkit().getImage(fruitFilePath);
				g.drawImage(img3, x, y, this);
		break;
		
			
		case SnakeBody:
			String bodyFilePath =  p.getProperty("user.dir") + 
			  File.separator +"res" + File.separator +"body.png";

				Image img = Toolkit.getDefaultToolkit().getImage(bodyFilePath);
			g.drawImage(img, x, y, this);
			break;
			
		case SnakeHead:
			String headFilePath; 
			
			switch(game.getDirection()) {
			case North: {
				 headFilePath =  p.getProperty("user.dir") +  File.separator +"res" + File.separator + "headN.png";

						Image img2 = Toolkit.getDefaultToolkit().getImage(
						  headFilePath);
						g.drawImage(img2, x, y, this);
				break;
			}
				
			case South: {
				headFilePath =  p.getProperty("user.dir") +  File.separator +"res" + File.separator  + "headS.png";

						Image img2 = Toolkit.getDefaultToolkit().getImage(
						  headFilePath);
						g.drawImage(img2, x, y, this);
				break;
			}
			
			case West: {
				headFilePath =  p.getProperty("user.dir") +  File.separator +"res" + File.separator + "headW.png";

						Image img2 = Toolkit.getDefaultToolkit().getImage(
						  headFilePath);
						g.drawImage(img2, x, y, this);
				break;
			}
				
			case East: {
				headFilePath =  p.getProperty("user.dir") +  File.separator +"res" + File.separator + "headE.png";

						Image img2 = Toolkit.getDefaultToolkit().getImage(
						  headFilePath);
						g.drawImage(img2, x, y, this);
				break;
			}
			
			}
			break;
		}
	}

}
