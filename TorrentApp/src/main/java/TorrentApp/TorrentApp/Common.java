package TorrentApp.TorrentApp;

import org.apache.log4j.Logger;

public abstract class Common
{
	private static final Logger LOG = Logger.getLogger(Common.class);
	protected int beginOffset;
	protected int endOffset;
	
	/////////////
	// Methods //
	/////////////
	
	public static String byteToHex (byte[] byteArray)
	{ // Gives the hexadecimal form of given bytes 
		
		char[] hexArray = "0123456789ABCDEF".toCharArray();
	
		char[] hexChars = new char[byteArray.length * 2];
		
	    for ( int j = 0; j < byteArray.length; j++ )
	    {
	        int v = byteArray[j] & 0xFF; // Copies a bit to the result if it exists in both operands
	        hexChars[j * 2] = hexArray[v >>> 4]; // The left operands value is moved right by the number of bits specified by the right operand and shifted values are filled up with zeros.
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
		
	    LOG.debug(new String(hexChars));
		
		return (new String(hexChars));
	}
	
	
	
	public void parseData(Parser parser)
	{ // Parse the data
		
		LOG.debug("Parse data");
		beginOffset = parser.offset - 1;
		for ( ; parser.offset < parser.fileSize; parser.offset++)
		{

			LOG.debug(parser.offset + ", " + (char) parser.datas[parser.offset]);

			if ((char) parser.datas[parser.offset] == 'e')
			{
				endOffset = parser.offset + 1;
				break;
			}
			nextDataType((char) parser.datas[parser.offset], parser);
		}
	}
	
	
	
	public void nextDataType(char type, Parser parser)
	{ // Determines the type of the next encountered data
		
		if (type == 'd')
		{ // Next data = new dictionary
			
			LOG.debug("new dico: " + parser.offset + ", " + parser.datas[parser.offset]);
			
			parser.offset++;
			doCorrespondingDicoCreation();
		}

		else if (type == 'l')
		{ // Next datas = new list
			parser.offset++;
			
			LOG.debug("new list: " + parser.offset + ", " + parser.datas[parser.offset]);
			
			doCorrespondingListCreation();
		}

		else if (type <= 0x39 && type >= 0x30)
		{ // If it's a number we look for delimiters (":") to get the length of the next string
			
			LOG.debug("get length string: " + type);
			
			String s_lengthOfNextString = "";
			s_lengthOfNextString += type;

			parser.offset++;
			for ( ; parser.offset < parser.fileSize; parser.offset++)
			{
				if ((char)parser.datas[parser.offset] == ':')
				{ // Delimiters found
					parser.offset++;
					break;
				}
				else s_lengthOfNextString += (char) parser.datas[parser.offset]; // Adds the current number to the end of the string
			}
			
			LOG.debug(s_lengthOfNextString);
			
			int i_lengthOfNextString = Integer.parseInt(s_lengthOfNextString); // Converts the string to integer
			getString(i_lengthOfNextString, parser);
		}

		else if (type == '-' || type == 'i')
		{ // It's an integer (which constitutes a definition)
			
			LOG.debug("get an integer from listes: " + type);
			
			String s_integer = "";
			parser.offset++;
	
			if (type == '-')
			{
				parser.offset++; // After the "-" there's always an "i" which is the begin tag of an integer
				s_integer += type;
			}

			for ( ; parser.offset < parser.fileSize; parser.offset++)
			{
				if ((char) parser.datas[parser.offset] == 'e')
				{ // Delimiters found
					break;
				}
				else s_integer += (char) parser.datas[parser.offset]; // Adds the current number to the end of the string
			}
	
			LOG.debug(s_integer);
			
			doCorrespondingAction(s_integer);
		}
	}
	
	
	
	public void getString(int lengthOfString, Parser parser)
	{ // Gets the string corresponding to the given length

		String info = "";

		int i;
		for (i = 0; i < lengthOfString; i++)
		{
			//if (parser.datas[parser.offset] == 0x00) parser.datas[parser.offset] = 0x39;
			info += (char) parser.datas[parser.offset];
			LOG.debug("char: " + (char) parser.datas[parser.offset] + ", offset: " + i);
			parser.offset++;
		}
		
		LOG.debug("string: " + info + ", length: " + i);
		
		parser.offset--; // Sets the cursor to the previous character for a proper analysis (to not interfere with the offset++ of the parseData function))
		doCorrespondingAction(info);
	}
	
	
	public abstract void doCorrespondingDicoCreation();		 // Creates a new dictionary
	public abstract void doCorrespondingListCreation(); 	 // Creates a new list
	public abstract void doCorrespondingAction(String info); // Creates the correct object (could be a dictionary, list or string)
	
}
