/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.zapocet1_knihy_zoles;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Zoles
 */
public class Projekt {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
    private static EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        updateBook("1234-5678", "Effective Java", 45.0);
        Map<String, Double> priceList = new HashMap<>();
        priceList.put("1234-5678", 40.0); 
        priceList.put("9876-5432", 55.0);
        updatePriceList(priceList);
    }

    public static boolean updateBook(String isbn, String title, Double price) {
        Book bookFromDB = em.find(Book.class, isbn);
        if (bookFromDB == null) {
            Book newBook = new Book();
            newBook.setISBN(isbn);
            newBook.setPrice(price);
            newBook.setTitle(title);
            persist(newBook);
        } else {

            if (bookFromDB.getTitle() == null && title != null) {
                bookFromDB.setTitle(title);
            }
            if (bookFromDB.getTitle() != null && title != null) {
                return false;
            }

            if (price != null) {
                bookFromDB.setPrice(price);
            }
            persist(bookFromDB);
            return true;
        }
        return true;
    }

    public static void persist(Object object) {
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    public static void updatePriceList(Map<String, Double> priceList) {
        for (Map.Entry<String, Double> entry : priceList.entrySet()) {
            String isbn = entry.getKey();
            Double price = entry.getValue();
            updateBook(isbn, null, price);
        }
    }
}
