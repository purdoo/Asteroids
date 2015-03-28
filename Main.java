package game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JFrame implements Runnable{
	//double buffering
	Image dbImage; 
	Graphics dbGraphics;

	//set screen size
	static int
	WIDTH = 700,
	HEIGHT = 700;

	//ship object
	Ship ship;
	otherShip oship;
	UFO ufo;
	Rogue rogue;
	Gravity gravObj;
	HighScore hs;

	private long FRAMES_PER_SECOND = 60;
	private long TICKS = 1000 / FRAMES_PER_SECOND;
	private long sleepTime = 0;
	private long gameTick = System.currentTimeMillis();

	private long spawnCount;

	Vector<Asteroid> astVector;

	Font fontBig = new Font("American Captain", Font.TRUETYPE_FONT, 75);
	Font fontSmall = new Font("American Captain", Font.TRUETYPE_FONT, 20);

	int level, score, initAst, prevAst, shipReset;
	boolean paused, unlimLives;

	Dimension screenSize = new Dimension(WIDTH, HEIGHT);

	StartMenu startmenu;
	PauseMenu pausemenu;
	SaveMenu savemenu;

	final int[] origXPts = {0, 8, 0, -8};
	final int[] origYPts = {-14, 10, 6, 10};
	int[] xPts = {0, 0, 0, 0}, yPts = {0, 0, 0, 0};

	Polygon shipLife1;
	Polygon shipLife2;
	Polygon shipLife3;

	public static void main(String[] args){
		Main main = new Main();
		//create threads
		Thread mainThread = new Thread(main);
		mainThread.start();
	}

	public Main(){
		//window initializations
		this.setTitle("Asteroids");
		this.setSize(screenSize);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setBackground(Color.BLACK);
		this.addKeyListener(new actionListener());

		this.shipLife1 = new Polygon(xPts, yPts, 4);
		this.shipLife2 = new Polygon(xPts, yPts, 4);
		this.shipLife3 = new Polygon(xPts, yPts, 4);
		for(int i=0; i < 4; i++){
			this.shipLife1.xpoints[i] = (int)origXPts[i];
			this.shipLife1.ypoints[i] = (int)origYPts[i];
			this.shipLife2.xpoints[i] = (int)origXPts[i];
			this.shipLife2.ypoints[i] = (int)origYPts[i];
			this.shipLife3.xpoints[i] = (int)origXPts[i];
			this.shipLife3.ypoints[i] = (int)origYPts[i];
		}

		//game initializations
		this.paused = true;
		this.level = 0;
		this.score = 0;
		ship = new Ship(WIDTH, HEIGHT);
		oship = new otherShip(WIDTH, HEIGHT);
		ufo = new UFO(WIDTH, HEIGHT);
		rogue = new Rogue(WIDTH, HEIGHT);
		astVector = new Vector<Asteroid>();

		//ufoVector = new Vector<UFO>();
		//rogueVector = new Vector<Rogue>();

		initAst = 3;

		startmenu = new StartMenu(this);
		startmenu.isActive();
		pausemenu = new PauseMenu(this);
		savemenu = new SaveMenu(this);

		this.spawnCount = 0;
		gravObj = new Gravity(Main.WIDTH, Main.HEIGHT);
		hs = new HighScore();
		this.shipReset = 0;
		this.unlimLives = false;
	}

	@Override
	public void paint(Graphics g){
		dbImage = createImage(getWidth(), getHeight());
		dbGraphics = dbImage.getGraphics();
		draw(dbGraphics);
		g.drawImage(dbImage, 0, 0, this);
	}

	public void draw(Graphics g){
		try{
			ship.draw(g);
			oship.draw(g);
			drawBullets(g);
			drawAsteroids(g);
			drawUFO(g);
			drawRogue(g);
			if(gravObj.isActive && gravObj.isVisible)
				gravObj.draw(g);
			g.setFont(fontSmall);
			g.setColor(Color.white);
			String position = String.format("(%.2f, %.2f)", ship.x, ship.y);
			g.drawString(position, 800, 800);


			if(ship.health == 3){
				for(int i = 0; i < 4; i++){
					this.shipLife1.xpoints[i] = (int)origXPts[i] + 30;
					this.shipLife1.ypoints[i] = (int)origYPts[i] + 70;
					this.shipLife2.xpoints[i] = (int)origXPts[i] + 50;
					this.shipLife2.ypoints[i] = (int)origYPts[i] + 70;
					this.shipLife3.xpoints[i] = (int)origXPts[i] + 70;
					this.shipLife3.ypoints[i] = (int)origYPts[i] + 70;
				}
				g.setColor(Color.WHITE);
				g.drawPolygon(shipLife1);
				g.drawPolygon(shipLife2);
				g.drawPolygon(shipLife3);
			}
			else if(ship.health == 2){
				for(int i = 0; i < 4; i++){
					this.shipLife1.xpoints[i] = (int)origXPts[i] + 30;
					this.shipLife1.ypoints[i] = (int)origYPts[i] + 70;
					this.shipLife2.xpoints[i] = (int)origXPts[i] + 50;
					this.shipLife2.ypoints[i] = (int)origYPts[i] + 70;
				}
				g.setColor(Color.WHITE);
				g.drawPolygon(shipLife1);
				g.drawPolygon(shipLife2);
			}
			else if(ship.health == 1){
				for(int i = 0; i < 4; i++){
					this.shipLife1.xpoints[i] = (int)origXPts[i] + 30;
					this.shipLife1.ypoints[i] = (int)origYPts[i] + 70;
				}
				g.setColor(Color.WHITE);
				g.drawPolygon(shipLife1);
			}
			if(paused && level == 0){
				g.setFont(fontBig);
				g.drawString("ASTEROIDS",  Main.WIDTH / 2 - 130,  Main.HEIGHT / 4);
				g.setFont(fontSmall);
				g.drawString("Press Enter to Start!",  Main.WIDTH / 2 - 80, Main.HEIGHT / 3);
			}
			if(paused && level != 0){
				g.setFont(fontBig);
				g.setColor(Color.white);
				g.drawString("PAUSED",  375,  200);
			}
			if(level != 0){
				g.setColor(Color.white);
				g.setFont(fontSmall);
				g.drawString(String.format("%d", this.score) ,  50, 50);
			}
		} catch(Exception e){
			//e.printStackTrace();
		}
		repaint();
	}


	public class actionListener extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			ship.keyPressed(e);

			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				if(paused == true)
					paused = false;
				else if(paused == false)
					paused = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if(level == 0){
					paused = false;
					levelUp();
				}
			}
		}
		@Override
		public void keyReleased(KeyEvent e){
			ship.keyReleased(e);
		}
	}

	private void levelUp(){
		int astX, astY, numAst = 0;
		this.score += this.level * 100;
		level += 1;
		ship = new Ship(WIDTH, HEIGHT);
		System.out.println("Level: " + this.level);
		if(level == 1){
			numAst = initAst;
			prevAst = numAst;
		}
		else{
			numAst = prevAst + 1;
			prevAst = numAst;
		}

		for(int i = 0; i < numAst; i++){
			//puts asteroids in random locations
			//if they are to close to the middle of the screen they are repositioned
			astX = (int)(Math.random() * WIDTH);
			astY = (int)(Math.random() * HEIGHT);
			while(astX > 300 && astX < 400){
				astX = (int)(Math.random() * WIDTH);
			}
			while(astY > 300 && astY < 400){
				astY = (int)(Math.random() * HEIGHT);
			}
			//public Asteroid(double x, double y, int size, double minV, double maxV, int health, int split)
			astVector.add(new Asteroid(astX, astY, 100, .5, 2.5, 3, 3)); //tweak velocities
		}
	}

	private void drawBullets(Graphics g){
		int i;
		for(i = 0; i < ship.bulletVector.size(); i++){
			ship.bulletVector.get(i).draw(g);
		}
		if(ufo.isActive){
			for(i = 0; i < ufo.bulletVector.size(); i++){
				ufo.bulletVector.get(i).draw(g);
			}
		}
		if(rogue.isActive){
			for(i=0; i < rogue.bulletVector.size(); i++){
				rogue.bulletVector.get(i).draw(g);
			}
		}
	}

	private void drawAsteroids(Graphics g){
		for(int i = 0; i < astVector.size(); i++){
			astVector.get(i).draw(g);
		}
	}

	private void drawUFO(Graphics g){
		if(ufo.isActive)
			ufo.draw(g);
	}

	private void drawRogue(Graphics g){
		if(rogue.isActive)
			rogue.draw(g);
	}

	private void bulletMove(){
		int temp;
		try{
			for(temp = 0; temp < ship.bulletVector.size(); temp++){
				ship.bulletVector.get(temp).move();
				if(ship.bulletVector.get(temp).getDuration() == 0){
					ship.bulletVector.remove(temp);
				}
			}
			if(ufo.isActive){
				for(temp = 0; temp < ufo.bulletVector.size(); temp++){
					ufo.bulletVector.get(temp).move();
					if(ufo.bulletVector.get(temp).getDuration() == 0){
						ufo.bulletVector.remove(temp);
					}
				}
			}
			if(rogue.isActive){
				for(temp = 0; temp < rogue.bulletVector.size(); temp++){
					rogue.bulletVector.get(temp).move();
					if(rogue.bulletVector.get(temp).getDuration() == 0){
						ufo.bulletVector.remove(temp);
					}
				}
			}
		} catch(Exception e){
			//e.printStackTrace();
		}
	}

	private void astMove(){
		int i=0, j=0, k=0;
		try{
			for(i = 0; i < astVector.size(); i++){
				astVector.get(i).move(WIDTH, HEIGHT);

				if(astVector.get(i).shipCollision(ship)){
					//TODO ship destroyed animation, decrement ship's lives
					if(ship.isActive)
						ship.health -= 1;
					ship.isActive = false;
				}
				for(j = 0; j < ship.bulletVector.size(); j++){
					if(astVector.get(i).bulletCollision(ship.bulletVector.get(j))){
						ship.bulletVector.remove(j);
						if(astVector.get(i).health > 1){
							Asteroid temp = astVector.get(i);
							for( k = 0; k < astVector.get(i).split; k++){
								//public Asteroid(double x, double y, int size, double minV, double maxV, int health, int split)
								astVector.add(new Asteroid(temp.x, temp.y, temp.size / 2, .5, 2.0, temp.health -1, temp.split));
							}
						}
						astVector.remove(i);
						j = ship.bulletVector.size();
						i -= 1;
						this.score += 5;
					}
				}
				for(j = 0; j < ufo.bulletVector.size(); j++){
					if(astVector.get(i).bulletCollision(ufo.bulletVector.get(j))){
						ufo.bulletVector.remove(j);
						if(astVector.get(i).health > 1){
							Asteroid temp = astVector.get(i);
							for( k = 0; k < astVector.get(i).split; k++){
								//public Asteroid(double x, double y, int size, double minV, double maxV, int health, int split)
								astVector.add(new Asteroid(temp.x, temp.y, temp.size / 2, .5, 2.0, temp.health -1, temp.split));
							}
						}
						astVector.remove(i);
						j = ufo.bulletVector.size();
						i -= 1;
					}
				}
				for(j = 0; j < rogue.bulletVector.size(); j++){
					if(astVector.get(i).bulletCollision(rogue.bulletVector.get(j))){
						rogue.bulletVector.remove(j);
						if(astVector.get(i).health > 1){
							Asteroid temp = astVector.get(i);
							for( k = 0; k < astVector.get(i).split; k++){
								//public Asteroid(double x, double y, int size, double minV, double maxV, int health, int split)
								astVector.add(new Asteroid(temp.x, temp.y, temp.size / 2, .5, 2.0, temp.health -1, temp.split));
							}
						}
						astVector.remove(i);
						j = rogue.bulletVector.size();
						i -= 1;
					}
				}
			}
		} catch(Exception e){
			//e.printStackTrace();

		}
	}

	private void shipMove(){
		if(gravObj.isActive){
			ship.gravMove(gravObj);
		}
		else{
			ship.move();
		}
		if(ship.gravCollision(this.gravObj)){
			if(ship.isActive){
				ship.health -= 1;
			}
			ship.isActive = false;
		}
		for(int i=0; i < ufo.bulletVector.size(); i++){
			if(ship.isActive){
				if(ship.bulletCollision(ufo.bulletVector.get(i))){
					ufo.bulletVector.remove(i);
					if(ship.isActive)
						ship.health -= 1;
					ship.isActive = false;
				}
			}
		}
		for(int i=0; i < rogue.bulletVector.size(); i++){
			if(ship.isActive){
				if(ship.bulletCollision(rogue.bulletVector.get(i))){
					rogue.bulletVector.remove(i);
					if(ship.isActive)
						ship.health -= 1;
					ship.isActive = false;
				}
			}
		}
	}

	private void ufoMove(){
		if(ufo.isActive){
			ufo.move(ship);
			if(ufo.shipCollision(ship)){
				if(ship.isActive)
					ship.health -= 1;
				ship.isActive = false;
			}
			for(int i=0; i < ship.bulletVector.size(); i++){
				if(ufo.bulletCollision(ship.bulletVector.get(i))){
					ship.bulletVector.remove(i);
					ufo.health -= 1;
					if(ufo.health <= 0){
						ufo = new UFO(Main.WIDTH, Main.HEIGHT);
						this.score += 100;
					}
				}
			}
		}
	}

	private void rogueMove(){
		if(rogue.isActive){
			rogue.move();
			if(rogue.shipCollision(ship)){
				if(ship.isActive)
					ship.health -= 1;
				ship.isActive = false;
			}
			for(int i=0; i < ship.bulletVector.size(); i++){
				if(rogue.bulletCollision(ship.bulletVector.get(i))){
					ship.bulletVector.remove(i);
					rogue.health -= 1;
					if(rogue.health <= 0){
						rogue = new Rogue(Main.WIDTH, Main.HEIGHT);
						this.score += 100;
					}
				}
			}
		}
	}

	@Override
	public void run(){
		try{
			while(true){
				if(!paused){ //run game while not paused
					if(ship.isActive){
						shipMove();
					}
					else{
						boolean bad = false;
						this.shipReset += 1;
						if(this.shipReset >= 100){
							for(int i=0; i < astVector.size(); i++){
								double dist = Math.sqrt( Math.pow((Main.WIDTH/2 + 50) - astVector.get(i).x, 2) + Math.pow(Main.HEIGHT/2 - astVector.get(i).y, 2));
								if(dist <= 50){
									bad = true;
								}
							}
							if(!bad){
								if(this.unlimLives){
									ship.health += 1;
								}
								ship.isActive = true;
								ship.x = Main.WIDTH/2 + 50;
								ship.y = Main.HEIGHT/2 ;
								ship.xVel = 0.0;
								ship.yVel = 0.0;
								this.shipReset = 0;
							}
						}
					}
					if(ship.health <= 0){
						JOptionPane.showMessageDialog(null, "You ran out of lives and reached level " + this.level);
						String name;
						do{
							name = JOptionPane.showInputDialog(null, "Enter a 5 character Name");
							while(name == null){
								name = JOptionPane.showInputDialog(null, "Enter a 5 character Name");
							}
						}while(name.length() >= 6 || name.length() <= 0);
						if(this.hs.addScore(this.score, name)){
							String temp = this.hs.getScores();
							JOptionPane.showMessageDialog(null, "Congrats! Your score of " + this.score + " made it in the Top 10:\n" + temp);
						}
						else{
							String temp = this.hs.getScores();
							JOptionPane.showMessageDialog(null, "Sorry! your score of " + this.score + " didn't make it into the Top 10:\n" + temp);
						}
						this.hs.writeFile();
						System.exit(0);
					}
					bulletMove();
					astMove();
					ufoMove();
					rogueMove();
					this.spawnCount += 1;
					if(spawnCount % 62 == 0){
						double temp = Math.random();
						if(temp < .05){
							if(!rogue.isActive){
								rogue.isActive = true;
								System.out.println("spawn rogue");
							}
						}
						else if(temp > .98){
							if(!ufo.isActive){
								ufo.isActive = true;
								System.out.println("spawn UFO");
							}
						}
					}
				}
				if(astVector.isEmpty() && !rogue.isActive && !ufo.isActive && level != 0){
					//System.out.println("levelup");
					levelUp();
				}
				else if(paused && level == 0){ //game start
					startmenu.check();
				}
				else if(paused){ //game is paused
					pausemenu.setVisible(true);
				}
				gameTick += TICKS;
				sleepTime = gameTick - System.currentTimeMillis();
				if(sleepTime >= 0){
					Thread.sleep(sleepTime);
				}
			}
		}catch(Exception e){
			//System.err.println(e.getMessage());
			e.printStackTrace();
			//StringWriter error = new StringWriter();
			//e.printStackTrace(new PrintWriter(error));
			//String errorString = error.toString();
			//System.err.println(errorString);
		}
	}
}