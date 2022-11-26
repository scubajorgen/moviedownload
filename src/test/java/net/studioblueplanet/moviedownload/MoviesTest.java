/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author jorgen
 */
public class MoviesTest
{
    
    public MoviesTest()
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
     * Test of readMoviesExcel method, of class Movies.
     */
    @Test
    @Ignore
    public void testReadMoviesExcel()
    {
        System.out.println("readMoviesExcel");
        Movies instance = new Movies();
        instance.readMoviesExcel("src/test/resources/movies.xslx");
        List<Movie> movies=instance.getMovies();
        assertEquals(5, movies.size());
        assertEquals("Alles is familie", movies.get(0).getTitle());
        assertEquals(2012L, (long)movies.get(0).getYear());
        assertEquals("comedy", movies.get(0).getFolder());
        assertEquals("Finding themselves at a crossroads in their tumultuous "+
                     "lives, the members of a daftly dysfunctional family "+
                     "struggle to sort out their hopes, fears, and expectations.", movies.get(0).getOverview());
        assertEquals("Drama", movies.get(0).getGenre().get(1));
        assertEquals(3, movies.get(0).getGenre().size());
        assertNull(movies.get(0).getDirector());
        assertEquals("Frederico Fellini", movies.get(1).getDirector());
        assertEquals("Rosa Salazar (Alita)", movies.get(0).getCast().get(0));
        assertEquals(5, movies.get(0).getCast().size());
        assertEquals(15.2, movies.get(0).getPopularity(), 0.001);
        assertEquals(12.5, movies.get(0).getVoteAverage(), 0.001);
        assertEquals(3L, (long)movies.get(0).getVoteCount());
        assertEquals(1.0, movies.get(0).getRating(), 0.0001);
        assertEquals("Hoi", movies.get(0).getRemark());
    
    }

    /**
     * Test of readMoviesExcel method, of class Movies.
     */
    @Test
    @Ignore
    public void testEnrichMovies()
    {
        System.out.println("enrichMovies");
        Movies instance = new Movies();
        instance.readMoviesExcel("src/test/resources/movies.xslx");
        List<Movie> movies=instance.getMovies();
        
        instance.enrichMovies(true);
    }
    
}
