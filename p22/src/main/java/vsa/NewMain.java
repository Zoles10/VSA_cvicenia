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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
}
