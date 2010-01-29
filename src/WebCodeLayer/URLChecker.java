package WebCodeLayer;

import java.util.logging.Logger;


/**
 * Classe de transition entre les URL relatives et les URL absolues
 * @author gregory
 */
public class URLChecker {
	
	
	/**
	 * L instance du gestionnaire d'evenements 
	 * utilisee dans l'execution presente
	 */
	private static Logger logs = Logger.getLogger("logs");
	
	/**
	 * Url relative
	 */
     private String relativeUrl;
     
     /**
      * Url de la page en cours
      */
     private String curUrl;     
     
    /**
     * Url absolue
     */
     private String absoluteUrl;         
     
     /**
      * Tableau contenant le decoupage de l url cible
      */
     private String [] relativeUrlCutting;
     
     /**
      * Tableau contenant le decoupage de l url actuelle
      */
     private String [] currentUrlCutting;
     
     
    /**
     * Constructeur permettant de passer d une url relative a une url absolue.
     * Met a jour l url dans absoluteURL.
     * @param currentUrl : url du site
     * @param relativeUrl : chemin relatif de la page
     * @param isTheLastFile : 
     */
     public URLChecker(String currentUrl,String relativeUrl, boolean isTheLastFile){
    	 logs.fine("URL CHECKER AVANT DECOUPE"+currentUrl+"++"+relativeUrl);
    	 this.relativeUrl=relativeUrl;
         this.curUrl=currentUrl;
         curUrl = getOffAnchor(curUrl);
         logs.fine("URLChecker home "+curUrl);
         currentUrlCutting = urlCissors(curUrl);
          
      
         //si le dernier element de l'url est un fichier, on construit
         //l'url absolue differemment avec une url de la forme http://(...)/fichier
         //qu'avec une url de la forme http://(...)/fichier.html
          if (currentUrlCutting.length > 3 && isTheLastFile
        		  && !currentUrlCutting[currentUrlCutting.length-1].contains(".")){
        	  if (!relativeUrl.trim().startsWith("#")) 
        		  relativeUrl = "../"+relativeUrl;
          }//if
         
          relativeUrlCutting=urlCissors(relativeUrl);
          
          absoluteUrl = absoluteUrlFactory(); //on crée l'URL Absolue
          
          logs.fine("URLChecker abs "+absoluteUrl);     
     }//constructor
     
     /**
      * accesseur a la valeur absolue construite
      * @return absoluteUrl
      */
     public String getAbsoluteURL(){
          return absoluteUrl;
      }//getAbsoluteUrl
     
   
     //méthodes privées utiles au traitement
     
     /**
      * Decoupe des Url en fonction des slashes
      */
     private static String[] urlCissors(String aUrl){
          int k = 0;
          int i = aUrl.indexOf('/',0);
          
          //comptage du nombre de "/" dans une URL
          while(i!=(-1)){
               k++;
               i=aUrl.indexOf('/',(i+1));
           }//while
          
          String result[] = new String[k+1];
          
          int indBegin = 0;
          int indEnd = aUrl.indexOf('/',0);
          
          //indice pour le remplissage du tableau
          k=0;
          
          //on stocke dans le tableau résultat l'ensemble des éléments de l'URL
          while(indEnd!=(-1)){
               result[k]=aUrl.substring(indBegin,indEnd);
               k++;
               indBegin=indEnd+1;
               indEnd=aUrl.indexOf('/',(indEnd+1));
          }//while
          
          indEnd = aUrl.length();
          result[k] = aUrl.substring(indBegin,(indEnd));
          
         return(result);
     }//urlCissors
          
     /**
      * analyse du type d'une url
      * @param aUrl
      * @return true si monURL est absolue - false sinon
      */
     private static boolean isAbsoluteURL(String[] aUrl) {
          int i=aUrl.length;
          if (i>2) {
               return ( ( ((aUrl[0]).equals("http:")) || 
            		   ((aUrl[0]).equals("https:")) || 
            		   ((aUrl[0]).equals("ftp:")) ) && ((aUrl[1]).equals("")) )?
                                   true : false;
          }//if
          return false;
     }//isAbsoluteURL
          
     /**
      * 
      * @param aUrl
      * @return true si commence par mailto - false sinon
      */
     private static boolean isAMailAddress(String aUrl) {
          return ( (aUrl.toLowerCase().indexOf("mailto:")!=(-1)) && (aUrl.toLowerCase().indexOf("@")!=(-1)) )?
               true : false;
      }//isAdresseEmail
          
     /**
      * 
      * @return
      */
     private String absoluteUrlFactory(){
          if (isAbsoluteURL(relativeUrlCutting)){
               return(relativeUrl);
          }
          else
          if (isAMailAddress(relativeUrl)){
               //System.out.println("on est dans le cas d'une adresse e-mail");
               return(relativeUrl);
          }
          if (relativeUrl.indexOf("#")==0){
               return(curUrl+relativeUrl);
          }
          else {
               int indLengthCurrentUrl = currentUrlCutting.length;
               int indLengthRelativeUrl = relativeUrlCutting.length;
               
               String result="";
               
               int k = indLengthCurrentUrl+indLengthRelativeUrl;
               
               //permet d'enlever le fichier affiché dans le répertoire home !!!
               //ex: blablabla/index.html --> blablabla/
               //ex2 : blablabla/ -->blablabla
               boolean boolxxx=( ( (currentUrlCutting[indLengthCurrentUrl-1].indexOf('.'))==(-1) )? false : true );
               boolean boolyyy=( (currentUrlCutting[indLengthCurrentUrl-1].equals("") )? true : false );
               int indRear=0;
                                        
               while (k>0){
                    int i, j;
                     
                    if (k>indLengthCurrentUrl){
                         j=k-indLengthCurrentUrl-1;
                         
                         if ((indRear!=0)&&(!relativeUrlCutting[j].equals(".."))){
                              k--;
                              indRear--;
                              continue;
                         }//if
                                                                           
                         if ( (relativeUrlCutting[j]).equals("..") ){
                              k--; //on va 1 fois en arriére
                              indRear++; //on se prépare à aller une deuxieme fois en arriere
                              continue;
                         }
                         else
                              {
                              if ((j==0)&&(relativeUrlCutting[j].equals(""))) {
                                   //on doit redescendre jusqu'à la racine...
                                   //ex http://www.exemple.fr
                                   k=3;
                                   boolxxx=false;
                                   boolyyy=false;
                              }                              
                              else 
                                   {
	                                   if (j!=(indLengthRelativeUrl-1)) {
	                                        result=(relativeUrlCutting[j]+"/"+result);
	                                   }
	                                   else {
	                                        result=(relativeUrlCutting[j]+result);
	                                   }
	                                   k--;
                                   }//else
                              }
                         
                         }
                    else {
                         i=k-1;
                         if ((indRear!=0)&&(!currentUrlCutting[i].equals(".."))) {
                              k--;
                              indRear--;
                              continue;
                         }
                         
                         if (boolxxx || boolyyy){
                              if (i>2)
                                   {
                                   k--;
                                   i--;
                                   boolxxx=false;
                                   boolyyy=false;
                                   }
                          }//if
                    
                         result=currentUrlCutting[i]+"/"+result;
                         
                         k--;
                         }
                    }
               return(result);
               }
          }//AbsoluteUrlFactory
     

     /**
      * Methode permettant de verifier si 2 url en parametre appartiennent
      * au meme site
      * @param aUrl1
      * @param aUrl2
      * @return
      */
     public static boolean isOnSameWebServer(String aUrl1,String aUrl2) {
    	 
    	 //System.out.println("URLCHecker getIsSameWebServer url1 "+aUrl1+" url2 "+aUrl2);
    	 
    	 //Si une url est une commande javascript on considere qu'elle fait partie du 
    	 //meme site meme si cela peut etre faux.
    	 if(isJavascriptCommand(aUrl1) || aUrl1.toLowerCase().startsWith("mailto:"))
    		 return true;
    	 if(isJavascriptCommand(aUrl2) || aUrl2.toLowerCase().startsWith("mailto:"))
    		 return true;
    	 
    	 aUrl1 = urlCissors(aUrl1)[2];
    	 aUrl2 = urlCissors(aUrl2)[2];
    	 return (aUrl1.compareTo(aUrl2)== 0) ? true : false;
    	
     }
     /**
      * Methode permettant de savoir si oui ou non l url passe en parametre est une commande
      * Javascript
      * @param theUrl : une url 
      * @return true si commence par javascript: - false sinon
      */
     public static boolean isJavascriptCommand(String theUrl) {
				return (theUrl.trim().toLowerCase().startsWith("javascript:"))?
					//Cas d'un lien sans javascript : <A href="http://englishblazere.free.fr"">
					true : false;
     }//isJavascriptCommand
     
     /**
      * enleve l'ancre de l'url courante lorsqu'il y en a une
      * @param url
      * @return url
      */
     public String getOffAnchor (String url){
    	 int i = url.indexOf('#');
    	 if (i<0) return url;
    	 url = url.substring(0, i);
       	 logs.fine("getoffAnchor2"+url);
    	 return url;
     }//getOffAchor
     
     
}//URLChecker

