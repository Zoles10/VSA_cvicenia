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
    
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
    public static EntityManager em = emf.createEntityManager();
    
  public static void main(String[] args) {
      // Uloha 1 
      novyTovar("notebook", 1000.00);
      novyTovar("auto", 10000.00);
      // novyTovar("notebook", 1000.00);        // return null pri zhode nazvu(unique=true)
      novyTovar("jedlo", 5.00);
      novyTovar("pero", 1.00);
      
      // Uloha 2
      List<Tovar> checkZoznam = drahsiTovar();
      for(Tovar t : checkZoznam) {
          System.out.println(t.getNazov() + "--> cena:" + t.getCena());
      }
      
      // Uloha 3
      znizCenu();
      
  }
  
  public static Long novyTovar(String nazov, Double cena) {
      try {
          Tovar newTovar = new Tovar();
          newTovar.setNazov(nazov);
          newTovar.setCena(cena);
          em.getTransaction().begin();
          em.persist(newTovar);
          em.getTransaction().commit();
          return newTovar.getId();

      } catch(Exception e) {
          return null;
      }
  }
  
  public static List<Tovar> drahsiTovar() {
      TypedQuery<Tovar> query = em.createQuery("select t from Tovar t where t.cena > 10", Tovar.class);
      List<Tovar> zoznam = query.getResultList();
      return zoznam;
  }
  
  public static void znizCenu() {
      List<Tovar> zoznam = drahsiTovar();
      em.getTransaction().begin();
      for(Tovar t : zoznam) {
          t.setCena(t.getCena() * 0.5);
      }
      em.getTransaction().commit();
  }
}
