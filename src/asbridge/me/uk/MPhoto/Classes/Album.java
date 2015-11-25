package asbridge.me.uk.MPhoto.Classes;

import java.io.File;

/**
 * Created by David on 25/11/2015.
 */
public class Album {
    private File albumFolder;
    private String albumName;
    private File firstFile;

    public Album(String name, File firstFile, File folder)
    {
        this.albumFolder = folder;
        this.albumName = name;
        this.firstFile = firstFile;
    }

    public File getFolder() { return this.albumFolder;}
    public File getFirstFile() { return this.firstFile;}
    public String getName() { return this.albumName; }

}
