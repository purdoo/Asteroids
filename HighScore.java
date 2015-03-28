package game;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class HighScore {
	private String scoreFile = "scores.txt";
	private Vector<Integer> highScores;
	private BufferedReader buffer = null;
	private File file;
	private HashMap<Integer, String> names;

	public HighScore(){
		highScores = new Vector<Integer>();
		names = new HashMap<Integer, String>();
		try{
			file = new File(scoreFile);

			if(!file.createNewFile()){//read score file
				try{
					String line;
					//int lineCount = 1;
					int tempVal = 0;

					buffer = new BufferedReader(new FileReader(scoreFile));

					while((line = buffer.readLine()) != null){
						String[] temp = line.split(",");
						tempVal = Integer.parseInt(temp[0]);
						String tempString = temp[1];
						names.put(tempVal, tempString);
						highScores.add(tempVal);
						//lineCount += 1;
					}
				} catch(IOException ee){
					ee.printStackTrace();
				} finally{
					try{
						if(buffer != null)
							buffer.close();
					} catch(IOException eee){
						eee.printStackTrace();
					}
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public void printVect(){
		Iterator<Integer> iter = highScores.iterator();
		while(iter.hasNext()){
			int temp = iter.next();
			System.out.println("" + temp + ": " + names.get(temp));
		}
	}

	public String getScores(){
		String tempStr;
		String returnString = "";
		String nameString = " ";
		int tempInt = 0;
		Iterator<Integer> iter = highScores.iterator();
		while(iter.hasNext()){
			tempInt = iter.next();
			tempStr = Integer.toString(tempInt);
			if(this.names.get(tempInt) == null){
				nameString = " ";
			}
			else{
				nameString = this.names.get(tempInt);
			}
			tempStr += " " + nameString + "\n";
			returnString += tempStr;
		}
		return returnString;
	}
	
	private void sortVect(){
		Collections.sort(highScores);
		Collections.reverse(highScores);
	}
	
	public boolean addScore(int newScore, String name){
		highScores.add(newScore);
		this.sortVect();
		highScores.remove(highScores.size() - 1);
		if(highScores.contains(newScore)){
			names.put(newScore, name);
			return true;
		}
		return false;
	}
	
	public void writeFile(){
		Iterator<Integer> iter = highScores.iterator();
		try{
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println("outstring");
			while(iter.hasNext()){
				Integer tempInt = iter.next();
				String nameString = " ";
				if(this.names.get(tempInt) == null){
					nameString = " ";
				}
				else{
					nameString = this.names.get(tempInt);
				}
				String tempStr = tempInt.toString() + "," + nameString + "\n";
				System.out.print(tempStr);
				bw.write(tempStr);
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void reset(){
		int i = 0;
		String temp = String.format("%d, \n", 0);
		this.names.clear();
		this.highScores.clear();
		try{
			FileWriter fw = new FileWriter(file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			for(i=0; i < 10; i++){
				highScores.add(i, 0);
				bw.write(temp);
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
