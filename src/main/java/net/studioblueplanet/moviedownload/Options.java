/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.studioblueplanet.moviedownload;

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
    private static final    String  MOVIEBACKUPFILENAME ="./movies_backup.xlsx";
    public String                   filename            =MOVIEFILENAME;
    public String                   backupFilename      =MOVIEBACKUPFILENAME;
    public boolean                  forceOverwrite      =true;
    public boolean                  processAll          =false;
    
    public static Options argsParser(String[] args)
    {
        Options options;
        int     i;
        char    option;
        
        if (args.length%2==0)
        {
            options=new Options();
            i=0;
            while (i<args.length)
            {
                if (args[i].startsWith("-"))
                {
                    option=args[i].charAt(1);
                    switch(option)
                    {
                        case 'o':
                            if ("true".equals(args[i+1]))
                            {
                                options.forceOverwrite=true;
                            }
                            else
                            {
                                options.forceOverwrite=false;
                            }
                            break;
                        case 'a':
                            if ("true".equals(args[i+1]))
                            {
                                options.processAll=true;
                            }
                            else
                            {
                                options.processAll=false;
                            }
                            break;
                        case 'f':
                            options.filename=args[i+1].trim();
                            break;
                        case 'b':
                            options.backupFilename=args[i+1].trim();
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
}
