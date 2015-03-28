package game;

import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class Ship{
	final int[] origXPts = {14, -10, -6, -10};
	final int[] origYPts = {0, -8, 0, 8};
	int[] xPts = {0, 0, 0, 0}, yPts = {0, 0, 0, 0}; //points used for initializing the ship
	double x, y, angle, rotation, xVel, yVel, accelSpeed, vDecay;
	int radius, i, j, screenWidth, screenHeight, delayCounter, bulletCounter, shotDelay, bigDelayCounter, bigDelayCounterDelay, health;
	
	boolean accel, left, right, canShoot, isShooting, shot, isActive;
	
	Polygon ship;
	
	//holds bullets
	Vector<Bullet> bulletVector;
	
	public Ship(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		this.x = width/2 + 50;
		this.y = height/2 ;
		this.radius = 6;
		this.angle = 1.5 * Math.PI; //starts the ship pointing up 
		this.rotation = 0.08; //a hardcoded turn speed. may need to be adjusted 
		this.ship = new Polygon(xPts, yPts, 4); //makes a polygon of the ship
		this.xVel = 0.0;
		this.yVel = 0.0;
		this.accelSpeed = 0.22; //acceleration speed. may need to be adjusted
		this.vDecay = 0.98; //slows the ship to make aiming easier. Nerfed Physics
		for(i = 0; i < 4; i++){
			this.ship.xpoints[i] = (int) (origXPts[i] * Math.cos(angle) - origYPts[i] * Math.sin(angle) + x);
			this.ship.ypoints[i] = (int) (origXPts[i] * Math.sin(angle) + origYPts[i] * Math.cos(angle) + y);
		}
		this.accel = false;
		this.left = false;
		this.right = false;
		
		//counters and stuff to do with Bullet
		//TODO comment this
		this.shot = false;
		this.canShoot = true;
		this.isShooting = false;
		this.shotDelay = 5;
		this.bulletCounter = 0;
		this.delayCounter = shotDelay;
		this.bigDelayCounter = 0;
		this.bigDelayCounterDelay = 35;
		bulletVector = new Vector<Bullet>();
		this.isActive = true;
		this.health = 3;
	}
	
	
	//TODO explain this
	public void move(){
		if(delayCounter > 0){
			delayCounter -= 1;
		}
		if(left){
			angle -= rotation;
		}
		if(right){
			angle += rotation;
		}
		if(angle > (2*Math.PI)){
			angle -= (2*Math.PI);
		}
		else if(angle < 0){
			angle += (2 * Math.PI);
		}
		if(accel){
			xVel += accelSpeed * Math.cos(angle);
			yVel += accelSpeed * Math.sin(angle);
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
		if(bulletCounter != 4){
			if(isShooting && canShoot && bulletCounter < 4 && delayCounter == 0){
				bulletVector.add(new Bullet(x, y, xVel, yVel, angle, screenWidth, screenHeight));			
				bulletCounter += 1;
				delayCounter = shotDelay;
			}
		}
		else{
			//System.out.println("WAIIIITITTT");
			bigDelayCounter -= 1;
			if(bigDelayCounter <= 0){
				bulletCounter = 0;
				bigDelayCounter = bigDelayCounterDelay;
			}
		}
		//System.out.println(bulletCounter);
	}
	
	public void gravMove(Gravity gravObj){
		if(delayCounter > 0){
			delayCounter -= 1;
		}
		if(left){
			angle -= rotation;
		}
		if(right){
			angle += rotation;
		}
		if(angle > (2*Math.PI)){
			angle -= (2*Math.PI);
		}
		else if(angle < 0){
			angle += (2 * Math.PI);
		}
		if(accel){
			xVel += accelSpeed * Math.cos(angle);
			yVel += accelSpeed * Math.sin(angle);
		}
		//x += xVel;
		//y += yVel;
		
		
		double tempX = this.x - gravObj.x;
		double tempY = this.y - gravObj.y;
		double tempDist = Math.sqrt(Math.pow(tempX, 2) + Math.pow(tempY, 2));
		double sq = 25 / Math.pow(tempDist,  2);
		//System.out.println(String.format("(xVel, yVel, tempx, tempy, dist, sq)"));
		//System.out.println(String.format("(%.3f,  %.3f, %.3f, %.3f, %.3f, %f)", xVel, yVel, tempX, tempY, tempDist, sq));
		
		double xReal = xVel - (tempX * sq);
		double yReal = yVel - (tempY * sq);
	
		x += xReal;
		y += yReal;
		xVel *= this.vDecay;
		yVel *= this.vDecay;

		
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
		if(bulletCounter != 4){
			if(isShooting && canShoot && bulletCounter < 4 && delayCounter == 0){
				bulletVector.add(new Bullet(x, y, xVel, yVel, angle, screenWidth, screenHeight));			
				bulletCounter += 1;
				delayCounter = shotDelay;
			}
		}
		else{
			//System.out.println("WAIIIITITTT");
			bigDelayCounter -= 1;
			if(bigDelayCounter <= 0){
				bulletCounter = 0;
				bigDelayCounter = bigDelayCounterDelay;
			}
		}
	}
	
	public void draw(Graphics g){
		for(i = 0; i < 4; i++){
			ship.xpoints[i] = (int) (origXPts[i] * Math.cos(angle) - origYPts[i] * Math.sin(angle) + x);
			ship.ypoints[i] = (int) (origXPts[i] * Math.sin(angle) + origYPts[i] * Math.cos(angle) + y);
		}
		g.setColor(Color.WHITE);
		g.drawPolygon(ship);
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_UP){
			this.accel = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			this.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			this.right = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			this.isShooting = true;
		}
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_UP){
			this.accel = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			this.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			this.right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			this.isShooting = false;
		}
	}
	
	public boolean gravCollision(Gravity grav){
		//uses distance formula to calculate collision
		//assumes bullet has size of 0
		double xVal = (this.x - grav.x) / this.radius;
		double yVal = (this.y - grav.y) / this.radius;
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