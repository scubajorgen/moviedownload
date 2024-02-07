/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class FolderCrossCheckTest
{
    private List<Movie> movies;
    
    
    public FolderCrossCheckTest()
    {
        movies=new ArrayList<>();
        
        Movie movie1=new Movie();                           // Folder, subfolder, directory
        movie1.setTitle("A Clockwork Orange");
        movie1.setYear(1971);
        movie1.setFolder("cult");
        movie1.setSubfolder("A Clockwork Orange (1971)");
        movies.add(movie1);
        
        Movie movie2=new Movie();                           // Folder, no subfolder
        movie2.setTitle("Bohemian Rhapsody");
        movie2.setYear(2018);
        movie2.setFolder("cult");
        movie2.setSubfolder("");
        movies.add(movie2);

        Movie movie3=new Movie();                           // Folder, subfolder, no directory
        movie3.setTitle("The Banshees of Inisherin");
        movie3.setYear(2022);
        movie3.setFolder("cult");
        movie3.setSubfolder("The Banshees of Inisherin (2022)");
        movies.add(movie3);

        Movie movie4=new Movie();                           // Foldedr, subfolder, directory
        movie4.setTitle("Alles is Familie");
        movie4.setYear(2012);
        movie4.setFolder("comedy");
        movie4.setSubfolder("Alles is Familie (2012)");
        movies.add(movie4);

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
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of crossCheck method, of class FolderCrossCheck.
     */
    @Test
    public void testCrossCheck()
    {
        System.out.println("crossCheck");
        String basePath = "src/test/resources/";
        FolderCrossCheck instance = new FolderCrossCheck();
        instance.crossCheck(movies, basePath);
        
        Map<String, FolderContents> folderMap=instance.getFolderMap();
        assertEquals(2, folderMap.size());
        
        assertEquals(1, folderMap.get("cult").getCorrectMovies().size());
        assertEquals("A Clockwork Orange (1971)", folderMap.get("cult").getCorrectMovies().get(0));

        assertEquals(1, folderMap.get("cult").getNonSpecifiedFolders().size());
        assertEquals("Bohemian Rhapsody", folderMap.get("cult").getNonSpecifiedFolders().get(0));

        assertEquals(1, folderMap.get("cult").getNonFoundFolders().size());
        assertEquals("The Banshees of Inisherin", folderMap.get("cult").getNonFoundFolders().get(0));

        assertEquals(3, folderMap.get("cult").getNonFoundRecords().size());
        assertEquals("Bohemian Rhapsody (2018)", folderMap.get("cult").getNonFoundRecords().get(0));
        assertEquals("Chocolat (2000)", folderMap.get("cult").getNonFoundRecords().get(1));
        assertEquals("The Red Turtle (2016)", folderMap.get("cult").getNonFoundRecords().get(2));

        assertEquals(1, folderMap.get("comedy").getCorrectMovies().size());

        assertNull(folderMap.get("nonExistent"));
    }
    
}
