package WebCodeLayer;


import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.dom.NodeFactory;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIWebBrowser;
import org.w3c.dom.Document;

import SWTLayer.SWTBrowserRunner;

/**
 * Classe permettant d executer le code Javascript d'une page web.
 * (Necessite l'installation de XulRunner)
 * @author Janszen Yoann
 */
public class JavascriptExecuter {

	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
	/**
	 * Le module d'affichage de pages web integrant Gecko
	 */
	private Browser theBrowser ;
	
	/**
	 * Document DOM de la page en cours de traitement
	 */
	private Document theDocumentDom ;
	
	/**
	 * Objet assurant le dialogue entre l application 
	 * et le systeme graphique du systeme d exploitation utilise
	 */
	private Display theDisplay ;
	
	/**
	 * La fenetre SWT
	 */
	private Shell theShell;
	

	/**
	 * Getter pour obtenir le document DOM de la page en cours de traitement
	 * @return theDocumentDom : Document DOM
	 */
	public Document getTheDomDocument() {
		return theDocumentDom;
	}

	
	

	
	/**
	 * Methode permettant d executer le code Javascript d une page web dont l url est passe
	 * en parametre.
	 * Met a jour la propriete theDocumentDom representant le document DOM
	 * de la page apres l execution du Javascript. 
	 * @param aUrl : une url
	 */
	public JavascriptExecuter(String aUrl){
		if((theDisplay = SWTBrowserRunner.getTheDisplay())==null)
				theDisplay = new Display();
		theShell = new Shell(theDisplay);
		try {
			theBrowser = new Browser(theShell, SWT.MOZILLA);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
		}
		
		theBrowser.setUrl(aUrl);
	
		theBrowser.addProgressListener(new ProgressAdapter() {
			public void completed(ProgressEvent event) {
				nsIWebBrowser webBrowser = (nsIWebBrowser)theBrowser.getWebBrowser();
				if (webBrowser == null) {
					System.out.println("Could not get the nsIWebBrowser from the Browser widget");
				}	
				nsIDOMWindow window = webBrowser.getContentDOMWindow();
				nsIDOMDocument document = window.getDocument();
				theDocumentDom = (Document)NodeFactory.getNodeInstance(document);
				theDocumentDom = URLResolver.getDomDocumentFromStringHTMLCode(DOMDump.xmlToString(theDocumentDom));
				theShell.dispose();
			}
		});
		
		while (!theShell.isDisposed()) {
			if (!theDisplay.readAndDispatch()) 
						theDisplay.sleep();	
		}
		
		if(SWTBrowserRunner.getTheDisplay()==null)
			theDisplay.dispose();
	}
	
	/**
	 * Methode permettant d executer le code Javascript d une page web dont le code
	 * est passe en parametre.
	 * Met a jour la propriete theDocumentDom representant le document DOM
	 * de la page apres l execution du Javascript. 
	 * @param theCode : code de la page
	 */
	public void ExecuteJavascript(String theCode) {
		if((theDisplay = SWTBrowserRunner.getTheDisplay())==null)
			theDisplay = new Display();
	theShell = new Shell(theDisplay);
	try {
		theBrowser = new Browser(theShell, SWT.MOZILLA);
	} catch (SWTError e) {
		System.out.println("Could not instantiate Browser: " + e.getMessage());
	}
	
	theBrowser.setText(theCode);
	theBrowser.addProgressListener(new ProgressAdapter() {
		public void completed(ProgressEvent event) {
			nsIWebBrowser webBrowser = (nsIWebBrowser)theBrowser.getWebBrowser();
			if (webBrowser == null) {
				System.out.println("Could not get the nsIWebBrowser from the Browser widget");
			}	
			nsIDOMWindow window = webBrowser.getContentDOMWindow();
			nsIDOMDocument document = window.getDocument();
			theDocumentDom = (Document)NodeFactory.getNodeInstance(document);
			theShell.dispose();
		}
	});
	//Syteme de securite en cas d execution javascript, par exemple au cas ou l execution de js provoque
	//une boucle infinie (le manque de parametre pourrait la provoquer) nous stopons apres un temps donne : dans notre cas 10 sec
	long date = System.currentTimeMillis();
	while (!theShell.isDisposed() && (System.currentTimeMillis() - date < 10000)) {
		if (!theDisplay.readAndDispatch()) 
					theDisplay.sleep();	
	}
	if(SWTBrowserRunner.getTheDisplay()==null)
		theDisplay.dispose();
	}
	/**
	 * Methode statique retournant le document DOM de la page apres l execution du 
	 * Javascript.
	 * @param theBrowser : browser correspondant a la page courante
	 * @return Document : document DOM de la page apres l execution du Javascript
	 */
	public static Document JavascriptExecuteAfterJSCommand(Browser theBrowser){
		Document theDomDocument;
		nsIWebBrowser webBrowser = (nsIWebBrowser)theBrowser.getWebBrowser();
		if (webBrowser == null) {
			System.out.println("Could not get the nsIWebBrowser from the Browser widget");
		}	
		nsIDOMWindow window = webBrowser.getContentDOMWindow();
		nsIDOMDocument document = window.getDocument();
		theDomDocument = (Document)NodeFactory.getNodeInstance(document);
		DOMDump.printDOMTree(theDomDocument, System.out);
		return theDomDocument;
	}
	
	
	
}
				
			