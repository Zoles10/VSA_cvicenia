/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsa;

import entities.Person;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author edu
 */
public class NewMain {

    public static void main(String[] args) {
        Person p;
        p = new Person();
        p.setName("Fero");
//        p.setBorn(new Date());
        p.setBorn(LocalDate.now());

        p.setMarried(false);
        p.setSalary(800.0);
        persist(p);

        p = new Person();
        p.setName("Eva");
//        p.setBorn(toDate(1997, 3, 10));
        p.setBorn(LocalDate.of(1997, 3, 10));

        p.setMarried(true);
        p.setSalary(1200.0);
        persist(p);

        p = new Person();
        p.setName("Adam");
        persist(p);

        increaseAllSalary();
        allPeopleUnder(1000.0);
        sumOfSalaries();
    }

    private static Date toDate(int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m, d, 0, 0, 0);
        return calendar.getTime();
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

    // pomocou JPQL-dotazu načítajte všetky osoby, zvýšte im plat o 100 a zapíše zmenu do databázy.
    public static void increaseAllSalary() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        //da sa aj takto
        //Query query = em.createNativeQuery("SELECT * FROM PERSON", Person.class);
        //alebo pouzit named
        TypedQuery<Person> q = em.createNamedQuery("Person.findAll", Person.class);
        List<Person> pl = q.getResultList();

        for (Person person : pl) {
            person.setSalary(person.getSalary() + 100);
            em.getTransaction().begin();
            try {
                em.persist(person);
                em.getTransaction().commit();
            } catch (Exception e) {
                e.printStackTrace();
                em.getTransaction().rollback();
            }
        }
    }

    //- pomocou JPQL-dotazu zistite mená osôb, ktoré majú plat nižší ako 1000.0.
    public static void allPeopleUnder(Double salary) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE p.salary < :s", Person.class);
        q.setParameter("s", salary);
        List<Person> p = q.getResultList();
        for (Person person : p) {
            System.out.println(person.getName());
        }
    }

    //- pomocou JPQL-dotazu zistite celkovú sumu platov všetkých osôb
    public static void sumOfSalaries() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> q = em.createNamedQuery("Person.findAll", Person.class);
        List<Person> pl = q.getResultList();
        Double sum = 0.0;
        for(Person person : pl){
            sum +=person.getSalary();
        }
        System.out.println(sum);
    }

}
