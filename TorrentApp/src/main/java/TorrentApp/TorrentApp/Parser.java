package TorrentApp.TorrentApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;

public class Parser extends Common
{
	
	////////////////
	// Attributes //
	////////////////
	
	public static long fileSize;			 // The file size
	public static char[] datas;				 // The data that the file contains
	public static int offset;				 // The cursor position in the file
	protected File torrentFile;		  	     // The current torrent file
	protected Vector <Global> torrentParsed; // An array that contains every parsed info
	protected Vector <Object> wordSend;
	protected Vector <Object> defSend;
	private static final Logger LOGGER = Logger.getLogger(Parser.class);

	
	/////////////////
	// Constructor //
	/////////////////
	
	public Parser(String torrentFileName)
	{
		torrentFile = new File(torrentFileName);
		torrentParsed = new Vector<Global>();
		wordSend= new Vector<Object>();
		defSend= new Vector<Object>();
		offset = 0;
	}
	
	
	/////////////
	// Methods //
	/////////////
	
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
			{ // Reads and stores all the data in the data array
				datas[i] = (char) br.read();
			}
			
			LOGGER.info("\n\nEnd of File, nombre de caractères: " + fileSize + "\n\n");
			
			System.out.println("Parse data\n");
			parseData();
			
			for (int i = 0; i < torrentParsed.size(); i++)
			{
				torrentParsed.elementAt(i).printGlobal();
			}
			
			
			//getDicoCombinaisonFromString("annouce"); // Ceci est un exemple
			getInformation();
			
			
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
	
	
	public void getInformation(){
		String valAnnounce = getDicoCombinaisonFromString("announce",null);
		Dictionary DicoInfo = getDicoCombinaisonFromString("info",null);
		String valPieces = getDicoCombinaisonFromString("pieces",DicoInfo); 
		System.out.println("valeur announce : "+valAnnounce+"valeur pieces"+valPieces);
		//getDicoCombinaisonFromString("info");
		
	}
	
	public <T> T getDicoCombinaisonFromString(String target, Dictionary temp)
	{ // Return the combination name / value of a given target

		int index;
		Dictionary dicoRecup = null;
		if (temp==null){
			
		for (index = 0; index < torrentParsed.size(); index++)
		{
			if (torrentParsed.elementAt(index).getType() == "Dictionary")
			{
				dicoRecup = torrentParsed.elementAt(index).aDico;
				break;
			}
		}
		}
		else {
			dicoRecup=temp;
		}

		Object defRecup = null;

		for (index = 0; index < dicoRecup.word.size(); index++)
		{
			if(dicoRecup.getWord(index).equals(target))
			{
				defRecup = dicoRecup.getWordDefinition(index);
				wordSend.add(dicoRecup.getWord(index));
				defSend.add(defRecup);
				break;
			}
		}
		
		
		//System.out.println("\n\nMot: " + dicoRecup.getWord(index) + "\nValeur: " + defRecup);
		return (T) defRecup;
		
		/*Pour l'instant cette fonction affiche juste la combinaison mot / valeur*/
		/*Pour les récupérer très simple: dicoRecup.getWord(index) correspond au mot et defRecup à la valeur*/
	
	}

	
	
	@Override
	public void doCorrespondingDicoCreation()
	{
		torrentParsed.add(new Global(new Dictionary(this)));
	}


	
	@Override
	public void doCorrespondingListCreation()
	{
		torrentParsed.add(new Global(new List(this)));
	}

	

	@Override
	public void doCorrespondingAction(String info)
	{
		torrentParsed.add(new Global(info));
	}
}
