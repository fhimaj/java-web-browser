package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Settings extends HBox {

    //Add a bookmarks list
    public static List<Bookmark> bookmarks = new ArrayList<Bookmark>();

    //Method to add a new bookmark
    public static void addBookmark(String name, String url) {
        bookmarks.add(new Bookmark(name, url));
    }

    //Method to show the bookmarks list
    public void showBookmarks() {
        // create a new window to display the bookmarks
        Stage bookmarksWindow = new Stage();
        bookmarksWindow.setTitle("Bookmarks");
        bookmarksWindow.setHeight(400);
        bookmarksWindow.setWidth(400);
        VBox root = new VBox();
        Scene scene = new Scene(root);

        //read bookmarks
        readBookmarks();
        //Create a list view to show the bookmarks
        ListView<Bookmark> bookmarksListView = new ListView<>();

        if(bookmarks == null){
            Bookmark b=new Bookmark("Name","Url");
            bookmarks=new ArrayList<>();
            bookmarks.add(b);
        }
        bookmarksListView.setItems(FXCollections.observableArrayList(bookmarks));
        bookmarksListView.setCellFactory(param -> new ListCell<Bookmark>() {
            @Override
            protected void updateItem(Bookmark bookmark, boolean empty) {
                super.updateItem(bookmark, empty);

                if (empty || bookmark == null) {
                    setText(null);
                } else {
                    setText(bookmark.getName() + " - " + bookmark.getUrl());
                }
            }
        });

        //Add the list view to the window
        root.getChildren().add(bookmarksListView);

        //Show the window
        bookmarksWindow.setScene(scene);
        bookmarksWindow.show();
    }

    //Method to show the settings window
    public void showSettings() {
        //Create a new window for the settings
        Stage settingsWindow = new Stage();
        settingsWindow.setTitle("Settings");
        VBox root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);
        settingsWindow.setWidth(400);
        settingsWindow.setHeight(400);

        //Create the addBookmarkButton
        Button addBookmarkButton = new Button("Add Bookmark");
        addBookmarkButton.setPadding(new Insets(10));
        addBookmarkButton.setPrefWidth(150);
        addBookmarkButton.setPrefHeight(40);

        //Create the showBookmarksButton
        Button showBookmarksButton = new Button("Show Bookmarks");
        showBookmarksButton.setPadding(new Insets(10));
        showBookmarksButton.setPrefWidth(150);
        showBookmarksButton.setPrefHeight(40);

        //Event to create a bookmark
        addBookmarkButton.setOnAction(event -> {
            //Create a dialog to prompt the user for the bookmark name
            TextInputDialog nameDialog = new TextInputDialog("Name");
            nameDialog.setTitle("Bookmark Name");
            nameDialog.setHeaderText("Enter the name for the bookmark:");

            //Show the dialog and get the bookmark name
            Optional<String> nameResult = nameDialog.showAndWait();
            if (nameResult.isPresent()) {
                String name = nameResult.get();

                //Create a dialog to prompt the user for the bookmark URL
                TextInputDialog urlDialog = new TextInputDialog("URL");
                urlDialog.setTitle("Bookmark URL");
                urlDialog.setHeaderText("Enter the URL for the bookmark:");

            //Show the dialog and get the bookmark URL
            Optional<String> urlResult = urlDialog.showAndWait();
            if (urlResult.isPresent()) {
                String url = urlResult.get();
                addBookmark(name,url);
                saveBookmarks();
            }

        }});

        //Event to show the Bookmarks
        showBookmarksButton.setOnAction(event -> {showBookmarks();});

        //Add the showBookmarksButton , addBookmarkButton to root
        root.getChildren().add(addBookmarkButton);
        root.getChildren().add(showBookmarksButton);

        //Show the window
        settingsWindow.setScene(scene);
        settingsWindow.show();
    }

    public static void readBookmarks(){
        String filePath="src/main/resources/bookmarks.json";
        bookmarks=new ArrayList<>();
        FileReader fr;
        try{
            Gson gson=new Gson();
            //check if file exists before reading it
            File f = new File(filePath);
            if(f.exists() && !f.isDirectory()) {
                fr = new FileReader(filePath);
                java.lang.reflect.Type listType = new TypeToken<ArrayList<Bookmark> >(){}.getType();
                bookmarks = gson.fromJson(fr, listType);
                fr.close();
            }
            else{//there is no history file, create an empty json file
                File fi=new File(filePath);
                fi.createNewFile();
                fr=new FileReader(filePath);
                java.lang.reflect.Type listType = new TypeToken<ArrayList<Bookmark>>(){}.getType();
                bookmarks = gson.fromJson(fr, listType);
                fr.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void saveBookmarks(){
        String filePath="src/main/resources/bookmarks.json";
        ArrayList<Bookmark> bookmarkEntries;
        FileWriter fw;
        try{
            //read the content from json file first
            Gson gson=new Gson();
            //check if file exists before reading it
            File f = new File(filePath);
            if(f.exists() && !f.isDirectory()) {
                FileReader fr = new FileReader(filePath);
                java.lang.reflect.Type listType = new TypeToken<ArrayList<Bookmark> >(){}.getType();
                bookmarkEntries = gson.fromJson(fr, listType);
                //check if there are no entries in json file
                if(bookmarkEntries==null){
                    bookmarkEntries=new ArrayList<>();
                }
                fr.close();
            }
            else{
                bookmarkEntries=new ArrayList<>();
            }

            fw=new FileWriter(filePath);
            //add only last bookmark if it's not already added
            if(!bookmarkEntries.contains(bookmarks.get(bookmarks.size()-1))){
                bookmarkEntries.add(bookmarks.get(bookmarks.size()-1));
            }
            gson.toJson(bookmarkEntries,fw);

            fw.flush();
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
