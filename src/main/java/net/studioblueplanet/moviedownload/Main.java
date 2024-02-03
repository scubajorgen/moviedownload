/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;



/**
 *
 * @author jorgen
 */
public class Main
{
    private final static    Logger LOGGER      = LogManager.getLogger(Main.class);
    
    /**
     * Print some nifty stuff if the user doesn't know how to use the application
     */
    private static void printHelp()
    {
        LOGGER.info("Usage: java -jar GalleryGenerator.jar <options>");
        LOGGER.info("       -f <filename>    Excel file to process. Default: movies.xlsx");
        LOGGER.info("       -b <filename>    File the original file is backed up to. Default: movies_backup.xslx");
        LOGGER.info("       -o <true/false>  Force overwrite existing fields. Default: false");
        LOGGER.info("       -a <true/false>  Process all. Default: false");
        LOGGER.info("       -k <API Key>     API key from themoviedb.org. Default: null. If not passed, it is read from apikey.txt");
        LOGGER.info("Example: java -jar moviedownload.jar -f movies.xlsx -b movies_backup.xslx -o true -a true");
    }

    /**
     * Rename file
     * @param file File to rename
     * @param newFile File to rename to
     * @return false if an error occured
     */
    public static boolean rename(String file, String newFile)
    {
        File fileOld=new File(file);
        File fileNew=new File(newFile);
        LOGGER.info("Renaming {} to {}", file, newFile);
        if (fileNew.exists())
        {
            LOGGER.warn("File \"{}\" to rename to already exists", newFile);
        }

        // Rename file (or directory)
        boolean success = fileOld.renameTo(fileNew);    
        return !success;
    }

    public static void main(String[] args)
    {
        Options             options;
        boolean             error;
        
        //Configurator.setRootLevel(Level.DEBUG);
        Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.INFO);        
        LOGGER.info("MOVIE DATABASE DOWNLOAD UTILITY");

        options=Options.argsParser(args);

        if (options!=null)
        {
            Movies movies=new Movies();

            error=movies.readMoviesExcel(options.filename);
            if (!error)
            {
                movies.findSubfolderNames("", options.processAll);
                movies.enrichMovies(options.apiKey, options.processAll);
                movies.updateMovieSheet(options.forceOverwrite);

                error=rename(options.filename, options.backupFilename);

                if (!error)
                {
                    movies.writeMoviesToExcel(options.filename);
                }
                else
                {
                    LOGGER.error("File not written because original could not be backed up");
                }
            }
            else
            {
                LOGGER.error("Exit because input file could not be read");
            }
        }
        else
        {
            printHelp();
        }

    }
}
