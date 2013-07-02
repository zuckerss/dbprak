package src;

import java.sql.*;
import java.io.*;
public class DB2Query {
	  // wird beim Initialisieren der Klasse ausgefuehrt
	  static {
	    // Laden des JDBC-Treibers
	    try {
	      Class.forName("com.ibm.db2.jcc.DB2Driver"); // Typ-4-Treiber

	    } catch (Exception e) {
	      e.printStackTrace();
	      System.err.println("JDBC-Treiber konnte nicht geladen werden.\n" +
	                         "Bitte den CLASSPATH ueberpruefen.");
	      System.exit(1);
	    }
	  }
	    // Verbindungsobjekt zur Datenbank
	    Connection con = null;
	    // Objekt zum Ausfuehren von Queries
	    Statement stmt = null;
	    PreparedStatement pstmt = null;
	    
	    // Enthaelt die Query-Ergebnisse
	    
	    
	    ResultSet rs = null;
	    // Nimmt die Query auf
	    String query = null;
	    //  JDBC-URL zur Datenbank
	    //String url = "jdbc:db2://139.18.4.34:4019/S1D931";
	    String url = "jdbc:db2://leutzsch.informatik.uni-leipzig.de:50001/PRAK13A";
	    // Login-Name aus Aufruf-Parameter
	    String userid = "dbprak02";

	    String passwd = ".pr$AK02";
	  public DB2Query(){
		  try {
			con = DriverManager.getConnection(url, userid, passwd);
		//	stmt = con.createStatement();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	  public boolean insert(String query) {
		  try {
			  //System.out.println(query);
			  stmt = con.createStatement();
			  stmt.executeUpdate(query);
			  stmt.close();
		  }catch (SQLException e) {
			  System.err.println("Error db2query" + e.getMessage()+ "\nQuery: "+ query);
			  return false;
		  }		  
		  return true;
	  }
	  public void close() {
		  try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
