package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Player {

  public int x, y;

  public int width, height;

  public boolean right, left;

  public Player(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = 40;
    this.height = 10;
  }

  public void tick() {
    if (right) {
      x++;
    } else if (left) {
      x--;
    }

    if (x + 40 > Game.WIDTH) {
      x = Game.WIDTH - 40;
    } else if (x < 0) {
      x = 0;
    }
  }

  public void render(Graphics g) {
    g.setColor(Color.blue);
    g.fillRect(x, y, width, height);
  }
}
