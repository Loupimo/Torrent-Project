package TorrentApp.TorrentApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;

public class Parser implements Common
{
	
	///////////////
	// Attributs //
	///////////////

	long fileSize;				   // The file size
	char[] datas;				   // The datas that contains the file
	int offset;					   // The cursor position in the file
	File torrentFile;		  	   // The current torrent file
	Vector <Global> torrentParsed; // An array that contains every parsed info
	private static final Logger LOGGER = Logger.getLogger(Parser.class);

	
	//////////////////
	// Constructeur //
	//////////////////
	
	public Parser(String torrentFileName)
	{
		torrentFile = new File(torrentFileName);
		torrentParsed = new Vector<Global>();
		offset = 0;
	}
	
	
	//////////////
	// Méthodes //
	//////////////
	
	public void readFile()
	{ // Read all the file
		
		BufferedReader br;	
		try 
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(torrentFile)));
			LOGGER.info("Read all file");

			fileSize = torrentFile.length(); // Size of the file

			datas = new char [(int)fileSize];
			System.out.println();

			for (int i = 0; i < fileSize ; i ++)
			{ // Reads and stores all the datas in the datas array
				datas[i] = (char) br.read();
			}
			
			LOGGER.info("\n\nEnd of File, nombre de caractères: " + fileSize + "\n\n");
			
			parseData();
		}
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 	
	}

	
	
	public void parseData()
	{ // Parse the datas
		
		System.out.println("Parse data from parser\n");
		for (offset = 0; offset < fileSize; offset++)
		{
			//System.out.print("\noffset");
			nextDataType(datas[offset]);

			//System.out.println(", " + datas[offset]);
			//System.out.println("offset");
		}
		for (int i = 0; i < torrentParsed.size(); i++)
		{
			//System.out.println("Parsed Info: " + i);
			torrentParsed.elementAt(i).printGlobal();
		}
	}

	
	
	public void nextDataType(char type)
	{ // Determines the type of the next encountred data
		
		if (type == 'd')
		{
			//System.out.println("new dico from parser: " + offset + ", " + datas[offset]);
			offset++;
			torrentParsed.add(new Global (new Dictionnaries(this)));
		}
		else if (type == 'l')
		{
			//System.out.println("new list from parser: " + offset + ", " + datas[offset]);
			torrentParsed.add(new Global(new Listes(this)));
		}
		else if (type <= 0x39 && type >= 0x30)
		{ // If it's a number we look for delimiters (":") to get the length of the next string
		  //System.out.println("get length string from dico: " + type);
			String s_lengthOfNextString = "";
			s_lengthOfNextString += type;

			for (offset = offset++; offset < fileSize; offset++)
			{
				if (datas[offset] == ':')
				{ // Delimiters found
					offset++;
					break;
				}
				else s_lengthOfNextString += datas[offset]; // Adds the current number to the end of the string
			}

			//System.out.println(s_lengthOfNextString);
			int i_lengthOfNextString = Integer.parseInt(s_lengthOfNextString); // Converts the string to int
			getString(i_lengthOfNextString);
		}

		else if (type == '-' || type == 'i')
		{ // It's an integer (which constitutes a definition)
		  //System.out.println("get an integer from dico: " + type);
			String s_integer = "";

			if (type == '-')
			{
				offset++; // After the "-" there's always an "i" which is the begin tag of an integer
				s_integer += type;
			}

			for (offset = offset++; offset < fileSize; offset++)
			{
				if (datas[offset] == 'e')
				{ // Delimiters found
					break;
				}
				else s_integer += datas[offset]; // Adds the current number to the end of the string
			}

			//System.out.println(s_integer);
			torrentParsed.add(new Global(s_integer));
		}
	}


	
	public void getString(int lengthOfString)
	{ // Gets the string corresponding to the length

		String info = "";
		for (int i = 0; i < lengthOfString; i++)
		{
			info += datas[offset];
			offset++;
		}
		//System.out.println("string: " + info + ", length: " + lengthOfString);

		offset--; // Sets the cursor to the previous character for a proper analysis (to not interfer with the offset++ of the parseData function))
		torrentParsed.add(new Global(info));
	}
}
