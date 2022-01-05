/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2SEModule/module-info.java to edit this template
 */

module m3u8Creator {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens org.kusev to javafx.fxml;
    
    exports org.kusev;
}
