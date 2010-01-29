package WebCodeLayer;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import SWTLayer.SWTBrowserRunner;

/**
 * Classe permettant le parsing du code de la page web
 * @author gregory
 *
 */
public class CodeParser  {
	

	
	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
	/**
	 * Url de la page en cours de traitement
	 */
	private String url;

	/**
	 * Si oui non la page est completement charge ( URLFileResolver() )
	 */
	private boolean isUrlReady = false;
	
	/**
	 * Si oui ou non le dernier element arborescant affiche d une url est un fichier
	 */
	private boolean isTheLastFile;
	
	

	/**
	 * Document DOM de la page en cours de traitement
	 */
	private Document theHTMLDocument;
	
	
	private Document theHtmlDocumentAfterJsExecution = URLResolver.getDomDocumentFromStringHTMLCode("<html><head></head><body></body></html>");

	/**
	 * Liste d'hyperliens pris dans la page en cours de visite
	 */
	private List<HyperLink> listOfHyperlinks;


	/**
	 * Instance de la classe qui execute le Javascript
	 */
	private JavascriptExecuter theJavascriptExecuter;
	
	/**
	 * Getter pour obtenir la variable isTheLastFile
	 * @return
	 */
	public boolean isTheLastFile() {
		return isTheLastFile;
	}
	
	/**
	 * Getter pour obtenir la liste d'hyperliens pris dans la page en cours de visite
	 * @return listOfHyperlinks: la liste d'hyperliens
	 */
	public List<HyperLink> getTheListOfHyperlinks() {
		return listOfHyperlinks;
	}

	/**
	 * Getter pour obtenir le document DOM de la page en cours de traitement
	 * @return
	 */
	public Document getTheHTMLCode() {
		return theHTMLDocument;
	}

	/**
	 * Methode permettant de remplacer le document DOM en cours d utilisation
	 * par celui passe en parametre (utilise dans la classe Main lors de l utilisation
	 * de commande Javascript)
	 * @param theHTMLDocument
	 */
	public void alterTheHTMLDocument(Document theHTMLDocument) {
		this.theHTMLDocument = theHTMLDocument;
	}	
	
	/**
	 * Constructeur permettant de recuperer (dans la variable theHTMLDocument)
	 * le document DOM de la page web dont l url est passe en parapmetre.
	 * @param theURL : une url
	 */
	public CodeParser (String theURL) {
			this.url = theURL;
			theHTMLDocument = URLResolver.getDomDocumentHTMLCodeFromURL(url);
			if (!System.getProperty("os.name").equals("Linux")) {
				theJavascriptExecuter = new JavascriptExecuter(url);
				theHtmlDocumentAfterJsExecution = theJavascriptExecuter.getTheDomDocument();
				
			}
			isTheLastFile = URLFileResolver();
	}//constructor

	/**
	 * Methode permettant de recuperer le code contenu dans les pages
	 * pointer par les frames et les iframes.
	 * Si du javascript est present dans ces frames, il doit
	 * etre execute.
	 * Met a jour la variable theHTMLDocument.
	 */
	private void finalCodeReconstitution(){
		boolean isJavascriptToExecuteAgain = false;
		//Pour resoudre le probleme des parametre d une url (pour la premiere page lance ex : http://www.lsis.org/ 
		// et http://www.lsis.org/index.php?processus=ok )
		//On test qui a le plus d elements entre le document avec js execute et sans le js
		// et on recupere  celui qui a le plus d elements
		Element theRootElement = theHTMLDocument.getDocumentElement();
		Element theJSRootElement = theHtmlDocumentAfterJsExecution.getDocumentElement();
		
		logs.finer("AVANT LE TRAITEMENT DE FRAME");
		
		NodeList theFramesNodeList = null; 
		NodeList theJSFramesNodeList = null; 
		
		/*
		 * D'abord on s'occupe des balises frame
		 */
		try {
			theFramesNodeList = theRootElement.getElementsByTagName("frame");
			theJSFramesNodeList = theJSRootElement.getElementsByTagName("frame");
			if (theJSFramesNodeList.getLength() > theFramesNodeList.getLength()) 
				theFramesNodeList = theJSFramesNodeList;
		do {
		//la boucle do-while sert a decompiler les frames dans les frames
			System.out.println("nombre de frames trouvees: "+theFramesNodeList.getLength());
		
		if (theFramesNodeList.getLength() > 0){
			for  (int i=0; i<theFramesNodeList.getLength() ; i++){
				String path = theFramesNodeList.item(i).getAttributes().getNamedItem("src").getNodeValue();
				logs.severe("code parser theFramesNodeList.item(i).getParentNode()"+theFramesNodeList.item(i).getParentNode());
				Document theRetrievedDocument = URLResolver.getDomDocumentHTMLCodeFromURL(URLResolver.relativeToAbsolute(url, path, isTheLastFile));
				System.out.println("Code parser DOM RECUPERE");			
				theFramesNodeList.item(i).getParentNode().appendChild(theRetrievedDocument.getDocumentElement());
				logs.severe("code parser theFramesNodeList.item(i).getParentNode() apres "+theFramesNodeList.item(i).getParentNode());
				logs.finer("Code parser APRES TRAITEMENT D'UNE FRAME");
				
			}//for
			for  (int i=0; i<theFramesNodeList.getLength() ; i++)
				theFramesNodeList.item(0).getParentNode().removeChild(theFramesNodeList.item(0));
			//parseLinks();
			//return;
			isJavascriptToExecuteAgain = true;
		}//if

		theFramesNodeList = theRootElement.getElementsByTagName("frame");
		} while (theFramesNodeList.getLength() > 0);
		
		} catch (Exception e){
			logs.warning("pb avec les frames");
		}
		
		/*
		 * Ensuite on s'occupe des balises iframe
		 */
		try {
			theFramesNodeList = theRootElement.getElementsByTagName("iframe");
			theJSFramesNodeList = theJSRootElement.getElementsByTagName("iframe");
			if (theJSFramesNodeList.getLength() > theFramesNodeList.getLength()) 
				theFramesNodeList = theJSFramesNodeList;
		do {
		
		logs.info("nombre de iframes trouvees: "+theFramesNodeList.getLength());
		
		if (theFramesNodeList.getLength() > 0){
			for  (int i=0; i<theFramesNodeList.getLength() ; i++){
				String path = theFramesNodeList.item(i).getAttributes().getNamedItem("src").getNodeValue();
				Document theRetrievedDocument = URLResolver.getDomDocumentHTMLCodeFromURL(URLResolver.relativeToAbsolute(url, path, isTheLastFile));
				logs.finest("Code Parser DOM RECUPERE");			
				theFramesNodeList.item(i).getParentNode().appendChild(theRetrievedDocument.getDocumentElement());
				logs.finer("Code Parser APRES TRAITEMENT D'UNE FRAME");
			}//for
			for  (int i=0; i<theFramesNodeList.getLength() ; i++)
				theFramesNodeList.item(0).getParentNode().removeChild(theFramesNodeList.item(0));
			//return;
			isJavascriptToExecuteAgain = true;
		}//if
		
		theFramesNodeList = theRootElement.getElementsByTagName("iframe");
		} while (theFramesNodeList.getLength() > 0);
		
		} catch (Exception e){
			logs.warning("pb avec les iframes");
		}
		
		logs.fine("TOUTES LES FRAMES ONT ETE TRAITEES");

		/**
		 * nouvelle execution de javascript
		 */
		try {
		if (isJavascriptToExecuteAgain)
			if (!System.getProperty("os.name").equals("Linux")){
				//theJavascriptExecuter.ExecuteJavascript(DOMDump.xmlToString(theHTMLDocument.getDocumentElement()));
				//theHTMLDocument = theJavascriptExecuter.getTheDomDocument();
			}
		} catch(Exception e) {
			logs.warning("Le javascript contenu dans la frame n est pas execute");
		}
	}//finalCodeReconstitution
	
	/**
	 * Methode faisant le parsing du code html et recuperant les liens
	 * dans la variable listOfHyperlinks
	 */
	public void parseLinks (){
		
		try {
			finalCodeReconstitution();
		} catch (Exception e){
			logs.severe("la reconstitution n'a pas fonctionne : " +
					"pb Javascript executer ou frames");
		}

		Element theRootElement = theHTMLDocument.getDocumentElement();
		Element theJSRootElement = theHtmlDocumentAfterJsExecution.getDocumentElement();
		
		//On vide la liste avant de proceder a un nouveau parcours
		//une fois que le document est transforme, on y cherche les liens
		listOfHyperlinks = new ArrayList<HyperLink>();
		NodeList theCurrentNodeList = null;
		NodeList theJSCurrentNodeList = null;
		
		/*
		 * Les objets Flash
		 */
		
		theCurrentNodeList = theRootElement.getElementsByTagName("embed");
		theJSCurrentNodeList = theJSRootElement.getElementsByTagName("embed");
		if (theJSCurrentNodeList.getLength() > theCurrentNodeList.getLength())
			theCurrentNodeList = theJSCurrentNodeList;
		for (int i=0; i<theCurrentNodeList.getLength(); i++){
			try {
				//src = "uneApplication.swf"
				String src = theCurrentNodeList.item(i).getAttributes().getNamedItem("src").getNodeValue();
				listOfHyperlinks.addAll(FLASHParser.getLinks(URLResolver.relativeToAbsolute(url, src, isTheLastFile)));
			} catch (Exception e){
				logs.finer("pb parcours Flash");
			}//catch
		}//for	
	

		/*
		 * Gestion des balises a
		 */
		theCurrentNodeList = theRootElement.getElementsByTagName("a");
		theJSCurrentNodeList = theJSRootElement.getElementsByTagName("a");
		if (theJSCurrentNodeList.getLength() > theCurrentNodeList.getLength())
			theCurrentNodeList = theJSCurrentNodeList;
		for (int i=0; i<theCurrentNodeList.getLength(); i++){
			String theUrl = "#";
			try {
				theUrl = theCurrentNodeList.item(i).getAttributes().getNamedItem("href").getNodeValue();
				String text = theCurrentNodeList.item(i).getNodeValue();
					if(!URLChecker.isJavascriptCommand(theUrl))
						//Cas d'un lien sans javascript : <A href="http://englishblazere.free.fr"">
						listOfHyperlinks.add(new HyperLink(URLResolver.relativeToAbsolute(url, theUrl, isTheLastFile), text, true));
					else { 
						//Cas d'un lien avec javascript : <A href="javascript:changeContext("Recette",2) ">
						listOfHyperlinks.add(new HyperLink(theUrl, text, false));
					}
				
			} catch (NullPointerException e){
				logs.finer("this a tag doesn't contain href attribute");
			}//catch
		}//for	
		
		
		
		/*
		 * Gestion des images mappees
		 */
		theCurrentNodeList = theRootElement.getElementsByTagName("map");
		theJSCurrentNodeList = theJSRootElement.getElementsByTagName("map");
		if (theJSCurrentNodeList.getLength() > theCurrentNodeList.getLength())
			theCurrentNodeList = theJSCurrentNodeList;
		logs.finer("nombre d'images morcelees :"+theCurrentNodeList.getLength());
		for (int i=0; i<theCurrentNodeList.getLength(); i++){
			try {
				String text = "une image mappee";
				Element elem = null;
				elem = (Element)theCurrentNodeList.item(i);
				NodeList theAreasList = elem.getElementsByTagName("area");
				logs.finer("nombre de parcelles pour cette image :"+theAreasList.getLength());
				
				for (int j=0; j<theAreasList.getLength(); j++){
					String theUrl = "#";
					try {
						theUrl = theCurrentNodeList.item(i).getFirstChild().getAttributes().getNamedItem("href").getNodeValue();
					} catch (Exception e){
						logs.warning("probleme dom : recuperation d'attribut");
					}
					listOfHyperlinks.add(new HyperLink(URLResolver.relativeToAbsolute(url, theUrl, isTheLastFile), text, true));
					logs.fine("un bout d'image mappee ajoute");
				}//for
			} catch (NullPointerException e){
				logs.finer("this map tag doesn't contain any clickable area");
			}//catch
		 catch (AbstractMethodError e){
			e.printStackTrace();
		}//catch
		}//for	
		
		/*
		 *  les input
		 */
		theCurrentNodeList = theRootElement.getElementsByTagName("input");
		theJSCurrentNodeList = theJSRootElement.getElementsByTagName("input");
		if (theJSCurrentNodeList.getLength() > theCurrentNodeList.getLength())
			theCurrentNodeList = theJSCurrentNodeList;
		logs.finer("nombre d'inputs :"+theCurrentNodeList.getLength());
		try {
		for (int i=0; i<theCurrentNodeList.getLength(); i++){
				String inputType = theCurrentNodeList.item(i).getAttributes().getNamedItem("type").getNodeValue();
				if (inputType == "submit"){
					String text = theCurrentNodeList.item(i).getAttributes().getNamedItem("value").getNodeValue();
					String theUrl = theCurrentNodeList.item(i).getFirstChild().getAttributes().getNamedItem("onclick").getNodeValue();
					listOfHyperlinks.add(new HyperLink(theUrl, text, false));
				}//if
		}//for	
		} catch (Exception e){
			logs.warning("recuperation de javascript deffectueuse");
		}
			
		
	}//parseLinks
	
	/**
	 * Methode permettant de savoir si le dernier element arborescant affiche 
	 * d une url est un repertoire ou un fichier.Cette etape est necessaire pour etablir 
	 * des url absolues a partir d url relatives.
	 * @return boolean : oui ou non le dernier element arborescant d une url est un fichier
	 */
	  public  boolean URLFileResolver(){
	    	 
	    	 System.out.println ("URLCHECKER : URLFileResolver");
	    	 
	    	 if (url.endsWith("/")) url = url.substring(0, url.length()-1);
	    	 Display theDisplay;
	    	 if((theDisplay = SWTBrowserRunner.getTheDisplay())==null){
	 			theDisplay = new Display();
		 		SWTBrowserRunner.setTheDisplay(theDisplay);
	    	 }
	 		
	 		final Shell theShell = new Shell(theDisplay);
	 		Browser theBrowser = null;
	 		try {
	 			theBrowser = new Browser(theShell, SWT.MOZILLA);
	 		} catch (SWTError e) {
	 			System.out.println("Could not instantiate Browser: " + e.getMessage());
	 		}
	 		theBrowser.setUrl(url);
	 		isUrlReady = false;
	 		theBrowser.addProgressListener(new ProgressAdapter() {
				public void completed(ProgressEvent event) {
					isUrlReady = true;
				}
			});
			
			while (!isUrlReady) {
				if (!theDisplay.readAndDispatch()) 
						theDisplay.sleep();
			}
	 		boolean tmp = (theBrowser.getUrl().length() == url.length());
	 		theShell.dispose();
	 		// if((theDisplay = SWTBrowserRunner.getTheDisplay())==null)
	 		//	 theDisplay = new Display();
	 		
	 		return (tmp);
	 	}
	  
	  
	
}//class
