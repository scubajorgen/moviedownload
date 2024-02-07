/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.studioblueplanet.moviedownload;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jorgen
 */
public class FolderContents
{
    private final List<String>    correctMovies;
    private final List<String>    nonSpecifiedFolders;
    private final List<String>    nonFoundFolders;
    private final List<String>    nonFoundRecords;
    
    public FolderContents()
    {
        correctMovies       =new ArrayList<>(); // Movies with folder, subfolder and corresponding directory
        nonSpecifiedFolders =new ArrayList<>(); // Movies without folder and/or subfolder
        nonFoundFolders     =new ArrayList<>(); // Movies with folder and subfolder but no corresponding directory
        nonFoundRecords     =new ArrayList<>(); // Directory without records
    }
    
    public void addSubfolder(String subfolder)
    {
        correctMovies.add(subfolder);
    }
    
    public void addNonSpecifiedFolder(String movie)
    {
        nonSpecifiedFolders.add(movie);
    }
    
    public void addNonFoundFolder(String movie)
    {
        nonFoundFolders.add(movie);
    }
    
    public void addNonFoundRecord(String movie)
    {
        nonFoundRecords.add(movie);
    }
    
    public int getCorrectRecordCount()
    {
        return correctMovies.size();
    }
    
    public int getWrongSpecCount()
    {
        return nonSpecifiedFolders.size();
    }

    public int getNonExistingDirectoryCount()
    {
        return nonFoundFolders.size();
    }
    
    public int getOrphantDirectoryCount()
    {
        return this.nonFoundRecords.size();
    }

    public List<String> getCorrectMovies()
    {
        return correctMovies;
    }

    public List<String> getNonFoundFolders()
    {
        return nonFoundFolders;
    }

    public List<String> getNonFoundRecords()
    {
        return nonFoundRecords;
    }
    
    public List<String> getNonSpecifiedFolders()
    {
        return nonSpecifiedFolders;
    }
    
}
