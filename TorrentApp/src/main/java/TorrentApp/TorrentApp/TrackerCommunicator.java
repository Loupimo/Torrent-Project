package TorrentApp.TorrentApp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
	
	public String HTTPGet(){
		String query = "";
		String infoTracker = "Fin co";
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
		query = "info_hash="+info_hash+"&peer_id="+peer_id+"&port="+port+"&downloaded="+downloaded+"&left="+left+"&event="+event;
		System.out.println(query);
		try{
			//Création de la connexion
			URL urlTracker = new URL("http://t411.download/7d8c1038e6ddb08272ccdb7e1e13d6c0/announce?info_hash=M%b1%28m%cc%5c%97%9f%9a%3ac%1d%c3~%17%b2%8c%60%cb%d6&peer_id=-UT3490-n%a6%d0%a6%c9%ec%9e%e5%e0z%f4%be&port=50755&uploaded=0&downloaded=10485760&left=116023550"); //l'url du tracker
			HttpURLConnection connection = (HttpURLConnection) urlTracker.openConnection(); //Ouverture de la connexion avec le tracker
			connection.setRequestMethod("GET"); //On cherche à faire un GET
			connection.setRequestProperty("Accept-Charset", charset); //On précise le charset utf-8
			connection.setRequestProperty("Host", "t411.download");
			//connection.setDoOutput(true);
			System.out.println("Connection");
		
			//Requête
			//DataOutputStream request = new DataOutputStream(connection.getOutputStream()); //On ouvre un stream pour envoyer la requête
			//request.writeBytes("/7d8c1038e6ddb08272ccdb7e1e13d6c0/announce?info_hash=M%b1%28m%cc%5c%97%9f%9a%3ac%1d%c3~%17%b2%8c%60%cb%d6 HTTP/1.1"); //Ecriture de la requête dans le stream
			//request.flush();
			//request.close();
			System.out.println("Requête");
			System.out.println(connection.getRequestMethod());
			//request.close(); //On ferme le stream
			
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
