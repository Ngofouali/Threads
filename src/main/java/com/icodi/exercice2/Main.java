package com.icodi.exercice2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        //Création d'un tableau d'entiers
        int tailleTableau = 10000;

        // Utilise le nombre de cœurs disponibles
        int nombreThreads = Runtime.getRuntime().availableProcessors();

        System.out.println("Configuration : Taille tableau = " + tailleTableau + ", Nombre de threads = " + nombreThreads);

        // --- Création et initialisation du tableau ---
        System.out.println("Création et initialisation du tableau...");
        int[] tableau = new int[tailleTableau];
        // Remplir le tableau (par exemple, avec des 1 pour une vérification facile)
        for (int i = 0; i < tailleTableau; i++) {
            tableau[i] = (int)(Math.random() * 10);
        }
        System.out.println("Tableau initialisé.");

        // --- Création du pool de threads ---
        ExecutorService executor = Executors.newFixedThreadPool(nombreThreads);

        // --- Division du travail et création des tâches ---
        List<Sommeur> taches = new ArrayList<>();
        List<Future<?>> futures = new ArrayList<>();
        int taillePortion = tailleTableau / nombreThreads;
        int indexDebut = 0;

        System.out.println("Création et soumission des tâches...");
        for (int i = 0; i < nombreThreads; i++) {
            int indexFin = (i == nombreThreads - 1)
                    ? tailleTableau - 1
                    : indexDebut + taillePortion - 1;

            if (indexFin < indexDebut && i == nombreThreads - 1) {
                indexFin = tailleTableau - 1;
            }
            if (indexDebut >= tailleTableau) break;

            Sommeur sommeur = new Sommeur(tableau, indexDebut, indexFin);
            taches.add(sommeur);
            Future<?> future = executor.submit(sommeur);
            futures.add(future);

            // System.out.println("Tâche créée pour la plage [" + indexDebut + ", " + indexFin + "]"); // Pour débogage
            indexDebut = indexFin + 1; // Préparer le début de la prochaine portion
        }

        // --- Attente de la fin de toutes les tâches ---
        System.out.println("Attente de la fin des calculs...");
        try {
            for (Future<?> future : futures) {
                // Bloque jusqu'à ce que la tâche associée soit terminée
                future.get();
            }
            System.out.println("Toutes les tâches sont terminées.");
        } catch (Exception e) {
            // Gestion des exceptions (InterruptedException, ExecutionException)
            System.err.println("Une erreur est survenue pendant l'attente des tâches:");
            e.printStackTrace();
            // Arrêter proprement en cas d'erreur
            executor.shutdownNow();
            return;
        }

        // --- Agrégation des résultats ---
        System.out.println("Agrégation des sommes partielles...");
        long sommeTotale = 0;
        for (Sommeur tache : taches) {
            sommeTotale += tache.getSomme();
        }

        // --- Arrêt du pool de threads ---
        System.out.println("Arrêt du pool de threads...");
        executor.shutdown();
        try {
            // Attend un peu que les tâches en cours se terminent proprement
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Le pool de threads ne s'est pas arrêté à temps, arrêt forcé.");
                executor.shutdownNow(); // Force l'arrêt si le délai est dépassé
            }
        } catch (InterruptedException e) {
            System.err.println("Attente de l'arrêt interrompue, arrêt forcé.");
            executor.shutdownNow();

            // Redéfinir le statut d'interruption
            Thread.currentThread().interrupt();
        }

        // --- Affichage du résultat final ---
        System.out.println("-------------------------------------------");
        System.out.println("La somme totale calculée (parallèle) est : " + sommeTotale);
        System.out.println("-------------------------------------------");


        // --- Vérification ---
        System.out.println("Calcul de la somme en séquentiel pour vérification...");
        long sommeVerification = 0;
        long debutSeq = System.nanoTime();
        for (int valeur : tableau) {
            sommeVerification += valeur;
        }
        long finSeq = System.nanoTime();
        System.out.println("La somme de vérification (séquentielle) est : " + sommeVerification);
        System.out.println("Temps séquentiel: " + TimeUnit.NANOSECONDS.toMillis(finSeq - debutSeq) + " ms");

        if (sommeTotale == sommeVerification) {
            System.out.println("Vérification réussie ! Les sommes correspondent.");
        } else {
            System.err.println("ERREUR DE VÉRIFICATION ! Les sommes diffèrent.");
        }
    }
}
