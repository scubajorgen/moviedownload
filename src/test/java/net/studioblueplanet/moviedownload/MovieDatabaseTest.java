/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jorgen
 */
public class MovieDatabaseTest
{
    
    public MovieDatabaseTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
        Configurator.setRootLevel(Level.DEBUG);
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getInstance method, of class MovieDatabase.
     */
    @Test
    public void testGetInstance()
    {
        System.out.println("getInstance");
        MovieDatabase expResult = null;
        MovieDatabase result = MovieDatabase.getInstance();
        assertNotNull(result);
        MovieDatabase result2 = MovieDatabase.getInstance();
        assertEquals(result, result2);
        
    }

    /**
     * Test of enrichMovie method, of class MovieDatabase.
     */
    @Test
    public void testEnrichMovie()
    {
        System.out.println("enrichMovie");
        Movie movie = new Movie();
        movie.setTitle("ghost in the shell");
        movie.setYear(1995);
        MovieDatabase instance = MovieDatabase.getInstance();
        instance.enrichMovie(movie);
        assertEquals(3, movie.getGenre().size());
        assertEquals("Action", movie.getGenre().get(0));
        assertEquals("Animation", movie.getGenre().get(1));
        assertEquals("Science Fiction", movie.getGenre().get(2));
        assertEquals("Ghost in the Shell", movie.getTitleRetrieved());
        assertEquals("GHOST IN THE SHELL", movie.getOriginalTitle());
        assertEquals("1995-11-18", movie.getReleaseDate());
    }
    
}
