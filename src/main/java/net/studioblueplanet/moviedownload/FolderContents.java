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
    private List<String>    subFolders;
    private int             emptySubfolders;
    
    public FolderContents()
    {
        subFolders      =new ArrayList<>();
        emptySubfolders =0;
    }
    
    public void addSubfolder(String subfolder)
    {
        subFolders.add(subfolder);
    }
    
    public int getSubfolderCount()
    {
        return subFolders.size();
    }
    
    public void increaseEmptySubfolderCount()
    {
        emptySubfolders++;
    }
    
    public int getEmptySubfolderCount()
    {
        return emptySubfolders;
    }
}
