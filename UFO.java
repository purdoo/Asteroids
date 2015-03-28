package game;

import java.util.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class UFO {
	final int[] upX = {-10, 10, 6, -6};
	final int[] upY = {-12, -12, -20, -20};
	final int[] midX = {-30, 30, 10, -10};
	final int[] midY = {0, 0, -12, -12};
	final int[] botX = {-30, 30, 14, -14};
	final int[] botY = {0, 0, 12, 12};
	
	int[] upXPts = {0,0,0,0,0,0,0,0}, upYPts = {0,0,0,0,0,0,0,0};
	int[] midXPts = {0,0,0,0,0,0,0,0}, midYPts = {0,0,0,0,0,0,0,0};
	int[] botXPts = {0,0,0,0,0,0,0,0}, botYPts = {0,0,0,0,0,0,0,0};
	
	int screenWidth, screenHeight, shotDelay, delayCounter, health;
	double x, y, xVel;
	
	boolean isActive;
	
	Polygon ufoUp;
	Polygon ufoMid;
	Polygon ufoBot;
	
	Vector<Bullet> bulletVector;
	
	public UFO(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		this.ufoUp = new Polygon(upXPts, upYPts, 4);
		this.ufoMid = new Polygon(midXPts, midYPts, 4);
		this.ufoBot = new Polygon(botXPts, botYPts, 4);
		//randomize these
		this.x = Math.random()*(this.screenWidth) < this.screenWidth / 2 ? 0 : this.screenWidth;
		this.y = Math.random()*(this.screenHeight);
		this.xVel = this.x == 0 ? .5 : -.5; //sets velocity based on what side of the screen it spawns on
		
		for(int i = 0; i < 4; i++){
			this.upXPts[i] = (int) (upX[i] + x);
			this.upYPts[i] = (int) (upY[i] + y);
		}
		for(int i = 0; i < 4; i++){
			this.midXPts[i] = (int) (midX[i] + x);
			this.midYPts[i] = (int) (midY[i] + y);
		}
		for(int i = 0; i < 4; i++){
			this.botXPts[i] = (int) (botX[i] + x);
			this.botYPts[i] = (int) (botY[i] + y);
		}
		
		bulletVector = new Vector<Bullet>();
		this.shotDelay = 95;
		this.delayCounter = this.shotDelay;
		this.health = 3;
		this.isActive = false;
	}

	public void move(Ship ship){
		if(delayCounter > 0){
			delayCounter -= 1;
		}

		x += xVel;
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

		if(delayCounter == 0){
			double deltaY = this.y - ship.y;
			double deltaX = this.x - ship.x;
			double shotAngle = Math.atan(deltaY / deltaX);
			if(ship.x < this.x){
				shotAngle += Math.PI;
			}
			bulletVector.add(new Bullet(45, 8, x, y, xVel, 0.0, shotAngle, this.screenWidth, this.screenHeight));
			delayCounter = shotDelay;
		}
	}

	
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		for(int i = 0; i < 4; i++){
			ufoUp.xpoints[i] = (int) (upX[i] + x);
			ufoUp.ypoints[i] = (int) (upY[i] + y);
		}
		for(int i = 0; i < 4; i++){
			ufoMid.xpoints[i] = (int) (midX[i] + x);
			ufoMid.ypoints[i] = (int) (midY[i] + y);
		}
		for(int i = 0; i < 4; i++){
			ufoBot.xpoints[i] = (int) (botX[i] + x);
			ufoBot.ypoints[i] = (int) (botY[i] + y);
		}
		g.setColor(Color.cyan);
		g.drawPolygon(ufoUp);
		g.drawPolygon(ufoMid);
		g.drawPolygon(ufoBot);
		g2.setColor(Color.red);
		g2.draw(new Ellipse2D.Double(this.x - 30, this.y - 16, 60, 30));
		g2.setColor(Color.yellow);
		g2.draw(new Ellipse2D.Double(this.x, this.y, 2, 2));

	}
	
	//detects collisions with the ship
	public boolean shipCollision(Ship ship){
		double xVal = (this.x - ship.x) / 30.0;
		double yVal = (this.y - ship.y) / 15.0;
		double xSq = Math.pow(xVal, 2);
		double ySq = Math.pow(yVal, 2);
		double total = xSq + ySq;
		if(total <= 1.0){
			return true;
		}
		return false;
	}
	
	public boolean bulletCollision(Bullet bullet){
		double xVal = (this.x - bullet.getX()) / 30.0;
		double yVal = (this.y - bullet.getY()) / 15.0;
		double xSq = Math.pow(xVal, 2);
		double ySq = Math.pow(yVal, 2);
		double total = xSq + ySq;
		if(total <= 1.0){
			return true;
		}
		return false;
	}
}