package Main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;



/**
 * Classe creeant l interface de notre application.Cette interface
 * permet de choisir les differentes options voulues pour notre navigateur
 * @author Issa DIALLO, Gregory ANNE
 *
 */
public class Interface extends JFrame{

	
	private static final long serialVersionUID = -390667861447375234L;
	
	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");

	/**
	 * Choix du navigateur : SWT ou Firefox
	 */
	private boolean  isSWT = false;
	
	/**
	 * Choix du mode de navigation : SWT avec ou sans onglet
	 */
	private boolean  isSWTWithTabs = false;
	
	/**
	 * Genre de navigation : sur le meme site ou sur tous les sites
	 */
	private boolean  isSameWebSite = false;
	
	/**
	 * Fenetre ferme ou non
	 */
	private boolean  isDisposed = false;

	/**
	 * Nombre de pages a visiter durant l'execution du programme
	 */
	private int  indNumberOfPages ;
	
	/**
	 * Temps de pause minimal entre chaque affichage de page
	 */
	private int  indTimeOfPage ;
	
	/**
	 * Url de la premiere page a visiter
	 */
	private String url;
	
	/**
	 * Choix selon lequel nous acceptons d'utiliser les liens de type mailto:
	 */
	private boolean isUsingMailTo = false;
	
	/**
	 * Choix selon lequel nous acceptons d'utiliser les liens de type javascript:uneFonction()
	 */
	private boolean isUsingJavaScriptLinks = false;

	/**
	 * Choix selon lequel nous acceptons d'utiliser les liens de telechargement
	 */
	private boolean isUsingDownloadLinks = false;
	
	/**
	 * Bouton de lancement de l'experience
	 */
	private JButton bLauncher;
	
	/**
	 * Texte decrivant tfUrl
	 */
	private JLabel lUrl;
	
	/**
	 * Legende decrivant rbFirefox
	 */
	private JLabel lFirefox;
	
	/**
	 * Legende decrivant rbSWTTabsOff
	 */
	private JLabel lSWTTabsOff;
	
	/**
	 * Legende decrivant rbSWTTabsOn
	 */
	private JLabel lSWTTabsOn;
	
	/**
	 * Legende decrivant sNumberOfPages
	 */
	private JLabel lNumberOfPages;
	
	/**
	 * Legende decrivant rbSameWebSite
	 */
	private JLabel lSameWebSite;
	
	/**
	 * Legende decrivant rbAllWebSites
	 */
	private JLabel lAllWebSites;
	
	/**
	 * Legende decrivant sTimeOfPage
	 */
	private JLabel lTimeOfPage;
	
	/**
	 * Legende decrivant cbUsingMailTo
	 */
	private JLabel lUsingMailTo;
	
	/**
	 * Legende decrivant cbUsingDownloadLinks
	 */
	private JLabel lUsingDownloadLinks;

	/**
	 * Legende decrivant cbUsingJavaScriptLinks
	 */
	private JLabel lUsingJavaScriptLinks;

	/**
	 * Champ contenant l url de la premiere page
	 */
	private JTextField tfUrl;

	/**
	 * RadioButton pour selectionner le navigateur Firefox
	 */
	private JRadioButton rbFirefox;

	/**
	 * RadioButton pour selectionner le navigateur SWT sans onglet
	 */
	private JRadioButton rbSWTTabsOff;

	/**
	 * RadioButton pour selectionner le navigateur SWT avec onglet
	 */
	private JRadioButton rbSWTTabsOn;

	/**
	 * RadioButton pour que les liens choisis n appartiennent qu au site de depart
	 */
	private JRadioButton rbSameWebSite;

	/**
	 * RadioButton pour que les liens choisis appartiennent a n importe quel site
	 */
	private JRadioButton rbAllWebSite;

	/**
	 * Spinner pour determiner le nombre de page a afficher
	 */
	private JSpinner sNumberOfPages;

	/**
	 * Spinner pour determiner le temps d affichage des pages
	 */
	private JSpinner sTimeOfPage;
	

	/**
	 * Checbox pour prendre en compte les liens de type de mailto
	 */
	private JCheckBox cbUsingMailTo;

	/**
	 * Checbox pour prendre en compte les liens de telechargement
	 */
	private JCheckBox cbUsingDownloadLinks;

	/**
	 * Checbox pour prendre en compte les liens du type javascript:Fonction()
	 */
	private JCheckBox cbUsingJavaScriptLinks;

	/**
	 * Methode recuperant les differentes options choisies grace a l interface
	 */
	public void getChoicesFromInterface(){
		url = tfUrl.getText();
		isSWT = !(rbFirefox.isSelected());
		isSWTWithTabs = !(rbSWTTabsOff.isSelected());
		isSameWebSite = rbSameWebSite.isSelected();
		isUsingMailTo = cbUsingMailTo.isSelected();
		isUsingDownloadLinks = cbUsingDownloadLinks.isSelected();
		isUsingJavaScriptLinks = cbUsingJavaScriptLinks.isSelected();
		
		indNumberOfPages = (Integer)sNumberOfPages.getValue();
		if (indNumberOfPages < 1) indNumberOfPages = Integer.MAX_VALUE;
		indTimeOfPage = (Integer) sTimeOfPage.getValue();
		isDisposed = true;
		dispose();
	}//getChoicesFromInterface
	
	/**
	 * Constructeur lancant l interface
	 */
	public Interface(){
		super("Bienvenue");
		
		
		bLauncher = new JButton("Lancer");
		
		
		lUrl = new JLabel("Url :                         ");
		lFirefox = new JLabel("Utiliser le navigateur Web Firefox    ");
		lSWTTabsOff = new JLabel("Utiliser un navigateur java(SWT) sans onglets ");
		lSWTTabsOn = new JLabel("Utiliser un navigateur java(SWT) avec onglets ");
		lNumberOfPages = new JLabel("Nombre maximal de page a ouvir :");
		lSameWebSite = new JLabel("Naviguer seulement sur le site web de cette Url                                                                  ");
		lAllWebSites = new JLabel("Naviguer sur tous les sites internet ");
		lTimeOfPage = new JLabel("                                     Temps en secondes laisse entre chaque traitement de page :");
		lUsingMailTo = new JLabel("Considerer les liens de type mailto                                                              ");
		lUsingDownloadLinks = new JLabel("Considerer les liens de telechargements de fichiers  (zip, pdf, ...)                         ");
		lUsingJavaScriptLinks = new JLabel("Considerer les liens de type javascript:uneProcedure()                                             ");
		
		
		tfUrl = new JTextField(31);
		tfUrl.setText("http://englishblazere.free.fr/moncv");
		tfUrl.selectAll();
		
		
		rbFirefox = new JRadioButton();
		rbFirefox.setBackground(Color.getHSBColor(53, 224, 201));
		rbSWTTabsOff = new JRadioButton();
		rbSWTTabsOff.setBackground(Color.getHSBColor(53, 224, 201));
		
		rbSWTTabsOn = new JRadioButton();
		rbSWTTabsOn.setBackground(Color.getHSBColor(53, 224, 201));
		ButtonGroup aButtonGroup = new ButtonGroup();
		aButtonGroup.add(rbFirefox);
		aButtonGroup.add(rbSWTTabsOff);
		aButtonGroup.add(rbSWTTabsOn);
		rbSameWebSite = new JRadioButton();
		rbSameWebSite.setBackground(Color.getHSBColor(53, 224, 201));
		rbAllWebSite = new JRadioButton();
		rbAllWebSite.setBackground(Color.getHSBColor(53, 224, 201));
		rbAllWebSite.setSelected(true);
		ButtonGroup anotherButtonGroup = new ButtonGroup();
		anotherButtonGroup.add(rbAllWebSite);
		anotherButtonGroup.add(rbSameWebSite);
		
		
		SpinnerNumberModel theNumberOfPagesSNM = new SpinnerNumberModel(5, 0, 20, 1);
		SpinnerNumberModel theTimeOfPageSNM = new SpinnerNumberModel(3, 1, 5, 1);
		sNumberOfPages = new JSpinner(theNumberOfPagesSNM);
		sTimeOfPage = new JSpinner(theTimeOfPageSNM);
		
		
		cbUsingMailTo = new JCheckBox();
		cbUsingMailTo.setBackground(Color.getHSBColor(53, 224, 201));
		cbUsingDownloadLinks = new JCheckBox();
		cbUsingDownloadLinks.setBackground(Color.getHSBColor(53, 224, 201));
		cbUsingJavaScriptLinks = new JCheckBox();
		cbUsingJavaScriptLinks.setBackground(Color.getHSBColor(53, 224, 201));
		

		if (!System.getProperty("os.name").equals("Linux")) {
			/*
			lFirefox.setEnabled(false);
			rbFirefox.setEnabled(false);*/
			rbSWTTabsOff.setSelected(true);
		}//if
		else {
			rbFirefox.setSelected(true);
			cbUsingJavaScriptLinks.setEnabled(false);
			lUsingJavaScriptLinks.setEnabled(false);
		}
		
		
		Container theContainer = getContentPane();
		theContainer.setLayout(new FlowLayout());
		
		JPanel pUrl = new JPanel();
		pUrl.setBackground(Color.CYAN);
		pUrl.add(lUrl);
		pUrl.add(tfUrl);
		JPanel pBrowserChoice = new JPanel();
		pBrowserChoice.setBackground(Color.getHSBColor(53, 224, 201));
		pBrowserChoice.add(rbFirefox);
		pBrowserChoice.add(lFirefox);
		pBrowserChoice.add(rbSWTTabsOff);
		pBrowserChoice.add(lSWTTabsOff);
		pBrowserChoice.add(rbSWTTabsOn);
		pBrowserChoice.add(lSWTTabsOn);
		JPanel pRobotProperties = new JPanel();
		pRobotProperties.setBackground(Color.getHSBColor(53, 224, 201));
		pRobotProperties.add(lNumberOfPages);
		pRobotProperties.add(sNumberOfPages);
		pRobotProperties.add(lTimeOfPage);
		pRobotProperties.add(sTimeOfPage);
		JPanel pWebSiteProperties = new JPanel();
		pWebSiteProperties.setBackground(Color.getHSBColor(53, 224, 201));
		pWebSiteProperties.add(rbSameWebSite);
		pWebSiteProperties.add(lSameWebSite);
		pWebSiteProperties.add(rbAllWebSite);
		pWebSiteProperties.add(lAllWebSites);
		
		JPanel pCheckBoxes = new JPanel();
		pCheckBoxes.setBackground(Color.getHSBColor(53, 224, 201));


		Box hBox1 = Box.createHorizontalBox();
        hBox1.setBackground(Color.getHSBColor(53, 224, 201));
        hBox1.setAlignmentX(Box.LEFT_ALIGNMENT);
		hBox1.add(cbUsingMailTo);
		hBox1.add(lUsingMailTo);
		Box hBox2 = Box.createHorizontalBox();
        hBox2.setBackground(Color.getHSBColor(53, 224, 201));
        hBox2.setAlignmentX(Box.LEFT_ALIGNMENT);
		hBox2.add(cbUsingDownloadLinks);
		hBox2.add(lUsingDownloadLinks);
		Box hBox3 = Box.createHorizontalBox();
        hBox3.setBackground(Color.getHSBColor(53, 224, 201));
        hBox3.setAlignmentX(Box.LEFT_ALIGNMENT);
		hBox3.add(cbUsingJavaScriptLinks);
		hBox3.add(lUsingJavaScriptLinks);
		
        Box vBox = Box.createVerticalBox();
        vBox.setBackground(Color.getHSBColor(53, 224, 201));
        vBox.add(hBox1);
        vBox.add(hBox2);
        vBox.add(hBox3);

        pCheckBoxes.add(vBox);
		
		JPanel pLauncher = new JPanel();
		pLauncher.setBackground(Color.getHSBColor(53, 224, 201));
		pLauncher.add(bLauncher);
		
		
		theContainer.add(pUrl);
		theContainer.add(pBrowserChoice);
		theContainer.add(pRobotProperties);
		theContainer.add(pWebSiteProperties);
		theContainer.add(pCheckBoxes);
		theContainer.add(pLauncher);
		
		
		tfUrl.addKeyListener(new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == e.VK_ENTER) {
				getChoicesFromInterface();
			}
		}

		
		public void keyTyped(KeyEvent e) {}

		public void keyReleased(KeyEvent e) {}

		});
		
		
		
		bLauncher.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						/** Si on valide les informations
						 * 
						 */
						getChoicesFromInterface();
					}
				}
				);

		setSize(new Dimension(1000,280));
		setLocation(100, 300);
		theContainer.setBackground(Color.getHSBColor(53, 224, 201));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
	}//constructor
	
	/**
	 * acces au type de navigateur choisi
	 * @return true si SWT - false si Firefox
	 */
	public boolean isSWT() {
		return isSWT;
	}

	/**
	 * utile si isSWT = true : choix de navigation
	 * avec ou sans onglets
	 * @return true si avec onglets
	 */
	public boolean isSWTWithTabs() {
		return isSWTWithTabs;
	}


	/**
	 * genre de navigation choisie
	 * @return true si l'on prefere ne naviguer que
	 * sur des liens internes au site
	 */
	public boolean isSameWebSite() {
		return isSameWebSite;
	}

	/**
	 * acces au nombre de pages
	 * @return nombre de pages a parcourir
	 */
	public int getNumberOfPages() {
		return indNumberOfPages;
	}


	/**
	 * temps minimal de pause sur la page
	 * @return 
	 */
	public int getTimeOfPage() {
		return indTimeOfPage;
	}


	/**
	 * acces a l'url de l'interface
	 * @return url de départ
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * accepte t-on les liens mailto: ?
	 * @return true si les liens mailto sont acceptés
	 */
	public boolean isUsingMailto() {
		return isUsingMailTo;
	}

	/**
	 * 
	 * @return true si on accepte d'arreter la navigation
	 * en rencontrant un lien de telechargement
	 */
	public boolean isUsingDownloadLinks() {
		return isUsingDownloadLinks;
	}

	/**
	 * 
	 * @return true si l'interface a été fermee
	 */
	public boolean isDisposed() {
		return isDisposed;
	}


	/**
	 * 
	 * @return true si on accepte de changer l'apparence
	 * d'une page en cliquant sur des boutons javascript:
	 */
	public boolean isUsingJavaScriptLinks() {
		return isUsingJavaScriptLinks;
	}

}//class
