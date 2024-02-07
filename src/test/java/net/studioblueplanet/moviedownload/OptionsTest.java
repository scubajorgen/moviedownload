/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

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
public class OptionsTest
{
    
    public OptionsTest()
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
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of argsParser method, of class Options.
     */
    @Test
    public void testArgsParser()
    {
        System.out.println("argsParser");
        String[] args = {"-o", "true", 
                         "-a", "false",
                         "-c", "crosscheck",
                         "-f", "moviesfile.xlsx",
                         "-b", "hoi",
                         "-k", "apikey",
                         "-m", "testdir"};
        Options result = Options.argsParser(args);
        assertEquals("crosscheck"       , result.command);
        assertEquals(true               , result.forceOverwrite);
        assertEquals(false              , result.processAll);
        assertEquals("moviesfile.xlsx"  , result.filename);
        assertEquals("apikey"           , result.apiKey);
        assertEquals("hoi"              , result.backupFilename);
        assertEquals("testdir/"         , result.movieDirectory);
        
        String[] emptyArgs= new String[0];
        Options result2 = Options.argsParser(emptyArgs);
        assertEquals("enrich"           , result2.command);
        assertEquals(true               , result2.forceOverwrite);
        assertEquals(false              , result2.processAll);
        assertNull  (result2.apiKey);
        
        String[] incompleteArgs ={"-c", "enrich", "-a"};
        Options result3 = Options.argsParser(incompleteArgs);
        assertNull(result3);
    }
    
}
