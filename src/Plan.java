import java.util.ArrayList;


public class Plan {

	private String name;
	private ArrayList<String> steps;
	private int time;
	
	public Plan(String n,int t){
		steps = new ArrayList<String>();
		name = n;
		time = t;
	}
	
	public void addStep(String s){
		steps.add(s);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getSteps() {
		return steps;
	}
	
	public boolean match(ArrayList<String> actions){
		for(String s : actions){
			if(!steps.contains(s))
				return false;
		}
		return true;
	}

	public void setSteps(ArrayList<String> steps) {
		this.steps = steps;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
		
}
