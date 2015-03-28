package game;

import java.util.*;
import java.awt.*;

public class Rogue {
	final int[] origXPts = {14, -10, -6, -10};
	final int[] origYPts = {0, -8, 0, 8};
	int[] xPts = {0, 0, 0, 0}, yPts = {0, 0, 0, 0}; //points used for initializing the ship
	double x, y, angle, xVel, yVel, boost, vDecay;
	int i, j, screenWidth, screenHeight, radius, health, shotDelay, delayCounter;
	boolean isActive;

	Polygon ship;

	Vector<Bullet> bulletVector;

	public Rogue(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		this.x = Math.random() * width;
		this.y = Math.random() * height;
		this.radius = 6;
		this.angle = Math.random() * Math.PI; //starts the ship pointing up 
		this.ship = new Polygon(xPts, yPts, 4); //makes a polygon of the ship
		this.xVel = 0.0;
		this.yVel = 0.0;
		this.boost = 3.0;
		this.vDecay = 0.98; //slows the ship to make aiming easier. Nerfed Physics
		for(i = 0; i < 4; i++){
			this.ship.xpoints[i] = (int) (origXPts[i] * Math.cos(angle) - origYPts[i] * Math.sin(angle) + x);
			this.ship.ypoints[i] = (int) (origXPts[i] * Math.sin(angle) + origYPts[i] * Math.cos(angle) + y);
		}
		bulletVector = new Vector<Bullet>();
		this.health = 1;
		this.shotDelay = 80;
		this.delayCounter = this.shotDelay;
		this.isActive = false;
	}

	public void move(){
		if(delayCounter > 0){
			delayCounter -= 1;
		}
		if(Math.abs(xVel) <= .2 && Math.abs(yVel) <= .2){
			this.angle = Math.random() * Math.PI;
			this.xVel = this.boost * Math.cos(this.angle);
			this.yVel = this.boost * Math.sin(this.angle);
		}
		x += xVel;
		y += yVel;
		xVel *= vDecay;
		yVel *= vDecay;
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
			bulletVector.add(new Bullet(60, 8, x, y, xVel, yVel, angle, screenWidth, screenHeight));
			delayCounter = shotDelay;
		}
	}

	public void draw(Graphics g){
		for(i = 0; i < 4; i++){
			ship.xpoints[i] = (int) (origXPts[i] * Math.cos(angle) - origYPts[i] * Math.sin(angle) + x);
			ship.ypoints[i] = (int) (origXPts[i] * Math.sin(angle) + origYPts[i] * Math.cos(angle) + y);
		}
		g.setColor(Color.GREEN);
		g.drawPolygon(ship);
	}

	public boolean shipCollision(Ship ship){
		double xVal = (this.x - ship.x) / this.radius;
		double yVal = (this.y - ship.y) / this.radius;
		double xSq = Math.pow(xVal, 2);
		double ySq = Math.pow(yVal, 2);
		double total = xSq + ySq;
		if(total <= 1.0){
			return true;
		}
		return false;
	}


	public boolean bulletCollision(Bullet bullet){
		//uses distance formula to calculate collision
		//assumes bullet has size of 0
		double xVal = (this.x - bullet.getX()) / this.radius;
		double yVal = (this.y - bullet.getY()) / this.radius;
		double xSq = Math.pow(xVal, 2);
		double ySq = Math.pow(yVal, 2);		
		double total = xSq + ySq;
		if(total <= 1.0){
			return true;
		}
		else{
			return false;
		}
	}
}
