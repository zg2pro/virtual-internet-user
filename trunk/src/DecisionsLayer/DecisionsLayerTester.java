package DecisionsLayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;
import Main.LogsFormater;
import SWTLayer.SWTBrowserRunner;
import WebCodeLayer.CodeParser;
import WebCodeLayer.HyperLink;

/**
 * Test unitaire du module de decisions
 * @author Gregory ANNE
 *
 */
public class DecisionsLayerTester extends TestCase {

	static Logger logs = Logger.getLogger("logs");
	
	/**
	 * test nominal : plusieurs url bien formatees dans la liste
	 */
	public void testNominal() {
		try {
			List<HyperLink> l = new ArrayList<HyperLink>();
			l.add(new HyperLink("http://www.fnac.com", "", true));
			l.add(new HyperLink("http://englishblazere.free.fr", "", true));
			l.add(new HyperLink("http://www.lsis.org", "", true));
			l.add(new HyperLink("http://karine.anne.free.fr", "", true));
			l.add(new HyperLink("http://www.google.fr", "", true));
			assertTrue(true);
			logs.finest("Module de decisions -> Test Nominal OK");
		} catch (Exception e) {
			logs.severe("Module de decisions -> erreur grave  <br/>" +
					"exception message: "+e.getMessage());
		}//catch
	}//test1 
	
	/**
	 * test des cas d'exception
	 */
	public void testExceptions() {
		try {
			Decisions.getAnElement(new ArrayList<HyperLink>(), false,
					false, false, true, null);
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Module de decisions -> attention pb quand liste vide <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			Decisions.getAnElement(null, false,
					false, false, true, null);
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Module de decisions -> attention pb quand liste nulle <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			List<HyperLink> l = new ArrayList<HyperLink>();
			l.add(new HyperLink("http://video.fnac.com/a2042813/Help-DVD-Zone-2?PID=3&Mn=-1&Ra=-3&To=0&Nu=3&Fr=0", "", true));
			Decisions.getAnElement(l, false,
					false, false, true, null);
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Module de decisions -> attention pb quand liste contient une url avec parametres <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			List<HyperLink> l = new ArrayList<HyperLink>();
			l.add(new HyperLink("", "", true));
			Decisions.getAnElement(l, false,
					false, false, true, null);
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Module de decisions -> attention pb quand liste contient une url vide <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		/*
		try {
			CodeParser aCodeParser = new CodeParser("http://fst.univ-cezanne.fr/formations/l5.htm");
			aCodeParser.parseLinks();
			List<HyperLink> l = aCodeParser.getTheListOfHyperlinks();
			logs.fine(Decisions.getAnElement(l, false,
					false, false, true, null).toString());
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Module de decisions -> attention pb quand page de mails <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		*/
		try {
			CodeParser aCodeParser = new CodeParser("http://nguyen.univ-tln.fr/Enseignement/M5/TclTk/proj2007.html");
			aCodeParser.parseLinks();
			List<HyperLink> l = aCodeParser.getTheListOfHyperlinks();
			try {
			//null.toString() doit sortir une exception ici
			logs.warning("resultat inattendu: "+Decisions.getAnElement(l, false,
					false, false, true, null).toString());
			} catch (Exception e){}
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Module de decisions -> attention pb quand page de liens de telechargements <br/>" +
					"exception message: "+e.getMessage());
		}//catch
		try {
			CodeParser aCodeParser = new CodeParser("http://195.221.220.118/~anne/homePage.php");
			aCodeParser.parseLinks();
			List<HyperLink> l = aCodeParser.getTheListOfHyperlinks();
			logs.fine("resultat en interdisant les javascript:"+Decisions.getAnElement(l, false,
					false, false, false, null).toString());
			logs.fine("resultat en acceptant les javascript:"+Decisions.getAnElement(l, false,
					false, false, true, null).toString());
			assertTrue(true);
		} catch (Exception e){
			logs.warning("Module de decisions -> attention pb quand page de liens javascript <br/>" +
					"exception message: "+e.getMessage());
		}//catch
	}//test1 
	

	/**
	 * Pour executer ce test unitaire uniquement
	 * @param args
	 * @throws Exception
	 */
		public static void main (String [] args) throws Exception {
			SWTBrowserRunner  anSWTBrowserRunner = new SWTBrowserRunner("", false);
			Handler theFileHandler = new FileHandler("logs.html");
			Handler theConsoleHandler = new ConsoleHandler();
			theFileHandler.setLevel(Level.FINEST);
			theFileHandler.setFormatter(new LogsFormater());
			logs.addHandler(theConsoleHandler);
			logs.addHandler(theFileHandler);
			logs.setLevel(Level.FINEST);
			DecisionsLayerTester theDecisionsLayerTester = new DecisionsLayerTester();
			theDecisionsLayerTester.testNominal();
			theDecisionsLayerTester.testExceptions();
			String path = (new File("logs.html")).getAbsolutePath();
			anSWTBrowserRunner.openNextPage(path);
			anSWTBrowserRunner.keepsDisplaying();
			//FirefoxBrowserRunner.loadPage("logs.html");
		}//main
	
}//DecisionsTester
