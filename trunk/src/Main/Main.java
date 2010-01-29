package Main;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import sun.nio.cs.ext.ISCII91;

import DecisionsLayer.Decisions;
import SWTLayer.FirefoxBrowserRunner;
import SWTLayer.SWTBrowserRunner;
import WebCodeLayer.CodeParser;
import WebCodeLayer.HyperLink;
import WebCodeLayer.URLChecker;


/**
 * Standard prefixes normalization for all classes :
 * 
 * Imported Components :
 * JPanel : p
 * JButton : b
 * JRadioButton : rb
 * JTextArea : ta
 * JTextField : tf
 * JEditorPane : ep
 * JScrollPane : sp
 * JLabel : l
 * JSpinner : s
 * JCheckBox : cb
 * JList : lst
 * ListModel, DefaultListModel : lst and ends with "Contents" suffix
 * JTabbedPane :: ends with "Tabs" suffix
 * JPasswordField : pf
 * Container : named c or container
 * URL : url
 * Exception : named e
 * Event : named evt
 * Connection : named conn
 * Statement : named st
 * Resultset : named rs
 * 
 * 
 * Inner classes :
 * a or an followed by the name of the class 
 * 		when the given instance is part of a group
 * the when the instance is given as a parameter, or else is unique
 * selected when the instance is coming from a swing component
 * 
 * Primitive types
 * int : named i, j, k inside loops, 
 *   but else index, or prefixed ind
 * string : no prefix
 * char : c
 * boolean : is
 * 
 * db stands for database
 * a List (arrayLists) can be named just l
 *  if it's used as a inner variable of a method
 *  and as well its iterator can be called just it or it1, it2 if needed
 * If the List is an attribute for an object it has to get a
 * full name preffixed by listOf
 */

/**
 * Classe centrale d'execution du projet
 */
public class Main {

	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
	/**
	 * Instance de l interface
	 */
	private Interface theInterface;
	
	/**
	 * Url de la premiere page a visiter
	 */
	private String url; 

	/**
	 * Si oui ou non le navigateur est SWT
	 */
	private boolean isSWT = true;
	
	/**
	 * Si oui ou non le navigateur SWT utlise des onglets
	 */
	private boolean isSWTTabsOn = true;
	
	/**
	 * Si oui ou non les mailto sont utlises
	 */
	private boolean isUsingMailTo = false; 
	
	/**
	 * Si oui ou non les liens de telechargement sont utilises
	 */
	private boolean isUsingDownloadLinks = false; 
	
	/**
	 * Si oui ou non les liens Javascript (javascript:Fonction()) sont utlises
	 */
	private boolean isUsingJavaScriptLinks = false ;
	
	/**
	 * Si oui ou non les liens choisis sont exclusivement du meme site que l url de depart
	 */
	private boolean isSameWebSite = false;
	
	/**
	 * Nombre de pages a visiter durant l'execution du programme
	 */
	private int indNumberOfPages = 8;
	
	
	/**
	 * Temps de pause minimal entre chaque affichage de page
	 */
	private int indTimeOfPage = 3;
	
	/**
	 * Constructeur lancant l application.
	 * @param args : une url 
	 * @throws SAXException
	 * @throws InterruptedException
	 */
	public Main (String [] args) throws SAXException, InterruptedException{
		/**
		 * logs affichera toutes infos plus graves que le niveau indique
		 * log.severe("severe");
		 * log.warning("warning");
		 * log.info("info");
		 * log.fine("fine");
		 * log.finer("finer");
		 * log.finest("finest");
		 */
		logs.setLevel(Level.SEVERE);		
		
		 Thread theInterfaceThread = new Thread (new Runnable(){
			public void run() {
				// TODO Auto-generated method stub
				theInterface = new Interface();
				while (!theInterface.isDisposed())
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//catch
			}//run
		 });
		 
		 
		try {
			url = args[0];
		} catch (Exception e){
			theInterfaceThread.start();
			theInterfaceThread.join();
			url = theInterface.getUrl();
			isSWT = theInterface.isSWT();
			isSWTTabsOn = theInterface.isSWTWithTabs();
			isUsingMailTo = theInterface.isUsingMailto();
			isUsingDownloadLinks = theInterface.isUsingDownloadLinks();
			isUsingJavaScriptLinks = theInterface.isUsingJavaScriptLinks();
			isSameWebSite = theInterface.isSameWebSite();
			indNumberOfPages = theInterface.getNumberOfPages();
			indTimeOfPage = theInterface.getTimeOfPage();
			System.out.println(" url "+url+" SWT sans onglet "+isSWTTabsOn+" nombre de page "+indNumberOfPages+" temps d'affichage "+indTimeOfPage+" sur le meme site  "+isSameWebSite );
		}//catch
		
		if (!url.startsWith("http")) url = "http://"+url;
	
			if(isSWT) {
				 System.out.println("url "+url);
					if (url!=null){
						SWTBrowserRunner anSWT = new SWTBrowserRunner(url, isSWTTabsOn);
						//boolean firstPage = true; 
						//boolean isHrefType = false;
						CodeParser aCodeParser = null;
						while (url != null && indNumberOfPages >0 ){
							System.out.println("Main Navigateur SWT ");
						
						/**
						 * la boucle est en stand by tant quon ne change pas d'url href
						 */
							Thread.sleep(indTimeOfPage*1000);
							
							if(URLChecker.isJavascriptCommand(url)) {
								Document theTransformedHTMLCode = anSWT.getDomDocumentAfterJSCommand(url);
								aCodeParser.alterTheHTMLDocument(theTransformedHTMLCode);
							}
							else {
							aCodeParser = new CodeParser(url);
							}
							aCodeParser.parseLinks();
							
							List<HyperLink> listHREF = aCodeParser.getTheListOfHyperlinks();
							System.out.println("Main "+listHREF.toString());
							HyperLink theNextHyperLink = Decisions.getAnElement(listHREF, isUsingMailTo,
									isUsingDownloadLinks, isSameWebSite, isUsingJavaScriptLinks, url);
							
							if( theNextHyperLink == null  ) anSWT.keepsDisplaying();
							
							url = theNextHyperLink.getUrl();
							indNumberOfPages--;
							
							
							logs.info("Main pages restantes "+indNumberOfPages);
							if (indNumberOfPages == 0) anSWT.keepsDisplaying();
							/*
							int timeCounter = 0;
							while (!anSWT.isChangePagePressed() && timeCounter < 20){
								Thread.sleep(1000);
								timeCounter+=1;
							}
							if (timeCounter == 30) anSWT.getBChangePage().setEnabled(true);
							while (!anSWT.isChangePagePressed()){
								Thread.sleep(200);
							}
							*/
							if (url != null && indNumberOfPages >0) anSWT.openNextPage(url);

						}//while
				//		}
					}//if
			}//if isSWT
			else {
				
				while (url != null && indNumberOfPages >0 ){
					System.out.println("Navigateur Firefox");
					FirefoxBrowserRunner.loadPage(url);
					/**
					 * la boucle est en stand by tant quon ne change pas d'url href
					 */
						try {
							Thread.sleep(indTimeOfPage*1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					CodeParser aCodeParser = new CodeParser(url);
					aCodeParser.parseLinks();
					List<HyperLink> listHREF = aCodeParser.getTheListOfHyperlinks();
					System.out.println("LISTE DES LIENS :"+listHREF);
					logs.fine(listHREF.toString());
					if (listHREF.isEmpty()) break;
					url = Decisions.getAnElement(listHREF, isUsingMailTo,
							isUsingDownloadLinks, isSameWebSite, isUsingJavaScriptLinks, url).getUrl();
					indNumberOfPages--;
				}//while
			}//else
		}//constructor
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Handler theFileHandler = new FileHandler("logs.html");
		Handler theConsoleHandler = new ConsoleHandler();
		theFileHandler.setLevel(Level.FINEST);
		theConsoleHandler.setLevel(Level.FINEST);
		theFileHandler.setFormatter(new LogsFormater());
		logs.addHandler(theConsoleHandler);
		logs.addHandler(theFileHandler);
		logs.setLevel(Level.FINEST);
		new Main(args);
		//String path = (new File("logs.html")).getAbsolutePath();
		//new SWTBrowserRunner(path, true);
	}//main

}//Main
