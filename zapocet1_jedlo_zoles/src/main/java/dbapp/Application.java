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
import javax.persistence.TypedQuery;

/**
 *
 * @author edu
 */
public class Application {
    
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
    public static EntityManager em = emf.createEntityManager();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(noveJedlo("Gulas", 2.50));
        System.out.println(noveJedlo("Hranolky", null));
        //System.out.printl();
    }
    
    public static Long noveJedlo(String nazov, Double cena) {
        try {
            Jedlo newJ = new Jedlo();
            newJ.setName(nazov);
            newJ.setPrice(cena);
            em.getTransaction().begin();
            em.persist(newJ);
            em.getTransaction().commit();
            return newJ.getId();
        } catch (Exception e) {
            return null;
        }
    }
    
    public static List<Jedlo> jedloBezCeny() {
        TypedQuery<Jedlo> q = em.createNamedQuery("Jedlo.findWithoutPrice", Jedlo.class);
        List<Jedlo> dbj = q.getResultList();
        return dbj;
    }
    
    public static void odstranJedla() {
        em.getTransaction().begin();
        List<Jedlo> meals = jedloBezCeny();
        for (Jedlo j : meals) {
            em.remove(j);
        }
        em.getTransaction().commit();
    }
}