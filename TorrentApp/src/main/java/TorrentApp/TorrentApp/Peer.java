package TorrentApp.TorrentApp;

public class Peer 
{
	
	////////////////
	// Attributes //
	////////////////
	
	protected String ip; // The IP address of peer
	protected String port;  // The port used to connect to the peer
	
	
	//////////////////
	// Constructors //
	//////////////////
	
	Peer (String p_ip, String p_port)
	{
		ip = p_ip;
		port = p_port;
	}
	
	
	/////////////
	// Methods //
	/////////////
	
	public void requestPiece ()
	{ // Gets a piece of a file from the peer
		
	}
}
