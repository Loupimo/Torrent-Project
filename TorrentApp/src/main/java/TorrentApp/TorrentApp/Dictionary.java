package TorrentApp.TorrentApp;

import java.util.Vector;

import org.apache.log4j.Logger;


public class Dictionary extends Common
{

	////////////////
	// Attributes //
	////////////////
	
	protected Parser parser; 		 // The common parser where are stored all the data, file size and the offset
	protected Vector<Global> word;   // A word of dictionary. It could be a List, a string and even a Dictionary
	protected Vector<Global> def;    // The definition of corresponding word. It could be a List, a string and even a Dictionary
	protected int wordOrDef; 		 // A flag used to know if the current string is a word or a definition (0 = word, 1 = definition)
	private static final Logger LOGGER = Logger.getLogger(Dictionary.class);

	
	/////////////////
	// Constructor //
	/////////////////

	public Dictionary(Parser p_parser)
	{
		parser = p_parser;
		word = new Vector<Global> ();
		def = new Vector<Global> ();
		wordOrDef = 0;
		parseData(parser);
	}
	
	
	/////////////
	// Methods //
	/////////////
	
	@SuppressWarnings("unchecked")
	public <T> T getWord(int index)
	{ // Return the word corresponding to the given index (T is a generic type which means it could be any Object but limited here to String, List and Dictionary)
		
		if (word.elementAt(index).getType() == "Dictionary") return (T) word.elementAt(index).aDico;
		else if (word.elementAt(index).getType() == "List") return (T) word.elementAt(index).aList;
		else if (word.elementAt(index).getType() == "String") return (T) word.elementAt(index).aString;
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getWordDefinition(int wordIndex)
	{ // Return the definition of corresponding word (T is a generic type which means it could be any Object but limited here to String, List and Dictionary)
		
		if (def.elementAt(wordIndex).getType() == "Dictionary") return (T) def.elementAt(wordIndex).aDico;
		else if (def.elementAt(wordIndex).getType() == "List") return (T) def.elementAt(wordIndex).aList;
		else if (def.elementAt(wordIndex).getType() == "String") return (T) def.elementAt(wordIndex).aString;
		return null;
	}

	
	
	public int getWordIndexByString(String wordIndex)
	{ // Return the position of given word
		
		for (int i = 0; i < word.size(); i++)
		{
			if(word.elementAt(i).aString.equals(wordIndex))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	
	
	public void printDico()
	{ // Prints the dictionary
		
		System.out.print("[ ");
		for (int i = 0; i < word.size(); i++)
		{
			word.elementAt(i).printGlobal();
			System.out.print(" => ");
			def.elementAt(i).printGlobal();
			if (i < word.size()-1) System.out.print(", ");
		}
		System.out.print(" ]");
	}



	@Override
	public void doCorrespondingDicoCreation()
	{
		if (wordOrDef == 0)
		{ // It's a word
			
			LOGGER.debug("new word from dico: " + parser.offset + ", " + parser.datas[parser.offset]);

			word.add(new Global(new Dictionary(parser)));
			wordOrDef = 1;
		}
		else
		{ // It's a definition
			
			LOGGER.debug("new def from dico: " + parser.offset + ", " + parser.datas[parser.offset]);

			def.add(new Global(new Dictionary(parser)));
			wordOrDef = 0;
		}
	}

	

	@Override
	public void doCorrespondingListCreation()
	{
		if (wordOrDef == 0)
		{ // It's a word
			
			LOGGER.debug("new word from dico: " + parser.offset + ", " + parser.datas[parser.offset]);
			word.add(new Global(new List(parser)));
			wordOrDef = 1;
		}
		else
		{ // It's a definition
			
			LOGGER.debug("new def from dico: " + parser.offset + ", " + parser.datas[parser.offset]);

			def.add(new Global(new List(parser)));
			wordOrDef = 0;
		}
	}


	
	@Override
	public void doCorrespondingAction(String info)
	{
		if (wordOrDef == 0)
		{ // It's a word
			
			LOGGER.debug("new word from dico: " + parser.offset + ", " + parser.datas[parser.offset]);
			
			word.add(new Global(info));
			wordOrDef = 1;
		}
		else
		{ // It's a definition
		 	
		 	LOGGER.debug("new def from dico: " + parser.offset + ", " + parser.datas[parser.offset]);
			
			def.add(new Global(info));
			wordOrDef = 0;
		}
	}
}
