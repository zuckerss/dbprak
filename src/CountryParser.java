package src;/*
 * src.XMLParserDemo.java
 */

// allgemeine Java-Klassen 
import java.io.IOException;
import java.util.Vector;

// Klassen der SAX-src.Parser-API
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

// Klasse des SAX XML-Parsers (Xerces von Apache)
import org.apache.xerces.parsers.SAXParser;

/** 
 * Demoprogramm zum Parsen von XML-Daten.
 * <P>
 * Klasse ist von DefaultHandler abgeleitet. Diese implementiert
 * alle notwendigen Methoden des ContentHandler-Interface, so
 * dass nur die projektspezifischen Methoden &uuml;berladen werden
 * m&uuml;ssen.<P>
 * Diese Demo-Klasse liest alle wichtigen Daten aus der cities.xml
 * Datei ein. F&uuml;r diese Daten fehlt nur noch die Datenbankschnittstelle.<BR>
 * Sie sollte sich aber auch einfach an die Anforderungen der country????.xml
 * Dateien anpassen lassen.
 * @author  Timo B&ouml;hme
 * @version 1.0
 */
public class CountryParser extends DefaultHandler {

  // -- Globale Variablen ----------------------------------------------------
  /** XML src.Parser (mit SAX API) */
  public final static String parserClass = "org.apache.xerces.parsers.SAXParser";

  /** enth&auml;lt Zeichendaten eines Elements<BR>
   *  Achtung: mixed content wird nicht bzw. falsch behandelt */ 
  private StringBuffer elementTextBuf = new StringBuffer();
  /** enth&auml;lt die Elementnamen des aktuellen Pfades */
  private Vector xmlPath = new Vector();
  
  /** enth&auml;lt aktuelles Land */
  private Land curCountry = new Land();
  private Zeitdaten curZD = new Zeitdaten();
  private Religion curRel = new Religion();
  private Aggrarprodukt curAg = new Aggrarprodukt();
  private Grenztan curGA=new Grenztan();

  // -- Inner Classes -------------------------------------------------------
  /** Kapselt Daten eines Landes */
  class Land {
    // Daten
    String lname = null;
    String altname = null;
    String lkurzname = null;
    String introduction = null;
    String erdteil = null;
    double flaeche = Double.NaN;
    int grenzID= 0;
    int mitgliedIn=0;
    String ort=null;
    String punktkoordinaten=null;
    /** Setzt Daten auf Standardwerte zur&uuml;ck. */
    public void reset() {
      lname = null; altname = null; lkurzname=null;introduction=null;
      erdteil = null; flaeche = Double.NaN; grenzID=0;mitgliedIn=0;
      ort = null; punktkoordinaten = null;
    }
  }
  
  class Zeitdaten {
	  int Jahr = 0;
	  double BIP = Double.NaN;
	  int einwohnerzahl = 0;
	  double geschlechtergeburt = Double.NaN;
	  double geschlechtergesamt = Double.NaN;
	  double lebenserwartung_weibl = Double.NaN;
	  double lebenserwartung_maennl = Double.NaN;
	  
	  public void reset() {
		  Jahr = 0; BIP= Double.NaN; einwohnerzahl=0;geschlechtergeburt = Double.NaN;geschlechtergesamt = Double.NaN;
		  lebenserwartung_maennl = Double.NaN;lebenserwartung_weibl = Double.NaN;
	  }
  }
  class Religion {
	  int anzahlanhaenger = 0;
	  String lname = null;
	  String rname = null;
	  
	  public void reset() {
		  anzahlanhaenger = 0;lname =null;rname = null;
	  }
  }
  class Grenztan {
	  int grenzid = 0;
	  double laenge  = Double.NaN;
	  String lname = null;
	  
	  public void reset() {
		 grenzid=0; laenge = Double.NaN; lname = null; 
	  }
  }
  
  class Aggrarprodukt {
	  String aname = null;
	  String lname = null;
	  double menge  = Double.NaN;
	  
	  public void reset() {
		  aname = null; lname=null; menge = Double.NaN;
	  }
  }

  
  // -------------------------------------------------------------------------
  /** Constructor */
  public CountryParser() {
  }

  // == XML spezifische Methoden =============================================
  // -------------------------------------------------------------------------
  /**
   * Ermittelt den Namen des &uuml;bergeordneten Elementes.
   * @return Namen des &uuml;bergeordneten Elementes oder <code>null</code>
   *         wenn kein &uuml;bergeordnetes Element vorhanden ist
   */
  private String getParent() {
    return (xmlPath.size() > 1) ? (String) xmlPath.elementAt(xmlPath.size()-2) : "";
  }
  
  // == Call back Routinen des Parsers =======================================
  // -------------------------------------------------------------------------
  /**
   * Wird vom src.Parser beim Start eines Elements aufgerufen.<BR>
   * (call back method)
   */
  public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) {
    
    // neues Element -> Textinhalt zuruecksetzen
    elementTextBuf.setLength(0);
    // aktuellen Elementnamen an Pfad anf�gen
    xmlPath.addElement(rawName);
    
    // gesonderte Behandlungsroutinen je nach Elementtyp
    if (rawName.equals("Government")) {
      curCountry.reset();
      return;
    }
    if (rawName.equals("Religions")) {
      curRel.reset();
      return;
    }
    if (rawName.equals("border_countries")) {
        curGA.reset();
        return;
      }
    if (rawName.equals("People")) {
        curZD.reset();
        return;
      }
    if (rawName.equals("Religions")) {
        curRel.reset();
        return;
      }
  }

  // -------------------------------------------------------------------------
  /**
   * Wird vom src.Parser beim Ende eines Elements aufgerufen.<BR>
   * (call back method)
   */
  public void endElement(String namespaceURI, String localName, String rawName) {
    
    // entferne Whitespace an Zeichendatengrenzen
    String elementText = elementTextBuf.toString().trim();
    
    try {      
      // gesonderte Behandlungsroutinen je nach Elementtyp
      if (rawName.equals("country")) {
	System.out.println("Daten eingelesen zu Land: " + curCountry.lname);
        return;
      }
      
//      if (rawName.equals("city")) {
//	System.out.println("Daten eingelesen zu Stadt: " + curCity.name);
//	// jetzt kann Stadt in Datenbank geschrieben werden ...
//        return;
//      }

      // land elements
      if (rawName.equals("conventional_long_form")) {
        curCountry.lname = elementText;
        return;
      }
      if (rawName.equals("conventional_short_form")) {
          curCountry.lkurzname = elementText;
          return;
        }
      if(rawName.equals("total")) {
    	  if(getParent().equals("area"))
    		  curCountry.flaeche = Double.parseDouble(elementText);
      }
      if(rawName.equals("")) {
    	  curCountry.altname = elementText;
      }
      if(rawName.equals("")) {
    	  curCountry.grenzID=0;
      }
      if(rawName.equals("")) {
    	  curCountry.introduction = elementText;
      }
      if(rawName.equals("")) {
    	  curCountry.ort = elementText;
      }
      if(rawName.equals("")) {
 //   	  curCountry.punktkoordinaten
      }
      if(rawName.equals("")) {
    	  curCountry.punktkoordinaten=elementText;
      }
      if(rawName.equals("")) {
    	  curCountry.punktkoordinaten+=";"+elementText;
      }
      if(rawName.equals("")) {
    	  curAg.aname = elementText;
      }
      if(rawName.equals("")) {
    	  curAg.lname = curCountry.lname;
      }
      if(rawName.equals("")) {
    	  curAg.menge = Double.parseDouble(elementText);
      }
      if(rawName.equals("")) {
    	  curRel.rname = elementText;
      }
      if(rawName.equals("")) {
    	  curRel.lname = curCountry.lname;
      }
      if(rawName.equals("")) {
    	  curRel.anzahlanhaenger = Integer.parseInt(elementText);
      }
      if(rawName.equals("")) {
    	  curZD.BIP = Double.parseDouble(elementText);
      }
      if(rawName.equals("")) {
    	  curZD.einwohnerzahl = Integer.parseInt(elementText);
      }
      if(rawName.equals("")) {
    	  curZD.geschlechtergeburt = Double.parseDouble(elementText);
      }
      if(rawName.equals("")) {
    	  curZD.geschlechtergesamt = Double.parseDouble(elementText);
      }
      if(rawName.equals("")) {
    	  curZD.Jahr = Integer.parseInt(elementText);
      }
      if(rawName.equals("")) {
    	curZD.lebenserwartung_maennl = Double.parseDouble(elementText);  
      }
      if(rawName.equals("")) {
    	  curZD.lebenserwartung_weibl = Double.parseDouble(elementText);
      }
    } finally {    
      // Element zu ende -> Textinhalt zuruecksetzen
      // notwendig bei mixed content
      elementTextBuf.setLength(0);
      // Element vom Pfad entfernen
      xmlPath.setSize(xmlPath.size()-1);
    }
    
  }

  // -------------------------------------------------------------------------
  /**
   * Wird vom src.Parser mit Textinhalt des aktuellen Elements aufgerufen.<BR>
   * (call back method)<P>
   * Achtung: Entities (auch Zeichenreferenzen wie &amp;ouml;) stellen eine
   *          Textgrenze dar und werden durch einen erneuten Aufruf dieser
   *          Funktion &uuml;bergeben.
   */
  public void characters(char[] ch, int start, int length) {
    elementTextBuf.append(ch, start, length);
  }
  
  // -------------------------------------------------------------------------
  /** Initialisiert src.Parser und started Proze�*/
  public void doit(String dataFilename) {

    // XML src.Parser
    XMLReader parser = null;
    
    // src.Parser instanziieren
    try {
      parser = XMLReaderFactory.createXMLReader(parserClass);
    } catch (SAXException e) {
      System.err.println("Fehler beim Initialisieren des Parsers (" +
                         parserClass + ")\n" + e.getMessage());
      System.exit(1);
    }
    
    // Ereignisse sollen von dieser Klasse behandelt werden
    parser.setContentHandler(this);
    
    // src.Parser starten
    try {
      parser.parse(dataFilename);
    } catch (SAXException e) {
      System.err.println("src.Parser Exception:\n" + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("src.Parser IOException:\n" + e.getMessage());
    }
    
  }
  
  // -------------------------------------------------------------------------
  /** Gibt Aufrufsyntax zur&uuml;ck */
  public static void usage() {
    System.out.println("usage: java src.XMLParserDemo <XML_FILE>");
    System.exit(1);
  }
  
  // -------------------------------------------------------------------------
  /**
  * Programmstart
  * @param args Aufrufparameter
  */
  public static void main (String args[]) {
    
    // Teste Kommandozeilen-Parameter
    //if (args.length != 1) usage();

    // Programminstanz erzeugen
    XMLParserDemo prg = new XMLParserDemo();
    // ausfuehren
    prg.doit("C:\\Users\\_\\Desktop\\dbpra\\dbprak_data_wfb\\cities.xml");
    
  }

}





