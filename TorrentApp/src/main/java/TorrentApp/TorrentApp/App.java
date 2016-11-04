package TorrentApp.TorrentApp;


/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args)
	{
		Parser parse;
		if (args.length > 0)
		{
			parse = new Parser (args[0]);
		}
		else
		{
			//String oo = "TorrentTest\\Assassins_Creed_IV_Black_Flag_Update_v1_02_with_DLC-RELOADED.torrent"; // HTTP
			//String oo = "TorrentTest\\Jennifer_Lopez_-_Hitz_Music_Video_2014_1080pHD_[TG-Encoder_www.torrentgarden.com].torrent"; // UDP
			//String oo = "TorrentTest\\Vicious_Rumors_-_Live_You_To_Death.torrent"; // the.piratebay.org (unknown host problem)
			//String oo = "TorrentTest\\lubuntu-16.10-desktop-amd64.iso.torrent"; // Needs Compact set to 1
			//String oo = "TorrentTest\\Game_Dev_Tycoon_v1.4.15_(2-click_run).torrent"; // Anounce-list présent: à gérer
			//String oo = "TorrentTest\\26FCB96CB5451F1080C09DBD7749219BC13D27CE.torrent";
			//String oo = "TorrentTest\\437487C32CE82F050CDBFCCFC04E4E20ACA0D4CB.torrent";
			//String oo = "TorrentTest\\Arrow.S05E03.HDTV.x264-font.color=#ccc--LOL--font-.torrent";
			//String oo = "TorrentTest\\Asphyx_-_Incoming_Death_[Metal]_(2016)_(mp3)_[Isohunt.to].torrent";
			//String oo = "TorrentTest\\Avenged_Sevenfold_Rocksmith_2014_CDLC_Pack.torrent";
			//String oo = "TorrentTest\\batman-unlimited-mechs-vs-mutants-french-webrip-2016.torrent";
			//String oo = "TorrentTest\\C053A10A526B34511FD970A224824062AACD699E.torrent";
			//String oo = "TorrentTest\\David Morrell-Portrait de l'assassin en artiste.epub.torrent";
			//String oo = "TorrentTest\\Mario Kart 8.torrent";
			//String oo = "TorrentTest\\Mario.Kart.8.WiiU.torrent";
			//String oo = "TorrentTest\\Stone Sour - House Of Gold & Bones Part 1 [FLAC].torrent";
			//String oo = "TorrentTest\\Stone Sour.torrent";
			String oo = "TorrentTest\\Super.Smash.Bros.for.WiiU.WiiU.torrent";

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
			//new BDDLinker ("jdbc:mysql://localhost:8000/", "jul", "root", parse);
			
			Tree arbo = new Tree (parse);
			arbo.createArbo();
		}
	}
}
