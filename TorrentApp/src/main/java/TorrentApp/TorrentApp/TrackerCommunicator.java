package TorrentApp.TorrentApp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;


public class TrackerCommunicator {
	private String url;
	private String charset;
	public static String info_hash;
	private static String peer_id;
	//private String ip;
	private String port;
	private int uploaded;
	private int downloaded;
	private int left;
	private String event;
	private int compact;
	private static final Logger LOG = Logger.getLogger(TrackerCommunicator.class);
	
	public TrackerCommunicator(String url, String info_hash, String peer_id,/* String ip,*/ String port, int uploaded, int downloaded, int left, String event, int compact)
	{
		this.url = url;
		this.charset = "gzip";
		this.info_hash = info_hash;
		this.peer_id = peer_id;
		//this.ip = ip;
		this.port = port;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
		this.event = event;
		this.compact = compact;
	}
	
	
	
	public TrackerCommunicator(String url, byte[] info_hash, String peer_id,/* String ip,*/ String port, int uploaded, int downloaded, int left, String event, int compact)
	{
		this.url = url;
		this.charset = "gzip";
		this.info_hash = encodeHash(Common.byteToHex(DigestUtils.sha1(info_hash)));
		this.peer_id = peer_id;
		//this.ip = ip;
		this.port = port;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
		this.event = event;
		this.compact = compact;
	}
	
	public byte[] HTTPGet()
	{
		String query = "";
		query = url+"?"+"info_hash="+info_hash+"&peer_id="+peer_id+"&port="+port+"&uploaded="+uploaded+"&downloaded="+downloaded+"&left="+left+"&event="+event+"&compact="+compact;
		System.out.println(query);
		try{
			// Création de la connexion
			URL urlTracker = new URL(query); //l'url du tracker
			HttpURLConnection connection = (HttpURLConnection) urlTracker.openConnection(); //Ouverture de la connexion avec le tracker
			connection.setRequestMethod("GET"); //On cherche à faire un GET
			connection.setRequestProperty("Accept-Charset", charset); //On précise le charset utf-8
			//System.out.println(url.substring(url.indexOf("//", 0) + 2, url.indexOf("/", url.indexOf("//", 0) + 2)));
			//connection.setRequestProperty("Host", url.substring(url.indexOf("//", 0) + 2, url.indexOf("/", url.indexOf("//", 0) + 2)));
			System.out.println("Connection");
		
			//Requête
			System.out.println("Requête");
			System.out.println(connection.getRequestMethod());
			//request.close(); //On ferme le stream
			
			//Réponse du tracker
			InputStream response = new BufferedInputStream(connection.getInputStream()); //Ouverture du stream de réponse du tracker
			System.out.println("InputStream");
			
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    byte[] buf = new byte[1024];
		    int n = 0;
		    while (-1!=(n=response.read(buf)))
		    {	    	
		    out.write(buf, 0, n);	
		    }
		    out.close();
		    response.close();
		    byte[] reponseB = out.toByteArray();
		    for (int i = 0; i < reponseB.length; i++)
		    {
		    	System.out.print((char)reponseB[i]);
		    }
		    System.out.println();
		    
			LOG.debug (Common.byteToHex(reponseB));

			return reponseB;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public String encodeHash (String hexHash)
	{ // Encode the given string to the Bittorrent request form  
		
		String encodedHash = ""; // The final encoded hash
		
		String hexInfo = ""; // The current hex value
		int realHex; // The integer corresponding to the hex value
		
		char[] array = hexHash.toCharArray();
		
		for (int i = 0; i < array.length; i+=2)
		{
			hexInfo = "" + array[i] + array[i+1]; // Build the hex value with two chars
			
			LOG.debug  ("Hexa: " + hexInfo);
			
			realHex = Integer.decode("0x" + hexInfo); // Convert the hex string to its integer value
			
			LOG.debug ("Int: " + realHex);
			
			if ((realHex >= 48 && realHex <= 57) || (realHex >= 65 && realHex <= 90) || (realHex >= 97 && realHex <= 122) || realHex == 45 || realHex == 46 || realHex == 95 || realHex == 126)
			{ // The integer value is the ASCII of one of the following character: "0-9", "A-Z", "a-z", "-", ".", "_", "~"
				encodedHash += ((char) realHex);
			}
			else
			{ // The integer value is considered as a non-safe value so we keep its hexadecimal form
				encodedHash += "%" + hexInfo.toLowerCase();
			}
		}
		
		LOG.debug(encodedHash);
		
		return encodedHash;
	}
	
	public String getInfo_hash() {
		return info_hash;
	}
	
	public String getPeer_id(){
		return peer_id;
	}
}
