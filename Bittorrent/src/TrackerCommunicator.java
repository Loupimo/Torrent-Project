import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

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
	
	public TrackerCommunicator(String url, String info_hash, String peer_id,/* String ip,*/ String port, int uploaded, int downloaded, int left, String event){
		this.url = url;
		this.charset = "UTF-8";
		this.info_hash = info_hash;
		this.peer_id = peer_id;
		//this.ip = ip;
		this.port = port;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
		this.event = event;
	}
	
	public String HTTPGet(){
		String query = "";
		String infoTracker = "Fin co";
		//On rentre les infos du fichier .torrent dans un même string pour la requête
		try {
			System.out.println("COUCOU");
			/*query = String.format("info_hash=%s&peer_id=%s&port=%s&uploaded=%d&downloaded=%d&left=%d&event=%s",
					URLEncoder.encode(info_hash, charset),
					URLEncoder.encode(peer_id, charset),
					//URLEncoder.encode(ip, charset),
					port,
					uploaded,
					left,
					event
					);*/
			//query = query + "&port="+port;//+"&downloaded="+downloaded+"&left="+left+"&event="+event;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//query = "info_hash="+info_hash+"&peer_id="+peer_id+"&ip="+ip+"&port="+port+"&downloaded="+downloaded+"&left="+left+"&even="+event;
		System.out.println(query);
		try{
			//Création de la connexion
			URL urlTracker = new URL(url); //l'url du tracker
			HttpURLConnection connection = (HttpURLConnection) urlTracker.openConnection(); //Ouverture de la connexion avec le tracker
			connection.setRequestMethod("GET"); //On cherche à faire un GET
			connection.setRequestProperty("Accept-Charset", charset); //On précise le charset utf-8
			connection.setDoOutput(true);
			System.out.println("Connection");
		
			//Requête
			DataOutputStream request = new DataOutputStream(connection.getOutputStream()); //On ouvre un stream pour envoyer la requête
			request.writeBytes(url+"?"+query); //Ecriture de la requête dans le stream
			System.out.println("Requête");
			request.close(); //On ferme le stream
			
			//Réponse du tracker
			InputStream response = connection.getInputStream(); //Ouverture du stream de réponse du tracker
			System.out.println("InputStream");
			BufferedReader reader = new BufferedReader(new InputStreamReader(response));
			StringBuffer buffer = new StringBuffer();
			String line;
			while((line = reader.readLine()) != null){ //Récupération de la réponse
				buffer.append(line);
				buffer.append('\r');
			}
			response.close();
			infoTracker = buffer.toString(); //Traduction de la réponse en string
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return infoTracker;
	}
}
