/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsa;

import entities.Kniha;
import entities.Osoba;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author edu
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        create();
        read();
    }

    public static void create() {
        Osoba o1 = new Osoba();
        o1.setMeno("Hopcroft");

        Osoba o2 = new Osoba();
        o2.setMeno("Ullman");

        Osoba o3 = new Osoba();
        o3.setMeno("Aho");
        //vytvorime osoby
        Osoba Huxley = new Osoba();
        Huxley.setMeno("Huxley");
        Osoba Orwell = new Osoba();
        Orwell.setMeno("Orwell");
        //vytvorim knihy
        Kniha BraveNewWorld = new Kniha();
        BraveNewWorld.setNazov("Uvod do teorie automatov");
        Kniha Tisic = new Kniha();
        Tisic.setNazov("1984");

        //osobam priradime spravne
        Huxley.setDielo(new ArrayList<>());
        Huxley.getDielo().add(BraveNewWorld);
        Orwell.setDielo(new ArrayList<>());
        Orwell.getDielo().add(Tisic);

        //kniham vymenime autorob
        BraveNewWorld.setAutor(new ArrayList<>());
        BraveNewWorld.getAutor().add(Orwell);
        Tisic.setAutor(new ArrayList<>());
        Tisic.getAutor().add(Huxley);


        Kniha k1 = new Kniha();
        k1.setNazov("Uvod do teorie automatov");

        Kniha k2 = new Kniha();
        k2.setNazov("Algoritmy a datove struktury");

        k1.setAutor(new ArrayList<>());
        k1.getAutor().add(o1);
        k1.getAutor().add(o2);

        k2.setAutor(new ArrayList<>());
        k2.getAutor().add(o1);
        k2.getAutor().add(o2);
        k2.getAutor().add(o3);

        // pre DB nie je nutna 
        o1.setDielo(new ArrayList<>());
        o1.getDielo().add(k1);
        o1.getDielo().add(k2);

        o2.setDielo(new ArrayList<>());
        o2.getDielo().add(k1);
        o2.getDielo().add(k2);

        o3.setDielo(new ArrayList<>());
        o3.getDielo().add(k2);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {

            em.persist(k1);
            em.persist(k2);
            em.persist(o1);
            em.persist(o2);
            em.persist(o3);
            em.persist(Huxley);
            em.persist(Orwell);
            em.persist(Tisic);
            em.persist(BraveNewWorld);

            em.getTransaction().commit();
            em.clear();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

    }

    public static void read() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        Osoba o = em.find(Osoba.class, 333L);
//        System.out.println(k.getNazov());
//        System.out.println(k.getAutor().size());
    }

    public static void createCv2() {
        Osoba o1 = new Osoba();
        o1.setMeno("Orwell");

        Osoba o2 = new Osoba();
        o2.setMeno("Huxley");

        Kniha k1 = new Kniha();
        k1.setNazov("1894");

        Kniha k2 = new Kniha();
        k2.setNazov("Brave new World");

        k1.setAutor(new ArrayList<>());
        k1.getAutor().add(o1);

        k2.setAutor(new ArrayList<>());
        k2.getAutor().add(o2);

        o1.setDielo(new ArrayList<>());
        o1.getDielo().add(k2);

        o2.setDielo(new ArrayList<>());
        o2.getDielo().add(k1);


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {

            em.persist(k1);
            em.persist(k2);
            em.persist(o1);
            em.persist(o2);

            em.getTransaction().commit();
            em.clear();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

    }



}