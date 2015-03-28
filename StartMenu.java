package game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

@SuppressWarnings("serial")
public class StartMenu extends JFrame{
	JPanel panel;
	JCheckBox ship;
	JCheckBox gravExist, gravVisible, unlimLives;
	JLabel title, astLabel;
	JTextField astText;
	JButton astEnter, resetHigh, loadGame, startGame;
	
	private JFileChooser fileChooser = new JFileChooser();
	
	private File saveFile;

	private boolean ge = false, gv = false, ul = false, sb = false;
	
	public StartMenu(final Main main){

		title = new JLabel("Start Menu", JLabel.CENTER);
		
		ship = new JCheckBox("Check for two ships", false);
		ship.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!sb){
					main.oship.isActive = true;
					sb = true;
				}
				else{
					main.oship.isActive = false;
					sb = false;
				}
			}
		});
		
		gravExist = new JCheckBox("Gravity object exists", false);
		gravExist.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!ge){
					//set gravity object to exist
					main.gravObj.isActive = true;
					ge = true;
				}
				else{
					main.gravObj.isActive = false;
					ge = false;
				}
			}
		});
		
		gravVisible = new JCheckBox("Gravity object is visible", false);
		gravVisible.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!gv){
					//set gravity object visible
					main.gravObj.isVisible = true;
					gv = true;
				}
				else{
					gv = false;
					main.gravObj.isVisible = false;
				}
			}
		});
		unlimLives = new JCheckBox("Unlimited lives", false);
		unlimLives.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!ul){
					//set unlimited lives
					ul = true;
					main.unlimLives = true;
				}
				else if (ul){
					ul = false;
					main.unlimLives = false;
				}
			}
		});
		
		astLabel = new JLabel("Number of asteroids: ");
		astText = new JTextField();
		astEnter = new JButton("Press to enter number of Asteroids");
		
		astEnter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//set number of asteroids in main
				if(!astText.getText().isEmpty()){
					int temp = Integer.parseInt(astText.getText());
					main.initAst = temp;
				}
			}
		});
		
		resetHigh = new JButton("Reset high score");
		resetHigh.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				main.hs.reset();
			}
		});
		loadGame = new JButton("Load game");
		loadGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.showDialog(null, "Choose");
				saveFile = fileChooser.getSelectedFile();
				loadFile(main);
			}
		});
		startGame = new JButton("Start Game");
		startGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});
		setTitle("Start Menu");
		setSize(300, 700);
		setLocation(10, 200);
		setVisible(true);

		panel = new JPanel();
		panel.setLayout(new GridLayout(11, 0, 5, 5));
		panel.add(title);
		panel.add(ship);
		panel.add(gravExist);
		panel.add(gravVisible);
		panel.add(unlimLives);
		panel.add(astLabel);
		panel.add(astText);
		panel.add(astEnter);
		panel.add(resetHigh);
		panel.add(loadGame);
		panel.add(startGame);

		add(panel, BorderLayout.CENTER);
		this.pack();
	}

	public void check(){
		if(this.isActive()){
			if(gravExist.isSelected()){
				//set gravity object to exist
			}
			if(gravVisible.isSelected()){
				//set gravity object to be visible
			}
			if(unlimLives.isSelected()){
				//set unlimited lives
			}
		}
	}
	
	private void loadFile(Main main){
		
	}
}