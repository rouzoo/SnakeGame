package snakegame;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * 
 * @author rouzoo
 *
 */
public class SnakeGame extends JFrame {
		
	/*
	 * установка времени отображения кадра
	 */
	private static final long FRAME_TIME = 1000L / 50L;
	
	/*
	 *длинна змеи
	 */
	private static final int MIN_SNAKE_LENGTH = 3;
	
	/*
	 *максимально количество опрашивемых направлений
	 */
	private static final int MAX_DIRECTIONS = 3;
	
	private BoardPanel board;
	
	private SidePanel side;
	
	private Random random;
	
	private Clock logicTimer;
	
	private boolean isNewGame;
		
	private boolean isGameOver;
	
	private boolean isPaused;
	
	/*
	 * список точек змеи
	 */
	private LinkedList<Point> snake;

	/*
	 * список направлений
	 */
	private LinkedList<Direction> directions;
	
	/*
	 * текущее количество очков
	 */
	private int score;
	
	/*
	 * съеденые фрукты
	 */
	private int fruitsEaten;
	
	/*
	 * количество очков за следующий фрукт
	 */
	private int nextFruitScore;
	
	/*
	 * создаем окно
	 */
	private SnakeGame() {
		super("Snake game");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		/*
		 * создаем панели внутри окна 
		 * и прочие настройки
		 */
		this.board = new BoardPanel(this);
		this.side = new SidePanel(this);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\res\\snake.png"));
		add(board, BorderLayout.CENTER);
		add(side, BorderLayout.EAST);
		
		/*
		 * считывание кнопок
		 */
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {

				/*
				 * управление если не пауза и не конец
				 */
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(!isPaused && !isGameOver) {
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.South && last != Direction.North) {
								directions.addLast(Direction.North);
							}
						}
					}
					break;

			
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if(!isPaused && !isGameOver) {
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.North && last != Direction.South) {
								directions.addLast(Direction.South);
							}
						}
					}
					break;
				
			
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if(!isPaused && !isGameOver) {
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.East && last != Direction.West) {
								directions.addLast(Direction.West);
							}
						}
					}
					break;
			
				
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if(!isPaused && !isGameOver) {
						if(directions.size() < MAX_DIRECTIONS) {
							Direction last = directions.peekLast();
							if(last != Direction.West && last != Direction.East) {
								directions.addLast(Direction.East);
							}
						}
					}
					break;
				
				/*
				 * Кнопка паузы
				 */
				case KeyEvent.VK_P:
					if(!isGameOver) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);
					}
					break;
				
				
				case KeyEvent.VK_ENTER:
					if(isNewGame || isGameOver) {
						resetGame();
					}
					break;
				}
			}
			
		});
		
		
		pack(); // заставляет окно соответствовать размеру панелей
		setLocationRelativeTo(null); //окно по центру экрана
		setVisible(true); // делаем видимым
	}
	
	private void startGame() {
		
		this.random = new Random();
		this.snake = new LinkedList<>();
		this.directions = new LinkedList<>();
		this.logicTimer = new Clock(9.0f);
		this.isNewGame = true;
		
		logicTimer.setPaused(true);

		/*
		 * цикл игры(до закрытия окна)
		 */
		while(true) {
			long start = System.nanoTime();
			
			logicTimer.update();
			
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
			
			board.repaint();
			side.repaint();
			
			/*
			 * ограничитель кадров
			 */
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void updateGame() {
		/*
		 * обработка столкновения
		 */
		TileType collision = updateSnake();
		
		/*
		 * логика подбора фруктов и столкновений
		 */
		if(collision == TileType.Fruit) {
			fruitsEaten++;
			score += nextFruitScore;
			spawnFruit();
		} else if(collision == TileType.SnakeBody) {
			isGameOver = true;
			logicTimer.setPaused(true);
		} else if(nextFruitScore > 10) {
			nextFruitScore--;
		}
	}
	
	/*
	 * обновление змеи
	 */
	private TileType updateSnake() {

		/*
		 * берем первый элемент из списка
		 */
		Direction direction = directions.peekFirst();
				
		/*
		 * вычисляем новую точку для головы змеи
		 */		
		Point head = new Point(snake.peekFirst());
		switch(direction) {
		case North:
			head.y--;
			break;
			
		case South:
			head.y++;
			break;
			
		case West:
			head.x--;
			break;
			
		case East:
			head.x++;
			break;
		}
		
		/*
		 * проверка выхода за границы
		 */
		if(head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0 || head.y >= BoardPanel.ROW_COUNT) {
			return TileType.SnakeBody; 
		}
		
		/*
		 * костыль, чтобы не срабатывал ложный проигрыш при 
		 * проходе через удаленный хвост
		 */
		TileType old = board.getTile(head.x, head.y);
		if(old != TileType.Fruit && snake.size() > MIN_SNAKE_LENGTH) {
			Point tail = snake.removeLast();
			board.setTile(tail, null);
			old = board.getTile(head.x, head.y);
		}
		
		/*
		 * обновление змеи на поле
		 */
		if(old != TileType.SnakeBody) {
			board.setTile(snake.peekFirst(), TileType.SnakeBody);
			snake.push(head);
			board.setTile(head, TileType.SnakeHead);
			if(directions.size() > 1) {
				directions.poll();
			}
		}
				
		return old;
	}
	
	/*
	 * рестарт
	 */
	private void resetGame() {
		this.score = 0;
		this.fruitsEaten = 0;
		
	
		this.isNewGame = false;
		this.isGameOver = false;
		
		
		Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT / 2);

		
		snake.clear();
		snake.add(head);
		
	
		board.clearBoard();
		board.setTile(head, TileType.SnakeHead);
		
		directions.clear();
		directions.add(Direction.North);
		
		logicTimer.reset();
		
		spawnFruit();
	}
	
	/*
	 * новая ли игра?
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/*
	 * конец игры?
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/*
	 * пауза?
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	/*
	 * создание фруктов
	 */
	private void spawnFruit() {
		//устанавливаем 100 очков новому фрукту
		this.nextFruitScore = 100;

		/*
		 * выбираем случайную свободную клетку
		 */
		int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - snake.size());
		
		/*
		 * 
		 */
		int freeFound = -1;
		for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
			for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
				TileType type = board.getTile(x, y);
				if(type == null || type == TileType.Fruit) {
					if(++freeFound == index) {
						board.setTile(x, y, TileType.Fruit);
						break;
					}
				}
			}
		}
	}
	
	/*
	 *геттер очков
	 */
	public int getScore() {
		return score;
	}
	
	/*
	 * геттер на съеденные фрукты
	 */
	public int getFruitsEaten() {
		return fruitsEaten;
	}
	
	/*
	 * геттер на количество очков за следующий фрукт
	 */
	public int getNextFruitScore() {
		return nextFruitScore;
	}
	
	/*
	 * геттер направления змеи
	 */
	public Direction getDirection() {
		return directions.peek();
	}
	
	public static void main(String[] args) {
		SnakeGame snake = new SnakeGame();
		snake.startGame();
	}

}
