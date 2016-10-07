package TorrentApp.TorrentApp;

public class Global 
{
	
	///////////////
	// Attributs //
	///////////////
	
	Listes aList;
	Dictionnaries aDico;
	String aString;
	
	
	///////////////////
	// Constructeurs //
	///////////////////
	
	public Global(Listes p_list, Dictionnaries p_dico, String p_string)
	{
		aList = p_list;
		aDico = p_dico;
		aString = p_string;
	}
	
	
	
	public Global(Dictionnaries p_dico) 
	{
		aList = null;
		aDico = p_dico;
		aString = "";
	}
	
	
	
	public Global(Listes p_list) 
	{
		aList = p_list;
		aDico = null;
		aString = "";
	}
	
	
	
	public Global(String p_string) 
	{
		aList = null;
		aDico = null;
		aString = p_string;
	}
	
	
	//////////////
	// Méthodes //
	//////////////
	
	public void printGlobal() 
	{
		if (aDico != null) aDico.printDico();
		if (aList != null) aList.printListes();
		if (aString != "") System.out.print(aString);
	}
}
