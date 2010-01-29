package SWTLayer;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;
import Main.LogsFormater;

/**
 * Browsers package TestCase
 * @author gregory
 *
 */
public class SWTLayerTester extends TestCase {

	/**
	 * variable d'acces au gestionnaire d'evenements
	 */
	static Logger logs = Logger.getLogger("logs");

	/**
	 * le navigateur SWT ne peut etre utilise de façon statique,
	 *  il necessite une instance de l'objet 
	 */
	static SWTBrowserRunner swtBrowser;
	
	/**
	 * test nominal : affichage deux deux pages web sans elements extra-ordinaires,
	 * cette procedure doit necessairement etre appelée avant les tests d'exception
	 */
	public void testNominalSWT() {
		try {
			swtBrowser = new SWTBrowserRunner("http://englishblazere.free.fr/moncv.html", true);
			swtBrowser.openNextPage("http://karine.anne.free.fr");
			logs.finest("test nominal SWT OK");
		} catch (Exception e){
			logs.severe("SWT -> probleme d'affichage de page <br/>" +
					"exception message: "+e.getMessage());
		}
	}//testNominal
	
	/**
	 * test nominal : affichage deux deux pages web sans elements extra-ordinaires
	 */
	public void testNominalFirefox() {
		try {
			FirefoxBrowserRunner.loadPage("http://www.google.fr/ig");
			FirefoxBrowserRunner.loadPage("http://karine.anne.free.fr");
			assertTrue(true);
			logs.finest("test nominal Firefox OK");
		} catch (Exception e){
			logs.severe("Navigator -> l'execution avec Firefox ne marche pas <br/>" +
					"exception message: "+e.getMessage());
		}//catch
	}//testNominal

	/**
	 * tests d'exceptions des outils SWT
	 */
	public void testExceptionsSWT()  {
		try {
			swtBrowser.openNextPage("http://karine.anne.free.fr/ http://www.lsis.org/~jean_caussanel.html");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("SWT -> pb lorsque l'on envoie deux url en parametre <br/>" +
					"exception message: "+e.getMessage());
		} try {
			swtBrowser.openNextPage("http://karine.anne.free.fr/index.php?title=Robot_navigateur#Piste_3_:_Mozilla_Firefox_Web_Browser");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("SWT -> pb lorsque l'on envoie des parametres dans l'url <br/>" +
					"exception message: "+e.getMessage());
		} try {
			swtBrowser.openNextPage("http://karine.anne.free.fr/index.php?title=robot_navigateur#piste_3_:_Mozilla_Firefox_Web_Browser");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("SWT -> pb lorsqu'il y a une erreur de majuscule dans l'url <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		 try {
			 swtBrowser.openNextPage("http://www.imaginaweb.ch/_a/gina/v2/free-flash/game.html");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("SWT -> pb pour afficher une page contenant du flash <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		 try {
			 swtBrowser.openNextPage("http://karine.anne.free.fr/workspace/IA/doc/");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("SWT -> pb pour afficher une page contenant des frames HTML transitionnel <br/>" +
					"exception message: "+e.getMessage());
		}//catch
	}//testBrowserTester


	/**
	 *  tests d'exceptions d'ouvertures de pages web par script de shell
	 */
	public void testExceptionsFirefox()  {
		try {
			FirefoxBrowserRunner.loadPage("http://karine.anne.free.fr/ http://www.lsis.org/~jean_caussanel.html");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Navigator -> pb lorsque l'on envoie deux url en parametre <br/>" +
					"exception message: "+e.getMessage());
		} try {
			FirefoxBrowserRunner.loadPage("http://karine.anne.free.fr/index.php?title=Robot_navigateur#Piste_3_:_Mozilla_Firefox_Web_Browser");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Navigator -> pb lorsque l'on envoie des parametres dans l'url <br/>" +
					"exception message: "+e.getMessage());
		} try {
			FirefoxBrowserRunner.loadPage("http://karine.anne.free.fr/index.php?title=robot_navigateur#piste_3_:_Mozilla_Firefox_Web_Browser");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Navigator -> pb lorsqu'il y a une erreur de majuscule dans l'url <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		 try {
			FirefoxBrowserRunner.loadPage(null);
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Navigator -> pb lorsque parametre null <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		 try {
			 FirefoxBrowserRunner.loadPage("http://www.imaginaweb.ch/_a/gina/v2/free-flash/game.html");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("SWT -> pb pour afficher une page contenant du flash <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		 try {
			 FirefoxBrowserRunner.loadPage("http://karine.anne.free.fr/workspace/IA/doc/");
			assertTrue(true);
		} catch (Exception e){
			logs.warning("SWT -> pb pour afficher une page contenant des frames HTML transitionnel <br/>" +
					"exception message: "+e.getMessage());
		}//catch
	}//testBrowserTester


	/**
	 * Execution de la classe de test
	 * @param args
	 * @throws Exception
	 */
	public static void main (String [] args) throws Exception {

		Handler theFileHandler = new FileHandler("logs.html");
		Handler theConsoleHandler = new ConsoleHandler();
		theFileHandler.setLevel(Level.FINEST);
		theFileHandler.setFormatter(new LogsFormater());
		logs.addHandler(theConsoleHandler);
		logs.addHandler(theFileHandler);
		logs.setLevel(Level.FINEST);
		SWTLayerTester theBrowserRunnerTester = new SWTLayerTester();
		theBrowserRunnerTester.testNominalSWT();
		theBrowserRunnerTester.testExceptionsSWT();
		if (System.getProperty("os.name").equals("Linux")) {
			theBrowserRunnerTester.testNominalFirefox();
			theBrowserRunnerTester.testExceptionsFirefox();
		}//if
		String path = (new File("logs.html")).getAbsolutePath();
		swtBrowser.openNextPage(path);
		swtBrowser.keepsDisplaying();
		
	}//main
	
}//class
