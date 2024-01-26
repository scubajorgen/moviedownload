/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author jorgen
 */
public class Movies
{
    private static final    int                 CELL_TITLE          =0;
    private static final    int                 CELL_YEAR           =1;
    private static final    int                 CELL_FOLDER         =2;
    private static final    int                 CELL_SUBFOLDER      =3;
    private static final    int                 CELL_OVERVIEW       =4;
    private static final    int                 CELL_GENRES         =5;
    private static final    int                 CELL_DIRECTOR       =6;
    private static final    int                 CELL_CAST           =7;
    private static final    int                 CELL_POPULARITY     =8;
    private static final    int                 CELL_VOTE_AVERAGE   =9;
    private static final    int                 CELL_VOTE_COUNT     =10;
    private static final    int                 CELL_RATING         =11;
    private static final    int                 CELL_REMARK         =12;
    private static final    int                 CELL_TITLE_DB       =13;
    private static final    int                 CELL_ORIGINAL_TITLE =14;
    private static final    int                 CELL_RELEASE_DATE   =15;
    private static final    int                 CELL_REMARK_DB      =16;
    private static final    int                 CELL_IDENTIFIER_DB  =17;
    private static final    int                 CELL_MEDIA_TYPE     =18;

    private final static    Logger              LOGGER = LogManager.getLogger(Movies.class);
    private final           List<Movie>         movies;
    private                 Workbook            workbook;
    
    public Movies()
    {
        movies=new ArrayList<>();
    }
    
    /**
     * Get the cell value as object
     * @param row Row to fetch the cell from
     * @param cellId Index of the cell, starting at 0
     * @param isInteger In case of numerical: return as integer
     * @return 
     */
    private Object getCellValue(Row row, int cellId, boolean isInteger)
    {
        Cell    cell;
        Object  value=null;
        cell=row.getCell(cellId);
        
        if (cell!=null)
        {
            if (cell.getCellType()==CellType.STRING)
            {
                value=cell.getStringCellValue();
            }
            else if (cell.getCellType()==CellType.NUMERIC)
            {
                value=cell.getNumericCellValue();
                if (isInteger)
                {
                    value=new Integer((int)Math.round(cell.getNumericCellValue()));
                }
                else
                {
                    value=cell.getNumericCellValue();
                }
            }
        }
        return value;
    }
    
    
    /**
     * Parse the movies sheet en generate an list of Movies
     * @param sheet Sheet to parse
     */
    private void parseMoviesSheet(Sheet sheet)
    {
        int         maxRow;
        long        value;
        String      typeName;
        String      valueName;
        Cell        cell;
        
        maxRow=sheet.getLastRowNum();
        for (int i=1; i<=maxRow; i++) 
        {
            Row  row=sheet.getRow(i);
            if (row!=null)
            {
                Movie movie=new Movie();
                // Title
                movie.setTitle((String)getCellValue(row, CELL_TITLE, false));
                // Year
                movie.setYear((Integer)getCellValue(row, CELL_YEAR, true));
                // Folder
                movie.setFolder((String)getCellValue(row, CELL_FOLDER, false));
                // Folder
                movie.setSubfolder((String)getCellValue(row, CELL_SUBFOLDER, false));
                // Overview
                movie.setOverview((String)getCellValue(row, CELL_OVERVIEW, false));
                // Genres
                String genreString=(String)getCellValue(row, CELL_GENRES, false);
                if (genreString!=null)
                {
                    String[] parts=genreString.split("/");
                    for(String part : parts)
                    {
                        movie.getGenre().add(part);
                    }
                }
                // Director
                movie.setDirector((String)getCellValue(row, CELL_DIRECTOR, false));
                // Cast
                String castString=(String)getCellValue(row, CELL_CAST, false);
                if (castString!=null)
                {
                    String[] parts=castString.split("\n");
                    for(String part : parts)
                    {
                        movie.getCast().add(part);
                    }
                }
                // Popularity
                movie.setPopularity((Double)getCellValue(row, CELL_POPULARITY, false));
                // Vote_average
                movie.setVoteAverage((Double)getCellValue(row, CELL_VOTE_AVERAGE, false));
                // Vote count
                movie.setVoteCount((Integer)getCellValue(row, CELL_VOTE_COUNT, true));
                // Rating
                movie.setRating((Double)getCellValue(row, CELL_RATING, false));
                // Remark
                movie.setRemark((String)getCellValue(row, CELL_REMARK, false));
                // Title retrieved
                movie.setTitleRetrieved((String)getCellValue(row, CELL_TITLE_DB, false));
                // Title original
                movie.setOriginalTitle((String)getCellValue(row, CELL_ORIGINAL_TITLE, false));
                // Release date
                movie.setReleaseDate((String)getCellValue(row, CELL_RELEASE_DATE, false));
                // Database remark
                movie.setDatabaseRemark((String)getCellValue(row, CELL_REMARK_DB, false));
                // ID
                movie.setId((Integer)getCellValue(row, CELL_IDENTIFIER_DB, true));
                // media type
                movie.setMediaType((String)getCellValue(row, CELL_MEDIA_TYPE, true));
                
                movies.add(movie);
            }
        }
        LOGGER.info("Read {} global profile types", movies.size());        
    }
    
    /**
     * Generic set cell value method. 
     * @param cell Cell to set the value from
     * @param value Value to set, must be String, Integer or Double
     */
    private void setCellValue(Cell cell, Object value)
    {
        if (value instanceof String)
        {
            cell.setCellValue((String)value);
        }
        else if (value instanceof Integer)
        {
            cell.setCellValue((Integer)value);
        }
        else if (value instanceof Double)
        {
            cell.setCellValue((Double)value);
        }
    }
    
    /**
     * Update the given cell in given row. If a cell allready has a value, 
     * it is not updated unless forceOverwrite is true
     * @param row Row to update cell in
     * @param column Column id of the cell
     * @param value Value to update
     * @param forceOverwrite Force overwrite if the cell already contains a value
     */
    private void updateCell(Row row, int column, Object value, boolean forceOverwrite)
    {
        Cell cell;
        
        cell=row.getCell(column);
        if (cell==null)
        {
            cell=row.createCell(column);
            setCellValue(cell, value);
        }
        else
        {
            if (forceOverwrite)
            {
                setCellValue(cell, value);
            }
        }        
    }
    
    public void updateMovieSheet(boolean forceOverwrite)
    {
        Sheet sheet=workbook.getSheetAt(0);
        Cell  cell;
        Movie movie;
        
        int i=0;
        while (i<movies.size())
        {
            Row  row=sheet.getRow(i+1);
            movie=movies.get(i);
            updateCell(row, CELL_ORIGINAL_TITLE , movie.getOriginalTitle()  , forceOverwrite);
            updateCell(row, CELL_TITLE_DB       , movie.getTitleRetrieved() , forceOverwrite);
            updateCell(row, CELL_OVERVIEW       , movie.getOverview()       , forceOverwrite);
            updateCell(row, CELL_DIRECTOR       , movie.getDirector()       , forceOverwrite);
            updateCell(row, CELL_CAST           , movie.getCastString()     , forceOverwrite);
            updateCell(row, CELL_POPULARITY     , movie.getPopularity()     , forceOverwrite);
            updateCell(row, CELL_VOTE_AVERAGE   , movie.getVoteAverage()    , forceOverwrite);
            updateCell(row, CELL_VOTE_COUNT     , movie.getVoteCount()      , forceOverwrite);
            updateCell(row, CELL_RELEASE_DATE   , movie.getReleaseDate()    , forceOverwrite);
            updateCell(row, CELL_REMARK_DB      , movie.getDatabaseRemark() , true);            // always update status
            updateCell(row, CELL_IDENTIFIER_DB  , movie.getId()             , forceOverwrite);
            updateCell(row, CELL_GENRES         , movie.getGenreString()    , forceOverwrite);
            updateCell(row, CELL_MEDIA_TYPE     , movie.getMediaType()      , forceOverwrite);

            i++;
        }
    }
    
    
    /**
     * Read the movies from the excel file. The excel is stored
     * as Workbook in this class
     * @return true if an error occurred, false otherwise
     */
    public boolean readMoviesExcel(String filename)
    {
        boolean error;
        error=true;
        try
        {
            InputStream file = new FileInputStream(new java.io.File(filename));
            
            if (file!=null)
            {
                workbook   = new XSSFWorkbook(file);
                this.parseMoviesSheet(workbook.getSheetAt(0));
                file.close();
                LOGGER.info("{} movies read from excel file", movies.size());
                error=false;
            }
            else
            {
                LOGGER.error("Cannot find movies file {}", filename);
            }
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("File {} not found: {}", filename, e.getMessage());
        }
        catch (IOException e)
        {
            LOGGER.error("Error reading file {}: {}", filename, e.getMessage());
        }
        return error;
    }
    
    /**
     * Write the workbook to file
     */
    public void writeMoviesToExcel(String filename)
    {
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            
            if (file!=null)
            {
                workbook.write(file);
                file.close();
                LOGGER.info("{} movies written to excel file {}", movies.size(), filename);
            }
            else
            {
                LOGGER.error("Cannot create movies file {}", filename);
            }
        }
        catch (IOException e)
        {
            LOGGER.error("Error writing file {}: {}", filename, e.getMessage());
        }
        LOGGER.info("Movie written to "+filename);          
    }
    
    /**
     * Enrich the list of movies with data from the database
     * @param apiKey Api key for the MovieDB. If null, it is read from 
     *               the file apikey.txt
     * @param processAll 
     */
    public void enrichMovies(String apiKey, boolean processAll)
    {
        int             i;
        MovieDatabase   db;
        Movie           movie;
        int             enrichCount;
        
        db=new MovieDatabase(apiKey);
        
        i=0;
        enrichCount=0;
        while (i<movies.size())
        {
            movie=movies.get(i);
            LOGGER.info("Processing movie {} ({})", movie.getTitle(), movie.getYear());
            if (!(MovieDatabase.STATUS_PROCESSED.equals(movie.getDatabaseRemark()) || 
                  MovieDatabase.STATUS_NOTUPDATED.equals(movie.getDatabaseRemark())) || processAll)
            {
                boolean success=db.enrichMovie(movie);
                if (success)
                {
                    enrichCount++;
                }
                
            }
            else
            {
                movie.setDatabaseRemark(MovieDatabase.STATUS_NOTUPDATED);
                LOGGER.info("{} not enriched", movie.getTitle());
            }
            i++;
        }
        LOGGER.info("Movies processed {}. Movies successfully enriched {}", movies.size(), enrichCount);
        LOGGER.info("_______________________________________________________");
    }
    
    /**
     * Return the list of movies read
     * @return List of movies
     */
    public List<Movie> getMovies()
    {
        return movies;
    }

    
}
