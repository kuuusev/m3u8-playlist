/*
 * Name: Kevin S
 * Date: Feb. 13, 2017
 * Filename: FXMLDocumentController.java
 *
 * A class to make populating the tableView easier.
 */
package m3u8creator;

import java.io.File;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/*
 * NameDirectory contains 3 fields: 
 * name for the string representation of the name of the file
 * directory for the string representation of the absolute path of the file
 * folder for the File representation of the file for later use
 *
 * This class just contains these fields so it is easier package to implement.
 */
public final class NameDirectory {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty directory = new SimpleStringProperty("");
    private final SimpleObjectProperty<File> folder = new SimpleObjectProperty<>();
    
    /*
     * Constructors
     */
    public NameDirectory(){
        this("","",null);
    }
    public NameDirectory(String name){
        setName(name);
    }
    public NameDirectory(String name, String directory){
        setName(name);
        setDirectory(directory);
    }
    public NameDirectory(String name, String directory, File folder){
        setName(name);
        setDirectory(directory);
        setFolder(folder);
    }
    
    /*
     * Methods (Set/Get and toString)
     */
    public void setName(String name){
        this.name.set(name);
    }
    public void setDirectory(String directory){
        this.directory.set(directory);
    }
    public void setFolder(File folder){
        this.folder.set(folder);
    }
    public String getName(){
        return name.get();
    }
    public String getDirectory(){
        return directory.get();
    }
    public File getFolder(){
        return folder.get();
    }
    
    @Override
    public String toString(){
        return name.get();
    }
}
