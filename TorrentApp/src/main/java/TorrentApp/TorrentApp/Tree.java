package TorrentApp.TorrentApp;
import java.io.File;

//[ announce => http://t411.download/7d8c1038e6ddb08272ccdb7e1e13d6c0/announce, comment => https://www.t411.ch/t/5000562, created by => https://www.t411.ch/u/861203, creation date => 1465313712, 
//info => [ files => { [ length => 30340512, path => { Crack, AC4BFMP.exe } ], [ length => 44981664, path => { Crack, AC4BFSP.exe } ], [ length => 896, path => { Crack, orbit_api.ini } ], [ length => 814084, path => { Crack, steam_api.dll } ], [ length => 2100, path => { Crack, steam_api.ini } ], [ length => 638980, path => { Crack, uplay_r1.dll } ], [ length => 64000, path => { Crack, uplay_r1_loader.dll } ], [ length => 49056777, path => { Update, setup-1.bin } ], [ length => 608969, path => { Update, setup.exe } ], [ length => 1328, path => { Update, whatsnew.txt } ] }, name => Assassins Creed IV Black Flag Update v1 02 with DLC-RELOADED, piece length => 262144, pieces => zX

public class Tree
{
	
	protected Dictionary DicoFiles;
	protected Parser parse;
	protected String NameTorrent;
	
	
	
	Tree (Parser p_parse)
	{
		parse = p_parse;
		DicoFiles = parse.getDicoCombinationFromString("info", null);
		NameTorrent = parse.getDicoCombinationFromString("name", DicoFiles);
	}
	
	
	Tree (Dictionary p_dico, Parser p_parse, String p_nameTorrent)
	{
		parse = p_parse;
		DicoFiles = p_dico;
		NameTorrent = p_nameTorrent;
	}
	
	
	
	public void createArbo()
	{ // Create the tree
		
		List filesList = parse.getDicoCombinationFromString("files", null); // Retrieves the file list inside the .torrent;
		List pathList;
		
		if (filesList == null)
		{ // It's a single file torrent but maybe it is in severals directories
			
			pathList = parse.getDicoCombinationFromString("path", DicoFiles); // Retrieves the file list inside the .torrent;
			if (pathList != null)
			{
				browsePathList (pathList);
			}
			else new File ("Torrent/" + NameTorrent).mkdirs();
		}
		else
		{ // It's a multiple file torrent
			
			for (int i = 0; i < filesList.list.size(); i++)
			{ // Adds every file length
				
				pathList = filesList.list.elementAt(i).aDico.getWordDefinition(filesList.list.elementAt(i).aDico.getWordIndexByString("path")); // Gets a file size 
				browsePathList (pathList);
			}
		}	
	}
	
	
	
	public void browsePathList (List pathList)
	{ // Create the tree corresponding to the path list
		
		String path = "";
		for (int i = 0; i < pathList.list.size() - 1; i++)
		{ // Adds name to the path
			path += "/" + pathList.list.elementAt(i).aString;
			System.out.println(path);
		}
		
		new File ("Torrent/" + NameTorrent + path).mkdirs();
	}
}