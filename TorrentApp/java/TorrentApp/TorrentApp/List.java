package TorrentApp.TorrentApp;

import java.util.Vector;

public class List extends Common
{

	////////////////
	// Attributes //
	////////////////
	
	protected Parser parser;		// The common parser where are stored all the data, file size and the offset
	protected Vector <Global> list; // An array of all the info that the list contains
	
	
	/////////////////
	// Constructor //
	/////////////////
	
	public List(Parser p_parser)
	{
		parser = p_parser;
		list = new Vector<Global> ();
		parseData();
	}

	
	
	/////////////
	// Methods //
	/////////////
	
	public void printList()
	{ // Prints the list
		
		System.out.print("{ ");
		for (int i = 0; i < list.size(); i++)
		{	
			list.elementAt(i).printGlobal();
			if (i < list.size()-1) System.out.print(", ");
		}
		System.out.print(" }");		
	}



	@Override
	public void doCorrespondingDicoCreation()
	{
		list.add(new Global(new Dictionary(parser)));
	}



	@Override
	public void doCorrespondingListCreation()
	{
		list.add(new Global(new List(parser)));
	}



	@Override
	public void doCorrespondingAction(String info)
	{
		list.add(new Global(info));	
	}	
}
