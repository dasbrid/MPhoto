package asbridge.me.uk.MPhoto.Classes;

import java.io.File;

/**
 * Created by David on 25/11/2015.
 */
public class Album {
    private File albumFolder;
    private String albumName;
    private File firstFile;
    private int year;
    private int month;
    private String albumType;

    public Album(String name, File firstFile, File folder)
    {
        this.albumName = name;
        this.firstFile = firstFile;
        this.albumFolder = folder;
        this.month = -1;
        this.year = -1;
        this.albumType = "folder";
    }

    public Album(String name, File firstFile, File folder, String type)
    {
        this.albumName = name;
        this.firstFile = firstFile;
        this.albumFolder = folder;
        this.month = -1;
        this.year = -1;
        this.albumType = type;
    }

    public Album(String name, int year, int month, File firstFile, File folder)
    {
        this.albumName = name;
        this.firstFile = firstFile;
        this.albumFolder = folder;
        this.month = month;
        this.year = year;
    }

    public int getMonth() { return this.month;}
    public int getYear() { return this.year;}
    public File getFolder() { return this.albumFolder;}
    public File getFirstFile() { return this.firstFile;}
    public String getName() { return this.albumName; }
    public String getType() { return this.albumType; }

}
