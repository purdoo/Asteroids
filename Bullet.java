package game;

import java.awt.*;

public class Bullet {
	private double x, y, xVel, yVel;
	private int screenWidth, screenHeight, duration, speed = 10;
	
	public Bullet(double x, double y, double xVel, double yVel, double angle, int width, int height){
		this.x = x;
		this.y = y;
		this.xVel = this.speed * Math.cos(angle) + yVel;
		this.yVel = this.speed * Math.sin(angle) + yVel;
		this.screenWidth = width;
		this.screenHeight = height;
		this.duration = 75; //playing with values
	}
	
	public Bullet(int duration, int speed, double x, double y, double xVel, double yVel, double angle, int width, int height){
		this.x = x;
		this.y = y;
		this.xVel = this.speed * Math.cos(angle) + yVel;
		this.yVel = this.speed * Math.sin(angle) + yVel;
		this.screenWidth = width;
		this.screenHeight = height;
		this.duration = duration; //playing with values
		this.speed = speed;
	}
	
	
	public void move(){
		duration -= 1;
		x += xVel;
		y += yVel;
		if(x < 0){
			x += screenWidth;
		}
		else if (x > screenWidth){
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
		g.setColor(Color.red);
		//TODO fix bullet, maybe polygon?
		g.fillOval((int)x, (int) y,  4,  4);
	}
	
	public int getDuration(){
		return this.duration;
	}
	
	public void setDuration(int duration){
		this.duration = duration;
	}
	
	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	public double getxV(){
		return this.xVel;
	}
	public double getyV(){
		return this.yVel;
	}
	public int getSpeed(){
		return this.speed;
	}
}