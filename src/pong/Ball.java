package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {

  public double x, y;
  public int width, height;

  public double dx, dy;
  public double speed = 1.8;

  private int minAngle = 70;
  private int maxAngle = 120;
  private int angle = (int) (Math.random() * (maxAngle - minAngle) + minAngle);

  public Ball(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = 4;
    this.height = 4;

    if (Math.random() < 0.5) {
      dx = Math.cos(Math.toRadians(angle));
      dy = Math.sin(Math.toRadians(angle));
    } else {
      dx = -Math.cos(Math.toRadians(angle));
      dy = -Math.sin(Math.toRadians(angle));
    }
  }

  public void tick() {
    x += dx * speed;
    y += dy * speed;

    if (x + (dx * speed) + width >= Game.WIDTH) {
      dx *= -1;
    } else if (x + (dx * speed) < 0) {
      dx *= -1;
    }

    if (y >= Game.HEIGHT) {
      // Ponto do inimigo.
      System.out.println("Ponto do inimigo!");

      new Game();
      Game.setEnemyPoints(Game.getEnemyPoints() + 1);

      return;
    } else if (y < 0) {
      // Ponto nosso!
      System.out.println("Ponto nosso!");

      new Game();
      Game.setPlayerPoints(Game.getPlayerPoints() + 1);

      return;
    }

    if (y + height > Game.HEIGHT - 10) {
      if (
        y < Game.player.y + Game.player.height &&
        x + width >= Game.player.x &&
        x <= Game.player.x + Game.player.width &&
        y + height >= Game.player.y
      ) {
        // Ajustar a posição da bola para que ela não entre dentro do jogador
        if (y + height <= Game.player.y + Game.player.height / 2) {
          y = Game.player.y - height;
          dy *= -1;

          if (Game.player.right) {
            dx = Math.cos(Math.toRadians(45));
          } else if (Game.player.left) {
            dx = Math.cos(Math.toRadians(135));
          }
        } else {
          dx *= -1;
        }
      }
    } else if (y < 10) {
      if (
        y < Game.enemy.y + Game.enemy.height &&
        x + width >= Game.enemy.x &&
        x <= Game.enemy.x + Game.enemy.width &&
        y + height >= Game.enemy.y
      ) {
        // Ajustar a posição da bola para que ela não entre dentro do inimigo
        if (y >= Game.enemy.y + Game.enemy.height / 2) {
          y = Game.enemy.y + Game.enemy.height;
          dy *= -1;

          if (Game.enemy.right) {
            dx = Math.cos(Math.toRadians(45));
          } else if (Game.enemy.left) {
            dx = Math.cos(Math.toRadians(135));
          }
        }
      } else {
        dx *= -1;
      }
    }
  }

  public void render(Graphics g) {
    g.setColor(Color.yellow);
    g.fillRect((int) x, (int) y, width, height);
  }
}
