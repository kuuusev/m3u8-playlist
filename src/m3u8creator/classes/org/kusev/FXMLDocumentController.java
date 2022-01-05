/*
 * Name: Kevin S
 * Date: Feb. 13, 2017
 * Filename: FXMLDocumentController.java
 *
 * Handles all logic for the program.
 * @FXML indicates something visible to the FXMLDocument and SceneBuilder
 */
package org.kusev;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    
    @FXML private TreeView<NameDirectory> treeView;
    @FXML private TextField fileFormat;
    @FXML private TextField newFileLocationText;
    @FXML private TextField fileName;
    @FXML private TableView<NameDirectory> tableView;
    @FXML private TableColumn fName;
    @FXML private TableColumn fDirectory;
    private String[] types;
    private String extension;
    private File f;
    private String initialDirectoryAbsolute;
    private Path filePathAbsolute;
    private Path filePathBase;
    private Path filePathRelative;
    private String formats;
    //linkedList is the internal data strucutre of the m3u8 playlist
    private LinkedList<NameDirectory> linkedList;
    private String savePath;
    
    private final String regexPatternAfterLastSlash = "([^\\\\]+$)";
    private final String regexPatternBeforeLastSlash = "^(.*[\\\\\\/])";
    /*
     * See Initialize Method instead for Constructor
     */
    public FXMLDocumentController(){
    }
    
    /*
     * handleOpenFileInputBrowser is used by the button labelled
     * "Open Folder" which will let the user choose a folder heirarchy
     * which is displayed in the tree view.
     *
     * input : ActionEvent (Button being clicked)
     * output: Dialog to Choose Folder. Goes to setTreeView
     */
    @FXML
    protected void handleOpenFileInputBrowser(ActionEvent event) {
        //Creates a Dialog to select a directory; titled "Open Folder"
        DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("Open Folder");
        f = folderChooser.showDialog(new Stage());
        
        //Perhaps to be used later?
        initialDirectoryAbsolute = f.getAbsolutePath();
        System.out.println(initialDirectoryAbsolute);
        
        //Goes to setTreeView
        treeView.setRoot(setTreeView(f));
    }
    
    /*
     *
     *
     *
     *
     */
    @FXML
    protected void handleLoadPlaylist(ActionEvent event){
        //create a extension filter to only show m3u8 files.
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("m3u8 files (*.m3u8)", "*.m3u8", "*.M3U8");
        
        //Create a dialog to select a file; titled "Open m3u8"
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.setTitle("Open m3u8");
        f = fileChooser.showOpenDialog(new Stage());
        
        //try catch as file f may not exist
        try{
            Pattern regexMatchPatternAfter = Pattern.compile(regexPatternAfterLastSlash);
            Pattern regexMatchPatternBefore = Pattern.compile(regexPatternBeforeLastSlash);
            
            Scanner getLines = new Scanner(f);
            
            //for every file in the playlist
            while(getLines.hasNextLine()){
                String line = getLines.nextLine();
                System.out.println(line);
                Matcher regexMatcherBefore = regexMatchPatternBefore.matcher(line);
                Matcher regexMatcherAfter = regexMatchPatternAfter.matcher(line);
                //we can only use find once before it starts matching elsewhere
                if(regexMatcherAfter.find() && regexMatcherBefore.find()){
                    NameDirectory item = new NameDirectory(regexMatcherAfter.group(1).toString(),regexMatcherAfter.group(1).toString());
                    linkedList.add(item);
                    populateTableView(item);
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("M3U8");
                    alert.setHeaderText(null);
                    alert.setContentText("REGEX MATCH ERROR");
                    alert.showAndWait();
                }
            }
        }catch (FileNotFoundException e){
            //Creates an alert to inform the user
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("M3U8");
            alert.setHeaderText(null);
            alert.setContentText("File Not Found!");
            alert.showAndWait();
        }
    }
    
    /*
     * handleOpenFileOutputBrowser is used by the button labelled
     * "Save At" which lets the user define a location for the playlist
     * to be created at.
     *
     * input : ActionEvent (Button being clicked)
     * output: Sets the text of a Text Field (newFileLocationText)
    */
    @FXML
    protected void handleOpenFileOutputBrowser(ActionEvent event){
        //Creates a dialog to select a directory to save the file
        DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("Choose an Output Folder");
        
        //Get the path of the directory chosen
        savePath = folderChooser.showDialog(new Stage()).getAbsolutePath();
        
        //Set the textField to that path to inform the user.
        newFileLocationText.setText(savePath);
    }
    
    /*
     * handleExtensionChanger is used by the button titled "Update"
     * which will update the treeView with only the file formats in
     * the text field defined by the user.
     * 
     * input : ActionEvent (Button being clicked)
     * output: Updated treeView. Goes to setTreeValue
     */
    @FXML
    protected void handleExtensionChanger(ActionEvent event){
        //Gets text from the textField
        formats = fileFormat.getText();
        //Splits the one long string by commas into the String array
        types = formats.split("\\s*,\\s*");
        System.out.println(types);
        
        //Call the treeView again to refresh
        treeView.setRoot(setTreeView(f));
        
        //Creates an alert to inform the user
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("M3U8");
        alert.setHeaderText(null);
        alert.setContentText("Changed file extensions and reloaded previous folder.");
        alert.showAndWait();
    }
    
    /*
     * onCreate is called by the button labelled "Create". This method creates
     * a .m3u8 file using specified save destination and name with the files
     * displayed in the listView.
     *
     * input : ActionEvent (Button being clicked)
     * output: Creates a .m3u8 file at a given destination with the given name
     */
    @FXML
    protected void onCreate(ActionEvent event){
        //If there is nothing in the list then throw an alert and exit.
        if(linkedList.isEmpty()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The playlist is empty.");
            alert.showAndWait();
            return;
        }
        
        System.out.println(savePath);
        //If the destination to save at is not specified throw an alert and exit.
        if(savePath==null){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("The playlist needs an output destination. Choose one by clicking the Save At button.");
            alert.showAndWait();
            return;
        }
        
        System.out.println(fileName.getText());
        //If the file name isn't specified throw an alert and exit.
        if (fileName.getText().equals("")){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("The playlist needs a name. Add one in the textfield in the bottom left.");
            alert.showAndWait();
            return;
        }
        
        //Try to write the file, if not throw a generic error. EXPAND THIS LATER ON.
        try{
            //Create a new file at the location encoded in Unicode8
            PrintWriter out = new PrintWriter(savePath + "\\" + fileName.getText()+".m3u8","UTF-8");
            //Use createPlaylistContent method
            out.println(createPlaylistContent());
            out.close();
        } catch (IOException e){
            //Inform user of failure
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error.");
        }
        //Inform user of success
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("M3U8");
        alert.setHeaderText(null);
        alert.setContentText("Created " + fileName.getText()+".m3u8 at" + savePath);
        alert.showAndWait();
    }
    
    /*
     * onDoubleClickSelectedFile is called by double clicking any folder or
     * file in the treeView. This method will determine if the line double
     * clicked is a folder or a file. If it is a file it will simply add it 
     * to the linkedList and the tableView. If it is a folder then it will
     * call addFolderToList.
     *
     * input : MouseEvent (Mouse Clicks)
     * output: Add an item to the linked list and the table view.
     */
    @FXML
    protected void onDoubleClickSelectedFile(MouseEvent event){
        //If the item is clicked twice
        if(event.getClickCount()==2){
            //Get the item
            TreeItem<NameDirectory> item = treeView.getSelectionModel().getSelectedItem();
            //If the item is a folder then go to addFolderToList else add to list and table.
            if(item.getValue().getFolder()!=null){
                addFolderToList(item.getValue().getFolder());
            } else {
                linkedList.add(item.getValue());
                populateTableView(item.getValue());
            }
        }
    }
    
    /*
     * OnEnterSelectedFile is the same as onDoubleClickSelectedFile except
     * it is for the Enter key rather than double click.
     *
     * input : KeyEvent (Key Clicked)
     * output: Add an item to the linked list and the table view.
     */
    @FXML
    protected void onEnterSelectedFile(KeyEvent event){
        //See if it is the enter key pressed
        if(event.getCode() == KeyCode.ENTER){
            //Get the item
            TreeItem<NameDirectory> item = treeView.getSelectionModel().getSelectedItem();
            //If the item is a folder then go to addFolderToList else add to list and table.
            if(item.getValue().getFolder()!=null){
                addFolderToList(item.getValue().getFolder());
            } else{
                linkedList.add(item.getValue());
                populateTableView(item.getValue());
            }
        }
    }
    
    /*
     * onDeleteListFile is called when the delete key is pressed while a table
     * value is selected. It removes that value both from the list and the 
     * table view.
     *
     * input : KeyEvent (Key pressed)
     * output: Removes an item from the list and table.
     */
    @FXML
    protected void onDeleteListFile(KeyEvent event){
        //If the key is the Delete key
        if(event.getCode() == KeyCode.DELETE){
            //Get all the data form the table
            ObservableList<NameDirectory> data = tableView.getItems();
            //Select the file
            NameDirectory currentEntry = tableView.getSelectionModel().getSelectedItem();
            //Remove the file from the list and the tableView
            data.remove(currentEntry);
            linkedList.remove(currentEntry);
            System.out.println(linkedList);
        }
    }
    
    /*
     * addFolderToList takes in a folder file. It then checks all the items in
     * that folder. If the item is a file it will add it to the list and table.
     * If the item is a folder it will call itself using that folder.
     *
     * input : File
     * output: Adds multiple items to the list and table.
     */
    private void addFolderToList(File folder){
        for (File file : folder.listFiles()){
            if(file.isDirectory()){
                addFolderToList(file);
            } else {
                String s = file.getName();
                int i = s.lastIndexOf('.');
                if(i>0){
                    extension = s.substring(i+1);
                }
                for (String type : types) {
                    if (extension.equals(type)) {
                        NameDirectory item = new NameDirectory(file.getName(),file.getAbsolutePath());
                        linkedList.add(item);
                        populateTableView(item);
                    }
                }
            }
        }
    }
    
    /*
     * createPlaylistContent will create all of the text in the new m3u8 file.
     * It uses the AbsolutePath of each of the files in the list and the path
     * of the save location to create a relative path for the file. Then, via
     * a StringBuilder, it adds each element from the list to the string then
     * returns that value as a string.
     *
     * input : None
     * output: String of every thing in the linkedList
     */
    private String createPlaylistContent(){
        StringBuilder sb = new StringBuilder();
        //Get path of the music file
        filePathAbsolute = Paths.get(linkedList.getFirst().getDirectory());
        //Get path of save directory
        filePathBase = Paths.get(savePath);
        //Create a relative path from that.
        filePathRelative = filePathBase.relativize(filePathAbsolute);
        //Append the first in the list (Fence Post problem)
        sb.append(filePathRelative);
        
        //Append everythign else separated by new lines
        for(int i = 1; i < linkedList.size(); i++){
            sb.append("\n");
            filePathAbsolute = Paths.get(linkedList.get(i).getDirectory());
            filePathRelative = filePathBase.relativize(filePathAbsolute);
            sb.append(filePathRelative);
        }
        System.out.println(sb);
        
        return sb.toString();
    }
    
    /*
     * populateTableView takes in a NameDirectory which is added into the table.
     * The back end of how the data is added is located in the <TableColumn> of
     * FXMLDocument.fxml
     *
     * input : NameDirectory
     * output: NameDirectory added to the tableView
     */
    private void populateTableView(NameDirectory entry){
        //Get the data
        ObservableList<NameDirectory> data = tableView.getItems();
        //Add the data
        data.add(entry);
        System.out.println(linkedList);
    }
    
    /*
     * setTreeView sets the tree view when given a directory to parse.
     * setTreeView first adds the file given to it (a folder) to the tree view.
     * setTreeView determines if the file given is a folder or not. If it is
     * a file then it checks to see if that file has one of the specified 
     * extension types thne adds that to the treeView. If it is a folder then
     * it will add that folder as a child of the superfodler and then call itself 
     * to add any files inside of that folder.
     *
     * input : File
     * output: TreeItem of NameDirectory type (Essentially the whole tree)
     */
    private TreeItem<NameDirectory> setTreeView(File directory) {
        //Create a NameDirectory from the folder
        NameDirectory folder = new NameDirectory(directory.getName(),directory.getAbsolutePath(),directory);
        //Sets that folder as the root for the tree
        TreeItem<NameDirectory> root = new TreeItem<>(folder);
        //For all the files in the directory
        for (File file : directory.listFiles()){
            //If the file is a directory then add that directory as a chile and call itself.
            if(file.isDirectory()){
                root.getChildren().add(setTreeView(file));
            }
            //Else the file is a file so check to see if it is of the right extension then add it to the treeView
            else{
                String s = file.getName();
                //Separate at the last .
                int i = s.lastIndexOf('.');
                //If there is a . in the file name.
                if(i>0){
                    //Get the extension
                    extension = s.substring(i+1);
                }
                //For all the extensions in the array
                for (String type : types) {
                    //If the current extension is equal to one in the array.
                    if (extension.equals(type)) {
                        //Add it as a child of the treeView
                        NameDirectory toAdd = new NameDirectory(file.getName(),file.getAbsolutePath());
                        root.getChildren().add(new TreeItem<>(toAdd));
                    }
                }
            }
        }
        return root;
    }
    
    /*
     * initialize is run AFTER FXMLDocument.fxml is loaded.
     * Currently it will make sure that the default formats are
     * loaded and ready for use as well as creating the linkedList
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formats=fileFormat.getText();
        types = formats.split("\\s*,\\s*");
        System.out.println(types);
        linkedList = new LinkedList<>();
    }    
    
}

