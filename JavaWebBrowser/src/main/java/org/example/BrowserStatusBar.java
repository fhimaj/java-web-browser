package org.example;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

import java.awt.event.MouseEvent;
public class BrowserStatusBar extends HBox {

    public static String currentUrl="";
    public static Label urlText=new Label(currentUrl);
    public static Label protocol=new Label("Protocol: ");
    public static Label port=new Label("Port: ");
    public static ProgressBar progressBar=new ProgressBar();
    public static boolean isStatusBarHovered=false;

    public BrowserStatusBar(String url, String p, int portIn){

        this.currentUrl+=url;
        urlText.setText("Page: "+this.currentUrl);
        protocol.setText("Protocol: "+p);
        port=new Label("Port: "+portIn);

        // Set the Style-properties of the status Bar
        this.setStyle("-fx-padding: 10;" +
                "-fx-opacity: 0;"+
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 1;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: blue;");

        //show status bar on hover
        this.hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                   BrowserStatusBar.this.setStyle("-fx-padding: 10;" +
                           "-fx-opacity: 100;"+
                           "-fx-border-style: solid inside;" +
                           "-fx-border-width: 1;" +
                           "-fx-border-insets: 5;" +
                           "-fx-border-radius: 2;" +
                           "-fx-border-color: transparent;");
                   BrowserStatusBar.isStatusBarHovered=true;
            }
        });
        //hide statusBar after mouse exits
        this.setOnMouseExited(mouseEvent -> {
            // Set the Style-properties of the status Bar
            this.setStyle("-fx-padding: 10;" +
                    "-fx-opacity: 0;"+
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 1;" +
                    "-fx-border-insets: 5;" +
                    "-fx-border-radius: 2;" +
                    "-fx-border-color: blue;");
            BrowserStatusBar.isStatusBarHovered=false;
                });

       //set position to bottom left
        this.setAlignment(Pos.BOTTOM_LEFT);

        //add all elements to status bar
        this.getChildren().addAll(progressBar,urlText,protocol,port);
    }
    public static void setUrlText(String url){
        urlText.setText("Page: "+url);
    }
    public static void setProtocol(String prot){
        protocol.setText("Protocol: "+prot);
    }
    public static void setPort(int p){
        port.setText("Port: "+p);
    }
}
