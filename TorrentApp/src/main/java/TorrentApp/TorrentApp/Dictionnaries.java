package TorrentApp.TorrentApp;

import java.util.Vector;

import org.apache.log4j.Logger;


public class Dictionnaries implements Common {

	///////////////
	// Attributs //
	///////////////
	
	Parser parser; 		 // The common parser where are stored all the datas, file size and the offset
	Vector<Global> word; // A word of dictionnary. It could be a List, a string and even a dictionnary
	Vector<Global> def;  // The definition of corresponding word. It could be a List, a string and even a dictionnary
	int wordOrDef; 		 // A flag used to know if the current string is a word or a definition (0 = word, 1 = definition)
	private static final Logger LOGGER = Logger.getLogger(Dictionnaries.class);

	
	//////////////////
	// Constructeur //
	//////////////////

	public Dictionnaries(Parser p_parser)
	{
		parser = p_parser;
		word = new Vector<Global> ();
		def = new Vector<Global> ();
		wordOrDef = 0;
		parseData();
	}
	
	
	//////////////
	// Méthodes //
	//////////////

	public void parseData()
	{ // Parse the datas
			
		LOGGER.debug("Parse data from dico");

		for (parser.offset = parser.offset; parser.offset < parser.fileSize; parser.offset++)
		{
			
			LOGGER.debug(parser.offset + ", " + parser.datas[parser.offset]);

			if (parser.datas[parser.offset] == 'e') break;
			nextDataType(parser.datas[parser.offset]);
			
			LOGGER.debug(", " + parser.datas[parser.offset-1]);
		}
		
	}

	
	
	public void nextDataType(char type)
	{ // Determines the type of the next encountred data
		if (type == 'd')
		{ // Next data = new dictionnary
			
			LOGGER.debug("new dico from dico: " + parser.offset + ", " + parser.datas[parser.offset]);

			parser.offset++;
			if (wordOrDef == 0)
			{ // It's a word
				
				LOGGER.debug("new word from dico: " + parser.offset + ", " + parser.datas[parser.offset]);

				word.add(new Global(new Dictionnaries(parser)));
				wordOrDef = 1;
			}
			else
			{ // It's a definition
				
				LOGGER.debug("new def from dico: " + parser.offset + ", " + parser.datas[parser.offset]);

				def.add(new Global(new Dictionnaries(parser)));
				wordOrDef = 0;
			}
		}

		else if (type == 'l')
		{ // Next datas = new list

			parser.offset++;
			
			LOGGER.debug("new list from dico: " + parser.offset + ", " + parser.datas[parser.offset]);
			if (wordOrDef == 0)
			{ // It's a word
				
				LOGGER.debug("new word from dico: " + parser.offset + ", " + parser.datas[parser.offset]);
				word.add(new Global(new Listes(parser)));
				wordOrDef = 1;
			}
			else
			{ // It's a definition
				
				LOGGER.debug("new def from dico: " + parser.offset + ", " + parser.datas[parser.offset]);

				def.add(new Global(new Listes(parser)));
				wordOrDef = 0;
			}
		}

		else if (type <= 0x39 && type >= 0x30)
		{ // If it's a number we look for delimiters (":") to get the length of the next string
			
			LOGGER.debug("get length string from dico: " + type);

			String s_lengthOfNextString = "";
			s_lengthOfNextString += type;

			parser.offset++;
			for (parser.offset = parser.offset; parser.offset < parser.fileSize; parser.offset++)
			{
				if (parser.datas[parser.offset] == ':')
				{ // Delimiters found
					parser.offset++;
					break;
				}
				else s_lengthOfNextString += parser.datas[parser.offset]; // Adds the current number to the end of the string
			}

			
			LOGGER.debug(s_lengthOfNextString);

			int i_lengthOfNextString = Integer.parseInt(s_lengthOfNextString); // Converts the string to int
			getString(i_lengthOfNextString);
		}

		else if (type == '-' || type == 'i')
		{ // It's an integer (which constitutes a definition)
			
			LOGGER.debug("get an integer from dico: " + type);
			
			String s_integer = "";
			parser.offset++;

			if (type == '-')
			{
				parser.offset++; // After the "-" there's always an "i" which is the begin tag of an integer
				s_integer += type;
			}

			for (parser.offset = parser.offset; parser.offset < parser.fileSize; parser.offset++)
			{
				if (parser.datas[parser.offset] == 'e')
				{ // Delimiters found
					break;
				}
				else s_integer += parser.datas[parser.offset]; // Adds the current number to the end of the string
			}

			
			LOGGER.debug(s_integer);

			if (wordOrDef == 0)
			{ // It's a word
				
				LOGGER.debug("new word from dico: " + parser.offset + ", " + parser.datas[parser.offset]);
				
				word.add(new Global(s_integer));
				wordOrDef = 1;
			}
			else
			{ // It's a definition
				
				LOGGER.debug("new def from dico: " + parser.offset + ", " + parser.datas[parser.offset]);
				
				def.add(new Global(s_integer));
				wordOrDef = 0;
			}
		}
		
	}

	
	
	public void getString(int lengthOfString)
	{
		// Gets the string corresponding to the length

		String info = "";
		for (int i = 0; i < lengthOfString; i++)
		{
			info  += parser.datas[parser.offset];
			parser.offset++;
		}
		
		LOGGER.debug("string: " + info + ", length: " + lengthOfString);

		parser.offset--; // Sets the cursor to the previous character for a proper analysis (to not interfer with the offset++ of the parseData function))

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

	
	
	public void printDico()
	{
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
}
