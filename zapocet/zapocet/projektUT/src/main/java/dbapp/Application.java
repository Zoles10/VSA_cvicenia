S/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbapp;

import static java.sql.Types.NULL;
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
    public static void main(String[] args) {
    novyNapoj("Mohito", 5.22);
    novyNapoj("KOfola", 1.5);
    novyNapoj(null,1.5);
    napojeBezNazvu();
    odstranNapoje();
    }
    
    public static Long novyNapoj(String nazov, Double cena){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Napoj> napojN = em.createQuery("SELECT n FROM Napoj n WHERE n.NAME = :nazov",Napoj.class);
        napojN.setParameter("nazov", nazov);
        
        if(napojN.getResultList().isEmpty() == true){
            Napoj n = new Napoj();
            n.setNAME(nazov);
            n.setPRICE(cena);

            em.getTransaction().begin();
            em.persist(n);
            em.getTransaction().commit();

            em.close();
            return n.getID();
        }
        else
            return null;
            
        
        
    }
    
    public static List<Napoj> napojeBezNazvu(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
        EntityManager em = emf.createEntityManager();
        
        List<Napoj>napojl = em.createQuery("SELECT n FROM Napoj n WHERE n.NAME is NULL").getResultList();
        
       if(napojl.isEmpty() == true){
            return null;
        }
       else
        return napojl;
    }
    
    public static void odstranNapoje(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
        EntityManager em = emf.createEntityManager();
        
        List<Napoj>napojl = em.createQuery("SELECT n FROM Napoj n WHERE n.NAME=NULL").getResultList();
        
        for(Napoj n : napojl){
           em.getTransaction().begin();
           em.remove(n);
           em.getTransaction().commit();
        }
        em.close();
    }

    public void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
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
    
}
