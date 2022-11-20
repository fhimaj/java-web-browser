package org.example;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class BrowserApp extends Application
{
	//set homepage url
	private String homePageUrl = "http://www.uni-prizren.com";

	@Override
	public void start(final Stage stage)
	{
		// Create the WebView
		WebView webView = new WebView();

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

		// Create the Navigation Bar
		BrowserNavigationBar navigationBar = new BrowserNavigationBar(webView, homePageUrl, true);

		// Create the VBox
		VBox root = new VBox(navigationBar, webView);

		// Set the Style-properties of the VBox
		root.setStyle("-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" +
				"-fx-border-radius: 5;" +
				"-fx-border-color: gray;");

		// Create the Scene
		Scene scene = new Scene(root);
		// Add the Scene to the Stage
		stage.setScene(scene);
		// Display the Stage
		stage.show();
	}

	public static void main(String[] args)
	{
		Application.launch(args);
	}
}