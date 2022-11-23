/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jorgen
 */
public class MovieDatabase
{
    private final static    Logger LOGGER                       = LogManager.getLogger(MovieDatabase.class);

    public static final     String STATUS_PROCESSED             ="Movie enriched";
    public static final     String STATUS_NOTIDENTIFYABLE       ="Movie could not be uniquely identified in database";
    public static final     String STATUS_NOTFOUND              ="Movie could not be found in database";
    public static final     String STATUS_NOTUPDATED            ="Movie not updated";
    private static final    String USER_AGENT                   = "Mozilla/5.0";
    private static final    String BEARERTOKEN                  = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjNjhmZTE5MDQ0NmYyYzNkOTcy"+
                                                                    "ZGE4ZTA5OGViY2MwMSIsInN1YiI6IjYzNzg5ODYwZmFiM2ZhMDBiNGQwZm"+
                                                                    "ViYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.bt7vi-"+
                                                                    "mgpTKwQ0nS5LThjcUIRHvkEzkCOzt6KANtpy4";
    
    private                 MovieDatabaseGenres movieGenres;
    private static          MovieDatabase       theInstance;
    private                 MovieDatabaseToken  requestToken;
    private                 String              apiKey;
    
    
    /**
     * Get the one and only singleton instance of this class
     * @return The instance
     */
    public static MovieDatabase getInstance()
    {
        if (theInstance==null)
        {
            theInstance=new MovieDatabase();
        }
        return theInstance;
    }

    /**
     * Constructor
     */
    private MovieDatabase()
    {
        readApiKeyFromFile();
        requestGenres();
    }
    
    /**
     * Read the API key from apikey.txt
     */
    private void readApiKeyFromFile()
    {
        BufferedReader reader;
        try
        {
            reader=new BufferedReader(new FileReader("apikey.txt")); 
            apiKey=reader.readLine();
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("File apikey.txt containing the MovieDatabase API key not found");
        }
        catch (IOException e)
        {
            LOGGER.error("API Key could not be read");
        }
    }
    
    /**
     * Request token for access. For future use...
     */
    private void requestToken()
    {
        String url="https://api.themoviedb.org/3/authentication/token/new?api_key="+apiKey;
        try
        {
            StringBuffer response    =sendGet(url);
            Gson g      =new Gson();
            if (response.length()>0)
            {
                requestToken=g.fromJson(response.toString(), MovieDatabaseToken.class);
                LOGGER.info("Token requested: {}, expiration {}", requestToken.success, requestToken.expires_at);
            }
            else
            {
                LOGGER.error("No response from database when requesting token");
            }
        }
        catch(IOException e)
        {
            LOGGER.error("Error on database requesting token: {}", e.getMessage());
        }        
    }
    
    /**
     * Request the movie genres from the Movie Database
     */
    private void requestGenres()
    {
        String url="https://api.themoviedb.org/3/genre/movie/list?api_key="+apiKey;
        try
        {
            StringBuffer response    =sendGet(url);
            Gson g      =new Gson();
            if (response.length()>0)
            {
                movieGenres=g.fromJson(response.toString(), MovieDatabaseGenres.class);
                LOGGER.info("{} genres read", movieGenres.genres.size());
            }
            else
            {
                LOGGER.error("No response from database when requesting genres");
            }
        }
        catch(IOException e)
        {
            LOGGER.error("Error on database requesting genres: {}", e.getMessage());
        }
    }

    /**
     * Request the movie genres from the Movie Database
     */
    private String requestDirector(int movieId)
    {
        String director=null;        
        String url="https://api.themoviedb.org/3/movie/"+movieId+"/credits?api_key="+apiKey;
        try
        {
            StringBuffer response    =sendGet(url);
            Gson g      =new Gson();
            if (response.length()>0)
            {
                MovieDatabaseCredits credits=g.fromJson(response.toString(), MovieDatabaseCredits.class);
                LOGGER.info("{} credits read", credits.cast.size());
                
                List<MovieDatabaseCrew> crew=credits.crew;
                int i=0;
                while (i<crew.size() && director==null)
                {
                    if ("Director".equals(crew.get(i).job))
                    {
                        director=crew.get(i).name;
                    }
                    i++;
                }
            }
            else
            {
                LOGGER.error("No response from database when requesting credits");
            }
        }
        catch(IOException e)
        {
            LOGGER.error("Error on database requesting credits: {}", e.getMessage());
        }
        return director;
    }    
    /**
     * Executes a HTTP request
     * @param getUrl The URL to request
     * @return The response as list of strings
     * @throws IOException 
     */
    private StringBuffer sendGet(String getUrl) throws IOException 
    {
        StringBuffer response = new StringBuffer();
        URL obj = new URL(getUrl);
        LOGGER.debug(getUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        LOGGER.info("GET Response Code: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) 
        { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream(), "UTF8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) 
            {
                response.append(inputLine);
            }
            in.close();
        } 
        else 
        {
            LOGGER.error("GET request did not work");
        }
        return response;
    }    
    
    /**
     * Create the query URL to find a movie
     * @param title Movie title
     * @param page Page to request, allowing for multi pages
     * @return The URL as string
     */
    private String queryUrl(String title, int page)
    {
        String url="https://api.themoviedb.org/3/search/multi?page="+page+"&api_key="+apiKey+"&query=";
        String[] words=title.split("\\s+");
        int i=0;
        while (i<words.length)
        {
            url+=words[i];
            if (i<words.length-1)
            {
                url+="+";
            }
            i++;
        }    
        return url;
    }
    
    /**
     * Parse the raw records from the database and try to identify the right 
     * movie based on the release year
     * @param records Array of raw records from The Movie Database
     * @param theYear The year to look for or null if not known
     * @return The record of the move produced in the year or null if not found
     */
    private MovieDatabaseRecord findMovieByYear(List<MovieDatabaseRecord> records, Integer theYear)
    {
        String releaseDate;
        
        MovieDatabaseRecord theRecord=null;
        
        if (theYear!=null)
        {
            int i=0;
            while (i<records.size() && theRecord==null)
            {
                if (MovieDatabaseRecord.MEDIATYPE_MOVIE.equals(records.get(i).media_type))
                {
                    releaseDate=records.get(i).release_date;
                }
                else
                {
                    releaseDate=records.get(i).first_air_date;
                }
                if (releaseDate!=null && releaseDate.length()>3)
                {
                    int year=Integer.parseInt(releaseDate.substring(0, 4));
                    if (year==theYear)
                    {
                        theRecord=records.get(i);
                    }
                }
                i++;
            }
        }
        else
        {
            if (records.size()==1)
            {
                theRecord=records.get(0);
            }
        }
            
        return theRecord;
    }
    
    /**
     * Enrich the movie with info from the Movie Database
     * @param movie Movie to enricht
     */
    public void enrichMovie(Movie movie)
    {
        StringBuffer                response;
        String                      url;
        int                         totalPages;
        int                         page;
        List<MovieDatabaseRecord>   records=new ArrayList<>();
        MovieDatabasePage           mdPage=null;
        MovieDatabaseRecord         theRecord=null;
        
        totalPages  =1;
        page        =1;
        url=queryUrl(movie.getTitle(), totalPages);
        try
        {
            Gson g      =new Gson();
            while (page<=totalPages && theRecord==null) 
            {
                url         =queryUrl(movie.getTitle(), page);
                response    =sendGet(url);

                mdPage      =g.fromJson(response.toString(), MovieDatabasePage.class);
                LOGGER.info("Page {} from {}, total results {}; parsing page of {} records", 
                            mdPage.page, mdPage.total_pages, mdPage.total_results, mdPage.results.size());
                
                if (mdPage.results.size()>0)
                {
                    theRecord=findMovieByYear(mdPage.results, movie.getYear());
                    if (theRecord==null)
                    {
                        LOGGER.info("Movie {} could not be identifed uniquely in the database", movie.getTitle());
                        movie.setDatabaseRemark(STATUS_NOTIDENTIFYABLE);                        
                    }
                }
                else
                {
                    LOGGER.info("Movie {} could not be found in the database", movie.getTitle());
                    movie.setDatabaseRemark(STATUS_NOTFOUND);                    
                }
                totalPages  =mdPage.total_pages;
                page++;
            }
            if (theRecord!=null)
            {
                movie.setId(theRecord.id);
                if (MovieDatabaseRecord.MEDIATYPE_MOVIE.equals(theRecord.media_type))
                {
                    movie.setTitleRetrieved(theRecord.title);
                    movie.setOriginalTitle(theRecord.original_title);
                    movie.setReleaseDate(theRecord.release_date);
                }
                else
                {
                    movie.setTitleRetrieved(theRecord.name);
                    movie.setOriginalTitle(theRecord.original_name);
                    movie.setReleaseDate(theRecord.first_air_date);
                }
                movie.setOverview(theRecord.overview);
                movie.setVoteAverage(theRecord.vote_average);
                movie.setVoteCount(theRecord.vote_count);
                movie.setPopularity(theRecord.popularity);
                movie.setMediaType(theRecord.media_type);
                List<String> genres=movie.getGenre();
                genres.clear();
                int i=0;
                while (i<theRecord.genre_ids.size())
                {
                    int genreId=theRecord.genre_ids.get(i);
                    String genreName=movieGenres.getGenre(genreId);
                    if (genreName!=null)
                    {
                        genres.add(genreName);
                    }
                    else
                    {
                        LOGGER.error("Non identifed genre {}", genreId);
                    }
                    i++;
                }
                movie.setDirector(requestDirector(theRecord.id));
                LOGGER.info("Movie {} enriched", movie.getTitle());
                movie.setDatabaseRemark(STATUS_PROCESSED);
            }
        }
        catch(IOException e)
        {
            LOGGER.error("Error on database request: {}", e.getMessage());
        }
    }
}
