/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.studioblueplanet.moviedownload;

import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author jorgen
 */
public class FolderCrossCheck
{
    private final static    Logger                              LOGGER = LogManager.getLogger(Movies.class);

    private                 HashMap<String,FolderContents>      dirList;
    
    public FolderCrossCheck()
    {
        dirList=new HashMap<>();
    }
    
    public void crossCheck(List<Movie> movies)
    {
        for(Movie movie : movies)
        {
            String folderName=movie.getFolder();
            String subfolderName=movie.getSubfolder();
            
            FolderContents folderContents=dirList.get(folderName);
            if (folderContents==null)
            {
                folderContents=new FolderContents();
                dirList.put(folderName, folderContents);
            }
            if (subfolderName!=null && !subfolderName.equals(""))
            {
                folderContents.addSubfolder(subfolderName);
            }
            else
            {
                folderContents.increaseEmptySubfolderCount();
            }
        }
        report();
    }
    
    /**
     * Print statistics per unique folder
     */
    public void report()
    {
        HashMap<String, FolderContents> temp
                    = dirList.entrySet()
                             .stream()
                             .sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
                  .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      Map.Entry::getValue,
                      (e1, e2) -> e1, LinkedHashMap::new));    

        LOGGER.info("Folder                                              Count  Empty");
        for (String key   : temp.keySet()) 
        {
             FolderContents contents = temp.get(key);
             LOGGER.info(String.format("%50s, %5d, %5d", key, 
                                                         contents.getSubfolderCount(), 
                                                         contents.getEmptySubfolderCount()));
        }    
    }
    
    /**
     * Returns the number of unique folders found
     * @return The number of folders
     */
    public int getFolderCount()
    {
        return dirList.size();
    }
}
