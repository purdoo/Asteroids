package game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SaveMenu extends JFrame{
	private JPanel panel;
	private JButton getDir, save, cancel;

	private JLabel title, blank1, blank2;

	private JFileChooser fileChooser = new JFileChooser();

	private File saveFile;

	public SaveMenu(final Main main){
		fileChooser = new JFileChooser();

		title = new JLabel("Save Menu", JLabel.CENTER);
		blank1 = new JLabel("", JLabel.CENTER);
		blank2 = new JLabel("", JLabel.CENTER);

		getDir = new JButton("Choose Save File");
		getDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.showDialog(null, "Choose");
				saveFile = fileChooser.getSelectedFile();
			}
		});

		save = new JButton("Save Game");
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				writeFile(main);
			}
		});

		cancel = new JButton("Back");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});

		setTitle("Save Menu");
		setSize(300, 300);
		setLocation(100, 300);
		setVisible(false);

		panel = new JPanel();
		panel.setLayout(new GridLayout(2, 3, 5, 5));
		panel.add(blank1);
		panel.add(title);
		panel.add(blank2);
		panel.add(getDir);
		panel.add(save);
		panel.add(cancel);

		add(panel, BorderLayout.CENTER);
		this.pack();
	}

	private void writeFile(Main main){
		try{
			int temp = 0;
			if(!saveFile.exists())
				saveFile.createNewFile();

			FileWriter fileW = new FileWriter(saveFile.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(fileW);
			
			buffer.write("main\n");
			buffer.write(String.format("%d,%d,%d,%d,%d\n", main.level, main.score, main.initAst, main.prevAst, main.shipReset));
			if(main.paused)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,",temp));
			if(main.unlimLives)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d\n", temp));
			buffer.write("grav\n");
			buffer.write(String.format("%.3f,%.3f\n", main.gravObj.x, main.gravObj.y));
			buffer.write(String.format("%d\n", main.gravObj.size));
			if(main.gravObj.isVisible)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,", temp));
			if(main.gravObj.isActive)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d\n",  temp));
			buffer.write("rogue\n");
			buffer.write(String.format("%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f\n", main.rogue.x, main.rogue.y, main.rogue.angle, main.rogue.xVel, main.rogue.yVel, main.rogue.boost, main.rogue.vDecay));
			buffer.write(String.format("%d,%d,%d,%d,%d,%d\n", main.rogue.i, main.rogue.j, main.rogue.radius, main.rogue.health, main.rogue.shotDelay, main.rogue.delayCounter));
			if(main.rogue.isActive)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d\n", temp));
			for(int i = 0; i < main.rogue.bulletVector.size(); i++){
				Bullet tempBullet = main.rogue.bulletVector.get(i);
				buffer.write(String.format("bullet\n"));
				buffer.write(String.format("%.3f,%.3f,%.3f,%.3f\n", tempBullet.getX(), tempBullet.getY(), tempBullet.getxV(), tempBullet.getyV()));
				buffer.write(String.format("%d,%d\n", tempBullet.getDuration(), tempBullet.getSpeed()));
			}
			buffer.write("UFO\n");
			buffer.write(String.format("%.3f,%.3f,%.3f\n", main.ufo.x, main.ufo.y, main.ufo.xVel));
			buffer.write(String.format("%d,%d,%d\n", main.ufo.shotDelay, main.ufo.delayCounter, main.ufo.health));
			if(main.ufo.isActive)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d\n", temp));
			for(int i = 0; i < main.ufo.bulletVector.size(); i++){
				Bullet tempBullet = main.ufo.bulletVector.get(i);
				buffer.write(String.format("bullet\n"));
				buffer.write(String.format("%.3f,%.3f,%.3f,%.3f\n", tempBullet.getX(), tempBullet.getY(), tempBullet.getxV(), tempBullet.getyV()));
				buffer.write(String.format("%d,%d\n", tempBullet.getDuration(), tempBullet.getSpeed()));
			}
			buffer.write("ship\n");
			buffer.write(String.format("%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f\n", main.ship.x, main.ship.y, main.ship.angle, main.ship.rotation, main.ship.xVel, main.ship.yVel, main.ship.accelSpeed, main.ship.vDecay));
			buffer.write(String.format("%d,%d,%d,%d,%d,%d,%d,%d\n", main.ship.i, main.ship.j, main.ship.delayCounter, main.ship.bulletCounter, main.ship.shotDelay, main.ship.bigDelayCounter, main.ship.bigDelayCounterDelay, main.ship.health));
			if(main.ship.accel)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,", temp));
			if(main.ship.left)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,", temp));
			if(main.ship.right)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,", temp));
			if(main.ship.canShoot)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,", temp));
			if(main.ship.isShooting)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,", temp));
			if(main.ship.shot)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d,", temp));
			if(main.ship.isActive)
				temp = 1;
			else
				temp = 0;
			buffer.write(String.format("%d\n", temp));
			for(int i = 0; i < main.ship.bulletVector.size(); i++){
				Bullet tempBullet = main.ship.bulletVector.get(i);
				buffer.write(String.format("bullet\n"));
				buffer.write(String.format("%.3f,%.3f,%.3f,%.3f\n", tempBullet.getX(), tempBullet.getY(), tempBullet.getxV(), tempBullet.getyV()));
				buffer.write(String.format("%d,%d\n", tempBullet.getDuration(), tempBullet.getSpeed()));
			}
			buffer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
