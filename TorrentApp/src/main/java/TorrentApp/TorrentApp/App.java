package TorrentApp.TorrentApp;

public class App 
{
	public static void main(String[] args)
	{
		Parser parse;
		int argsCount = 0;
		if (args.length > 0)
		{
			parse = new Parser (args[0]);
			argsCount = 1;
		}
		else
		{
			//String oo = "TorrentTest\\Assassins_Creed_IV_Black_Flag_Update_v1_02_with_DLC-RELOADED.torrent"; // HTTP
			//String oo = "TorrentTest\\Jennifer_Lopez_-_Hitz_Music_Video_2014_1080pHD_[TG-Encoder_www.torrentgarden.com].torrent"; // UDP
			//String oo = "TorrentTest\\Vicious_Rumors_-_Live_You_To_Death.torrent"; // the.piratebay.org (unknown host problem)
			//String oo = "TorrentTest\\lubuntu-16.10-desktop-amd64.iso.torrent"; // Needs Compact set to 1
			//String oo = "TorrentTest\\Game_Dev_Tycoon_v1.4.15_(2-click_run).torrent"; // Anounce-list présent: à gérer
			//String oo = "TorrentTest\\batman-unlimited-mechs-vs-mutants-french-webrip-2016.torrent"; // 
			//String oo = "TorrentTest\\David Morell - Les conjures de la pierre.epub.torrent";
			//String oo = "TorrentTest\\David Morrell-Portrait de l'assassin en artiste.epub.torrent";
			String oo = "TorrentTest\\Windows 10 (Français), anniversary update 10.0.14393 (x64).iso.torrent";
			
			parse = new Parser(oo);
		}
			
		if (parse.torrentFile != null)
		{
			parse.readFile();
			System.out.println("\n\nFin Parser");
			/*
			TrackerCommunicator tc = new TrackerCommunicator((String) parse.getDicoCombinationFromString("announce", null), parse.getBencodedDicoInfo(), "-UT3490-n%a6c%15%9d%ae%fd%1c%3e%0fd%aa", "50755", 0, 0, 10000, "started", 1);
			TrackerResponse tr = new TrackerResponse (tc.HTTPGet());
			tr.createPeerList();*/
			String [] parameters = {"localhost", "", "Symfony", "root", "root", "Jul'o", "Je viens de l'appli"}; // Stores the default parameters for the BDD. In order: host, port, dbName, userName, password, author, description
			for (int i = 0; i < args.length; i++, argsCount++)
			{
				parameters[i] = args[argsCount];
			}
			new BDDLinker (parameters[0], parameters[1], parameters[2], parameters[3], parameters[4], parameters[5], parameters[6], parse);
			
			/*Tree arbo = new Tree (parse);
			arbo.createArbo();*/
		}
	}
}
