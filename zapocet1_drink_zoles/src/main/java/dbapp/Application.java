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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        novyNapoj("7up", 10.0);
        novyNapoj("7up", 10.0);
        novyNapoj("CocaCola", 10.0);
        novyNapoj("Pepsi", 10.0);
        novyNapoj("Kofola", 10.0);
        novyNapoj(null, 11.0);
        novyNapoj(null, 12.0);
        novyNapoj(null, 13.0);
        napojeBezNazvu();
        odstranNapoje();

    }

//    Metóda vytvorí v tabuľke DRINK nový záznam s názvom a cenou zadanými v argumentoch a vráti
//jeho ID.
//Pozor. keďže stĺpec NAME je unikátny, mala by metóda ešte pred vložením nového záznamu overiť,
//či sa už v tabuľke nápoj s rovnakým názvom nenachádza. Ak v nej už taký nápoj je, nový záznam
//nevloží a vráti null.
    public static Long novyNapoj(String nazov, Double cena) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        Napoj novyDrink = new Napoj();
        novyDrink.setName(nazov);
        novyDrink.setPrice(cena);
        em.getTransaction().begin();

        if (!em.createQuery("SELECT n FROM Napoj n WHERE n.name = :nazov", Napoj.class).setParameter("nazov", nazov).getResultList().isEmpty()) {
            return null;
        }

        try {
            em.persist(novyDrink);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Existuje meno");
        } finally {
        }
        return novyDrink.getId();

    }
    //Metóda vráti zoznam všetkých nápojov, ktoré nemajú zadaný názov.
    //Na vyhľadanie týchto nápojov v databáze použite JPQL dotaz.

    public static List<Napoj> napojeBezNazvu() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Napoj> q = em.createQuery("SELECT n FROM Napoj n WHERE n.name = null", Napoj.class);
        for (Napoj n : q.getResultList()) {
            System.out.println(n.getPrice());
        }
        return q.getResultList();
    }

    //Metóda vyhľadá všetky nápoje bez názvu a odstráni ich z databázy
    public static void odstranNapoje() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Napoj> q = em.createQuery("SELECT n FROM Napoj n WHERE n.name = null", Napoj.class);
        for (Napoj n : q.getResultList()) {
            em.getTransaction().begin();
            try {
                em.remove(n);
                em.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Existuje meno");
            }
        }
        em.close();
    }

}
