package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class PauseMenu extends JFrame {
	JPanel panel;
	
	JLabel title, blank1, blank2;
	
	JButton cont, save, quit;
	
	JOptionPane quitPane;
	
	public PauseMenu(final Main main){
		title = new JLabel("Pause Menu", JLabel.CENTER);
		blank1 = new JLabel("", JLabel.CENTER);
		blank2 = new JLabel("", JLabel.CENTER);
		
		cont = new JButton("Continue");
		cont.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//do stuff
				setVisible(false);
				main.paused = false;
			}
		});
		
		save = new JButton("Save");
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				main.savemenu.setVisible(true);
			}
		});
		
		quit = new JButton("Quit");
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Are you sure?", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION){
					String name;
					do{
						name = JOptionPane.showInputDialog(null, "Enter a 5 character Name");
						while(name == null){
							name = JOptionPane.showInputDialog(null, "Enter a 5 character Name");
						}
					}while(name.length() >= 6 || name.length() <= 0);
					if(main.hs.addScore(main.score, name)){
						String temp = main.hs.getScores();
						JOptionPane.showMessageDialog(null, "Congrats! Your score of " + main.score + " made it in the Top 10:\n" + temp);
					}
					else{
						String temp = main.hs.getScores();
						JOptionPane.showMessageDialog(null, "Sorry! your score of " + main.score + " didn't make it into the Top 10:\n" + temp);
					}
					main.hs.writeFile();
					System.exit(0);
				}
			}
		});
		
		setTitle("Pause Menu");
		setSize(300, 300);
		setLocation(50, 200);
		setVisible(false);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(2, 3, 5, 5));
		panel.add(blank1);
		panel.add(title);
		panel.add(blank2);
		panel.add(cont);
		panel.add(save);
		panel.add(quit);
		
		add(panel, BorderLayout.CENTER);
		this.pack();
	}
	
	
}
