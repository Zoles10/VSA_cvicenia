/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsa;

import entities.Person;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author edu
 */
public class NewMain {

    // Ilustruje generovany kluc
    public static void main(String[] args) {
        Person p;
        p = new Person();
        p.setName("Fero");
        p.setId(1231L);
        persist(p);
    }

    private static void persist(Object object) {
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

    // Nasledujuce programy ilustruju persistence context 
    // a managovane objekty
    public static void main2(String[] args) {
        Person p;
        p = new Person();
        p.setName("Fero");

        persist(p);

        long id = p.getId();
        System.out.println("id: " + id);

        Person p2 = findById(id);
        System.out.println("p == p2 :     " + (p == p2));
        System.out.println("p.equals(p2): " + p.equals(p2));
    }

    private static Person findById(long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        em.close();
        return p;
    }

    // To iste co v main2 ale s pouzitim toho isteho entity managera
    public static void main3(String[] args) {
        Person p;
        p = new Person();
        p.setName("Fero");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();    // skuste zakomentovat
        em.persist(p);
        em.getTransaction().commit();   // skuste zakomentovat

        long id = p.getId();
        System.out.println("id: " + id);

        Person p2 = em.find(Person.class, id);
        System.out.println("p == p2 :     " + (p == p2));
        System.out.println("p.equals(p2): " + p.equals(p2));

        em.close();
    }

    // ilustracia clear a detach
    public static void main4(String[] args) {
        Person p;
        p = new Person();
        p.setName("Fero");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();    
        em.persist(p);
        em.getTransaction().commit();   

        long id = p.getId();
        
        em.clear();     // em.detach(p);

        Person p2 = em.find(Person.class, id);
        System.out.println("p == p2 :     " + (p == p2));
        System.out.println("p.equals(p2): " + p.equals(p2));

        em.close();
    }
    
    // ilustruje refresh
    public static void main5(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

        Person p = em.find(Person.class, 1L);      // pozriet kluc v DB
        System.out.println("" + p.getName() + " " + p.getSalary());

        p.setSalary(3000.0);
        System.out.println("" + p.getName() + " " + p.getSalary());

        em.refresh(p);
        System.out.println("" + p.getName() + " " + p.getSalary());

        em.close();
    }

    // ilustruje merge
    public static void main6(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        
        // vytvorim kopiu
        Person p = new Person();
        p.setId(1L);
        p.setName("Fero");
        // plat zmenim
        p.setSalary(333.33);

        // persist vyvola unique key violation
//        em.getTransaction().begin();
//        em.persist(p2);
//        em.getTransaction().commit();
//        System.out.println(p2);
        
        // merge
        em.getTransaction().begin();
        Person p2 = em.merge(p);
        em.getTransaction().commit();
        System.out.println(p2);
        
        System.out.println("p == p2 :     " + (p == p2));

        em.close();
    }

    // ilustruje getreference a remove
    // zapnite sql-log a pozrite rozdiel medzi find a getReference
    public static void main7(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        
        // nemusi inicializovat vsetky datove cleny 
        Person p = em.getReference(Person.class, 1L);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();

        em.close();
    }
}
