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
 * @author Zoles
 */
public class Application {

    public static void main(String[] args) {
        System.out.println(novyTovar("Auto", 10.0));
        System.out.println(novyTovar("Auto", 10.0));
        System.out.println(novyTovar("Auto-1", 9.0));
        System.out.println(novyTovar("Auto1", 12.0));
        System.out.println(novyTovar("Auto2", 13.0));
        System.out.println(novyTovar("Auto3", 14.0));
        List<Tovar> tovaries = drahsiTovar();
        tovaries.forEach(tovar -> System.out.println("Meno: " + tovar.getNAZOV() + " Cena:" + tovar.getCENA()));
        znizCenu();
        
    }

    public static Long novyTovar(String nazov, Double cena) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsa_zapocet1_tovar_jar_1.0-SNAPSHOTPU");
        EntityManager em = emf.createEntityManager();
        Tovar novyTovarr = new Tovar();
        novyTovarr.setCENA(cena);
        novyTovarr.setNAZOV(nazov);
        TypedQuery<Tovar> q1 = em.createQuery("select t from Tovar t where t.NAZOV = :nazov", Tovar.class);
        q1.setParameter("nazov", nazov);
        if (q1.getResultList().size() != 0) {
            return null;
        }
        em.getTransaction().begin();
        try {
            em.persist(novyTovarr);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return null;
        }
        return novyTovarr.getId();
    }

    public static List<Tovar> drahsiTovar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsa_zapocet1_tovar_jar_1.0-SNAPSHOTPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Tovar> q = em.createQuery("select t from Tovar t where t.CENA > 10.0", Tovar.class);
        return q.getResultList();
    }

    public static void znizCenu() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsa_zapocet1_tovar_jar_1.0-SNAPSHOTPU");
    EntityManager em = emf.createEntityManager();
    TypedQuery<Tovar> q = em.createQuery("SELECT t FROM Tovar t WHERE t.CENA > 10.0", Tovar.class);
    List<Tovar> draheTovary = q.getResultList();
    em.getTransaction().begin();
    try {
        for (Tovar tovar : draheTovary) {
            tovar.setCENA(tovar.getCENA() * 0.5);
            em.merge(tovar); 
        }
        em.getTransaction().commit(); 
    } catch (Exception e) {
        e.printStackTrace();
        em.getTransaction().rollback(); 
    } finally {
        em.close(); 
    }
}


}
