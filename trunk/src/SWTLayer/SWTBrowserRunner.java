package SWTLayer;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import WebCodeLayer.JavascriptExecuter;
import WebCodeLayer.URLResolver;



/**
 * Classe permettant de creer un navigateur SWT avec ou sans onglet
 * @author Yoann Janszen
 *
 */
public class SWTBrowserRunner {
	
	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");

	/**
	 * La page actuellement visionnee dans le navigateur
	 */
	private String url ;

	/**
	 * Le module d'onglets insere dans la fenetre
	 */
	private TabFolder theTabFolder;
	
	/**
	 * Objet assurant le dialogue entre l application 
	 * et le systeme graphique du systeme d exploitation utilise
	 */
	static Display theDisplay;



	/**
	 * Le module d'affichage de pages web integrant Gecko
	 */
	private static Browser theBrowser;
	
	
	/**
	 * Enieme onglet visite
	 */
	private int indPage;
	

	/**
	 * L affichage se fait il au moyen d'onglets conservant l'historique de la navigation
	 */
	private boolean isTabsOn;
	
	/**
	 * La fenetre SWT
	 */
	private Shell theShell;
	
	/**
	 * L indicateur de chargement de la page web
	 */
	private ProgressBar theProgressBar;
	
	/**
	 * L indicateur textuel d'etat des pages web
	 */
	private Label lStatus;
	
	/**
	 * Le navigateur est-il en train de telecharger une page web
	 */
	private boolean isPageFullyLoaded;
	
	/**
	 * Bouton permettant de changer de page (ne s'active que lorsque la page est chargee)
	 */
	private Button bChangePage; 
	
	/**
	 * etat du bouton bChange
	 */
	private boolean isChangePagePressed;
	

	/**
	 * Getter pour obtenir l instance du bouton de changement de page
	 * @return bChangePage
	 */
	public Button getBChangePage() {
		return bChangePage;
	}

	/**
	 * Le navigateur est-il en train de telecharger une page web
	 * @return true si le navigateur est a l'arret
	 */
	public boolean isPageFullyLoaded() {
		return isPageFullyLoaded;
	}

	/**
	 * Getter pour obtenir une instance du Display
	 * @return module d'affichage SWT
	 */
	public static Display getTheDisplay() {
		return theDisplay;
	}


	/**
	 * Setter static pour enregistrer un display donne par URLFileResolver
	 * @param theDisplay
	 */
	public static void setTheDisplay(Display theDisplay) {
		SWTBrowserRunner.theDisplay = theDisplay;
	}

	/**
	 * Constructeur permettant de faire le choix entre un navigateur
	 * SWT avec ou sans onglet
	 * @param aUrl
	 * @param isTabsOn
	 */
	public SWTBrowserRunner(String aUrl, Boolean isTabsOn) {
		url = aUrl;
		
		if (url == null){
			keepsDisplaying();
		}
		
		//this.indNumberOfPages = indNumberOfPages;
		isPageFullyLoaded = false;
		this.isTabsOn = isTabsOn;
		
		// dessin de la fenetre SWT, positionnement des widgets
		theDisplay = new Display();

		theShell = new Shell(theDisplay);

		GridLayout theShellGridLayout = new GridLayout();
	    theShellGridLayout.numColumns = 3;
	    theShell.setLayout(theShellGridLayout);
	    
	    GridData theGridData = new GridData();
		theGridData.verticalAlignment = GridData.FILL;
		theGridData.horizontalAlignment = GridData.FILL;
		theGridData.grabExcessVerticalSpace = true;
		theGridData.grabExcessHorizontalSpace = true;
	   theGridData.horizontalSpan= 3;
		try {
			if (isTabsOn){
				theTabFolder = new TabFolder (theShell, SWT.MOZILLA);
				theBrowser = new Browser(theTabFolder, SWT.MOZILLA);
				theTabFolder.setLayoutData(theGridData);
				
			} else {
				theBrowser = new Browser(theShell, SWT.MOZILLA);
				theBrowser.setLayoutData(theGridData);
			}
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			return;
		}


		
		theGridData = new GridData(GridData.FILL_HORIZONTAL);
		lStatus = new Label(theShell, SWT.NONE);
		lStatus.setVisible(true);
		theGridData.horizontalSpan= 1;
		lStatus.setLayoutData(theGridData);
		
		
		bChangePage = new Button (theShell, SWT.PUSH);
		bChangePage.setText ("Page Suivante");
		bChangePage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				logs.info("Page suivante");
				isChangePagePressed = true;
			}
		});
		
		theGridData = new GridData();
		theGridData.horizontalAlignment = GridData.CENTER;
		theGridData.horizontalSpan= 1;
		bChangePage.setLayoutData(theGridData);
		bChangePage.setEnabled(false);
		
		
		theGridData = new GridData();
		theProgressBar = new ProgressBar(theShell, SWT.NONE);
		theGridData.horizontalAlignment = GridData.END;
		theGridData.horizontalSpan= 1;
		theProgressBar.setLayoutData(theGridData);
		

		if (isTabsOn){
			TabItem theFirstTabItem = new TabItem (theTabFolder, SWT.MOZILLA);
			theFirstTabItem.setControl (theBrowser);
			theFirstTabItem.setText(url);
		}
		
		//chargement de la page initiale
		theBrowser.setUrl(url);
		
		//ecoute du chargement de la page pour mettre a jour la progressbar, 
		//le label de statut et isPageFullyLoaded
		theBrowser.addProgressListener(new ProgressAdapter() {
			public void changed(ProgressEvent evt) {
				if (evt.total == 0) return;                            
				int indRatio = evt.current * 100 / evt.total;
				theProgressBar.setSelection(indRatio);
		}
		
			public void completed(ProgressEvent event) {
				isPageFullyLoaded = true;
				bChangePage.setEnabled(true);
			}
		});
		theBrowser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent event) {
				lStatus.setText(event.text);	
			}
		});
		
		
		theBrowser.addCloseWindowListener(new CloseWindowListener() {
			public void close(WindowEvent event) {
				((Browser) event.widget).getShell().close();
			}
		});

		
		
		theShell.open();
		theShell.setVisible(true);
		theShell.setText(url);
	}//constructor

	/**
	 * Methode permettant l ouverture d'une nouvelle page dans le navigateur
	 * @param anURL : une url
	 */
	public void openNextPage(String anURL){
		bChangePage.setEnabled(false);
		isChangePagePressed = false;
		isPageFullyLoaded = false;
		url = anURL;
		logs.info("SWTBrowserRunner openNext url "+anURL);
		if (url == null){
			keepsDisplaying();
		}
		
		if (isTabsOn){
			theBrowser = new Browser(theTabFolder, SWT.MOZILLA);
			TabItem theTabItem = new TabItem (theTabFolder, SWT.MOZILLA);
			theTabItem.setControl (theBrowser);
			theTabItem.setText(url);
			indPage++;
			theTabFolder.setSelection(indPage);
		}//if
		
		theBrowser.setUrl(url);
		theShell.setText(url);
		
	}//openNextPage
	

	/**
	 * Methode permettant de garder le shell SWT
	 *  ouvert apres toutes les visites de page
	 */
	public void keepsDisplaying(){
			JOptionPane.showMessageDialog(null, "Navigation terminee");
				while (!theShell.isDisposed()) {
					//Si il n y a plus d evenement a traiter
					if (!theDisplay.readAndDispatch()) {
						//Permet de mettre en attente le thread d ecoute d evenements jusqu a l arrivee d un nouvel evenement.
						theDisplay.sleep();
					}//if
				}//while
		}//keepsDisplaying


	/**
	 * Methode permettant de recuperer le document DOM de la page :
	 * 	  Linux : document DOM de la page courante
	 *    Windows : apres l execution de la commande javascript passe en parametre
	 * @param javascriptMethod : commande javascript
	 * @return Document : Linux : document DOM de la page courante
	 * 					  Windows : document DOM de la page apres l execution de la commande
	 */
	public  org.w3c.dom.Document getDomDocumentAfterJSCommand(String javascriptMethod){
	
		if (System.getProperty("os.name").equals("Linux")) return URLResolver.getDomDocumentHTMLCodeFromURL(url);
		return JavascriptExecuter.JavascriptExecuteAfterJSCommand(theBrowser);
	}

	/**
	 * acces a l'etat du bouton
	 * @return true si le bouton a ete presse.
	 */
	public boolean isChangePagePressed() {
		return isChangePagePressed;
	}
	
}//class
