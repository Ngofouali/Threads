package com.icodi.exercice1;

public class Main {
    public static void main(String[] args) {

        System.out.println("Démarrage du programme...");

        for (int i = 0; i < 10; i++) {

            //Nouvelle instance de Talkative avec son id unique initialisé
            Talkative talk = new Talkative(i);

            // Création d'une nouvelle instance de Thread
            Thread thread = new Thread(talk);

            System.out.println("Thread d'ID : " + i);

            // Appel de la méthode start() sur chacun des objet de type thread
            thread.start();
        }
    }
}