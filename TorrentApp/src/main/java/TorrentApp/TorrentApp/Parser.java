package TorrentApp.TorrentApp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;

public class Parser extends Common
{
	
	////////////////
	// Attributes //
	////////////////
	
	public long fileSize;			 		 // The file size
	public byte[] datas;				 	 // The data that the file contains
	public int offset;				 		 // The cursor position in the file
	protected File torrentFile;		  	     // The current torrent file
	protected Vector <Global> parsedInfo; 	 // An array that contains every parsed info
	protected Vector <Peer> peerList;		 // A list containing all the peers
	private static final Logger LOG = Logger.getLogger(Parser.class);

	
	/////////////////
	// Constructor //
	/////////////////
	
	public Parser(String torrentFileName)
	{
		torrentFile = new File(torrentFileName);
		fileSize = torrentFile.length();
		parsedInfo = new Vector<Global>();
		offset = 0;
	}
	
	
	
	public Parser(byte[] p_datas)
	{
		parsedInfo = new Vector<Global>();
		datas = p_datas;
		fileSize = p_datas.length;
		offset = 0;
	}
	
	
	/////////////
	// Methods //
	/////////////
	
	public void readFile()
	{ // Read all the file
		
		try 
		{
			LOG.info("Read all file");

			datas = new byte [(int)fileSize];
			
			FileInputStream iosTorrent = new FileInputStream (torrentFile);
			iosTorrent.read(datas); // Read all bytes and stored them in the array
			iosTorrent.close();
			
			LOG.info("\n\nEnd of File, nombre de caractères: " + fileSize + "\n\n");
			
			System.out.println("Parse data\n");
			parseData(this);
			
			for (int i = 0; i < parsedInfo.size(); i++)
			{
				parsedInfo.elementAt(i).printGlobal();
			}

			//getDicoCombinationFromString ("files", null); // Ceci est un exemple
			//getStringFromList ("length", (List) getDicoCombinationFromString ("files", null)); // Ceci est un exemple
			//getInformation();
			//getDicoCombinationFromString ("private", null);		
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

	
	
	public void getInformation()
	{ // Get all the information required to build the tracker request
		//String valAnnounce = getDicoCombinationFromString("announce",null);
		//Dictionary DicoInfo = getDicoCombinationFromString("info",null);
		//String valPieces = getDicoCombinationFromString("pieces",DicoInfo); 
		//System.out.println("valeur announce : "+valAnnounce+"valeur pieces"+valPieces);
	}
	
	
	
	public byte[] getBencodedDicoInfo ()
	{ // Return the info dictionary in the its Bencoded form (usually used to build the torrent info_hash)
		
		Dictionary DicoInfo = getDicoCombinationFromString("info", null);
		
		LOG.debug("beginOffset: " + DicoInfo.beginOffset + ", endOffset: " + DicoInfo.endOffset);
		
		byte[] bencodedInfo = new byte[DicoInfo.endOffset-DicoInfo.beginOffset]; // Same size as the retrieved dictionary
		
		int i;
		int j = 0;
		
		for (i = DicoInfo.beginOffset; i < DicoInfo.endOffset; i++)
		{ // Extracts the correct data from the torrent file
			bencodedInfo [j] = datas [i];
			j++;
		}
		
		for (i = 0; i < bencodedInfo.length; i++)
		{ // Prints the bencodedInfo array for debug
			LOG.debug((char) bencodedInfo[i]);
		}
		
		return bencodedInfo;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getDicoCombinationFromString(String target, Dictionary temp)
	{ // Return the combination name / value of a given target
		
		int index;
		Dictionary dicoRecup = null;
		
		if (temp == null)
		{ // We are searching for the first Dictionary
			
			for (index = 0; index < parsedInfo.size(); index++)
			{
				if (parsedInfo.elementAt(index).getType() == "Dictionary")
				{
					dicoRecup = parsedInfo.elementAt(index).aDico;
					break;
				}
			}
		}
		else
		{ // We use the given Dictionary
			dicoRecup = temp;
		}

		Object defRecup = null;

		for (index = 0; index < dicoRecup.word.size(); index++)
		{ // We are searching for the index where the word is stored
			
			if(dicoRecup.getWord(index).equals(target))
			{ // We found it
				defRecup = dicoRecup.getWordDefinition(index);
				break;
			}
			
		}
		
		if (defRecup == null)
		{ // We didn't find the word yet
			
			for (index = 0; index < dicoRecup.def.size(); index++)
			{ // Maybe the word is in another Dictionary / List so we have to check all the Dictionaries / Lists stored in the def array
				
				LOG.debug(dicoRecup.getWordDefinition(index).getClass().toString());
				
				if(dicoRecup.getWordDefinition(index).equals(target))
				{ // It means that the word was in reality a definition
					
					defRecup = dicoRecup.getWordDefinition(index);
					break;
				}
				
				else if (dicoRecup.getWordDefinition(index).getClass().toString().equals("class TorrentApp.TorrentApp.Dictionary"))
				{ // We have found another Dictionary. So we use a recursive method to search in this Dictionary
					
					defRecup = getDicoCombinationFromString (target, (Dictionary) dicoRecup.getWordDefinition(index));
					if (defRecup != null ) break; // If it breaks that mean we have found the word definition
				}
				
				else if (dicoRecup.getWordDefinition(index).getClass().toString().equals("class TorrentApp.TorrentApp.List"))
				{ // We have found another List. So we use a recursive method to search in this List
					
					defRecup = getStringFromList (target, (List) dicoRecup.getWordDefinition(index));
					if (defRecup != null ) break; // If it breaks that mean we have found the word definition
				}
			}
		}
		
		if (defRecup != null) System.out.println("\n\nMot: " + dicoRecup.getWord(index) + "\nValeur: " + defRecup);
		return (T) defRecup;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getStringFromList (String target, List temp)
	{
		
		if (temp == null)
		{ // We need a List to look into
			return null;
		}
		
		else
		{ // We have a List
			
			int index = 0;
			Object objectRecup = null;
			
			for (index = 0; index < temp.list.size(); index++)
			{ // We are searching the target string in the given List
				
				if (temp.list.get(index).getType() == "Dictionary")
				{ // We found a Dictionary. Maybe the target string is in it.
					objectRecup = getDicoCombinationFromString (target, temp.list.get(index).aDico);
					if (objectRecup != null) break;
				}
				
				else if (temp.list.get(index).getType() == "List")
				{ // We found another List. Maybe the target string is in it.
					objectRecup = getStringFromList (target, temp.list.get(index).aList);
					if (objectRecup != null) break;
				}
				
				else if (temp.list.get(index).getType() == "String")
				{ // We found it
					return (T) temp.list.get(index).aString;
				}
			}

			return (T) objectRecup;
		}
		
	}

	
	
	@Override
	public void doCorrespondingDicoCreation()
	{
		parsedInfo.add(new Global(new Dictionary(this)));
	}


	
	@Override
	public void doCorrespondingListCreation()
	{
		parsedInfo.add(new Global(new List(this)));
	}

	

	@Override
	public void doCorrespondingAction(String info)
	{
		parsedInfo.add(new Global(info));
	}
}
