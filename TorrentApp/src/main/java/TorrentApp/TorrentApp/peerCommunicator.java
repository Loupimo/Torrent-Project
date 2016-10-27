package TorrentApp.TorrentApp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class peerCommunicator {
	
	private String port;
	private String ipAdress;
	private Socket s;
	private DataOutputStream out = null;
	private DataInputStream response= null;
	private TrackerCommunicator track;
	private int lengthInfo;
	
	public peerCommunicator(String ipAdress, String port){
		this.ipAdress=ipAdress;
		this.port=port;
	}
	
	public boolean openCom(){
		try{
			s= new Socket(ipAdress, Integer.parseInt(port));
			return true;
		}
		catch(Exception e){
			System.out.println(e);
			return false;
			
		}
	}
	
	public boolean closeCom(){
		try{
			if (s!=null){
				s.close();
			
		}
			return true;
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}	
	}
	
	public boolean Stream(){
		try{
			out =new DataOutputStream(s.getOutputStream());
			response = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		}
		catch (Exception e){
			System.out.println(e);
		}
		if (out ==null || response ==null){
			return false;
		}else{		
		return true;
		}
	}
	
	public boolean SendHandshake(String info_hash, String peer_id){
		//on cree une variable de longueur total
		//on cree un byte contenant tout notre handshake 
		//on fait des variables contenant chaque element 
		//on raccorde ca a notre byte total avec arraycopy qui va nous permettre de mettre a la suite
		int length=0;
		byte[] handshake =new byte [120]; //49+len(pstr)  on augment car l info hash fait 52
		handshake[0]=0x13;  //equivalent a 19 c'est le pstrln
		length++;
		
		byte[] pstr = new String("BitTorrent protocol").getBytes();
		System.arraycopy(pstr,0,handshake,length,pstr.length); //on ajoute pstr a handshake
		length=length + pstr.length; //on allonge la longueur
		
		byte[] reserved= new byte[8];
		System.arraycopy(reserved,0,handshake,length,reserved.length);
		length=length+reserved.length;
		
		byte[] info_hashByte = info_hash.getBytes(); //  on convertit en byte
		lengthInfo=info_hashByte.length;
		System.arraycopy(info_hashByte, 0, handshake, length, info_hashByte.length);//on ajoute le info_hash
		length=length+info_hashByte.length;
		byte[] peer_idByte =peer_id.getBytes();
		System.arraycopy(peer_idByte, 0, handshake, length, peer_idByte.length);
		length=length+peer_idByte.length;
		
	
		
		try{
			out.write(handshake);
			out.flush();
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	public boolean ReceiveHandshake(){
		//on recupere la reponse 
		//on la compare au info_hash si ca correspond alors on renvoit true 
		byte[] responseHandshake= new byte[120]; //reponse total
		byte[] responseInfo_hash =new byte[lengthInfo]; //reponse de ce qui nous interesse le info_hash
		System.out.println("l'info fait :"+lengthInfo);
		try{
			response.read(responseHandshake); // on va lire toute la reponse
			System.arraycopy(responseHandshake, 28, responseInfo_hash, 0, responseInfo_hash.length); //on va selectionner juste la partie hash que l'on va stocker dans responseInfo_hash 
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	
}
