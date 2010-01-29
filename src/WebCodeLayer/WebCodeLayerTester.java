package WebCodeLayer;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;
import Main.LogsFormater;
import SWTLayer.SWTBrowserRunner;

/**
 * TODO
 * @author gregory
 *
 */
public class WebCodeLayerTester extends TestCase {


	static Logger logs = Logger.getLogger("logs");

	/**
	 * 
	 * TODO
	 */
	public void testNominal() {
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://englishblazere.free.fr/moncv.html");
			DOMDump.printDOMTree(aWebCodeLayer.getTheHTMLCode().getDocumentElement(), System.out);
			aWebCodeLayer.parseLinks();
			logs.fine(aWebCodeLayer.getTheListOfHyperlinks().toString());
			assertTrue(true);
		} catch (Exception e){
			logs.severe("WebCodeLayer -> grave pb <br/>" +
					"exception message: "+e.getMessage());
		}
	}//test1


	/**
	 * 
	 * TODO
	 */
	public void testExceptions() {

		try {
			CodeParser aWebCodeLayer = new CodeParser("http://englishblazere.free.fr/moncv");
			logs.fine("TEST D'AFFICHAGE XML : <br/>" +
					DOMDump.xmlToString(aWebCodeLayer.getTheHTMLCode().getDocumentElement()));
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec l'outil de controle XML <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			logs.fine("TEST FLASHPARSER : <br/>" +
					FLASHParser.getLinks("http://www.imaginaweb.ch/_a/gina/v2/free-flash/game.html"));
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec l'outil d'analyse Flash <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			JavascriptExecuter jsExec = new JavascriptExecuter("http://www.archisoft.ch/realisation/technologie/Flash/Demo.html");
			logs.fine("TEST JAVASCRIPTEXECUTER -url- : <br/>" +
					DOMDump.xmlToString(jsExec.getTheDomDocument()));
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec l'interpretation js -par url- <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			JavascriptExecuter jsExec = new JavascriptExecuter("http://www.archisoft.ch/realisation/technologie/Flash/Demo.html");
			jsExec.ExecuteJavascript("<html><title>Snippet</title><body><p id='myid'>Best Friends</p><p id='myid2'>Cat and Dog</p></body></html>");
			logs.fine("TEST JAVASCRIPTEXECUTER -code- : <br/>" +
					DOMDump.xmlToString(jsExec.getTheDomDocument()));
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec l'interpretation js -par code- <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			assertFalse((URLChecker.isOnSameWebServer("http://englishblazere.free.fr","http://english.free.fr")));
			assertTrue(URLChecker.isOnSameWebServer("http://englishblazere.free.fr","http://englishblazere.free.fr/toto.html?e=1"));
			assertTrue(URLChecker.isOnSameWebServer("http://englishblazere.free.fr","http://englishblazere.free.fr/otot.html#totot"));
			assertTrue(URLChecker.isOnSameWebServer("http://englishblazere.free.fr","http://englishblazere.free.fr/moncv.html"));	
			assertTrue(URLChecker.isOnSameWebServer("http://englishblazere.free.fr","http://englishblazere.free.fr/"));
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec isOntheSameWebServer <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			assertTrue(new CodeParser("http://englishblazere.free.fr/moncv").URLFileResolver());
			assertTrue(new CodeParser("http://englishblazere.free.fr/moncv.html").URLFileResolver());
			assertFalse(new CodeParser("http://karine.anne.free.fr/workspace/IA/doc/").URLFileResolver());
			assertFalse(new CodeParser("http://karine.anne.free.fr/workspace/IA/doc").URLFileResolver());
			assertTrue(new CodeParser("http://195.221.220.118/~anne/homePage.php").URLFileResolver());
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec l'analyse d'url (isTheLastFile) <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			assertTrue(URLChecker.isJavascriptCommand("JavaScript:uneFonction()"));
			assertTrue(URLChecker.isJavascriptCommand(" javascript:fonction()"));
			assertFalse(URLChecker.isJavascriptCommand("http://dtc"));
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec isJavaScriptCommand() <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			URLChecker uc = new URLChecker("http://englishblazere.free.fr/moncv", "#com", true);
			assertEquals("http://englishblazere.free.fr/moncv#com", uc.getAbsoluteURL());
			uc = new URLChecker("http://englishblazere.free.fr/moncv.html", "moncvGB.html", true);
			assertEquals("http://englishblazere.free.fr/moncvGB.html", uc.getAbsoluteURL());
			uc = new URLChecker("http://karine.anne.free.fr/workspace/IA/doc/", "Transition.html", false);
			assertEquals("http://karine.anne.free.fr/workspace/IA/doc/Transition.html", uc.getAbsoluteURL());
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec URLChecker -> URL relatives->absolues <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			String expected = "<html><tag yo=\"Hello\" rt='GGTTuuVV' /></html>";
			String actual = "<HTML><tag Yo=\"Hello\" RT='GGTTuuVV' /></HTML>";
			actual = URLResolver.checkXMLTagsLowerCase(actual);
			assertEquals(expected, actual);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec URLResolver : checkXMLTagsLowerCase <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			logs.fine("Code HTML recup a http://englishblazere.free.fr/moncv"+
					URLResolver.getDomDocumentHTMLCodeFromURL("http://englishblazere.free.fr/moncv"));
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb avec getHTMLCodeWithURL <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://englisHBLazere.free.fr/moncv.html");
			aWebCodeLayer.parseLinks();
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb quand mauvais caracteres maj/minuscules dans l'URL <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://karine.anne.free.fr/index.php?title=Robot_navigateur#Piste_3_:_Mozilla_Firefox_Web_Browser");
			aWebCodeLayer.parseLinks();
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb quand parametres dans l'URL <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://karine.anne.free.fr/workspace/web2/homePage.html");
			aWebCodeLayer.parseLinks();
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb quand la page contient du javascript <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://tecfa.unige.ch/guides/htmlman/imagemap-ex.html");
			aWebCodeLayer.parseLinks();
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb lorsque l'on navigue sur des images mappees <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://karine.anne.free.fr/workspace/IA/doc/");
			aWebCodeLayer.parseLinks();
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb lorsque l'on navigue sur des pages d'inclusions de frames <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://www.imaginaweb.ch/_a/gina/v2/free-flash/game.html");
			aWebCodeLayer.parseLinks();
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb lors de la navigation sur une page contenant du Flash <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			CodeParser aWebCodeLayer = new CodeParser("http://195.221.220.118/~anne/homePage.php");
			aWebCodeLayer.parseLinks();
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb lorsque l'on navigue sur des pages contenant des Submits Javascript <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			CodeParser aWebCodeLayer = new CodeParser(null);
			aWebCodeLayer.parseLinks();
			logs.finer("le test avec parametre null ne pose pas de probleme");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb lorsque parametre null <br/>" +
					"exception message: "+e.getMessage());
		}
		try {
			CodeParser aWebCodeLayer = new CodeParser("dtc");
			aWebCodeLayer.parseLinks();
			logs.finer("le test avec parametre d'url mal formattee ne pose pas de probleme");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("WebCodeLayer - > pb lorsque parametre n'est pas une url absolue <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		
	}//test1

	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main (String [] args) throws Exception {
		SWTBrowserRunner  anSWTBrowserRunner = new SWTBrowserRunner("", false);
		Handler fh = new FileHandler("logs.html");
		Handler ch = new ConsoleHandler();
		fh.setLevel(Level.FINEST);
		fh.setFormatter(new LogsFormater());
		logs.addHandler(ch);
		logs.addHandler(fh);
		logs.setLevel(Level.FINEST);
		WebCodeLayerTester t = new WebCodeLayerTester();
		t.testNominal();
		t.testExceptions();
		String path = (new File("logs.html")).getAbsolutePath();
		anSWTBrowserRunner.openNextPage(path);
		anSWTBrowserRunner.keepsDisplaying();
	}//main
}//class
