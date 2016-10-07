package TorrentApp.TorrentApp;

import java.util.Vector;

import org.apache.log4j.Logger;

public class Listes implements Common
{

	///////////////
	// Attributs //
	///////////////
	
	Parser parser;
	Vector <Global> list;
	private static final Logger LOGGER = Logger.getLogger(Listes.class);
	
	
	//////////////////
	// Constructeur //
	//////////////////
	
	public Listes(Parser p_parser)
	{
		parser = p_parser;
		list = new Vector<Global> ();
		parseData();
	}

	
	
	////////////
	//Méthodes//
	////////////
	
	public void parseData()
	{ // Parse the datas
		
		LOGGER.debug("Parsedata from listes");
		for (parser.offset = parser.offset; parser.offset < parser.fileSize; parser.offset++)
		{
			
			LOGGER.debug(parser.offset + ", " + parser.datas[parser.offset]);
			
			if (parser.datas[parser.offset] == 'e') break;
			nextDataType(parser.datas[parser.offset]);
			
			LOGGER.debug(", " + parser.datas[parser.offset]);
		}
	}
	
	
	
	public void nextDataType(char type)
	{ // Determines the type of the next encountred data
		if (type == 'd')
		{ // Next data = new dictionnary
			
			LOGGER.debug("new dico from listes: " + parser.offset + ", " + parser.datas[parser.offset]);
			
			parser.offset++;
			list.add(new Global(new Dictionnaries(parser)));
		}

		else if (type == 'l')
		{ // Next datas = new list
			parser.offset++;
			
			LOGGER.debug("new list from listes: " + parser.offset + ", " + parser.datas[parser.offset]);
			
			list.add(new Global(new Listes(parser)));
		}

		else if (type <= 0x39 && type >= 0x30)
		{ // If it's a number we look for delimiters (":") to get the length of the next string
			
			LOGGER.debug("get length string from listes: " + type);
			
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
			
			LOGGER.debug("get an integer from listes: " + type);
			
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
			
			list.add(new Global(s_integer));
		}
	}

	
	
	public void getString(int lengthOfString)
	{ // Gets the string corresponding to the length

		String info = "";
		for (int i = 0; i < lengthOfString; i++)
		{
			info += parser.datas[parser.offset];
			parser.offset++;
		}
		
		LOGGER.debug("string: " + info + ", length: " + lengthOfString);
		
		parser.offset--; // Sets the cursor to the previous character for a proper analysis (to not interfer with the offset++ of the parseData function))
		list.add(new Global(info));		
	}
	
	
	
	public void printListes()
	{
		System.out.print("{ ");
		for (int i = 0; i < list.size(); i++)
		{	
			list.elementAt(i).printGlobal();
			if (i < list.size()-1) System.out.print(", ");
		}
		System.out.print(" }");		
	}
}
