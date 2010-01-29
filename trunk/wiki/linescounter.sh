#! /bin/bash
echo $1
echo "nombre de lignes en tout : "
tr "\t" "\n" < $1 | tr -s "\n" | tr -s "\t" | wc -l
echo "nombre de lignes de code : "
cat $1 | sed  's!/\*.*\*/!!' | sed  '/\/\*/,/\*\//d' | sed '/^\ *\/\//d' | sed '/^\ *$/d'  | wc -l
echo "nombre de lignes de commentaires : "
expr `tr "\t" "\n" < $1 | tr -s "\n" | tr -s "\t" | wc -l` - `cat $1 | sed  's!/\*.*\*/!!' | sed  '/\/\*/,/\*\//d' | sed '/^\ *\/\//d' | sed '/^\ *$/d'  | wc -l`
echo ' '
echo ' '
