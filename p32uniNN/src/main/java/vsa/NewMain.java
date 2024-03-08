/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsa;

import entities.Kniha;
import entities.Osoba;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author edu
 */
public class NewMain {

    public static void main(String[] args) {
        Osoba autor1 = new Osoba();
        autor1.setMeno("Kernighan");
        Osoba autor2 = new Osoba();
        autor2.setMeno("Ritchey");
        Kniha kniha = new Kniha();
        kniha.setNazov("Jazyk C");
        kniha.setAutori(new ArrayList<>());
        kniha.getAutori().add(autor1);
        kniha.getAutori().add(autor2);
        Kniha kniha2 = new Kniha();
        kniha2.setNazov("jazyk java");
        kniha2.setAutori(new ArrayList<>());
        kniha2.getAutori().add(autor2);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {

            em.persist(kniha);
            em.persist(kniha2);
            em.persist(autor1);
            em.persist(autor2);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        int id = 1;
        Long longId = (long) 1;
        System.out.println("HERE");
        List<Osoba> autori = nájdiAutorov(longId);
        for (Osoba o : autori) {
            System.out.println(o.getMeno());
        }

        System.out.println("Pocet Knih od" + " Kernighan " + pocetKnih("Kernighan"));
        System.out.println("Ritchey Kernighan:");
        List<Kniha> knihy = nájdiKnihy("Kernighan");
        for (Kniha k : knihy) {
            System.out.println(k.getNazov());
        }
        System.out.println("Pocet Knih od" + " Ritchey " + pocetKnih("Ritchey"));
        System.out.println("Ritchey KNIHY:");
        List<Kniha> knihy2 = nájdiKnihy("Ritchey");
        for (Kniha k : knihy2) {
            System.out.println(k.getNazov());
        }

    }

    public static void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static List<Osoba> nájdiAutorov(Long idKnihy) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        Kniha k = em.find(Kniha.class, idKnihy);
        return k.getAutori();
    }

    public static int pocetKnih(String menoAutora) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Osoba> osobaQuery = em.createQuery("SELECT o FROM Osoba o WHERE o.meno = :meno", Osoba.class);
        osobaQuery.setParameter("meno", menoAutora);
        if (osobaQuery.getResultList().isEmpty()) {
            return 0;
        }
        TypedQuery<Kniha> knihaQuery = em.createQuery("SELECT COUNT(k) FROM Kniha k INNER JOIN Osoba o ON k.autori.meno = :meno GROUP BY k.id", Kniha.class);
        knihaQuery.setParameter("meno", menoAutora);
        return knihaQuery.getResultList().size();
    }

    public static List<Kniha> nájdiKnihy(String menoAutora) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Osoba> osobaQuery = em.createQuery("SELECT o FROM Osoba o WHERE o.meno = :meno", Osoba.class);
        osobaQuery.setParameter("meno", menoAutora);
        if (osobaQuery.getResultList().isEmpty()) {
            return null;
        }
        TypedQuery<Kniha> knihyQuery = em.createQuery("SELECT DISTINCT k FROM Kniha k INNER JOIN Osoba o ON k.autori.meno = :meno", Kniha.class);
        knihyQuery.setParameter("meno", menoAutora);
        return knihyQuery.getResultList();
    }

}