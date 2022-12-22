package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.example.BrowserApp.tabs;

public class BrowserHistory extends HBox
{
    public static List<Entry> myHistoryList;
    public static WebHistory history;
    public TableView<List<StringProperty>> tableViewHistory;
    public ArrayList<HistoryEntry> historyData;
    public GridPane tabContent=new GridPane();

    public BrowserHistory(WebView webView)
    {
        // Set Spacing
        this.setSpacing(4);

        // Set the Style-properties of the BrowserHistory navigation Bar
        this.setStyle("-fx-padding: 2;");

        // Create the WebHistory
        history = webView.getEngine().getHistory();

        // Create the Buttons
        Button backButton = new Button("\uD83E\uDCA6");
        backButton.setDisable(true);
        Button forwardButton = new Button("\uD83E\uDCA5");
        forwardButton.setDisable(true);
        Button historyButton=new Button("History");

        // Add an ActionListener to the Back and Forward Buttons
        backButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                history.go(-1);
            }
        });

        forwardButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                history.go(1);
            }
        });

        // Add an ChangeListener to the currentIndex property
        history.currentIndexProperty().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov,
                                final Number oldvalue, final Number newvalue)
            {
                int currentIndex = newvalue.intValue();

                if (currentIndex <= 0)
                {
                    backButton.setDisable(true);
                }
                else
                {
                    backButton.setDisable(false);
                }

                if (currentIndex >= history.getEntries().size())
                {
                    forwardButton.setDisable(true);
                }
                else
                {
                    forwardButton.setDisable(false);
                }
            }
        });

        // Add an ActionListener for the historyButton
        historyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tab historyTab=new Tab("History");
                tabContent.setHgap(10);
                tabContent.setVgap(10);
                tabContent.setPadding(new javafx.geometry.Insets(0, 10, 0, 10));
                readHistory();//read history from json file
                Label historyTitleLabel=new Label("History");
                historyTitleLabel.setFont(Font.font(30));
                tabContent.add(historyTitleLabel,2,2);
                historyTab.setContent(tabContent);
                tabs.getTabs().add(historyTab);
            }
        });

        // add BrowserHistory children elements
        this.getChildren().addAll(backButton, forwardButton,historyButton);
    }

    //save history to json file
    public static void saveHistory(){

        FileWriter fw;
        try{
            //read the content from json file first
            Gson gson=new Gson();
            FileReader fr = new FileReader("src/main/resources/history.json");
            java.lang.reflect.Type listType = new TypeToken<ArrayList<HistoryEntry> >(){}.getType();
            ArrayList<HistoryEntry> historyEntries = gson.fromJson(fr, listType);
            //check if there are no entries in json file
            if(historyEntries==null){
                historyEntries=new ArrayList<HistoryEntry>();
            }
            fr.close();

            fw=new FileWriter("src/main/resources/history.json");
            myHistoryList=history.getEntries();
            ArrayList<HistoryEntry> list= entryListToStringList(myHistoryList);
            for (HistoryEntry e:list) {
                historyEntries.add(e);
            }
            gson.toJson(historyEntries,fw);

            fw.flush();
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //read history from file
    public void readHistory(){
        FileReader fr;
        try{
            Gson gson=new Gson();
            fr=new FileReader("src/main/resources/history.json");
            java.lang.reflect.Type listType = new TypeToken<ArrayList<HistoryEntry>>(){}.getType();
            historyData = gson.fromJson(fr, listType);
            fr.close();
            createHistoryTable();//create historytableview
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //create tableView with history data
    public void createHistoryTable(){
        int rows = historyData.size();
        int cols = 3;//title, url and lastVisitedDate

        // show values in table
        tableViewHistory = new TableView<>();
        tableViewHistory.setPrefSize(800,500);
        ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();

        for (int c = 0; c < cols; c++) {
            String colTitle="Title";
            if(c==1){colTitle="Url";}
            if(c==2){colTitle="Visited Date";}
            TableColumn<List<StringProperty>, String> col = new TableColumn<>(colTitle);

            final int colIndex = c ;
            col.setCellValueFactory(item -> item.getValue().get(colIndex));
            tableViewHistory.getColumns().add(col);
        }

        for (int r = 0; r < rows; r++) {
            List<StringProperty> row = new ArrayList<>();

            for (int c = 0; c < 3; c++) {
                if(c==0){
                    row.add(new SimpleStringProperty(historyData.get(r).title));
                }
                else if(c==1){
                    row.add(new SimpleStringProperty(historyData.get(r).url));
                }
                else{
                    row.add(new SimpleStringProperty(String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS", historyData.get(r).date)));
                }
            }
            data.add(row);
        }

        tableViewHistory.setItems(data);
        tabContent.add(tableViewHistory,2,4);
    }
    //list<WebHistory.Entry> to ArrayList<String>
    public static ArrayList<HistoryEntry> entryListToStringList(List<Entry> list){
        ArrayList<HistoryEntry> result=new ArrayList<>();
        for (Entry e: list) {
            HistoryEntry hEntry=new HistoryEntry(e.getTitle(),e.getUrl(),e.getLastVisitedDate());
            result.add(hEntry);
        }
        return result;
    }

    public static class HistoryEntry{
        public String title;
        public String url;
        public Date date;
        public HistoryEntry(String t, String s, Date d){
            this.title=t;
            this.url=s;
            this.date=d;
        }
    }
}