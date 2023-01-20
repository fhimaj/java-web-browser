package org.example;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

import java.util.*;

import static org.example.Settings.bookmarks;

public class BrowserBookmarkBar extends HBox {
    private Button bookmarkLabel;
    public String url;
    private ArrayList<Button> bookmarksBtns=new ArrayList<>();
    private ArrayList<String> bookmarksUrls=new ArrayList<>();

    public BrowserBookmarkBar(WebView webView){
        this.setSpacing(10);

        // Set the Style-properties of the bookmarksBar
        this.setStyle("-fx-padding: 10;");
        this.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE,null,null)));

        //read bookmarks
        Settings.readBookmarks();
        //add bookmarks
        addBookmarks();
        //show bookmark to bar
        showButtonsBookmark();
    }

    public void addBookmarks(){
        if(Settings.bookmarks!=null){
            for(Bookmark b:Settings.bookmarks){
                url=b.getUrl();
                bookmarkLabel=new Button(b.getName());
                bookmarkLabel.setMaxWidth(80);
                bookmarkLabel.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE,null,null)));
                bookmarksBtns.add(bookmarkLabel);
                bookmarksUrls.add(url);
            }
        }
    }

    public void showButtonsBookmark(){
        for(Button b:bookmarksBtns){
            b.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>()
            {
                @Override
                public void handle(javafx.event.ActionEvent event) {
                    BrowserApp.webView.getEngine().load(bookmarksUrls.get(bookmarksBtns.indexOf(b)));
                }
            });
            this.getChildren().add(b);
        }

    }

    public void addLastBookmark(){
        Bookmark b=Settings.bookmarks.get(bookmarks.size()-1);
        url=b.getUrl();
        bookmarkLabel=new Button(b.getName());
        bookmarkLabel.setMaxWidth(80);
        bookmarkLabel.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE,null,null)));
        bookmarksBtns.add(bookmarkLabel);
        bookmarksUrls.add(url);
    }

    public void showLastButtonBookmark(){
        Button b=bookmarksBtns.get(bookmarksBtns.size()-1);
        b.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>()
        {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                BrowserApp.webView.getEngine().load(bookmarksUrls.get(bookmarksBtns.indexOf(b)));
            }
        });
        this.getChildren().add(b);
    }

}
