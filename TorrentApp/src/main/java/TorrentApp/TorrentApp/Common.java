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
	
	public void parseData()
	{ // Parse the data
		
		LOG.debug("Parse data from dico");
		beginOffset = Parser.offset - 1;
		for ( ; Parser.offset < Parser.fileSize; Parser.offset++)
		{

			LOG.debug(Parser.offset + ", " + (char) Parser.datas[Parser.offset]);

			if ((char) Parser.datas[Parser.offset] == 'e')
			{
				endOffset = Parser.offset + 1;
				break;
			}
			nextDataType((char) Parser.datas[Parser.offset]);
		}
	}
	
	
	
	public void nextDataType(char type)
	{ // Determines the type of the next encountered data
		
		if (type == 'd')
		{ // Next data = new dictionary
			
			LOG.debug("new dico: " + Parser.offset + ", " + Parser.datas[Parser.offset]);
			
			Parser.offset++;
			doCorrespondingDicoCreation();
		}

		else if (type == 'l')
		{ // Next datas = new list
			Parser.offset++;
			
			LOG.debug("new list: " + Parser.offset + ", " + Parser.datas[Parser.offset]);
			
			doCorrespondingListCreation();
		}

		else if (type <= 0x39 && type >= 0x30)
		{ // If it's a number we look for delimiters (":") to get the length of the next string
			
			LOG.debug("get length string: " + type);
			
			String s_lengthOfNextString = "";
			s_lengthOfNextString += type;

			Parser.offset++;
			for ( ; Parser.offset < Parser.fileSize; Parser.offset++)
			{
				if ((char)Parser.datas[Parser.offset] == ':')
				{ // Delimiters found
					Parser.offset++;
					break;
				}
				else s_lengthOfNextString += (char) Parser.datas[Parser.offset]; // Adds the current number to the end of the string
			}
			
			LOG.debug(s_lengthOfNextString);
			
			int i_lengthOfNextString = Integer.parseInt(s_lengthOfNextString); // Converts the string to integer
			getString(i_lengthOfNextString);
		}

		else if (type == '-' || type == 'i')
		{ // It's an integer (which constitutes a definition)
			
			LOG.debug("get an integer from listes: " + type);
			
			String s_integer = "";
			Parser.offset++;
	
			if (type == '-')
			{
				Parser.offset++; // After the "-" there's always an "i" which is the begin tag of an integer
				s_integer += type;
			}

			for ( ; Parser.offset < Parser.fileSize; Parser.offset++)
			{
				if ((char) Parser.datas[Parser.offset] == 'e')
				{ // Delimiters found
					break;
				}
				else s_integer += (char) Parser.datas[Parser.offset]; // Adds the current number to the end of the string
			}
	
			LOG.debug(s_integer);
			
			doCorrespondingAction(s_integer);
		}
	}
	
	
	
	public void getString(int lengthOfString)
	{ // Gets the string corresponding to the given length

		String info = "";

		int i;
		for (i = 0; i < lengthOfString; i++)
		{
			//if (Parser.datas[Parser.offset] == 0x00) Parser.datas[Parser.offset] = 0x39;
			info += (char) Parser.datas[Parser.offset];
			LOG.debug("char: " + (char) Parser.datas[Parser.offset] + ", offset: " + i);
			Parser.offset++;
		}
		
		LOG.debug("string: " + info + ", length: " + i);
		
		Parser.offset--; // Sets the cursor to the previous character for a proper analysis (to not interfere with the offset++ of the parseData function))
		doCorrespondingAction(info);
	}
	
	
	public abstract void doCorrespondingDicoCreation();		 // Creates a new dictionary
	public abstract void doCorrespondingListCreation(); 	 // Creates a new list
	public abstract void doCorrespondingAction(String info); // Creates the correct object (could be a dictionary, list or string)
	
}
