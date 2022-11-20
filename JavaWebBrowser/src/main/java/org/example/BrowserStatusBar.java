package org.example;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class BrowserStatusBar extends HBox {

    public static String currentUrl="Page: ";
    public static Label urlText=new Label(currentUrl);
    public static Label protocol=new Label("Protocol: ");
    public static Label port=new Label("Port: ");

    public BrowserStatusBar(String url, String p, int portIn){

        this.currentUrl+=url;
        urlText.setText(url);
        protocol=new Label(p);
        port=new Label(""+portIn);

        this.setSpacing(10);

        // Set the Style-properties of the status Bar
        this.setStyle("-fx-padding: 7;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 1;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 2;" +
                "-fx-border-color: blue;");

       //set position to bottom left
        this.setAlignment(Pos.BOTTOM_LEFT);

        //add all elements to status bar
        this.getChildren().addAll(urlText,protocol,port);
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
