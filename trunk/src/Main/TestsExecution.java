package Main;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestSuite;
import SWTLayer.FirefoxBrowserRunner;
import SWTLayer.SWTBrowserRunner;

/**
 * Lancement de tous les tests unitaires
 * @author Gregory ANNE
 */
public class TestsExecution {

	static Logger logs = Logger.getLogger("logs");
	/**
	 * Declaration de TestSuite et lancement des tests
	 */
	  public static Test all() {
		  TestSuite all = new TestSuite("Lancement de tous les tests  :  ");
		  all.addTestSuite(DecisionsLayer.DecisionsLayerTester.class);
		  all.addTestSuite(MainTester.class);
		  all.addTestSuite(SWTLayer.SWTLayerTester.class);
		  all.addTestSuite(WebCodeLayer.WebCodeLayerTester.class);
		  return all;
	  }//all

	  public static void main(String args[]) throws Exception {
			Handler theFileHandler = new FileHandler("logs.html");
			Handler theConsoleHandler = new ConsoleHandler();
			theFileHandler.setLevel(Level.FINEST);
			theFileHandler.setFormatter(new LogsFormater());
			logs.addHandler(theConsoleHandler);
			logs.addHandler(theFileHandler);
			logs.setLevel(Level.FINEST);
		  	junit.textui.TestRunner.run(all());
			String path = (new File("logs.html")).getAbsolutePath();
			SWTBrowserRunner  anSWTBrowserRunner = new SWTBrowserRunner(path, true);
			anSWTBrowserRunner.keepsDisplaying();
	  }//main

}//class