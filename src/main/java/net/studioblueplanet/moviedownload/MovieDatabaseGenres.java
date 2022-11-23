/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.util.List;

/**
 *
 * @author jorgen
 */
public class MovieDatabaseGenres
{
    List<MovieDatabaseGenre> genres;
    
    /**
     * Find genre by genre ID
     * @param id Genre index
     * @return The genre name
     */
    public String getGenre(int id)
    {
        String  genre=null;
        int     i;
        
        i=0;
        while (i<genres.size() && genre==null)
        {
            if (genres.get(i).id==id)
            {
                genre=genres.get(i).name;
            }
            i++;
        }
        return genre;
    }
}
