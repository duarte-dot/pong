package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy {

  public double x, y;

  public int width, height;

  public boolean right, left;

  public Enemy(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = 40;
    this.height = 10;
  }

  public void tick() {
    if (!Game.isMultiplayer) {
      x += (Game.ball.x - x - 6) * 0.08;

      if (x + width > Game.WIDTH) {
        x = Game.WIDTH - width;
      } else if (x < 0) {
        x = 0;
      }
    } else {
      if (right) {
        x+=2;
      } else if (left) {
        x-=2;
      }

      if (x + 40 > Game.WIDTH) {
        x = Game.WIDTH - 40;
      } else if (x < 0) {
        x = 0;
      }
    }
  }

  public void render(Graphics g) {
    g.setColor(Color.red);
    g.fillRect((int) x, (int) y, width, height);
  }
}
