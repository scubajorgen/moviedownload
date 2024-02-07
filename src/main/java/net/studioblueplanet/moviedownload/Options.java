/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jorgen
 */
public class Options
{
    private final static    Logger  LOGGER              = LogManager.getLogger(Options.class);    
    private static final    String  MOVIEFILENAME       ="./movies.xlsx";
    public String                   command             ="enrich";
    public String                   filename            =MOVIEFILENAME;
    public String                   movieDirectory      ="./";
    public String                   backupFilename      =null;
    public String                   apiKey              =null;
    public boolean                  forceOverwrite      =true;
    public boolean                  processAll          =false;
    
    
    private Options()
    {
        LocalDateTime now=LocalDateTime.now();
        backupFilename=String.format("%04d%02d%02d_%02d%02d%02d_movies_backup.xlsx", 
                                         now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 
                                         now.getHour(), now.getMinute(), now.getSecond());
    }
    
    /**
     * Parse arguments into options
     * @param args The arguments 
     * @return An Options object
     */
    public static Options argsParser(String[] args)
    {
        Options options;
        int     i;
        char    option;
        String  value;
        
        if (args.length%2==0)
        {
            options=new Options();
            i=0;
            while (i<args.length)
            {
                if (args[i].startsWith("-"))
                {
                    option  =args[i].toLowerCase().charAt(1);
                    value   =args[i+1].trim();
                    switch(option)
                    {
                        case 'o':
                            if ("true".equals(value))
                            {
                                options.forceOverwrite=true;
                            }
                            else
                            {
                                options.forceOverwrite=false;
                            }
                            break;
                        case 'a':
                            if ("true".equals(value))
                            {
                                options.processAll=true;
                            }
                            else
                            {
                                options.processAll=false;
                            }
                            break;
                        case 'c':
                            options.command=value;
                            break;
                        case 'f':
                            options.filename=value;
                            break;
                        case 'b':
                            options.backupFilename=value;
                            break;
                        case 'k':
                            options.apiKey=value;
                            break;
                        case 'm':
                            options.movieDirectory=value;
                            if (!options.movieDirectory.endsWith("/") && 
                                !options.movieDirectory.endsWith("\\"))
                            {
                                options.movieDirectory+="/";
                            }
                            break;
                    }
                }
                else
                {
                    options=null;
                    LOGGER.error("Illegal option {}", args[i]);
                }
                i+=2;
            }
        }
        else
        {
            options=null;
            LOGGER.error("Incorrect options");
        }
        return options;
    }    
    
    /**
     * Show options
     */
    public void dumpOptions()
    {
        LOGGER.info("OPTIONS: ");
        LOGGER.info("- command          {}", command);
        LOGGER.info("- filename         {}", filename);
        LOGGER.info("- movie directory  {}", movieDirectory);
        LOGGER.info("- foce overwrite   {}", forceOverwrite);
        LOGGER.info("- process all      {}", processAll);
        LOGGER.info("- backup file      {}", backupFilename);
        LOGGER.info("- api key          {}", apiKey);
    }
}
