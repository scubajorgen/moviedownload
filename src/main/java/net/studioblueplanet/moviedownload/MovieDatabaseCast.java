/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

/**
 *
 * @author jorgen
 */
public class MovieDatabaseCast implements Comparable<MovieDatabaseCast>
{
    Boolean             adult;
    Integer             gender;
    Integer             id;
    String              known_for_department;
    String              name;
    String              original_name;
    Double              popularity;
    String              profile_path;
    Integer             cast_id;
    String              character;
    String              credit_id;
    Integer             order;
    
    /**
     * Compare to method based on order, use by sorting methods
     * @param cast Cast to compare to
     * @return -1, 0, 1
     */
    public int compareTo(MovieDatabaseCast cast)
    {
        int returnValue;
        if (order>cast.order)
        {
            returnValue=1;
        }
        else if (order<cast.order)
            {
                returnValue=-1;
            }

        else
        {
            returnValue=0;
        }
        return returnValue;
    }
}
