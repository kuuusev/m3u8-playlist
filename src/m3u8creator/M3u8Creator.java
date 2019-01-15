/*
 * Name: Kevin S
 * Date: Feb. 13, 2017
 * Filename: M3u8Creator.java
 *
 * The start of the program which sets the stage and scene and loads
 * the FXML documents.
 */
package m3u8creator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class M3u8Creator extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //uses the FXMLDocument
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("M3U8");
        stage.setScene(scene);
        stage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}
