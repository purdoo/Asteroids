package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class otherShip extends Ship {

	public otherShip(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
		this.isActive = false;
	}

	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_W){
			this.accel = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			this.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			this.right = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_Z){
			this.isShooting = true;
		}
	}

	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_W){
			this.accel = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			this.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			this.right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_Z){
			this.isShooting = false;
		}
	}

	public void draw(Graphics g){
		if(this.isActive){
			for(i = 0; i < 4; i++){
				ship.xpoints[i] = (int) (origXPts[i] * Math.cos(angle) - origYPts[i] * Math.sin(angle) + x);
				ship.ypoints[i] = (int) (origXPts[i] * Math.sin(angle) + origYPts[i] * Math.cos(angle) + y);
			}
			g.setColor(Color.MAGENTA);
			g.drawPolygon(ship);
		}
	}

}