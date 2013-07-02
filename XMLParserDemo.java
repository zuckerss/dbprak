/*
 * XMLParserDemo.java
 */

// allgemeine Java-Klassen 
import java.io.IOException;
import java.util.Vector;
import java.util.Hashtable;

// Klassen der SAX-Parser-API
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
public class XMLParserDemo extends DefaultHandler {

  // -- Globale Variablen ----------------------------------------------------
  /** XML Parser (mit SAX API) */
  public final static String parserClass = "org.apache.xerces.parsers.SAXParser";

  /** enth&auml;lt Zeichendaten eines Elements<BR>
   *  Achtung: mixed content wird nicht bzw. falsch behandelt */ 
  private StringBuffer elementTextBuf = new StringBuffer();
  /** enth&auml;lt die Elementnamen des aktuellen Pfades */
  private Vector xmlPath = new Vector();
  
  /** enth&auml;lt aktuelles Land */
  private Country curCountry = new Country();
  /** enth&auml;lt aktuelle Stadt */
  private City curCity = new City();

  // -- Inner Classes --------------------------------------------------------
  /** Kapselt Daten einer Stadt */
  class City {
    // Daten
    String name = null;
    boolean isCapital = false;
    boolean isPort = false;
    String alternateName = null;
    int inhabitants = -1;
    int inhabitantsRegion = -1;
    float longitude = Float.NaN;
    float latitude = Float.NaN;
    
    /** Setzt Daten auf Standardwerte zur&uuml;ck. */
    public void reset() {
      name = null; isCapital = false; alternateName = null;
      inhabitants = -1; inhabitantsRegion = -1;
      longitude = Float.NaN; latitude = Float.NaN;
    }
  }

  /** Kapselt Daten eines Landes */
  class Country {
    // Daten
    String name = null;

    /** Setzt Daten auf Standardwerte zur&uuml;ck. */
    public void reset() {
      name = null;
    }
  }

  
  // -------------------------------------------------------------------------
  /** Constructor */
  public XMLParserDemo() {
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
   * Wird vom Parser beim Start eines Elements aufgerufen.<BR>
   * (call back method)
   */
  public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) {
    
    // neues Element -> Textinhalt zuruecksetzen
    elementTextBuf.setLength(0);
    // aktuellen Elementnamen an Pfad anfügen
    xmlPath.addElement(rawName);
    
    // gesonderte Behandlungsroutinen je nach Elementtyp
    if (rawName.equals("country")) {
      curCountry.reset();
      return;
    }
    if (rawName.equals("city")) {
      curCity.reset();
      return;
    }
  }

  // -------------------------------------------------------------------------
  /**
   * Wird vom Parser beim Ende eines Elements aufgerufen.<BR>
   * (call back method)
   */
  public void endElement(String namespaceURI, String localName, String rawName) {
    
    // entferne Whitespace an Zeichendatengrenzen
    String elementText = elementTextBuf.toString().trim();
    
    try {      
      // gesonderte Behandlungsroutinen je nach Elementtyp
      if (rawName.equals("country")) {
	System.out.println("Daten eingelesen zu Land: " + curCountry.name);
        return;
      }
      
      if (rawName.equals("city")) {
	System.out.println("Daten eingelesen zu Stadt: " + curCity.name);
	// jetzt kann Stadt in Datenbank geschrieben werden ...
        return;
      }
      
      if (rawName.equals("name")) {
        // Landname
        if (getParent().equals("country")) {
          curCountry.name = elementText;
          System.out.println("Parse Land: " + curCountry.name);
          return;
        }
        // Stadtname
        if (getParent().equals("city")) {
          curCity.name = elementText;
          System.out.println("Parse Stadt: " + curCity.name);
          return;
        }
      }

      // city Elemente
      if (rawName.equals("alternateName")) {
        curCity.alternateName = elementText;
        return;
      }
      if (rawName.equals("inhabitants")) {
        try {
          curCity.inhabitants = Integer.parseInt(elementText);
        } catch (NumberFormatException e) {
          System.err.println("ERR: Falscher Wert für inhabitants (" +
                             curCountry.name + ", " + curCity.name +
                             ", " + elementText + ")\n" + e.getMessage());
        }
        return;
      }
      if (rawName.equals("inhabitantsRegion")) {
        try {
          curCity.inhabitantsRegion = Integer.parseInt(elementText);
        } catch (NumberFormatException e) {
          System.err.println("ERR: Falscher Wert für inhabitantsRegion (" +
                             curCountry.name + ", " + curCity.name +
                             ", " + elementText + ")\n" + e.getMessage());
        }
        return;
      }
      if (rawName.equals("latitude")) {
        try {
          curCity.latitude = (Float.valueOf(elementText)).floatValue();
        } catch (NumberFormatException e) {
          System.err.println("ERR: Falscher Wert für Breite (" +
                             curCountry.name + ", " + curCity.name +
                             ", " + elementText + ")\n" + e.getMessage());
        }
        return;
      }
      if (rawName.equals("longitude")) {
        try {
          curCity.longitude = (Float.valueOf(elementText)).floatValue();
        } catch (NumberFormatException e) {
          System.err.println("ERR: Falscher Wert für Länge (" +
                             curCountry.name + ", " + curCity.name +
                             ", " + elementText + ")\n" + e.getMessage());
        }
        return;
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
   * Wird vom Parser mit Textinhalt des aktuellen Elements aufgerufen.<BR>
   * (call back method)<P>
   * Achtung: Entities (auch Zeichenreferenzen wie &amp;ouml;) stellen eine
   *          Textgrenze dar und werden durch einen erneuten Aufruf dieser
   *          Funktion &uuml;bergeben.
   */
  public void characters(char[] ch, int start, int length) {
    elementTextBuf.append(ch, start, length);
  }
  
  // -------------------------------------------------------------------------
  /** Initialisiert Parser und started Prozeß*/
  public void doit(String dataFilename) {

    // XML Parser
    XMLReader parser = null;
    
    // Parser instanziieren
    try {
      parser = XMLReaderFactory.createXMLReader(parserClass);
    } catch (SAXException e) {
      System.err.println("Fehler beim Initialisieren des Parsers (" +
                         parserClass + ")\n" + e.getMessage());
      System.exit(1);
    }
    
    // Ereignisse sollen von dieser Klasse behandelt werden
    parser.setContentHandler(this);
    
    // Parser starten
    try {
      parser.parse(dataFilename);
    } catch (SAXException e) {
      System.err.println("Parser Exception:\n" + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("Parser IOException:\n" + e.getMessage());
    }
    
  }
  
  // -------------------------------------------------------------------------
  /** Gibt Aufrufsyntax zur&uuml;ck */
  public static void usage() {
    System.out.println("usage: java XMLParserDemo <XML_FILE>");
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





