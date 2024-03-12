/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vsa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author edu
 */
@Entity
public class Kniha {

    
    
    @Id
    private String ISBN;
    
    //unique urcuje, ze sa nemoze v DB nachadzat 2 rovnake
    @Column(unique = true,nullable = false)
    private String nazov;

    
    //nullable zabrani tomu, aby mal nejaky column 
    // null hodnotu, ale ak pouzijeme double, nie Double
    //tak bude hodnota 0 a teda mie null
    @Column(nullable = false)
    private double cena;
    //V SQL pridanie ISBN stlpca ale nepridanie do classy
    //ALTER TABLE kniha ADD ISBNe VARCHAR; 
    //pridanie autora do classy ale nie do tabulky

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    //@Transient nenamapuje na ziadny column
    //pri pouziti drop-and-create sa vytvori rabulka aj s autorom
    private String autor;

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    @Override
    public String toString() {
        return "Kniha{" + "nazov=" + nazov + ", cena=" + cena + '}';
    }

}
