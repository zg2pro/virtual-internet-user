package DecisionsLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import WebCodeLayer.HyperLink;
import WebCodeLayer.URLChecker;




/**
 * Classe de substitut de la couche Decisions : 
 * A partir d'une modelisation de l'internaute, on choisit des pages
 * web a visiter les unes apres les autres, ici ce choix est fait aleatoirement
 * @author Papa Issa DIALLO, Gregory ANNE
 */
public class Decisions {

	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
	/**
	 * Conservation de l'historique :
	 * Il peut etre utile au prochain module de decision de disposer de la liste
	 * des url par lesquelles l'internaute est deja passe
	 */
	private static List<String> listOfAllPagesVisited = new ArrayList<String>();
	
	
	/**
	 * Methode statique :
	 * Procedure de choix d'une url au hasard parmi une liste
	 * et en consideration des criteres souhaites
	 * @param listOfLinks - choix de pages visitables
	 * @param isUsingMailTo - le module de decision aura t'il le droit de choisir un lien mailto:
	 * @param isUsingDowloadLinks - le module de decision aura t'il le droit de choisir un lien de telechargement
	 * @param isOnSameWebSite - le module de decision aura t'il le droit de choisir un lien se trouvant sur un autre site
	 * @param isUsingJavaScriptLinks - le module de decision aura t'il le droit de choisir un lien javascript:uneFonction()
	 * @param currentUrl - la page contenant les liens de la liste
	 * @return next  - prochaine page visitee
	 */
	public static HyperLink getAnElement(List<HyperLink> listOfLinks, boolean isUsingMailTo,
			boolean isUsingDowloadLinks, boolean isOnSameWebSite, boolean isUsingJavaScriptLinks, 
			String currentUrl){
		//conservation dans l'historique de la derniere page visitee
		listOfAllPagesVisited.add(currentUrl);
		try {
			String next = "";
			List<Integer> listOfNotTryedIndexes = new ArrayList<Integer>();
			for (int i = 0; i < listOfLinks.size(); i++) listOfNotTryedIndexes.add(i);
			//melange des possibilites
			Collections.shuffle(listOfNotTryedIndexes);
			int indRandomIndexInList = -1;
			//on fera un choix et s'il n'est pas acceptable 
			//on recommencera la procedure en eliminant la possibilite deja testee
			boolean isDecisionOkay = false;
			while (!isDecisionOkay && !listOfNotTryedIndexes.isEmpty()){
				indRandomIndexInList = listOfNotTryedIndexes.get(0);
				next = listOfLinks.get(indRandomIndexInList).getUrl();
				listOfNotTryedIndexes.remove(0);
				//un choix est fait
				isDecisionOkay = true;
				//on le verifie
				if (isOnSameWebSite) 
					isDecisionOkay = URLChecker.isOnSameWebServer(currentUrl, next);
				if (!isUsingMailTo) 
					if (next.toLowerCase().startsWith("mailto:")) isDecisionOkay = false;
				if (!isUsingDowloadLinks) 
					if (next.toLowerCase().endsWith("pdf") ||
					next.toLowerCase().endsWith(".zip") ||
					next.toLowerCase().endsWith("gz") ||
					next.toLowerCase().endsWith(".tar") ||
					next.toLowerCase().endsWith(".avi") ||
					next.toLowerCase().endsWith(".mpg") ||
					next.toLowerCase().endsWith(".mov") ||
					next.toLowerCase().endsWith(".mp3") ||
					next.toLowerCase().endsWith(".mp4") ||
					next.toLowerCase().endsWith(".m3u") ||
					next.toLowerCase().endsWith(".doc") ||
					next.toLowerCase().endsWith(".odt") ||
					next.toLowerCase().endsWith(".odb") ||
					next.toLowerCase().endsWith(".odc") ||
					next.toLowerCase().endsWith(".odg") ||
					next.toLowerCase().endsWith(".ppm") ||
					next.toLowerCase().endsWith(".ods") ||
					next.toLowerCase().endsWith(".odp") ||
					next.toLowerCase().endsWith(".ppt") ||
					next.toLowerCase().endsWith(".pps") ||
					next.toLowerCase().endsWith(".psd") ||
					next.toLowerCase().endsWith(".exe") ||
					next.toLowerCase().endsWith(".iso") ||
					next.toLowerCase().endsWith(".jar") ||
					next.toLowerCase().endsWith(".rtf") ||
					next.toLowerCase().endsWith(".sda") ||
					next.toLowerCase().endsWith(".sdc") ||
					next.toLowerCase().endsWith(".sdf") ||
					next.toLowerCase().endsWith(".sdd") ||
					next.toLowerCase().endsWith(".sdp") ||
					next.toLowerCase().endsWith(".torrent") ||
					next.toLowerCase().endsWith(".sdw") ||
					next.toLowerCase().endsWith(".xls") ||
					next.toLowerCase().endsWith(".txt") ||
					next.toLowerCase().endsWith(".wav") ||
					next.toLowerCase().endsWith(".wmv") ||
					next.toLowerCase().endsWith(".xsl") ||
					next.toLowerCase().endsWith(".hlp") ||
					next.toLowerCase().endsWith(".xsd") ||
					next.toLowerCase().endsWith(".rar")
					)
						isDecisionOkay = false;
				if (!isUsingJavaScriptLinks && !listOfLinks.get(indRandomIndexInList).isHrefType()) {
					isDecisionOkay = false;
				}//if
			}//while
			if (isDecisionOkay) {
				logs.info("prochaine url: "+next);
				return listOfLinks.get(indRandomIndexInList);
			}//if
			else return null;
		} catch (ArithmeticException e){
			//dans le cas d'une division par zero : pas de liens disponibles depuis le debut
			logs.info("pas de liens sur cette page");
			return null;
		}//catch
	}//getAnElement
	
}//Decisions
