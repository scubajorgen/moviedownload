/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorgen
 */
public class Movie
{
    private static final int        MAXCASTMEMBERS=5;
    private String                  title;
    private String                  titleRetrieved;
    private String                  originalTitle;
    private String                  releaseDate;
    private Integer                 year;
    private String                  folder;
    private String                  subfolder;
    private String                  overview;
    private Integer                 id;
    private List<String>            genre;
    private List<String>            cast;
    private String                  director;
    private Double                  voteAverage;
    private Integer                 voteCount;
    private Double                  popularity;
    private Double                  rating;
    private String                  remark;
    private String                  databaseRemark;
    private String                  mediaType;

    public Movie()
    {
        genre   =new ArrayList<>();
        cast    =new ArrayList<>();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitleRetrieved()
    {
        return titleRetrieved;
    }

    public void setTitleRetrieved(String titleRetrieved)
    {
        this.titleRetrieved = titleRetrieved;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle)
    {
        this.originalTitle = originalTitle;
    }

    public Integer getYear()
    {
        return year;
    }

    public void setYear(Integer year)
    {
        this.year = year;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getFolder()
    {
        return folder;
    }

    public void setFolder(String folder)
    {
        this.folder = folder;
    }

    public String getSubfolder()
    {
        return subfolder;
    }

    public void setSubfolder(String subfolder)
    {
        this.subfolder = subfolder;
    }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<String> getGenre()
    {
        return genre;
    }

    public void setGenre(List<String> genre)
    {
        this.genre = genre;
    }

    public List<String> getCast()
    {
        return cast;
    }

    public void setCast(List<String> cast)
    {
        this.cast = cast;
    }

    public String getDirector()
    {
        return director;
    }

    public void setDirector(String director)
    {
        this.director = director;
    }

    public Double getVoteAverage()
    {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount()
    {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount)
    {
        this.voteCount = voteCount;
    }

    public Double getPopularity()
    {
        return popularity;
    }

    public void setPopularity(Double popularity)
    {
        this.popularity = popularity;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getDatabaseRemark()
    {
        return databaseRemark;
    }

    public void setDatabaseRemark(String databaseRemark)
    {
        this.databaseRemark = databaseRemark;
    }

    public String getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }

    /**
     * Return the list of genres as one string, genres separated by /
     * @return List of genres as single string
     */
    public String getGenreString()
    {
        String genreString="";

        int i=0;
        while (i<genre.size())
        {
            genreString+=genre.get(i);
            if (i<genre.size()-1)
            {
                genreString+="/";
            }
            i++;
        }
        return genreString;
    }    

    /**
     * Return the list of the cast, genres separated by /
     * @return List of cast as single string
     */
    public String getCastString()
    {
        String  castString="";

        if (cast!=null)
        {
            int i=0;
            while (i<Math.min(cast.size(), MAXCASTMEMBERS))
            {
                if (i>0)
                {
                    castString+="\n";
                }
                castString+=cast.get(i);
                i++;
            }
        }
        return castString;
    }    

}
