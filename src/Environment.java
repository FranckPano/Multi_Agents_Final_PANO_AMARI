import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JTextArea;


public class Environment {

	public enum Room { Cuisine, Bains, Chambre, Salon, Out};

	//Entier compris en 0 et 23
	private int clock;

	private Room operatorPosition;
	private String actionPerformed;

	public String getActionPerformed() {
		return actionPerformed;
	}

	public void setActionPerformed(String actionPerformed) {
		this.actionPerformed = actionPerformed;
		actualiseRoom();
	}

	public void actualiseRoom(){
		for(Room r : Room.values())
			if(r.toString().equals(actionPerformed))
				operatorPosition = r;
	}
	
	private ArrayList<Plan> plans;

	public Environment (){
		operatorPosition = Room.Chambre;
		clock = 8;
		Simulator.clockText.setText(clock + "H");
		plans = new ArrayList<Plan>();
		openPlanFile("C:/workspace/Multi_Agents_Final_PANO_AMARI/src/Plans.txt");
	}

	public void Update(){
		if(isPlan(actionPerformed)){
			addTime(1);
			Simulator.clockText.setText(clock + "H");
		}
	}

	public ArrayList<Plan> getPlans() {
		return plans;
	}

	public void setOperatorPosition(Room operatorPosition) {
		this.operatorPosition = operatorPosition;
	}

	public void addTime (int time){
		clock += time;
		clock = clock %24;
	}

	public int getClock() {
		return clock;
	}

	public void setClock(int clock) {
		this.clock = clock;
	}

	public Room getOperatorPosition() {
		return operatorPosition;
	}

	public void setPositionOperator(Room positionOperator) {
		this.operatorPosition = positionOperator;
	}

	public void openPlanFile(String path){
		try{
			InputStream ips= new FileInputStream(path); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				int planTime = Integer.parseInt(ligne.substring(ligne.lastIndexOf('>')+1));
				String planTitle = ligne.substring(ligne.indexOf('>')+1, ligne.lastIndexOf('>')); 
				ligne = ligne.substring(0,ligne.indexOf('>'));
				ArrayList<String> toAdd = new ArrayList<String>(Arrays.asList(ligne.split(",")));

				Plan p = new Plan(planTitle,planTime);
				for (String s : toAdd){
					p.addStep(s);
				}
				plans.add(p);
				//System.out.println("plan: ," + planTitle +", et time: " + planTime);
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

	public void printPlans(){
		for(Plan p : plans){
			System.out.println(p.getName() +", " + p.getTime() + "h");
			for(String s : p.getSteps()){
				System.out.println("-"+s);
			}
			System.out.println("---------------------------------------");
		}
	}

	public boolean isPlan(String s){
		for(Plan p : plans)
			if(p.getName().equals(s))
				return true;
		return false;
	}

	public ArrayList<String> getFacts() {
		ArrayList<String> toReturn = new ArrayList<String>();

		toReturn.add("Last" + actionPerformed);
		toReturn.add("Time"+clock);

		return toReturn;
	}

}
