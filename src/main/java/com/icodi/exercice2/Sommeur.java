package com.icodi.exercice2;

public class Sommeur implements Runnable{
    private final int[] tableau;
    private final int debut;
    private final int fin;
    private long sommePartielle;


    public Sommeur(int[] tableau, int debut, int fin) {
        this.tableau = tableau;
        this.debut = debut;
        this.fin = fin;
        this.sommePartielle = 0;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " calcule de " + debut + " à " + fin);
        long sommeLocale = 0; // Variable locale pour le calcul
        for (int i = debut; i <= fin; i++) {
            sommeLocale += this.tableau[i];
        }
        this.sommePartielle = sommeLocale; // Stocke le résultat final

        System.out.println(Thread.currentThread().getName() + " a terminé. Somme partielle = " + this.sommePartielle);
    }

    public long getSomme(){
        return this.sommePartielle;
    }


}
