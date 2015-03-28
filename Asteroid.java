package game;

import java.awt.*;

public class Asteroid {
	double x, y, xVel, yVel;
	int health, split, size;

	
	public Asteroid(double x, double y, int size, double minV, double maxV, int health, int split){
		this.x = x;
		this.y = y;
		this.size = size;
		this.health = health;
		this.split = split;
		double vel = Math.random()*(maxV - minV);
		vel += minV;
		double angle = 2 * Math.PI*Math.random();
		this.xVel = vel * Math.cos(angle);
		this.yVel = vel * Math.sin(angle);
	}
	
	public void move(int screenWidth, int screenHeight){
		this.x += this.xVel;
		this.y += this.yVel;
		
		if(x < 0){
			x += screenWidth;
		}
		else if(x > screenWidth){
			x -= screenWidth;
		}
		if(y < 0){
			y += screenHeight;
		}
		else if(y > screenHeight){
			y -= screenHeight;
		}
	}
	
	
	public void draw(Graphics g){
		g.setColor(Color.gray);
		g.drawOval((int)x, (int)y, size, size);
	}
	
	public boolean shipCollision(Ship ship){
		//using distance formula to calculate collision
		//assumes the ship is a circle
		double left = Math.sqrt(Math.pow(ship.radius + this.size/2.0,  2));
		double right = Math.sqrt(Math.pow(ship.x - this.x - size/2.0, 2) + Math.pow(ship.y - this.y - size/2.0, 2));

		if(left > right)
			return true;
		else
			return false;
	}
	
	public boolean bulletCollision(Bullet bullet){
		//uses distance formula to calculate collision
		//assumes bullet has size of 0
		double left = Math.sqrt(Math.pow(this.size/2.0,  2));
		double right = Math.sqrt(Math.pow(bullet.getX() - this.x - this.size/2.0, 2) + Math.pow(bullet.getY() - this.y - size/2.0, 2));
		if(left > right){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Asteroid splitAsteroid(double minV, double maxV){
		return new Asteroid(x, y, size/2, minV, maxV, health - 1, split);
	}
	
}