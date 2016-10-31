package TorrentApp.TorrentApp;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;
import java.sql.Connection;
import org.apache.log4j.Logger;


public class BDDLinker 
{
	////////////////
	// Attributes //
	////////////////
	
	/* Database part */
	protected String serverName; // Url of the database
	protected String portNumber; // Port use to connect to the database
	protected String dbName;	 // The name of the desired database
	protected String user;		 // User login
	protected String password;	 // Password login
	
	/* Torrent part */
	protected String torrentName; // Name of the torrent
	protected long realFileSize;  // The total size of torrent contents (in byte). Note: most of the time 1 byte = 1 octet
	protected Parser parser;	  // The parser
	protected String fileName;	  // Name of the file
	private static final Logger LOG = Logger.getLogger(BDDLinker.class);
	
	
	/////////////////
	// Constructor //
	/////////////////
	
	BDDLinker (String p_serverName, String p_portNumber, String p_dbName, String p_user, String p_password, Parser p_parser)
	{
		this.serverName = p_serverName;
		this.portNumber = p_portNumber;
		this.dbName = p_dbName;
		this.user = p_user;
		this.password = p_password;
		this.parser = p_parser;
		runSetters();
		connectToBDD();
	}
	
	
	
	BDDLinker (String p_serverName, String p_portNumber, String p_dbName, String p_user, String p_password, Parser p_parser, String p_torrentName, long p_realFileSize)
	{
		this.serverName = p_serverName;
		this.portNumber = p_portNumber;
		this.dbName = p_dbName;
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
		fileName = parser.torrentFile.getName();
		
		if (torrentName == null)
		{ // As "name" is an optional parameter if it doesn't exist we use the torrent file name
			torrentName = parser.torrentFile.getName().replace(".torrent", "");
		}
		
		torrentName = torrentName.replace("'", "\\\'");
		fileName = fileName.replace("'", "\\\'");
		System.out.println(torrentName);
	}
	
	
	
	protected void setRealFileSize ()
	{ // Gets the size of torrent contents and set the variable
		
		List fileList = parser.getDicoCombinationFromString("files", null); // Retrives the file list inside the .torrent
		String tempSize;
		
		if (fileList != null)
		{ // There's severals files in the torrent
			for (int i = 0; i < fileList.list.size(); i++)
			{ // Adds every file length
			
				tempSize = fileList.list.elementAt(i).aDico.getWordDefinition(fileList.list.elementAt(i).aDico.getWordIndexByString("length")); // Gets a file size 
				realFileSize += Long.parseLong(tempSize); // Converts the string value to long
			
				LOG.debug(tempSize + " + ");
			}
		}
		else
		{ // There's only one file in the torrent
			realFileSize = Integer.parseInt((String)parser.getDicoCombinationFromString("length", null)); // Retrives the file size inside the .torrent
		}
		
		LOG.debug(" = " + realFileSize + " bytes");
	}
	
	
	
	public void connectToBDD ()
	{ // Connect the app to the database

		try 
		{
		    Class.forName( "com.mysql.jdbc.Driver" ).newInstance(); // Load the JDBC driver to communicate with the mysql database
		    
		    /* Properties of the database connection */
		    Connection conn = null;
		    Properties connectionProps = new Properties();
		    connectionProps.setProperty("user", this.user);
		    connectionProps.setProperty("password", this.password);
		    connectionProps.setProperty("useSSL", "false");
		    
		    /* Connection to the database */
		    System.out.println("Try to connect to database");
		    conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + this.serverName /*+ ":" + this.portNumber */+ "/" + this.dbName, connectionProps);
		    System.out.println("Connected to database");
		    
		    insertRowInDB(conn);
		} 
		catch ( ClassNotFoundException e ) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e)
		{
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public void insertRowInDB(Connection conn)
	{
		try {
			// Create a Statement from the connection
			Statement statement = conn.createStatement();
			
			// Insert the data
			statement.executeUpdate("INSERT INTO Advert (NomTorrent, TailleFichier, date, auteur, description, Path_To_File) " + "VALUES ( '" + this.torrentName + "', " + this.realFileSize + ", '" + (new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()).toString()) + "', 'root', 'Je viens de l appli :p', '../uploads/torrent/" + this.fileName + "')");
			
			LOG.debug("Added an entry to the database");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
