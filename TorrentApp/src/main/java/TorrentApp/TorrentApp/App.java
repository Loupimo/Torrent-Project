package TorrentApp.TorrentApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

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
			
			
			
			try {
				File torrentFile = new File("sha1test.torrent");
				long fileSize = torrentFile.length(); // Size of the file

				byte[] datas = new byte [(int)fileSize];
				
				FileInputStream iosTorrent;
				iosTorrent = new FileInputStream (torrentFile);
				iosTorrent.read(datas); // Read all bytes and stored them in the array
				iosTorrent.close();
				TrackerCommunicator tc = new TrackerCommunicator("http://t411.download/7d8c1038e6ddb08272ccdb7e1e13d6c0/announce", DigestUtils.sha1Hex(datas), "192.168.20.1", "6881", 0, 0, 1000, "started");
				System.out.println(tc.HTTPGet());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
