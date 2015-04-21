import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Observator {

	private enum State { guessPlan, guessNext};

	private Environment environment;
	private ArrayList<Plan> plans; 
	private HashMap<Plan, Float> previsions;

	private Environment.Room operatorPosition;
	private ArrayList<String> actions_performed; 

	private Plan expected;
	private Plan nextExpected;
	State state = State.guessPlan;


	/*Tableau représentant les probabilités de chaque plan en fonction de l'heure*/
	private HashMap[] knowledge; 

	public Observator (Environment e){
		expected = new Plan("",0);
		nextExpected = new Plan("",0);
		knowledge = new HashMap[24];
		environment = e;
		plans = environment.getPlans();
		initKnowledge();
		previsions = new HashMap<Plan,Float>();
		actions_performed = new ArrayList<String>();
	}

	@SuppressWarnings("unchecked")
	public void initKnowledge(){
		for(int i=0; i<knowledge.length; i++){
			knowledge[i] = new HashMap<String, Integer>();
			for(Plan p : plans){
				knowledge[i].put(p.getName(),0);
			}
		}
	}

	public Environment.Room roomSensor(){
		return environment.getOperatorPosition();
	}

	public String actionSensor(){
		return environment.getActionPerformed();
	}

	public boolean isPlan(String s){
		for(Plan p : plans)
			if(p.getName().equals(s))
				return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public void Update() {
		operatorPosition = roomSensor();
		String lastAction = actionSensor();
		int time = environment.getClock();

		/*Ajout a la table des connaissances*/
		if(isPlan(lastAction)){
			int count = (int) knowledge[time].get(lastAction);
			knowledge[time].remove(lastAction);
			knowledge[time].put(lastAction, count+1);
			state = State.guessNext;
		}

		/*Si un plan a été fini*/
		if(!isPlan(lastAction)){
			if(actions_performed.size()!=0 && isPlan(actions_performed.get(actions_performed.size()-1))){
				Simulator.observatorText.append("------------------------ " + time + "H ------------------------\n");
				actions_performed.clear();
				expected = new Plan("",0);
				nextExpected = new Plan("",0);
				previsions.clear();
				state = State.guessPlan;
			}
		}

		actions_performed.add(actionSensor());

		/*En mode determination de plan en cours*/
		//TODO:proposition d'actions
		//TODO:Utilisation des règles
		if(state.equals(State.guessPlan)){
			int nbTotal = totalAtTime(time);
			
			for(Plan p : plans){
				if(p.match(actions_performed)){
					//print(time);
					System.out.println("-------------------------------------------------");
					for(String s : actions_performed)
						System.out.println(s);
					System.out.println("plan:" + p.getName());
					if(nbTotal!=0){
						previsions.put(p,(float) ((int)knowledge[time].get(p.getName())/(float)nbTotal));
					}
					else{
						previsions.put(p,0.f);
					}
				}
			}
			float compare = -1.f;
			Plan actualExpected = expected;
			Set cles = previsions.keySet();
			Iterator it = cles.iterator();
			while (it.hasNext()){
				Plan cle = (Plan) it.next();
				if(previsions.get(cle) > compare){
					expected = cle;
					compare = previsions.get(cle);
				}
			}
			if(!expected.getName().equals(actualExpected.getName())){
				if(!expected.getName().equals(""))
					Simulator.observatorText.append("Prévision:\n" +expected.getName() +"\n");
				if(previsions.get(expected) != null && previsions.get(expected) != 0.0f)
					Simulator.observatorText.append(" avec " + previsions.get(expected) + "%\n");
			}
		}
		
		/*En mode determination du plan suivant*/
		//TODO: Propositions d'actions
		else if(state.equals(State.guessNext)){
			for(Plan p : plans)
				if(p.getName().equals(lastAction))
					expected = p;
			time += expected.getTime() - nbIteration(actions_performed, expected.getName()) +1;
			time = time%24;

			int total = totalAtTime(time);
			Plan p = nextExpected;
			nextExpected = getMaxProb(time);
			//print(time);

			if(!nextExpected.getName().equals(p.getName())){
				Simulator.observatorText.append("Prochain plan à " + time + "H : \n" + nextExpected.getName() +"\n");
				if(total !=0)
					Simulator.observatorText.append("probabilité : " + (((int)knowledge[time].get(nextExpected.getName()))/
							total)*100 + "%\n");
			}

		}
	}

	public int nbIteration(ArrayList<String> ls, String s){
		int toReturn = 0;
		for(String t : ls)
			if(t.equals(s))
				toReturn ++;
		return toReturn;
	}

	public void print(int time){

		Set cles = knowledge[time].keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
			String cle = (String) it.next();
			System.out.println(cle + " : " +(int)knowledge[time].get(cle));
		}
	}

	public int totalAtTime(int time){
		int toReturn = 0;

		Set cles = knowledge[time].keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
			String cle = (String) it.next();
			toReturn += (int)knowledge[time].get(cle);
		}
		return toReturn;	
	}

	public Plan getMaxProb(int time){
		String toReturn = "";
		int compare = -1;
		Set cles = knowledge[time].keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
			String cle = (String) it.next();
			int tempo = (int)knowledge[time].get(cle);
			if(tempo> compare){
				toReturn = cle;
				compare = tempo;
			}
		}
		
		for(Plan p : plans)
			if(p.getName().equals(toReturn))
				return p;
		
		return new Plan("",0);
	}

}
