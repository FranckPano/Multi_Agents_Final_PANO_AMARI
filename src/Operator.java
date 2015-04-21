import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;


public class Operator {

	private Environment environment;
	private ArrayList<Plan> plans; 

	LinkedList<String> queue;

	ArrayList<String>[] probPlans = new ArrayList[24];

	private ArrayList<Regle> liste_regles;

	private int repas = 0;

	public Operator (Environment e){
		environment = e;
		plans = environment.getPlans();
		liste_regles = new ArrayList<Regle>();
		queue = new LinkedList<String>();
		readRules("C:/workspace/Multi_Agents_Final_PANO_AMARI/src/OperatorRules.txt");
		openPlansFile("C:/workspace/Multi_Agents_Final_PANO_AMARI/src/Logic.txt");
	}

	/*Détermine un plan d'action et rempli la pile des actions*/
	public void choosePlan(){

		//Récupération des faits
		ArrayList<String> facts = environment.getFacts();
		Plan p = null;
		String planTitle ="";

		/*On parcourt les règles pour savoir si une renvoi un résultat*/
		for(Regle r : liste_regles){
			if(r.satisfaitConditions(facts)){
				planTitle = r.getConsequence();
				break;
			}
		}

		/*Si aucune règle ne s'est appliquée, on tir au hasard dans la table de probabilités*/
		if(planTitle.equals("")){

			int time = environment.getClock();
			int random = getRandomNum(0, probPlans[time].size()-1);
			planTitle = probPlans[time].get(random);
		}

		/*Récuperation du plan à partir de son nom*/
		for(Plan e : plans){
			if(e.getName().equals(planTitle)){
				p = e;
				break;
			}
		}

		/*Ajout des actions à la file*/
		queue.addAll(p.getSteps());
		for(int i =0; i<p.getTime();i++){
			queue.add(p.getName());
		}

		Simulator.operatorText.append("------------------------ " + environment.getClock() + "H ------------------------\n");

	}

	/*Initialise à partir du fichier les probabilités de plans d'actions*/
	public void openPlansFile(String path){

		for( int i = 0; i < probPlans.length; i++) {
			probPlans[i] = new ArrayList<String>();
		}

		try{
			InputStream ips= new FileInputStream(path); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				int weight = Integer.parseInt(ligne.substring(ligne.lastIndexOf('>')+1));
				String planTitle = ligne.substring(ligne.indexOf('>')+1, ligne.lastIndexOf('>')); 
				int time = Integer.parseInt(ligne.substring(0,ligne.indexOf('>')));

				for(int i =0; i<weight; i++)
					probPlans[time].add(planTitle);
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

	public void Update() {
		if(queue.isEmpty())
			choosePlan();

		String action = queue.remove();
		environment.setActionPerformed(action);
		Simulator.operatorText.append(action+"\n");
	}

	/*Renvoi une valeur x telle que : minNim <= x <= maxNum*/
	private int getRandomNum(int minNum, int maxNum){

		int MonChiffre = maxNum + 1;

		while(MonChiffre>maxNum){

			MonChiffre = (int)(minNum + (Math.random() * (maxNum+1)));
		}
		return MonChiffre;
	}

	/*Lit les règles dans le fichier de règles et remplie la liste*/
	private void readRules(String path){
		//lecture du fichier texte	
		try{
			InputStream ips= new FileInputStream(path); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				String co;
				co = ligne.substring(ligne.lastIndexOf('>')+1);
				ligne = ligne.substring(0,ligne.lastIndexOf('='));
				ArrayList<String> toAdd = new ArrayList<String>(Arrays.asList(ligne.split(",")));

				liste_regles.add(new Regle(toAdd,co));
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

}
