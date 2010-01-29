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

import DecisionsLayer.Decisions;
import SWTLayer.FirefoxBrowserRunner;
import SWTLayer.SWTBrowserRunner;
import WebCodeLayer.CodeParser;
import WebCodeLayer.HyperLink;


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
public class SomeOtherMain {

	static Logger logs = Logger.getLogger("logs");
	
	Interface theInterface;
	private String url  = "http://englishblazere.free.fr/moncv"; 

	boolean isSWT = true, isSWTTabsOn = true, 
	isUsingMailTo = false, isUsingDownloadLinks = false, isSameWebSite = false;
	int indNumberOfPages = 8, indTimeOfPage = 3;
	
	
	public SomeOtherMain (String [] args) throws SAXException, InterruptedException{
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
			//url = "http://englishblazere.free.fr/moncv"; 
		} catch (Exception e){
			theInterfaceThread.start();
			theInterfaceThread.join();
			url = theInterface.getUrl();
			isSWT = theInterface.isSWT();
			isSWTTabsOn = theInterface.isSWTWithTabs();
			isUsingMailTo = theInterface.isUsingMailto();
			isUsingDownloadLinks = theInterface.isUsingDownloadLinks();
			isSameWebSite = theInterface.isSameWebSite();
			indNumberOfPages = theInterface.getNumberOfPages();
			indTimeOfPage = theInterface.getTimeOfPage();
			System.out.println(" url "+url+" SWT sans onglet "+isSWTTabsOn+" nombre de page "+indNumberOfPages+" temps d'affichage "+indTimeOfPage+" sur le meme site  "+isSameWebSite );
		}//catch
		
	
			if(isSWT) {
				/*
				 System.out.println("url "+url);
					if (url!=null){
						SWTBrowserRunner anSWT = new SWTBrowserRunner(url, isSWTTabsOn, indNumberOfPages);
						boolean firstPage = true; 
					
						while (url != null && indNumberOfPages >0 ){
							System.out.println("Main Navigateur SWT ");
						
						
							if (!firstPage) anSWT.openNextPage(url);
							firstPage = false;
						
							try {
								Thread.sleep(indTimeOfPage*1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							CodeParser aCodeParser = new CodeParser(url);
							aCodeParser.parseLinks();
							
							List<HyperLink> listHREF = aCodeParser.getTheListOfHyperlinks();
							//System.out.println("Main "+listHREF.toString());
							HyperLink theNextHyperLink = Decisions.getAnElement(listHREF, isUsingMailTo,
									isUsingDownloadLinks, isSameWebSite, true, url);
							if( theNextHyperLink == null  ) anSWT.keepsDisplaying();
							boolean isHrefType = theNextHyperLink.isHrefType();
							
							url = theNextHyperLink.getUrl();
							//indNumberOfPages--;
							
							if (isHrefType) indNumberOfPages--;
							//Boucle car on doit considerer l url de base 
							while (!isHrefType && indNumberOfPages > 0){
								anSWT.openNextPage(url);
								//On recupere le document DOM de la page generee avec la commande javascript
								Document theTransformedHTMLCode = anSWT.getDomDocumentAfterJSCommand(url);
								try {
									Thread.sleep(indTimeOfPage*1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								aCodeParser.alterTheHTMLDocument(theTransformedHTMLCode);
								aCodeParser.parseLinks();
								listHREF = aCodeParser.getTheListOfHyperlinks();
								theNextHyperLink = Decisions.getAnElement(listHREF, isUsingMailTo,
										isUsingDownloadLinks, isSameWebSite, true, url);
								//System.out.println("Main boucle"+listHREF.toString());

								if (theNextHyperLink == null) anSWT.keepsDisplaying();
								isHrefType = theNextHyperLink.isHrefType();
								url = theNextHyperLink.getUrl();
								indNumberOfPages--;
								System.out.println("Main pages restantes dans la boucle "+indNumberOfPages);
								
							}
							System.out.println("Main pages restantes "+indNumberOfPages);
							if (indNumberOfPages < 1) anSWT.keepsDisplaying();
						}//while
				//		}
					}//if*/
				
				SWTBrowserRunner anSWT = new SWTBrowserRunner(url, isSWTTabsOn);
				boolean firstPage = true; 
				do {
					if (!firstPage) anSWT.openNextPage(url);
					firstPage = false;
					try {
						Thread.sleep(indTimeOfPage*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					CodeParser aCodeParser = new CodeParser(url);
					aCodeParser.parseLinks();
				    List<HyperLink> listHREF = aCodeParser.getTheListOfHyperlinks();
				    HyperLink theNextHyperLink = Decisions.getAnElement(listHREF, isUsingMailTo,
							isUsingDownloadLinks, isSameWebSite, true, url);
					System.out.println("Main boucle"+listHREF.toString());

				    boolean isHrefType = theNextHyperLink.isHrefType();
				    String next = theNextHyperLink.getUrl();
				    indNumberOfPages--;
				    while(!isHrefType && indNumberOfPages > 0) {
				    	try {
							Thread.sleep(indTimeOfPage*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	//On recupere le document DOM de la page generee avec la commande javascript
						Document theTransformedHTMLCode = anSWT.getDomDocumentAfterJSCommand(next);
						while(!anSWT.isPageFullyLoaded()) ;
							
						
				    	aCodeParser.alterTheHTMLDocument(theTransformedHTMLCode);
				    	aCodeParser.parseLinks();
				    	theNextHyperLink = Decisions.getAnElement(listHREF, isUsingMailTo,
								isUsingDownloadLinks, isSameWebSite, true, url);
				    	next = theNextHyperLink.getUrl();
				    	isHrefType = theNextHyperLink.isHrefType();
				    	indNumberOfPages--;
				    	System.out.println("Main pages restantes dans la boucle "+indNumberOfPages);
				    }
					System.out.println("Main pages restantes "+indNumberOfPages);
					url = next;	
				} while(url != null && indNumberOfPages > 0); 
				if (indNumberOfPages < 1) anSWT.keepsDisplaying();	
				
			}//if isSWT
			else {
				
				while (url != null){
					System.out.println("Navigateur Firefox");
					FirefoxBrowserRunner.loadPage(url);
			
				/**
				 * la boucle est en stand by tant quon ne change pas d'url href
				 */
					CodeParser aCodeParser = new CodeParser(url);
					aCodeParser.parseLinks();
					List<HyperLink> listHREF = aCodeParser.getTheListOfHyperlinks();
					logs.fine(listHREF.toString());
					url = Decisions.getAnElement(listHREF, isUsingMailTo,
							isUsingDownloadLinks, isSameWebSite, true, url).getUrl();
				}//while
			}//else
		}//constructor
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Handler theFileHandler = new FileHandler("logs.html");
		Handler theConsoleHandler = new ConsoleHandler();
		theFileHandler.setLevel(Level.SEVERE);
		theFileHandler.setFormatter(new LogsFormater());
		logs.addHandler(theConsoleHandler);
		logs.addHandler(theFileHandler);
		logs.setLevel(Level.FINEST);
		new SomeOtherMain(args);
		//String path = (new File("logs.html")).getAbsolutePath();
		//new SWTBrowserRunner(path, true);
	}//main

}//Main
