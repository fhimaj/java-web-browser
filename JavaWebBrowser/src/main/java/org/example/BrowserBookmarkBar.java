package org.example;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

import java.awt.event.*;
import java.beans.EventHandler;
import java.util.*;

public class BrowserBookmarkBar extends HBox {
    private Bookmark bookmark;
    private Button bookmarkLabel;
    public String url;
    private HashMap<String,String> bookmarks=new HashMap<>();

    public BrowserBookmarkBar(WebView webView){
        this.setSpacing(10);

        // Set the Style-properties of the bookmarksBar
        this.setStyle("-fx-padding: 10;");
        this.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE,null,null)));

        //read bookmarks
        Settings.readBookmarks();

        //
        if(Settings.bookmarks!=null){
            for(Bookmark b:Settings.bookmarks){
                System.out.println("reading from: "+b.getUrl());
                url=b.getUrl();
                bookmarkLabel=new Button(b.getName());
                bookmarkLabel.setMaxWidth(80);
                bookmarkLabel.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE,null,null)));
                bookmarkLabel.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>()
                {
                    @Override
                    public void handle(javafx.event.ActionEvent event)
                    {
                        System.out.println("url of: "+bookmarks.get(bookmarkLabel.getText()));
                        webView.getEngine().load(bookmarks.get(bookmarkLabel.getText()));
                    }
                });
                bookmarks.put(bookmarkLabel.getText(),url);
                this.getChildren().add(bookmarkLabel);
            }
        }

    }

}
