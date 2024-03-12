/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbapp;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author edu
 */
public class Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(novyProdukt("Pocitac", 10.0));
        System.out.println(novyProdukt("Pocitac", 10.0));
        System.out.println(novyProdukt("Auto", null));
        System.out.println(novyProdukt("Myska", null));
        produktyBezCeny();
        zadajCenu();
    }

    public static Long novyProdukt(String nazov, Double cena) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
        EntityManager em = emf.createEntityManager();
        if (!em.createQuery("SELECT p FROM Produkt p WHERE p.nazov = :nazov", Produkt.class)
                .setParameter("nazov", nazov)
                .getResultList()
                .isEmpty()) {
            return null;
        }
        em.getTransaction().begin();
        Produkt novyProd = new Produkt();
        try {
            novyProd.setCena(cena);
            novyProd.setNazov(nazov);
            em.persist(novyProd);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return novyProd.getId();
    }

    public static List<Produkt> produktyBezCeny() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
        EntityManager em = emf.createEntityManager();
        for (Produkt p : em.createQuery("SELECT p FROM Produkt p WHERE p.cena IS null", Produkt.class).getResultList()) {
            System.out.println(p.getNazov());
        }
        return em.createQuery("SELECT p FROM Produkt p WHERE p.cena IS null", Produkt.class).getResultList();
    }

    public static void zadajCenu() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
        EntityManager em = emf.createEntityManager();
        List<Produkt> produktList = em.createQuery("SELECT p FROM Produkt p WHERE p.cena IS null", Produkt.class).getResultList();
        for (Produkt p : produktList) {
            p.setCena(10.0);
            em.getTransaction().begin();
            try {
                em.persist(p);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }
        em.close();
    }
}
