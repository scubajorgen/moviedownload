/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.studioblueplanet.moviedownload;

import java.io.File;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author jorgen
 */
public class FolderCrossCheck
{
    private final static    Logger                              LOGGER = LogManager.getLogger(Movies.class);

    private                 Map<String,FolderContents>          dirList;
    
    public FolderCrossCheck()
    {
        dirList=new HashMap<>();
    }
    
    /**
     * Do a cross check between the movie records and the directory where the
     * corresponding movies are stored
     * @param movies The movie database
     * @param basePath The base directory where the movies are stored
     */
    public void crossCheck(List<Movie> movies, String basePath)
    {
        findMissingFolders(movies, basePath);
        findMissingRecords(movies, basePath);
        report();
    }
    
    /**
     * Find incomplete records
     * @param movies The movie database
     * @param basePath The base directory where the movies are stored
     */
    private void findMissingFolders(List<Movie> movies, String basePath)
    {
        for(Movie movie : movies)
        {
            String folderName   =movie.getFolder();
            String subfolderName=movie.getSubfolder();
            
            FolderContents folderContents=dirList.get(folderName);
            if (folderContents==null)
            {
                folderContents=new FolderContents();
                dirList.put(folderName, folderContents);
            }
            if (subfolderName!=null && !subfolderName.equals(""))
            {
                File file=new File(basePath+folderName+"/"+subfolderName);
                if (file.exists())
                {
                    folderContents.addSubfolder(subfolderName);
                }
                else
                {
                    folderContents.addNonFoundFolder(movie.getTitle());
                }
            }
            else
            {
                folderContents.addNonSpecifiedFolder(movie.getTitle());
            }
        }        
    }
    
    /**
     * Find directories that are not listed in the movie database.
     * Basically only the directories/folders are scanned that are present
     * as 'folder' in the database; hence not every directory in the basePath
     * are scanned (!)
     * @param movies The movie database
     * @param basePath The base directory where the movies are stored
     */
    private void findMissingRecords(List<Movie> movies, String basePath) 
    {
       for (String key   : dirList.keySet()) 
        {
            FolderContents contents    = dirList.get(key);

            String  path                =basePath+key+"/";

            File dirFile=new File(path);

            if (dirFile!=null && dirFile.listFiles()!=null)
            {
                List<String> foundSubdirs= 
                        Stream.of(dirFile.listFiles())
                        .filter(file -> file.isDirectory() && !file.getName().startsWith("_"))
                        .map(File::getName)
                        .collect(Collectors.toList());
                
                for (String dir : foundSubdirs)
                {
                    boolean found=false;
                    for (String dir2 : contents.getCorrectMovies())
                    {
                        if (dir.equals(dir2))
                        {
                            found=true;
                        }
                    }
                    if (!found)
                    {
                        contents.addNonFoundRecord(dir);
                    }
                }
            }
        }            
    }
    
    /**
     * Print statistics per unique folder
     */
    public void report()
    {
        int sumCorrect=0;
        int sumSpec   =0;
        int sumNoDir  =0;
        int sumOrphant=0;
        HashMap<String, FolderContents> temp 
                    = dirList.entrySet()
                             .stream()
                             .sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
                             .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));    

        LOGGER.info("_____________________________________________________________");
        LOGGER.info("\nCOMPLETE RECORDS: ");
        for (String key   : temp.keySet()) 
        {
             FolderContents contents = temp.get(key);
             List<String> movies=contents.getCorrectMovies();
             sumCorrect+=movies.size();
             for (String movie: movies)
             {
                 LOGGER.info("{} - {}", key, movie);
             }
        }    
        
        LOGGER.info("_____________________________________________________________");
        LOGGER.info("\nRECORDS WITH NON SPECIFIED SUBFOLDER: ");
        for (String key   : temp.keySet()) 
        {
             FolderContents contents = temp.get(key);
             List<String> movies=contents.getNonSpecifiedFolders();
             sumSpec+=movies.size();
             for (String movie: movies)
             {
                  LOGGER.info("{} - {}", key, movie);
             }
        }    
        
        LOGGER.info("_____________________________________________________________");
        LOGGER.info("\nFOLDERS NOT FOUND FOR MOVIES: ");
        for (String key   : temp.keySet()) 
        {
             FolderContents contents = temp.get(key);
             List<String> movies=contents.getNonFoundFolders();
             sumNoDir+=movies.size();
             for (String movie: movies)
             {
                 LOGGER.info("{} - {}", key, movie);
             }
        }    
        
        LOGGER.info("_____________________________________________________________");
        LOGGER.info("\nNOT FOUND RECORDS FOR FOLDERS: ");
        for (String key   : temp.keySet()) 
        {
             FolderContents contents = temp.get(key);
             List<String> folders=contents.getNonFoundRecords();
             sumOrphant+=folders.size();
             for (String folder: folders)
             {
                 LOGGER.info("{} - {}", key, folder);
             }
        }    

        LOGGER.info("_____________________________________________________________");
        LOGGER.info("Folder                                              Count   spec    dir  orphant");
        for (String key   : temp.keySet()) 
        {
             FolderContents contents = temp.get(key);
             LOGGER.info(String.format("%50s, %5d, %5d, %5d, %5d", key, 
                                                         contents.getCorrectRecordCount(), 
                                                         contents.getWrongSpecCount(),
                                                         contents.getNonExistingDirectoryCount(),
                                                         contents.getOrphantDirectoryCount()));
        }    
        LOGGER.info("                                    ________________________");
        LOGGER.info(String.format("%50s, %5d, %5d, %5d, %5d", "TOTAL", 
                                                    sumCorrect, 
                                                    sumSpec,
                                                    sumNoDir,
                                                    sumOrphant));
        LOGGER.info("Records processed: {}", sumCorrect+sumSpec+sumNoDir);

    }
    
    /**
     * Returns the number of unique folders found
     * @return The number of folders
     */
    public int getFolderCount()
    {
        return dirList.size();
    }
    
    public Map getFolderMap()
    {
        return dirList;
    }
}
