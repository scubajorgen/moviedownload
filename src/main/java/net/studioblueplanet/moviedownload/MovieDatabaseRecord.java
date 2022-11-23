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
public class MovieDatabaseRecord
{
    public static final String MEDIATYPE_MOVIE="movie";
    public static final String MEDIATYPE_TV="tv";
    
    Boolean         adult;
    String          title;
    String          name;
    Integer         id;
    Double          vote_average;
    Integer         vote_count;
    Double          popularity;
    String          release_date;
    String          first_air_date;
    String          overview;
    List<Integer>   genre_ids;
    String          original_title;
    String          original_name;
    String          media_type;
}
