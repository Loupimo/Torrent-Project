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
	private String info_hash;
	private String peer_id;
	private String ip;
	private String port;
	private int uploaded;
	private int downloaded;
	private int left;
	private String event;
	private static final Logger LOG = Logger.getLogger(TrackerCommunicator.class);
	
	public TrackerCommunicator(String url, String info_hash, String peer_id,/* String ip,*/ String port, int uploaded, int downloaded, int left, String event)
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
	}
	
	
	
	public TrackerCommunicator(String url, byte[] info_hash, String peer_id,/* String ip,*/ String port, int uploaded, int downloaded, int left, String event)
	{
		this.url = url;
		this.charset = "gzip";
		this.info_hash = encodeHashB(DigestUtils.sha1(info_hash));
		this.peer_id = peer_id;
		//this.ip = ip;
		this.port = port;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
		this.event = event;
	}
	
	public String HTTPGet()
	{
		String query = "";
		String infoTracker = "Fin co";
		System.out.println("COUCOU");
		query = url+"?"+"info_hash="+info_hash+"&peer_id="+peer_id+"&port="+port+"&uploaded="+uploaded+"&downloaded="+downloaded+"&left="+left+"&event="+event;
		System.out.println(query);
		try{
			//Création de la connexion
			URL urlTracker = new URL(query); //l'url du tracker
			HttpURLConnection connection = (HttpURLConnection) urlTracker.openConnection(); //Ouverture de la connexion avec le tracker
			connection.setRequestMethod("GET"); //On cherche à faire un GET
			connection.setRequestProperty("Accept-Charset", charset); //On précise le charset utf-8
			//connection.setRequestProperty("Host", "t411.download");
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
		    byte[] responseB = out.toByteArray();
		    for (int i = 0; i < responseB.length; i++)
		    {
		    	System.out.print((char)responseB[i]);
		    }
		    System.out.println("");
		    /*
			while((line = reponse.read()) != null){ //Récupération de la réponse
				buffer.append(line);
				buffer.append('\r');
			}*/
			response.close();
			//infoTracker = buffer.toString(); //Traduction de la réponse en string
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return infoTracker;
	}
	
	
	
	public static String encodeHash (String hexHash)
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
				encodedHash += (char) realHex;
			}
			else
			{ // The integer value is considered as a non-safe value so we keep its hexadecimal form
				encodedHash += "%" + hexInfo;
			}
		}
		
		LOG.debug(encodedHash);
		
		return encodedHash;
	}
	
	
	
	public static String encodeHashB (byte[] hash)
	{ // Encode the given bytes to the Bittorrent request form
		
		char[] hexArray = "0123456789ABCDEF".toCharArray();
	
		char[] hexChars = new char[hash.length * 2];
		
	    for ( int j = 0; j < hash.length; j++ )
	    {
	        int v = hash[j] & 0xFF; // Copies a bit to the result if it exists in both operands
	        hexChars[j * 2] = hexArray[v >>> 4]; // The left operands value is moved right by the number of bits specified by the right operand and shifted values are filled up with zeros.
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
		
	    LOG.debug(new String(hexChars));
		
		return encodeHash(new String(hexChars));
	}
}
