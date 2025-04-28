package com.icodi.exercice1;

public class Talkative implements Runnable {
    private int id;

    public Talkative(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {

        // affichage 100 fois de la valeur de l'attribut "id"
        for (int i = 0; i < 100; i++) {
            System.out.println("Thread N° [" + id + "]: " + id + " Itération " + (i + 1));
        }
        System.out.println("Le Thread N° [" + id + "] a terminé");
    }
}
