package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 
 * @author rouzoo
 *
 */
public class SidePanel extends JPanel {
	
	/*
	 * Что-то очень важное
	 */
	private static final long serialVersionUID = 7029771736656025927L;

	/*
	 * настройки шрифтов
	 */
	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 20);
	private static final Font MEDIUM_FONT = new Font("Tahoma", Font.BOLD, 16);
	private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);
	
	private SnakeGame game;
	
	/**
	 * создаем правую панель
	 */
	public SidePanel(SnakeGame game) {
		this.game = game;
		
		setPreferredSize(new Dimension(270, BoardPanel.ROW_COUNT * BoardPanel.TILE_SIZE));
		setBackground(new Color(70, 80, 70));
	}
	
	private static final int STATISTICS_OFFSET = 150;
	
	private static final int CONTROLS_OFFSET = 320;
	
	private static final int MESSAGE_STRIDE = 30;
	
	private static final int SMALL_OFFSET = 30;
	
	private static final int LARGE_OFFSET = 50;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		
		/*
		 * пишем, что это змейка
		 */
		g.setFont(LARGE_FONT);
		g.drawString("Snake Game", getWidth() / 2 - g.getFontMetrics().stringWidth("Snake Game") / 2, 50);
		
		/*
		 * пишем что тут будет статистика
		 */
		g.setFont(MEDIUM_FONT);
		g.drawString("Статистика", SMALL_OFFSET, STATISTICS_OFFSET);
		g.drawString("Управление", SMALL_OFFSET, CONTROLS_OFFSET);
		g.setFont(SMALL_FONT);
		
		//пишем статистику
		int drawY = STATISTICS_OFFSET;
		g.drawString("Очки: " + game.getScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Съедено фруктов: " + game.getFruitsEaten(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Очков за фрукт: " + game.getNextFruitScore(), LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		//Draw the content for the controls category.
		drawY = CONTROLS_OFFSET;
		g.drawString("Вверх: W / Стрелка верх", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Вниз: S / Стрелка вниз", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Влево: A / Стрелка влево", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Вправо: D / Стреока вправо", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
		g.drawString("Пауза: P", LARGE_OFFSET, drawY += MESSAGE_STRIDE);
	}

}
