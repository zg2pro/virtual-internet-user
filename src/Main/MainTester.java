package Main;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;
import DecisionsLayer.DecisionsLayerTester;
import SWTLayer.FirefoxBrowserRunner;
import SWTLayer.SWTBrowserRunner;

/**
 * 
 * @author 
 *
 */
public class MainTester extends TestCase {

	/**
	 * variable de gestion des evenements
	 */
	static Logger logs = Logger.getLogger("logs");
	
	/**
	 * test nominal : l'interface est lancee
	 */
	public void testNominal (){
		try {
		Interface theInterface = new Interface();
		theInterface.dispose();
		assertTrue(true);
		logs.finest("Main -> Test Nominal OK");
		} catch (Exception e) {
			logs.severe("Main -> l'interface de bienvenue ne fonctionne pas\n" +
					e.getMessage());
		}//catch
	}//testNominal
	
	/**
	 * procedure d'execution du test du package Main uniquement
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
		MainTester theMainTester = new MainTester();
		theMainTester.testNominal();
		String path = (new File("logs.html")).getAbsolutePath();
		SWTBrowserRunner  anSWTBrowserRunner = new SWTBrowserRunner(path, true);
		anSWTBrowserRunner.keepsDisplaying();
	}//main
	
}//TestCase
