package SWTLayer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * Classe d'affichage des URL visitees par des onglets sur Firefox
 * @author gregory
 *
 */
public class FirefoxBrowserRunner {
	
	/**
	 * Chaine de caractere representant le systeme d exploitation
	 */
	private static String operatingSystem;
	
	/**
	 * PID du processus
	 */
	private static Process theProcess;
	
	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
	/**
	 * Methode statique permettant d ouvrir l Url passe en parametre dans un nouvel onglet
	 * @param theURL : une url
	 */
	public static void loadPage (String theURL){
		
		if (theURL == null) {
			JOptionPane.showMessageDialog(null, "Navigation terminee");
			System.exit(0);
		}
		
		operatingSystem = System.getProperty("os.name");
		logs.info("FireFoxBR operatingSystem "+operatingSystem);
		logs.fine(operatingSystem);
		if (operatingSystem.startsWith("Windows"))
			windowsProcedure(theURL); 
		else 
			linuxProcedure(theURL); 
		}//BrowserRunner

	/**
	 * Recuperation du PID courant
	 * @return
	 */
	public Process getProc() {
		return theProcess;
	}//getProc
	
	/**
	 * Methode statique de lancement de page sur linux
	 * @param theURL : une url
	 */
	private static void linuxProcedure(String theURL){
		File f = new File("bin/SWTLayer/firefox.sh");
		f.setExecutable(true, false);
		try{
			theProcess = Runtime.getRuntime().exec("bin/SWTLayer/firefox.sh "+theURL);
			Thread.sleep(2000);
		} catch(IOException e){
			logs.severe("lancement de Firefox impossible (probleme de script) :" + e.getMessage());
		} catch (InterruptedException e) {
			logs.severe("probleme dans le delai entre executions :" + e.getMessage());
		}//catch
	}//linuxProcedure
	
	/**
	 * 
	 * @param theURL
	 */
	private static void windowsProcedure(String theURL) {
		File f = new File("bin/SWTLayer/firefoxBrowser.bat");
		f.setExecutable(true, false);
		try{
			theProcess = Runtime.getRuntime().exec("bin/SWTLayer/firefoxBrowser.bat "+theURL);
			Thread.sleep(2000);
		} catch(IOException e){
			logs.severe("lancement de Firefox impossible (probleme de script) :" + e.getMessage());
		} catch (InterruptedException e) {
			logs.severe("probleme dans le delai entre executions :" + e.getMessage());
		}//catch
	}
}//BrowserRunner
