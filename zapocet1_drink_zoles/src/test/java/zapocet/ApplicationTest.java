/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zapocet;

import dbapp.Application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author edu
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest {
    
    private static int BODY;

    public ApplicationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        BODY = 0;
//        prepareTables();
    }

    @AfterClass
    public static void vysledok() {
        System.out.println("BODY = " + BODY);
    }
    

    @Test
    public void test_01() {
        prepareTables0();
        Long r1 = Application.novyNapoj("P0", 20.50);
        assertNotNull("null returned",r1);
        Long r2 = Application.novyNapoj("P1", 20.50);
        assertNotNull("null returned",r2);        
        BODY +=1;

        
    }

    @Test
    public void test_02() {
        prepareTables0();
        Long r = Application.novyNapoj("P2", 30.50);
        assertNotNull("null returned",r);
        try {
            r = Application.novyNapoj("P2", 33.50);
        } catch (Exception ex) {
            fail("Exception: " + ex.getMessage());
        }
        assertNull(r);

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM DRINK");
            assertTrue("empty result set", rs.next());            
            assertEquals("P2", rs.getString("NAME"));
            assertEquals(30.50, rs.getDouble("PRICE"), 0.01);
             assertFalse("result set to big",rs.next());                
        } catch (SQLException ex) {
            fail("SQLException: " + ex.getMessage());
        }

        BODY +=1;
    }
    
    @Test
    public void test_03() {
        prepareTables0();
        Long r1 = Application.novyNapoj("P3", null);
        assertNotNull("null returned",r1);

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM DRINK");
            assertTrue("empty result set", rs.next());            
            assertEquals("P3", rs.getString("NAME"));
            assertNull(rs.getObject("PRICE"));
             assertFalse("result set to big",rs.next());                
        } catch (SQLException ex) {
            fail("SQLException: " + ex.getMessage());
        }

        BODY +=1;
    }
    
    @Test
    public void test_04() {
        prepareTables0();
        Long r1 = Application.novyNapoj(null, 0.0);
        assertNotNull("null returned",r1);

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM DRINK");
            assertTrue("empty result set", rs.next());            
            assertNull(rs.getObject("NAME"));
            assertFalse("result set to big",rs.next());                
        } catch (SQLException ex) {
            fail("SQLException: " + ex.getMessage());
        }

        BODY +=1;
    }
    
    @Test
    public void test_05() {
        prepareTables1();
        List r = Application.napojeBezNazvu();
        assertEquals(1,r.size());

        BODY +=1;
    }
    
    @Test
    public void test_06() {
        prepareTables2();
        List r = Application.napojeBezNazvu();
        assertTrue("empty result list", r==null || r.isEmpty());

        BODY +=1;
    }
    
    @Test
    public void test_07() {
        prepareTables1();
        Application.odstranNapoje();

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT count(*) FROM DRINK");
            assertTrue("empty result set", rs.next());            
            assertEquals(2, rs.getInt(1));

            rs = s.executeQuery("SELECT price FROM DRINK WHERE id = 1111");
            assertTrue("empty result set", rs.next());            
            rs = s.executeQuery("SELECT price FROM DRINK WHERE id = 1122");
            assertTrue("empty result set", rs.next());            
            rs = s.executeQuery("SELECT price FROM DRINK WHERE id = 1133");
            assertFalse("record not removed", rs.next());            
        } catch (SQLException ex) {
            fail("SQLException: " + ex.getMessage());
        }


        BODY +=1;
    }
        
    @Test
    public void test_08() {
        prepareTables0();
        
        Long r = Application.novyNapoj("P0", 5.50);
        assertNotNull("null returned",r);
        r = Application.novyNapoj("P1", 5.50);
        assertNotNull("null returned",r);        
        r = Application.novyNapoj("P3", null);
        assertNotNull("null returned",r);
        r = Application.novyNapoj(null, null);
        assertNotNull("null returned",r);
        
        Application.odstranNapoje();

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {
            Statement s = con.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT count(*) FROM DRINK");
            assertTrue("empty result set", rs.next());            
            assertEquals(3, rs.getInt(1));

        } catch (SQLException ex) {
            fail("SQLException: " + ex.getMessage());
        }

        BODY +=1;
    }
    
    
    
    
////////////////////////////////////////////////////////////////////////////////    
    static private void dropTable(Statement st, String table) {
        try {
//            System.out.println("drop...");
            st.executeUpdate("DROP TABLE " + table);
        } catch (SQLException ex) {
        }
    }

    static private void createTable(Statement st, String table) {
        try {
//            System.out.println("create...");
            st.executeUpdate("CREATE TABLE " +table+ " (ID BIGINT NOT NULL, PRICE FLOAT, NAME VARCHAR(255) UNIQUE, PRIMARY KEY (ID)) ");
        } catch (SQLException ex) {
        }
    }

    static private void resetSequence(Statement st) {
        try {
//            System.out.println("update...");
            st.executeUpdate("UPDATE SEQUENCE SET SEQ_COUNT = 1 WHERE SEQ_NAME = 'SEQ_GEN'");
        } catch (SQLException ex) {
        }
    }

    static private void prepareTables0() {

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {

            Statement st = con.createStatement();
//            System.out.println("IKO dropping ...");

            dropTable(st, "DRINK");
            createTable(st, "DRINK");
            resetSequence(st);
        } catch (SQLException ex) {
            Logger.getLogger(ApplicationTest.class.getName()).log(Level.INFO, ex.getMessage());
        }
    }
    
    static private void prepareTables1() {

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {

            Statement st = con.createStatement();
//            System.out.println("IKO dropping ...");

            dropTable(st, "DRINK");
            createTable(st, "DRINK");
            resetSequence(st);
            
            st.executeUpdate("INSERT INTO DRINK VALUES (1111, 20.50, 'P1111')");
            st.executeUpdate("INSERT INTO DRINK VALUES (1122, NULL,  'P1122')");
            st.executeUpdate("INSERT INTO DRINK VALUES (1133, NULL,  NULL)");

        } catch (SQLException ex) {
            System.out.println("" + ex.getMessage());
        }
    }
    
    static private void prepareTables2() {

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/edu", "edu", "edu4edu")) {

            Statement st = con.createStatement();
//            System.out.println("IKO dropping ...");

            dropTable(st, "DRINK");
            createTable(st, "DRINK");
            resetSequence(st);
            
            st.executeUpdate("INSERT INTO DRINK VALUES (1111, 20.50, 'P1111')");
            st.executeUpdate("INSERT INTO DRINK VALUES (1122, 20.50, 'P1122')");
            st.executeUpdate("INSERT INTO DRINK VALUES (1133, 20.50, 'P1133')");

        } catch (SQLException ex) {
            System.out.println("" + ex.getMessage());
        }
    }
    
}
