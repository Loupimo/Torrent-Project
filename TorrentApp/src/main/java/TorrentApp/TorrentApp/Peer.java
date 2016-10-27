package TorrentApp.TorrentApp;

import java.io.IOException;
import java.net.Socket;

public class Peer 
{
	
	////////////////
	// Attributes //
	////////////////
	
	protected String ip; // The IP address of peer
	protected String port;  // The port used to connect to the peer
	private peerCommunicator peerCom;
	boolean connexion;
	private String info_hash;
	private String peer_id; 
	//////////////////
	// Constructors //
	//////////////////
	
	Peer (String p_ip, String p_port, String info_hash, String peer_id)
	{
		ip = p_ip;
		port = p_port;
		peerCom= new peerCommunicator(ip, port); 
		this.info_hash=info_hash;
		this.peer_id=peer_id;
		requestPiece();
		
	}
	
	/////////////
	// Methods //
	/////////////
	
	public void requestPiece ()
	{ // Gets a piece of a file from the peer
		connexion =peerCom.openCom();
		if (connexion ==true){
		peerCom.Stream();
		peerCom.SendHandshake(info_hash,peer_id);
		peerCom.ReceiveHandshake();
		peerCom.closeCom();
		}
		
		
	}
}
