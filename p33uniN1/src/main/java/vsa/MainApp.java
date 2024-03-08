/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsa;

import entities.Firma;
import entities.Kniha;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author igor
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Firma f = new Firma();
        f.setAdresa("Veda");

        Kniha k = new Kniha();
        k.setNazov("1984-opice");
        Kniha k2 = new Kniha();
        k2.setNazov("parizania");
        k.setVydavatel(f);
        k2.setVydavatel(f);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {

            em.persist(k);
            em.persist(k2);
            em.persist(f);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        pridajKnihu("parizania", "Veda");
        pridajKnihu("Nemci", "Veda");
        pridajKnihu("parizania", "Olomouc");
        pridajKnihu("Dogy", "Macky");
    }

    public static void pridajKnihu(String nazov, String adresa) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Kniha> kq = em.createQuery("SELECT k FROM Kniha k WHERE k.nazov = :nazov", Kniha.class);
        kq.setParameter("nazov", nazov);

        TypedQuery<Firma> fq = em.createQuery("SELECT f FROM Firma f WHERE f.adresa = :adresa", Firma.class);
        fq.setParameter("adresa", adresa);

        if (fq.getResultList().isEmpty()) {
            Firma novaFirma = new Firma();
            novaFirma.setAdresa(adresa);
            System.out.println("Vytvoril som vydavatelstvo");
            em.persist(novaFirma);
            if (!kq.getResultList().isEmpty()) {
                kq.getSingleResult().setVydavatel(novaFirma);
                em.persist(kq.getSingleResult());
            }
        }

        TypedQuery<Firma> fq2 = em.createQuery("SELECT f FROM Firma f WHERE f.adresa = :adresa", Firma.class);
        fq2.setParameter("adresa", adresa);

        if (kq.getResultList().isEmpty()) {
            Kniha novaKniha = new Kniha();
            novaKniha.setNazov(nazov);
            novaKniha.setVydavatel(fq2.getSingleResult());
            em.persist(novaKniha);
        }
        em.getTransaction().commit();
    }

}