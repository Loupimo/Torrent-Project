package TorrentApp.TorrentApp;

public class Global 
{
	
	////////////////
	// Attributes //
	////////////////
	
	protected List aList;		   // A List
	protected Dictionary aDico; // A Dictionary
	protected String aString;	   // A String
	
	
	//////////////////
	// Constructors //
	//////////////////
	
	public Global(List p_list, Dictionary p_dico, String p_string)
	{
		aList = p_list;
		aDico = p_dico;
		aString = p_string;
	}
	
	
	
	public Global(Dictionary p_dico) 
	{
		aList = null;
		aDico = p_dico;
		aString = "";
	}
	
	
	
	public Global(List p_list) 
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
	
	
	/////////////
	// Methods //
	/////////////
	
	public String getType()
	{ // Return type of the set value
		
		if (aDico != null) return "Dictionary";
		else if (aList != null) return "List";
		else if (aString != "") return "String";
		else return null;
	}
	
	
	
	public void printGlobal() 
	{
		if (aDico != null) aDico.printDico();
		if (aList != null) aList.printList();
		if (aString != "") System.out.print(aString);
	}
}
