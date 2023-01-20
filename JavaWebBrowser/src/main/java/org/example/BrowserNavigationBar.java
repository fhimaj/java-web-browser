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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrowserNavigationBar extends HBox{
    public String protocol;
    public TextField pageUrl;
    public int port ;
    boolean reloaded=false;
    private boolean urlWasChanged=false;
    private BrowserBookmarkBar bookmarksBar;
    public BrowserNavigationBar(WebView webView, String homePageUrl, boolean goToHomePage,BrowserBookmarkBar browserBookmarkBar)
    {
        // Set Spacing
        this.setSpacing(4);

        // Set the Style-properties of the Navigation Bar
        this.setStyle("-fx-padding: 10;" );

        // Create the WebEngine
        WebEngine webEngine = webView.getEngine();

        bookmarksBar=browserBookmarkBar;

        // Create the TextField
        pageUrl = new TextField();

        // Create the Buttons
        Button refreshButton = new Button("\u27F3");
        Button goButton = new Button("\uD83D\uDD0E");
        Button homeButton = new Button("\uD83C\uDFE0");
        Button bookmarkButton=new Button("\u2605");
        Button zoomInButton = new Button("+");
        Button zoomOutButton = new Button("-");

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
                    urlWasChanged=true;
                }
            }
        });

       pageUrl.textProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
               urlWasChanged=true;
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
                    bookmarksBar.addLastBookmark();
                    bookmarksBar.showLastButtonBookmark();
                }
                else if(!Settings.bookmarks.contains(b)){
                    Settings.addBookmark(b.getName(),b.getUrl());
                    Settings.saveBookmarks();
                    bookmarksBar.addLastBookmark();
                    bookmarksBar.showLastButtonBookmark();
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
                    urlWasChanged=false;
                }
            }
        });

        // Add an ActionListener for the Go Button
        goButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(urlWasChanged){
                    if(parseUrl(pageUrl.getText())){
                        webEngine.load(pageUrl.getText());
                        BrowserStatusBar.setUrlText(pageUrl.getText());
                        BrowserStatusBar.setPort(port);
                        BrowserStatusBar.setProtocol(protocol);
                        BrowserHistory.saveHistory();

                        urlWasChanged=false;
                    }
                }
            }
        });
        // Add an ActionListener for the Home Button
        homeButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(parseUrl(BrowserApp.homePageUrl)){
                    webEngine.load(BrowserApp.homePageUrl);
                    BrowserStatusBar.setUrlText(pageUrl.getText());
                    BrowserStatusBar.setPort(port);
                    BrowserStatusBar.setProtocol(protocol);
                    BrowserHistory.saveHistory();

                    urlWasChanged=false;
                }
            }
        });


        // add actionlistener for zoomInButton
        zoomInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (webView.getZoom() < 2) {
                    webView.setZoom(webView.getZoom() + 0.1);
                }
            }
        });

        // add actionlistener for zoomOutButton
        zoomOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (webView.getZoom() > 0.1) {
                    webView.setZoom(webView.getZoom() - 0.1);
                }
            }
        });

        // Add the Children to the Navigation Bar
        this.getChildren().addAll(zoomInButton,zoomOutButton,bookmarkButton,pageUrl,goButton, refreshButton, homeButton);

        if (goToHomePage)
        {
            // Load the URL
            if(parseUrl(homePageUrl)){
                webEngine.load(homePageUrl);
                BrowserStatusBar.setUrlText(pageUrl.getText());
                BrowserStatusBar.setPort(port);
                BrowserStatusBar.setProtocol(protocol);
                BrowserHistory.saveHistory();

                urlWasChanged=false;
            }
        }
    }
    //parses url and extracts url information
    public boolean parseUrl(String url){
        URL myUrl;
        String myString="http://";
        String searchEngineQuery=BrowserApp.searchEngine;
        //temporary solution
        List<String> urlSuffixes=Arrays.asList(".com",".org",".net",".edu",".mil",".tech",".info",".al",".uk",".us");
        // Create a URL
        try {
            //so url isn't duplicated if all above conditions are not met on refresh
            if (!reloaded) {
                if (!url.contains(".") || url.contains(" ")) {
                    pageUrl.setText(searchEngineQuery + url);
                    myUrl = new URL(searchEngineQuery + url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    reloaded=false;
                    return true;
                }
                if (!urlSuffixes.contains(url.substring((url.lastIndexOf("."))))) {
                    pageUrl.setText(searchEngineQuery + url);
                    myUrl = new URL(searchEngineQuery + url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    reloaded=false;
                    return true;
                }
                if (!url.contains("www") && !url.contains("http") && !url.contains("https")) {
                    myString += "www." + url;
                    pageUrl.setText(myString);
                    myUrl = new URL(searchEngineQuery + url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    reloaded=false;
                    return true;
                }
                if (!url.contains("http") && !url.contains("https")) {
                    myString += url;
                    pageUrl.setText(myString);
                    myUrl = new URL(searchEngineQuery + url);
                    protocol = myUrl.getProtocol();
                    port = myUrl.getPort();
                    reloaded=false;
                    return true;
                }

                /*pageUrl.setText(searchEngineQuery + url);
                myUrl = new URL(searchEngineQuery + url);
                protocol = myUrl.getProtocol();
                port = myUrl.getPort();*/
                reloaded=false;
                return true;
            }
            reloaded=false;
            return true;

        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        }
        return false;
    }
}
