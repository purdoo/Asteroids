package game;

import java.awt.Color;
import java.awt.Graphics;

public class Gravity {
	double x, y;
	int screenWidth, screenHeight, size;
	
	boolean isVisible, isActive;
	
	public Gravity(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		this.x = width / 2.0;
		this.y = height / 2.0;
		this.isVisible = false;
		this.isActive = false;
		this.size = 1;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.PINK);
		g.drawOval((int)this.x, (int)this.y, size, size);
	}
}
