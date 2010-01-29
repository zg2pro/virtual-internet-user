 #!/bin/sh

#ici on met la commande pidof firefox dans une variable sans qu'elle soit executee grace aux anti cotes. pidof est la commande qui permet d'obtenir le pid d'un processus exactement comme en C.
var=`pidof firefox` 

#ps aux est la commande linux qui permet de lister toutes les programmes qui tourtnent actuellement. on y cherche le pid de firefox et on regarde son etat (Rl, Dl ou bien Sl) on met le resultat dans une variable (nombre de firefox en cours qui ne sont pas Sl)
var2=`ps aux | grep $var | grep -v -c Sl`

#tant que ce nombre est positif (greater than = plus grand que)
while [ $var2 -gt 0 ]
do 
	#on recommence a verifier l'etat du processus de l'application firefox
	var2=`ps aux | grep $var | grep -v -c Sl`
done      

#une fois que firefox est stable (ne consomme plus activement de ressourvces) on lui envoie la nouvelle commande : 
# ouverture dans un nouvel onglet de firefox deja lance OU ( || c'est ou ) ouverture dans une nouvelle fenetre firefox s'il la premiere solution echoue.
# le & en fin de commande permet de creer un nouveau processus dans lequel lancer la commande (si tu lances la commande depuis le meme processus duquel tu executes le script tu vas bloquer cette ressource jusqu'a ce qu'elle soit tuee, -fenetre fermee-)
firefox -remote "openURL ($1, new-tab)" || firefox $1 &
