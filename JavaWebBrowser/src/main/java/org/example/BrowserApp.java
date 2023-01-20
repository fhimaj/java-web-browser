package org.example;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class BrowserApp extends Application
{
	//set homepage url
	public static String homePageUrl;
	public static String searchEngine;
	private BrowserNavigationBar navigationBar;
	public BrowserStatusBar statusBar;
    Settings settings = new Settings();
	public static TabPane tabs=new TabPane();
	public BrowserBookmarkBar browserBookmarksBar;
	public static WebView webView;


	@Override
	public void start(final Stage stage)
	{
		//read configurations first
		Settings.readConfig();
		//set homepage
		homePageUrl = Settings.homepage;
		//set searchEngine
		searchEngine = Settings.currentSearchEngine;

		// Create the WebView
		webView = new WebView();
		// Create the VBox
		VBox root = new VBox(tabs);
		// Create the Scene
		Scene scene = new Scene(root);

		// Update the stage title when a new web page title is available
		webView.getEngine().titleProperty().addListener(new ChangeListener<String>()
		{
			public void changed(ObservableValue<? extends String> ov,
								final String oldvalue, final String newvalue)
			{
				// Set the Title of the Stage
				stage.setTitle(newvalue);
			}
		});
		// Create BrowserHistory
		BrowserHistory browserHistory = new BrowserHistory(webView);

		//create bookmarksbar
		browserBookmarksBar=new BrowserBookmarkBar(webView);

		// Create the Navigation Bar
		navigationBar = new BrowserNavigationBar(webView, homePageUrl, true,browserBookmarksBar);
		//add browserHistory to navogation bar
		navigationBar.getChildren().add(browserHistory);
		navigationBar.setPrefWidth(stage.getWidth());
		//Create the status bar
		statusBar=new BrowserStatusBar(
				homePageUrl,navigationBar.protocol,navigationBar.port);

		//Create the Button to show Settings
		Button showSettingsButton = new Button();
        	showSettingsButton.setText("\uD83D\uDD27");
        	showSettingsButton.setOnAction(event -> {
            	Label settingsLabel=new Label("Settings");
            	settingsLabel.setFont(Font.font(30));
            	settings.showSettings();
        	});
       		 navigationBar.getChildren().add(showSettingsButton);

		//create a layout manager
		GridPane tabContent=new GridPane();
		tabContent.setHgap(10);
		tabContent.setVgap(10);
		tabContent.setPadding(new javafx.geometry.Insets(0, 10, 0, 10));
		tabContent.add(navigationBar,0,0);
		tabContent.add(browserBookmarksBar,0,1);
		tabContent.add(webView,0,2);
		tabContent.add(statusBar,0,3);
		//create a tab
		Tab homeTab=new Tab("Homepage");
		homeTab.setContent(tabContent);
		tabs.getTabs().add(homeTab);

		// Set the Style-properties of the VBox
		root.setStyle("-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" +
				"-fx-border-radius: 5;" +
				"-fx-border-color: gray;");

		//set initial size of stage
		stage.setMaximized(true);

		// Add the Scene to the Stage
		stage.setScene(scene);
		// Display the Stage
		stage.show();

		//set initial size of navigationBar,statusBar and webView
		navigationBar.setPrefWidth(stage.getWidth());
		statusBar.setPrefWidth(stage.getWidth());
		webView.setPrefWidth(stage.getWidth());

		//listen for changes in width or height size
		stage.widthProperty().addListener((obs, oldVal, newVal) -> {
			// change width of navigationBar,StatusBar and WebView
			navigationBar.setPrefWidth(stage.getWidth());
			statusBar.setPrefWidth(stage.getWidth());
			webView.setPrefWidth(stage.getWidth());
		});

		stage.heightProperty().addListener((obs, oldVal, newVal) -> {
			// change height of WebView
			webView.setPrefHeight(stage.getHeight());
		});
	}

	public static void main(String[] args)
	{
		Application.launch(args);

	}

}
