package TorrentApp.TorrentApp;
import java.io.File;



//[ announce => http://t411.download/7d8c1038e6ddb08272ccdb7e1e13d6c0/announce, comment => https://www.t411.ch/t/5000562, created by => https://www.t411.ch/u/861203, creation date => 1465313712, 
//info => [ files => { [ length => 30340512, path => { Crack, AC4BFMP.exe } ], [ length => 44981664, path => { Crack, AC4BFSP.exe } ], [ length => 896, path => { Crack, orbit_api.ini } ], [ length => 814084, path => { Crack, steam_api.dll } ], [ length => 2100, path => { Crack, steam_api.ini } ], [ length => 638980, path => { Crack, uplay_r1.dll } ], [ length => 64000, path => { Crack, uplay_r1_loader.dll } ], [ length => 49056777, path => { Update, setup-1.bin } ], [ length => 608969, path => { Update, setup.exe } ], [ length => 1328, path => { Update, whatsnew.txt } ] }, name => Assassins Creed IV Black Flag Update v1 02 with DLC-RELOADED, piece length => 262144, pieces => zX

public class Tree {
	
	Dictionary DicoFiles = getDicoCombinationFromString("files", null);
	
	List ListInfo = Parser.getStringFromList("DicoFiles", null);
	
	
	//On crée le répertoire de base du torrent du même nom que le torrent
	String NameTorrent = getDicoCombinationFromString("name", null);
	new File("/Torrent/"+NameTorrent).mkdir();
			
			while(int i = 0; i < ListInfo.size();)
			{
				List ListPath = getStringFromList ("path", ListInfo)
				String NameFolder = getStringFromList ()
				new File("/Torrent/"+NameTorrent+"").mkdir();
				
				
				i++;
			}
	
	//kkk
	
	
}
