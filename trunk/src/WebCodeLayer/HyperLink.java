package WebCodeLayer;

import java.util.logging.Logger;

/**
 * Classe representant un lien dans un code HTML
 * @author Gregory ANNE
 *
 */
public class HyperLink {

	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
	
	
	/**
	 * Les liens href et les liens javascript sont geres differemment
	 * sur le navigateur SWT, il faut donc tester le type. :
	 * true correspond typiquement aux liens classiques 
	 * que l'on trouve dans les balises a par exemple : 
	 * des URL (relatives ou absolues)
	 * false correspond a des appels de methodes javascript :
	 * "javascript:uneFonction();" 
	 */
	private boolean isHrefType;
	

	/**
	 * Url du lien
	 */
	private String url;
	
	/**
	 * Le texte du lien
	 */
	private String text;
	

	/**
	 * Getter pour obtenir l url de l instance HyperLink
	 * @return url : une url
	 */
	public String getUrl() {
		return url;
	}//getUrl

	/**
	 * Getter pour savoir si l instance HyperLink est un lien normal
	 * ou contenant du Javascript (href="javascript:") 
	 * @return isHrefType : vrai si href ne contient pas de javascript faux sinon
	 */
	public boolean isHrefType() {
		return isHrefType;
	}
	
	/**
	 * Constructor permettant de creer une instance du type HyperLink
	 * @param url : une url
	 * @param text : le texte du lien
	 * @param isHrefType : vrai si href ne contient pas de javascript faux sinon 
	 */
	public HyperLink (String url, String text, boolean isHrefType){
		this.url = url;
		this.text = text;
		this.isHrefType = isHrefType;
	}//constructor

	@Override
	public String toString() {
		return 
		" type-->" + isHrefType +
		" url-->" + url + 
		" text-->" + text ;
	}//toString

	
}//class
