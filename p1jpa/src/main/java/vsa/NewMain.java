/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author edu
 */
public class NewMain {

  public static void main(String[] args) {
      citajKnihy();
//      zmenaCien();
//      najdiKnihu();
      vytvorKnihu();
  
  }

    /* Ilustruje 
        EntityManagerFactory 
        EntityManager 
        createNativeQuery
     */
    public static void citajKnihy() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

        Query q = em.createNativeQuery("select * from kniha", Kniha.class);
        List<Kniha> knihy = q.getResultList();

        for (Kniha k : knihy) {
            System.out.println("" + k);
        }
    }

    /* Zlava na vsetky knihy - ilustruje:
        modifikaciu nacitaneho (manazovaneho) objektu kniha 
        otvorenie a commit transakcie
    
    Skuste najprv bez pouzitia transakcie    
     */
    public static void zmenaCien() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

        Query q = em.createNativeQuery("select * from kniha", Kniha.class);
        List<Kniha> knihy = q.getResultList();

        em.getTransaction().begin();
        for (Kniha k : knihy) {
            k.setCena(k.getCena() * 0.8);
        }
        em.getTransaction().commit();

        for (Kniha k : knihy) {
            System.out.println("" + k);
        }
    }

    /* Vyhladanie knihy podla kluca - ilustruje: 
        find
    Skuste tiez 
        vyhladat neexiatujucu knihu
     */
    public static void najdiKnihu() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

        Kniha k = em.find(Kniha.class, "Pipi");
        System.out.println("" + k);
    }
    
    /* 
        persist
     */
    public static void vytvorKnihu() {
        Kniha k = new Kniha();
        k.setISBN("1234");
        k.setNazov("Pipi");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(k);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }    
}
