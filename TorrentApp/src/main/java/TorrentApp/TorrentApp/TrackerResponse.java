package TorrentApp.TorrentApp;

import java.util.Vector;

public class TrackerResponse
{
	
	////////////////
	// Attributes //
	////////////////
	
	protected byte[] responseData;
	protected Parser parserResponse;
	protected Vector <Peer> peerList;
	private String info_hash; 
	private String peer_id;
	
	//////////////////
	// Constructors //
	//////////////////
	
	TrackerResponse (byte[] p_responseData)
	{
		responseData = p_responseData;
		peerList = new Vector <Peer>();
		parserResponse = new Parser (responseData);
		parserResponse.parseData(parserResponse);
	}
	
	TrackerResponse (byte[] p_responseData,String info_hash, String peer_id)
	{
		responseData = p_responseData;
		peerList = new Vector <Peer>();
		parserResponse = new Parser (responseData);
		parserResponse.parseData(parserResponse);
		this.info_hash=info_hash;
		this.peer_id=peer_id;
	}
	
	
	/////////////
	// Methods //
	/////////////
	
	public void createPeerList ()
	{ // Extracts the peer list from the tracker response
		
		Object peerListType = parserResponse.parsedInfo.elementAt(0).aDico.getWordDefinition(parserResponse.parsedInfo.elementAt(0).aDico.getWordIndexByString("peers"));
		
		if (peerListType.getClass().toString().equals("class TorrentApp.TorrentApp.List"))
		{ // The peer list is in a Bencoded dictionary
			peerListType = (List) peerListType;
		}
		else
		{ // The peer List is in a Binary format
			
			String peerListBinary = (String) peerListType, ipAddress = "", port = "";
			peerListBinary = Common.byteToHex(peerListBinary.getBytes());
			int hexToInt;
			peerList = new Vector <Peer> ();
			
			for (int i = 0, delimiter = 0; i < peerListBinary.length(); i += 2, delimiter++)
			{ // Convert the Binary format into a Peer list
				
				// Delimiter is here to split IP address and port. When delimiter = 5 it means that the next data is part of a new peer
				hexToInt = Integer.decode("0x" + peerListBinary.charAt(i) + peerListBinary.charAt(i+1));
				
				if (delimiter < 4)
				{ // IP address
					ipAddress += Integer.decode("0x" + peerListBinary.charAt(i) + peerListBinary.charAt(i+1)) + ".";
				}
				
				else if (delimiter < 5)
				{ // Port
					port += Integer.decode("0x" + peerListBinary.charAt(i) + peerListBinary.charAt(i+1) + peerListBinary.charAt(i+2) + peerListBinary.charAt(i+3));
					i += 2; // Because the port is coded on 2 octets
				}
				else
				{ // We have all the info to create the peer
					ipAddress = ipAddress.substring(0, ipAddress.length()-1); // We deletes the last "." at the end of the string
					delimiter = 0; // Reset the delimiter
					peerList.add(new Peer (ipAddress, port,info_hash, peer_id));
					System.out.println("Peer n°" + peerList.size() + ": IP = " + ipAddress + ", Port: " + port);
					ipAddress = hexToInt + "."; // Don't forget that we still have an unused hexToInt value
					port = "";
				}
				
			}
		}
	}
}
