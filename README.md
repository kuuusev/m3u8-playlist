# README

![m3u8](https://i.imgur.com/gLzoA5R.png)

## Background Information

With JDK11, Oracle removed JavaFX from the kit so we need to include a bunch of things in order to get it working. The previous version used JDK 8 which included JavaFX and as such worked fine if you had Java8.

With this removal, the old jar file would not run as it was missing the JavaFX libraries, so I re-compiled the program to include these features.

This current version uses Java JDK 17.0.1 and JavaFX 17.0.1 (October 2021)

## Compiling

I used Netbeans with Ant and created a modular version of the program following this tutorial: [https://openjfx.io/openjfx-docs/#install-javafx](https://openjfx.io/openjfx-docs/#install-javafx)

Setup a Project as defined in the tutorial (JavaFX and NetBeans -> Modular from IDE) and with the correct settings.

## Running

In order to get it to run, a custom runtime image is created with Jlink.

The output is at dist\jlinik\m3u8Creator\bin\m3u8Creator and running the m3u8Creator.bat file runs the program.

## Functionality

This program allows the creation of **relative** M3U8 files for use in playlists where one has a number of local files. As it is relative this means that the generated M3U8 file can **NOT** be moved once created.

### Opening

To get started, either Open a folder to where one's music files are located or Open an existing M3U8 file.

Opening a folder creates a file tree structure of all subfolders while opening an exisitng M3U8 file can be done to edit an existing one or to change the relative paths by re-saving the file in a different spot.

### Viewports

The left hand field displays a list representation of the file structure from the opened folder and the right displays a table of either the opened M3U8 file or an empty M3U8 file.

### Add Files

Add files to the playlist by double clicking a folder on the left hand side (which recursively adds every file of the filtered type) into the playlist or by expanding the folder until individual files are visible and double clicking them to add them. This can also be done by single clicking on a folder/file and pressing ENTER.

### Remove Files

Removing files from the playlist can be done by selecting them in the right hand pane and pressing DELETE.

Currently, there is no way to rearrange the playlist files once they are added.

### Filter

The file formats field acts a filter to remove any extraneous files that are not of the specified file format from the folder view on the left hand side. If there are formats not specified that one would like, simply add them separated by commas (no spaces) and then push "Update" to refresh the folder listing.

### Saving and Creating

The file name field allows the specification of the playlist M3U8 name and the "Save At" button specifies a location to save the playlist file at. Note that this file location is how the relative path's will be based off of so ensure that this location is the desired spot.

After the playlist is created, filename is given and location specified press the "Create" button to make the file.

## Resources

JavaFX SDK and JavaFX jmods can be found at: 

https://gluonhq.com/products/javafx/
