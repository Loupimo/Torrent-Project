package TorrentApp.TorrentApp;


/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			String torrentName = args[0];

			Parser parse = new Parser (torrentName);
			if (parse.torrentFile != null)
			{
				parse.readFile();
			}
			System.out.println("\n\nFin Parser");
		}
		else
		{
			String oo = "Assassins_Creed_IV_Black_Flag_Update_v1_02_with_DLC-RELOADED.torrent"; // Chemin rentré en brutal pour accélérer les tests

			Parser parse = new Parser(oo);
			if (parse.torrentFile != null)
			{
				parse.readFile();
			}
			System.out.println("\n\nFin Parser");
			
			//TrackerCommunicator tc = new TrackerCommunicator((String) parse.getDicoCombinationFromString("announce", null), parse.getBencodedDicoInfo(), "192.168.20.1", "50755", 0, 10485760, 116023550, "started");
			//System.out.println(tc.HTTPGet());
			new BDDLinker ("jdbc:mysql://localhost:8000/", "jul", "root", parse);
		}
	}
	
}
