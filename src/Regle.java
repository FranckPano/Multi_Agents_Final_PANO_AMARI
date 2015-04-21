import java.util.ArrayList;


public class Regle {
	
	ArrayList<String> liste_premisses;
	String consequence;
	
	public Regle(ArrayList<String> lp, String c){
		liste_premisses = lp;
		consequence = c;
	}
	
	//Renvoi le fait inféré
	public String getConsequence(){
		return consequence;
	}
	
	//Vérifie si un fait satisfait une prémisse de la règle
	public boolean satisfaitCondition(String fait){
		for(String t : liste_premisses)
			if(fait.equals(t))
				return true;
		return false;
	}
	
	//Verifie si une lise de faits satisfont toutes les prémisses de la règle
	public boolean satisfaitConditions(ArrayList<String> faits){
		if(faits.size() < liste_premisses.size())
			return false;
		
		int compteurPremmissesSatisfaites = 0;
		
		for(String s : faits)
			if(satisfaitCondition(s))
				compteurPremmissesSatisfaites++;
		
		return (compteurPremmissesSatisfaites == liste_premisses.size());
	}

}
