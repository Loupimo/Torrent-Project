package TorrentApp.TorrentApp;

import org.apache.log4j.Logger;

public class BDDLinker 
{
	////////////////
	// Attributes //
	////////////////
	
	/* Database part */
	protected String url;		// Url of the database
	protected String user;		// User login
	protected String password;	// Password login
	
	/* Torrent part */
	protected String torrentName; // Name of the torrent
	protected long realFileSize;  // The total size of torrent contents (in byte). Note: most of the time 1 byte = 1 octet
	protected Parser parser;	  // The parser
	private static final Logger LOG = Logger.getLogger(BDDLinker.class);
	
	
	/////////////////
	// Constructor //
	/////////////////
	
	BDDLinker (String p_url, String p_user, String p_password, Parser p_parser)
	{
		this.url = p_url;
		this.user = p_user;
		this.password = p_password;
		this.parser = p_parser;
		runSetters();
	}
	
	
	
	BDDLinker (String p_url, String p_user, String p_password, Parser p_parser, String p_torrentName, long p_realFileSize)
	{
		this.url = p_url;
		this.user = p_user;
		this.password = p_password;
		this.parser = p_parser;
		this.torrentName = p_torrentName;
		this.realFileSize = p_realFileSize;
	}
	
	
	/////////////
	// Methods //
	/////////////
	
	void runSetters ()
	{ // Executes all the setters
		
		setTorrentName();
		setRealFileSize();
	}
	
	
	
	protected void setTorrentName ()
	{ // Gets the name of the torrent and set the variable
		
		torrentName = parser.getDicoCombinationFromString("name", null); // Retrives the name inside the .torrent
		
		if (torrentName == null)
		{ // As "name" is an optional parameter if it doesn't exist we use the torrent file name
			torrentName = parser.torrentFile.getName();
		}
	}
	
	
	
	protected void setRealFileSize ()
	{ // Gets the size of torrent contents and set the variable
		
		List fileList = parser.getDicoCombinationFromString("files", null); // Retrives the file list inside the .torrent;
		String tempSize;
		
		for (int i = 0; i < fileList.list.size(); i++)
		{ // Adds every file length
			
			tempSize = fileList.list.elementAt(i).aDico.getWordDefinition(fileList.list.elementAt(i).aDico.getWordIndexByString("length")); // Gets a file size 
			realFileSize += Long.parseLong(tempSize); // Converts the string value to long
			
			LOG.debug(tempSize + " + ");
		}
		
		LOG.debug(" = " + realFileSize + " bytes");
	}
	
	
	
	void connectToBDD ()
	{ // Connect the app to the database

		try 
		{
		    Class.forName( "com.mysql.jdbc.Driver" ); // Load the JDBC driver to communicate with the mysql database
		} 
		catch ( ClassNotFoundException e ) 
		{
		   
		}
	}
}
