import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class OrgImport {

	/**
	 * @param args
	 */
	String file;
	int i=0;
	DB2Query db;
	
	public static void main(String[] args) {
		OrgImport oi = new OrgImport("C:\\Users\\_\\Desktop\\dbpra\\dbprak_data_wfb\\abbreviation.lst");

	}
	
	public OrgImport(String file) {
		this.file = file;
		handleFile();
		db.close();
	}
	
	public void handleFile() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String zeile = null;
			db = new DB2Query();
			while ((zeile = in.readLine()) != null) {
				handleLine(zeile);
			}
			System.out.println("All Imported! "+ i);
		}catch (Exception e) {
			System.err.println("Error: " + e);
		}
	}
	
	public void handleLine(String line) {
		try{
			line=line.replace("\'", "\'\'");
			String shortName = line.split(";")[0];
			String longName = line.split(";")[1];
			System.out.println(shortName+"---"+longName);
			db.insert("insert into Organisation values (\'"+longName+"\',\'"+shortName+"\')");
			i++;
		} catch (Exception e) {
			
		}


	}

}
