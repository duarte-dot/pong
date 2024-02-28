package pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game extends Canvas implements Runnable, KeyListener {

  private static final long serialVersionUID = 1L;

  public static int WIDTH = 240;
  public static int HEIGHT = 120;
  public static int SCALE = 3;

  public BufferedImage layer = new BufferedImage(
    WIDTH,
    HEIGHT,
    BufferedImage.TYPE_INT_RGB
  );

  public static Player player;
  public static Enemy enemy;
  public static Ball ball;

  private boolean isRunning = false;
  private Thread thread;
  public static boolean isMultiplayer = false;

  public Game() {
    this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
    this.addKeyListener(this);
    player = new Player(100, HEIGHT - 10);
    enemy = new Enemy(100, 0);
    ball = new Ball(100, HEIGHT / 2 - 1);
  }

  public static void main(String[] args) {
    Game game = new Game();
    JFrame frame = new JFrame("Pong");

    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(game);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    game.showGameModeSelection(); // Chamada do método showGameModeSelection
    System.out.println("isMultiplayer: " + isMultiplayer);

    game.start();
  }

  public synchronized void start() {
    if (isRunning) {
      return;
    }

    isRunning = true;
    thread = new Thread(this);
    thread.start();
  }

  public synchronized void stop() {
    if (!isRunning) {
      return;
    }

    isRunning = false;
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void tick() {
    player.tick();
    enemy.tick();
    ball.tick();
  }

  public void render() {
    BufferStrategy bs = this.getBufferStrategy();

    if (bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = layer.getGraphics();
    g.setColor(Color.black);
    g.fillRect(0, 0, WIDTH, HEIGHT);
    player.render(g);
    enemy.render(g);
    ball.render(g);

    g = bs.getDrawGraphics();
    g.drawImage(layer, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

    bs.show();
  }

  @Override
  public void run() {
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;

    int frames = 0;
    double timer = System.currentTimeMillis();
    while (isRunning) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;

      lastTime = now;

      if (delta >= 1) {
        tick();
        render();
        frames++;
        delta--;
      }

      if (System.currentTimeMillis() - timer >= 1000) {
        System.out.println("FPS:" + frames);
        frames = 0;
        timer += 1000;
      }
    }

    stop();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (!isMultiplayer) {
      if (
        e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D
      ) {
        player.right = true;
      } else if (
        e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A
      ) {
        player.left = true;
      }
    } else {
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        player.right = true;
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        player.left = true;
      } else if (e.getKeyCode() == KeyEvent.VK_D) {
        enemy.right = true;
      } else if (e.getKeyCode() == KeyEvent.VK_A) {
        enemy.left = true;
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (!isMultiplayer) {
      if (
        e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D
      ) {
        player.right = false;
      } else if (
        e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A
      ) {
        player.left = false;
      }
    } else {
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        player.right = false;
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        player.left = false;
      } else if (e.getKeyCode() == KeyEvent.VK_D) {
        enemy.right = false;
      } else if (e.getKeyCode() == KeyEvent.VK_A) {
        enemy.left = false;
      }
    }
  }

  public void showGameModeSelection() {
    String[] options = { "IA", "Multiplayer Local" };
    int choice = JOptionPane.showOptionDialog(
      null,
      "Choose a game mode:",
      "Game Mode Selection",
      JOptionPane.DEFAULT_OPTION,
      JOptionPane.PLAIN_MESSAGE,
      null,
      options,
      options[0]
    );

    if (choice == 1) {
      isMultiplayer = true;
    }
  }
}
