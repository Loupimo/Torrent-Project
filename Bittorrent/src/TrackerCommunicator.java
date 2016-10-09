import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
	private String downloaded;
	private String left;
	private String event;
	
	public TrackerCommunicator(String url, String info_hash, String peer_id, String ip, String port, String downloaded, String left, String event){
		this.url = url;
		this.charset = "UTF-8";
		this.info_hash = info_hash;
		this.peer_id = peer_id;
		this.ip = ip;
		this.port = port;
		this.downloaded = downloaded;
		this.left = left;
		this.event = event;
	}
	
	public String HTTPGet(){
		String query = "";
		InputStream response;
		String infoTracker = "";
		//On rentre les infos du fichier .torrent dans un m�me string pour la requ�te
		try {
			query = String.format("info_hash=%s&peer_id=%s&ip=%s&port=%s&downloaded=%s&left=%s&event=%s",
					URLEncoder.encode(info_hash, charset),
					URLEncoder.encode(peer_id, charset),
					URLEncoder.encode(ip, charset),
					URLEncoder.encode(port, charset),
					URLEncoder.encode(downloaded, charset),
					URLEncoder.encode(left, charset),
					URLEncoder.encode(event, charset)
					);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			URLConnection connection = new URL(url + "?" + query).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			response = connection.getInputStream();
			try(Scanner scanner = new Scanner(response)){
				String responseBody = scanner.useDelimiter("\\A").next();
				infoTracker = infoTracker + responseBody;
				System.out.println(responseBody);
			} 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return infoTracker;
	}
}
