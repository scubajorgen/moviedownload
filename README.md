# Movie Download

## Introduction
This is a private application I wrote to enrich the excel sheet in which I maintain my movie collection with data from [The Movie Database](https://www.themoviedb.org/).
It is rather quick and dirty, not many unit tests.

## Usage
A folder/file structure is assumed

```
[movie directory]
    [folder 01]
        [moviefolder 01]
        [moviefolder 02]
        ...
        [moviefolder NN]
    [folder 02]
        _[folder01]
        [moviefolder 01]
        [moviefolder 02]
        ...
```
* ```[folder NN]``` can be arbitrary, e.g. a genrename
* ```[moviefolder NN]``` is a folder in which movie video files are stored. The format is ```[moviename] ([year])```
* ```_[folder NN]``` is a subfolder. The underscore is used to distiguish it from a movie folder

You'll find a example structure in ```/src/test/resources```

## API Key
You need to provide an _API key_ for The Movie Database in the file apikey.txt. For this you need a developer account at The Movie Database. Rever to [the developer pages](https://developers.themoviedb.org/3). The software uses version 3 of the API.

> **You have to pass the key using the -k option or put the API key in a file called ```apikey.txt```**. The file should be placed in the same directory from which you run the code from. The file is needed for building the software, since during tests connection is made with the Movie Database API.

## Excel file
An sample excel file is provided. Orange columns are columns you can fill in, the yellow columns are columns the application fills in.
You have to fill in the first two columns:
* Movie name
* Movie year
* Folder
Then run the application.

## Usage
Build using maven:
```
mvn clean install
```

To run the application

```
java -jar MovieDownload.jar -c [command] -f [input excel] -b [backup excel] -o [true/false] -a [true/false] -k [API Key] -m [movie directory]
```

* -c Defines the command: enrich, folders. ```enrich``` requests missing info from the Movie Database, ```folders``` fills in missing movie folders, given the name and year of the movie, ```crosscheck``` checks the excel sheet against the folderstructure and find missing names
* -f Defines the input file
* -b The input file is backuped to this file, but before it is back upped for the case that the input file is destroyed. If not defined, the software automatically generates a unique name.
* -o Overwrite. By default once filled in fields are not overwritten. -o true overwrites.
* -a Indicates to process only the records that have not succesfully been processed, or all (-a true)
* -k API Key can optionally be passed. If not passed, the program tries to read it from apikey.txt
* -m Defines the base directory where movie files are stored

Or simply
```
java -jar MovieDownload.jar
```

This statement is equivalent to
```
java -jar MovieDownload.jar -c enrich -f movies.xslx -o false -a false -m ./
```

It processes the example file enclosed.

## Design
Next class diagram shows the classes

![design](images/design.png)

## License
Use the software however you like. 

## Disclaimer
Use the software at your own responsibility or don't use it.
No support given.