package TorrentApp.TorrentApp;
import java.io.File;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
import java.util.Vector;

//[ announce => http://t411.download/7d8c1038e6ddb08272ccdb7e1e13d6c0/announce, comment => https://www.t411.ch/t/5000562, created by => https://www.t411.ch/u/861203, creation date => 1465313712, 
//info => [ files => { [ length => 30340512, path => { Crack, AC4BFMP.exe } ], [ length => 44981664, path => { Crack, AC4BFSP.exe } ], [ length => 896, path => { Crack, orbit_api.ini } ], [ length => 814084, path => { Crack, steam_api.dll } ], [ length => 2100, path => { Crack, steam_api.ini } ], [ length => 638980, path => { Crack, uplay_r1.dll } ], [ length => 64000, path => { Crack, uplay_r1_loader.dll } ], [ length => 49056777, path => { Update, setup-1.bin } ], [ length => 608969, path => { Update, setup.exe } ], [ length => 1328, path => { Update, whatsnew.txt } ] }, name => Assassins Creed IV Black Flag Update v1 02 with DLC-RELOADED, piece length => 262144, pieces => zX

public class Tree
{
	
	protected Dictionary DicoFiles;
	protected List ListInfo;
	protected Parser parse;
	protected String NameTorrent;
	
	
	
	Tree (Parser p_parse)
	{
		parse = p_parse;
		DicoFiles = parse.getDicoCombinationFromString("info", null);
		ListInfo = parse.getDicoCombinationFromString("files", DicoFiles);
		NameTorrent = parse.getDicoCombinationFromString("name", DicoFiles);
	}
	
	
	Tree (Dictionary p_dico, List p_list, Parser p_parse, String p_nameTorrent)
	{
		parse = p_parse;
		DicoFiles = p_dico;
		ListInfo = p_list;
		NameTorrent = p_nameTorrent;
	}
	
	
	
	public void createArbo()
	{
		//On crée le répertoire de base du torrent du même nom que le torrent
		new File("/Torrent/"+NameTorrent).mkdirs();
		System.out.println("Coucou");
		
		List ListPath = parse.getStringFromList ("path", ListInfo);
		//System.out.println(((List) ListPath).get(0));
		ListPath.printList();
		/*for( int i =0; i < ListInfo.size(); i++)
		{
			//List ListPath = parse.getStringFromList ("path", ListInfo)
			//String NameFolder = parse.getStringFromList ()
			//new File("/Torrent/"+NameTorrent+"").mkdir();
			System.out.println(i);
			
		}*/

	}
	
	
	//kkk
	
	
}
