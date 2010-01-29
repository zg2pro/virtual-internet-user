package WebCodeLayer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jswiff.FlashParserTool;
import com.jswiff.xml.Transformer;



/**
 * Classe qui permet d utiliser le package Jswiff afin de recuperer
 * les liens dans les applications Flash
 */
public class FLASHParser {
	
	
	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
  /**   
   * acces au liens produits par l'application Flash
   * @return Liste des liens de type HyperLink
   */
  public static List<HyperLink> getLinks(String k) {
    try {
    	InputStream is = new URL("http://www.imaginaweb.ch/_a/gina/v2/free-flash/game.swf").openStream(); 
		Transformer.parseSWFDocument(is);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		System.err.println("FILEPARSER : FILE NOT FOUND");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.err.println("FILEPARSER : IOEXCEPTION");
	}
    List<String> theURLsList = FlashParserTool.theURLs;
    List<HyperLink> listOfHyperLinks = new ArrayList<HyperLink>();
    for (String s : theURLsList)
    	listOfHyperLinks.add(new HyperLink(s, "", !URLChecker.isJavascriptCommand(s)));
    System.out.println("FLASHParser ------->"+listOfHyperLinks);
    return listOfHyperLinks;
      }
  
}//FlashParser
