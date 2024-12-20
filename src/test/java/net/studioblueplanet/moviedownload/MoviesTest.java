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
    public void testReadMoviesExcel()
    {
        System.out.println("readMoviesExcel");
        Movies instance = new Movies();
        instance.readMoviesExcel("src/test/resources/movies.xlsx");
        List<Movie> movies=instance.getMovies();
        assertEquals(7, movies.size());
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
        assertEquals("Movie not updated", movies.get(0).getDatabaseRemark());
        assertEquals(1, movies.get(0).getId().intValue());
        assertEquals("movie", movies.get(0).getMediaType());
    
    }

    /**
     * Test of readMoviesExcel method, of class Movies.
     */
    @Test
    public void testEnrichMovies()
    {
        System.out.println("enrichMovies");
        Movies instance = new Movies();
        instance.readMoviesExcel("src/test/resources/movies.xlsx");
        List<Movie> movies=instance.getMovies();
        
        instance.enrichMovies(null, false);
        
        assertEquals(7, movies.size());
        Movie newMovie=movies.get(5);

        // This data was returned from the Movie Database 2024-11-17 and it may change over time, letting the test fail
        // To do: improve test to best practices
        assertEquals("In celebration of the release of 63 Up, the 2019 installment of Michael Apted's groundbreaking "+
                     "documentary series that began in 1964, a number of British and American celebrity fans now "+
                     "discuss its impact on popular culture and its lasting legacy. The film also candidly explores "+
                     "pivotal moments of celebrities’ own lives—from school days and first loves to family and fulfilled "+
                     "ambitions—while tapping into changes in social history around topics such as class, education, and "+
                     "parenting.", newMovie.getOverview());
        assertEquals(1, newMovie.getGenre().size());
        assertEquals("Documentary", newMovie.getGenre().get(0));
        assertEquals("John Piper", newMovie.getDirector());
        assertEquals(29, newMovie.getCast().size());
        assertEquals("Joanna Lumley (Narrator (voice))", newMovie.getCast().get(0));
        assertEquals(1.064, newMovie.getPopularity(), 0.001);       // likely to change in future
        assertEquals(7.5, newMovie.getVoteAverage(), 0.1);          // likely to change in future
        assertEquals(2, newMovie.getVoteCount().intValue());        // likely to change in future
        assertEquals("7 Up & Me", newMovie.getOriginalTitle());
        assertEquals("7 Up & Me", newMovie.getTitleRetrieved());
        assertEquals("2019-06-03", newMovie.getReleaseDate());
        assertEquals(607591, newMovie.getId().intValue());
        
        
        assertEquals(9.0, newMovie.getRating(), 0.1);
        assertEquals("Moet nog bekijken", newMovie.getRemark());
        assertEquals("movie", newMovie.getMediaType());
        assertEquals("Movie enriched", newMovie.getDatabaseRemark());
        
        Movie oldMovie=movies.get(3);
        assertEquals("Movie not updated", oldMovie.getDatabaseRemark());
        assertNull(oldMovie.getDirector());
        
        instance.enrichMovies(null, true);
        assertEquals("Movie enriched", oldMovie.getDatabaseRemark());
        assertEquals("Lasse Hallström", oldMovie.getDirector());
    }
    
    /**
     * Test of findSubfolders method, of class Movies.
     */
    @Test
    public void testFindSubfolders()
    {
        System.out.println("findSubfolders");
        Movies instance = new Movies();
        instance.readMoviesExcel("src/test/resources/movies.xlsx");

        List<Movie> movies=instance.getMovies();
        instance.findSubfolderNames("src/test/resources", false);
        assertEquals("subfolder", movies.get(0).getSubfolder());
        assertEquals("A Clockwork Orange (1971)", movies.get(2).getSubfolder());

        movies=instance.getMovies();
        instance.findSubfolderNames("src/test/resources", true);
        assertEquals("Alles is Familie (2012)", movies.get(0).getSubfolder());
        assertEquals("A Clockwork Orange (1971)", movies.get(2).getSubfolder());
    }    
    
    /**
     * Test of findSubfolders method, of class Movies.
     */
    @Test
    public void testCrossCheck()
    {
        System.out.println("crossCheck");
        Movies instance = new Movies();
        instance.readMoviesExcel("src/test/resources/movies.xlsx");
        FolderCrossCheck check=instance.crossCheck("");
        
        assertEquals(4, check.getFolderCount());
    }


    
    
}
