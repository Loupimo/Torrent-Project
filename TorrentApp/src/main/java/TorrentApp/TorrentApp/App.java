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
			String oo = "D:\\Documents\\Visual Studio 2015\\Projects\\TorrentParser\\Debug\\Assassins_Creed_IV_Black_Flag_Update_v1_02_with_DLC-RELOADED.torrent";

			Parser parse = new Parser(oo);
			if (parse.torrentFile != null)
			{
				parse.readFile();
			}
			System.out.println("\n\nFin Parser");
		}
	}
}
