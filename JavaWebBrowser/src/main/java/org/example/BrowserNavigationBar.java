package org.example;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowserNavigationBar extends HBox {
    public String protocol = "\t\tProtocol: ";
    public TextField pageUrl;
    public int port ;
    boolean reloaded=false;
    public BrowserNavigationBar(WebView webView, String homePageUrl, boolean goToHomePage)
    {
        // Set Spacing
        this.setSpacing(4);

        // Set the Style-properties of the Navigation Bar
        this.setStyle("-fx-padding: 10;" );

        // Create the WebEngine
        WebEngine webEngine = webView.getEngine();

        // Create the TextField
        pageUrl = new TextField();

        // Create the Buttons
        Button refreshButton = new Button("\u27F3");
        Button goButton = new Button("\uD83D\uDD0E");
        Button homeButton = new Button("\uD83C\uDFE0");
        Button bookmarkButton=new Button("\u2605");

        // Let the TextField grow horizontallly
        HBox.setHgrow(pageUrl, Priority.ALWAYS);

        // Add an ActionListener to navigate to the entered URL
        pageUrl.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(parseUrl(pageUrl.getText())){
                    webEngine.load(pageUrl.getText());
                    BrowserStatusBar.setUrlText(pageUrl.getText());
                    BrowserStatusBar.setPort(port);
                    BrowserStatusBar.setProtocol(protocol);
                    BrowserHistory.saveHistory();
                }
            }
        });

        // Update the stage title when a new web page title is available
        webEngine.locationProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> ov,
                                final String oldvalue, final String newvalue)
            {
                // Set the Title of the Stage
                pageUrl.setText(newvalue);
            }
        });

        //add actionlistener to bookmarkButton, if pressed add bookmark to bookmark list and save it to file
        bookmarkButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Bookmark b=new Bookmark(webEngine.getTitle(),pageUrl.getText());
                Settings.readBookmarks();
                if(Settings.bookmarks==null){
                    Settings.bookmarks=new ArrayList<>();
                    Settings.addBookmark(b.getName(),b.getUrl());
                    Settings.saveBookmarks();
                }
                else if(!Settings.bookmarks.contains(b)){
                    Settings.addBookmark(b.getName(),b.getUrl());
                    Settings.saveBookmarks();
                }
            }
        });

        // Add an ActionListener for the Refresh Button
        refreshButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                reloaded=true;
                if(parseUrl(pageUrl.getText())){
                    webEngine.reload();
                    BrowserStatusBar.setUrlText(pageUrl.getText());
                    BrowserStatusBar.setPort(port);
                    BrowserStatusBar.setProtocol(protocol);
                }
            }
        });

        // Add an ActionListener for the Go Button
        goButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(parseUrl(pageUrl.getText())){
                    webEngine.load(pageUrl.getText());
                    BrowserStatusBar.setUrlText(pageUrl.getText());
                    BrowserStatusBar.setPort(port);
                    BrowserStatusBar.setProtocol(protocol);
                    BrowserHistory.saveHistory();
                }
            }
        });
        // Add an ActionListener for the Home Button
        homeButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(parseUrl(homePageUrl)){
                    webEngine.load(homePageUrl);
                    BrowserStatusBar.setUrlText(pageUrl.getText());
                    BrowserStatusBar.setPort(port);
                    BrowserStatusBar.setProtocol(protocol);
                    BrowserHistory.saveHistory();
                }
            }
        });

        // Add the Children to the Navigation Bar
        this.getChildren().addAll(bookmarkButton,pageUrl,goButton, refreshButton, homeButton);

        if (goToHomePage)
        {
            // Load the URL
            if(parseUrl(homePageUrl)){
                webEngine.load(homePageUrl);
                BrowserStatusBar.setUrlText(pageUrl.getText());
                BrowserStatusBar.setPort(port);
                BrowserStatusBar.setProtocol(protocol);
                BrowserHistory.saveHistory();
            }
        }
    }
    //parses url and extracts url information
    public boolean parseUrl(String url){
        URL myUrl;
        String myString="http://";
        String googleQuery="https://www.google.com/search?q=";
        //temporary solution
        List<String> urlSuffixes=Arrays.asList(".com",".org",".net",".edu",".mil",".tech",".info",".al",".uk",".us");
        // Create a URL
        try {
            //so url isn't duplicated if all above conditions are not met on refresh
            if(reloaded) {
                return true;
            }
            else{
                if(!url.contains(".") || url.contains(" ")){
                    pageUrl.setText(googleQuery+url);
                    myUrl=new URL(googleQuery+url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    return true;
                }
                if(!urlSuffixes.contains(url.substring((url.lastIndexOf("."))))){
                    pageUrl.setText(googleQuery+url);
                    myUrl=new URL(googleQuery+url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    return true;
                }
                if(!url.contains("www") && !url.contains("http") && !url.contains("https")){
                    myString+="www."+url;
                    pageUrl.setText(myString);
                    myUrl=new URL(googleQuery+url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    return true;
                }
                if(!url.contains("http") && !url.contains("https")){
                    myString+=url;
                    pageUrl.setText(myString);
                    myUrl=new URL(googleQuery+url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    return true;
                }

                pageUrl.setText(googleQuery+url);
                myUrl=new URL(googleQuery+url);
                protocol = myUrl.getProtocol();
                port = myUrl.getPort();
                return true;
            }

        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        }
        return false;
    }
}
