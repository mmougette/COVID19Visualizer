package application;

////////////////////////////////////////////////////////////
//
//Title: COVID19 Visualizer
//
//Files: Country.java, Main.java
//
//Author: Maxwell Mougette, mougette@wisc.edu
//
///////////////////////////////////////////////////////////

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import application.Country.DayNode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Main Class to drive the application.
 * @author mougette
 */
public class Main extends Application {
    private List<String> args;
    public static ArrayList<Country> countryList;

    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 450;
    private static final String APP_TITLE = "COVID19 Visualizer";
    
    @Override
    public void start(Stage primaryStage) throws Exception {
      
      HBox hbox = new HBox();
      hbox.setAlignment(Pos.BASELINE_CENTER);
      
      /**
       * Right Side - Title, Options & BUttons
       */
      VBox rightSide = new VBox(10);
      rightSide.setAlignment(Pos.CENTER_RIGHT);
      rightSide.setPadding(new Insets(10, 20, 20, 10));
      rightSide.setMinHeight(WINDOW_HEIGHT);
      rightSide.setMinWidth(250);
      
      Label label = new Label("COVID19 Visualizer");
      label.setPadding(new Insets(0, 0, 0, 10));
      label.setFont(Font.font("Amble CN", FontWeight.BOLD, 24));
      label.setTextFill(Color.web("#ffff00"));
      rightSide.getChildren().add(label);
      
      // List of days taken from csv file
      List<DayNode> ds = countryList.get(0).getDays();
      // Options of countries and dates taken from CSV file
      String[] countryComboBoxOptions = new String[countryList.size()];
      String[] startDateComboBoxOptions = new String[ds.size()];
      String[] endDateComboBoxOptions = new String[ds.size()];
      
      // Loop through and populate comboBoxOptions
      for (int i = 0; i < countryList.size(); i++) {
        countryComboBoxOptions[i] = countryList.get(i).name;
      }
      
      for (int i = 0; i < ds.size(); i++) {
        startDateComboBoxOptions[i] = ds.get(i).date;
        endDateComboBoxOptions[i] = ds.get(i).date;    
      }
      
      
      ComboBox countryComboBox = new ComboBox(FXCollections.observableArrayList(countryComboBoxOptions));
      countryComboBox.setValue("Select Country");
      countryComboBox.setMinWidth(150);
      countryComboBox.setMaxWidth(150);
      rightSide.getChildren().add(countryComboBox);
      
      
      ComboBox startDateComboBox = new ComboBox(FXCollections.observableArrayList(startDateComboBoxOptions));
      startDateComboBox.setValue("Select Satrt Date");
      startDateComboBox.setMinWidth(150);
      startDateComboBox.setMaxWidth(150);
      rightSide.getChildren().add(startDateComboBox);
      
      ComboBox endDateComboBox = new ComboBox(FXCollections.observableArrayList(startDateComboBoxOptions));
      endDateComboBox.setValue("Select End Date");
      endDateComboBox.setMinWidth(150);
      endDateComboBox.setMaxWidth(150);
      rightSide.getChildren().add(endDateComboBox);
      
      Button trackButton = new Button("Track No. Cases");
      rightSide.getChildren().add(trackButton);
      trackButton.setMinWidth(150);
      trackButton.setMaxWidth(150);
      trackButton.setPadding(new Insets(10, 10, 10, 10));
      
      Button trackButton1 = new Button("Track No. Deaths");
      rightSide.getChildren().add(trackButton1);
      trackButton1.setMinWidth(150);
      trackButton1.setMaxWidth(150);
      trackButton1.setPadding(new Insets(10, 10, 10, 10));
      
      Button trackbutton2 = new Button("Track Deaths to Cases");
      rightSide.getChildren().add(trackbutton2);
      trackbutton2.setMinWidth(150);
      trackbutton2.setMaxWidth(150);
      trackbutton2.setPadding(new Insets(10, 10, 10, 10));
      
      Button trackbutton3 = new Button("Track Cases to Pop");
      rightSide.getChildren().add(trackbutton3);
      trackbutton3.setMinWidth(150);
      trackbutton3.setMaxWidth(150);
      trackbutton3.setPadding(new Insets(10, 10, 10, 10));
      
      Button trackButton4 = new Button("100 L. Sq. Regression");
      rightSide.getChildren().add(trackButton4);
      trackButton4.setMinWidth(150);
      trackButton4.setMaxWidth(150);
      trackButton4.setPadding(new Insets(10, 10, 10, 10));
      
      Button clearButton = new Button("Clear Graph");
      rightSide.getChildren().add(clearButton);
      clearButton.setMinWidth(150);
      clearButton.setMaxWidth(150);
      clearButton.setPadding(new Insets(10, 10, 10, 10));
      
      
      /*
       * LeftSide - Graph To Display Data
       */
      VBox leftSide = new VBox();
      leftSide.setAlignment(Pos.CENTER_LEFT);
      
      NumberAxis xAxis = new NumberAxis();
      xAxis.setLabel("Days");

      NumberAxis yAxis = new NumberAxis();
      yAxis.setLabel("No. Cases");

      LineChart lineChart = new LineChart(xAxis, yAxis);

      leftSide.getChildren().add(lineChart);
      
      /**
       * EventHandler for when Track Button is pressed.
       * Populates Line Chart based on given inputs.
       */
      EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e) 
        {
          yAxis.setLabel("No. Cases");
          
          String country = (String) countryComboBox.getValue();
          String start = (String) startDateComboBox.getValue();
          String end = (String) endDateComboBox.getValue();
          
          System.out.println(country);

          XYChart.Series dataSeries = new XYChart.Series();
          dataSeries.setName(country);
          
          int k = 0;
          int m = 1;
          for (int i = 0; i < ds.size(); i++) {
            if (ds.get(i).date.contentEquals(start)) k = i;
            if (ds.get(i).date.contentEquals(end)) m = i;
          }
          
          for (int i = 0; i < countryList.size(); i++) {
            if (countryList.get(i).name.contentEquals(country)) {
              List<DayNode> dayList = countryList.get(i).getDays();
              for (int j = k; j < m; j++) {
                int numCases = dayList.get(j).numCases;
                dataSeries.getData().add(new XYChart.Data(j , numCases));
              } 
            }
          }

          lineChart.getData().add(dataSeries);
        } 
       };
      
       trackButton.setOnAction(event);
       
       /**
        * Event Handler to track the number of deaths.
        */
       EventHandler<ActionEvent> trackDeaths = new EventHandler<ActionEvent>() { 
         public void handle(ActionEvent e) 
         {
             yAxis.setLabel("No. Deaths");
           
             String country = (String) countryComboBox.getValue();
             String start = (String) startDateComboBox.getValue();
             String end = (String) endDateComboBox.getValue();
             
             System.out.println(country);

             XYChart.Series dataSeries = new XYChart.Series();
             dataSeries.setName(country);
             
             int k = 0;
             int m = 1;
             for (int i = 0; i < ds.size(); i++) {
               if (ds.get(i).date.contentEquals(start)) k = i;
               if (ds.get(i).date.contentEquals(end)) m = i;
             }
             
             for (int i = 0; i < countryList.size(); i++) {
               if (countryList.get(i).name.contentEquals(country)) {
                 List<DayNode> dayList = countryList.get(i).getDays();
                 for (int j = k; j < m; j++) {
                   int numDeaths = dayList.get(j).numDeaths;
                   
                   dataSeries.getData().add(new XYChart.Data(j , numDeaths));
                 } 
               }
             }

             lineChart.getData().add(dataSeries);
             
           
           
         } 
        };
        
        trackButton1.setOnAction(trackDeaths);
        
        
        /**
         * Event Handler to graph the number of deaths with respect
         * to the number of cases.
         */
        EventHandler<ActionEvent> deathsToCases = new EventHandler<ActionEvent>() { 
          public void handle(ActionEvent e) 
          {
              yAxis.setLabel("Deaths to Cases");
            
              String country = (String) countryComboBox.getValue();
              String start = (String) startDateComboBox.getValue();
              String end = (String) endDateComboBox.getValue();
              
              System.out.println(country);

              XYChart.Series dataSeries = new XYChart.Series();
              dataSeries.setName(country);
              
              int k = 0;
              int m = 1;
              for (int i = 0; i < ds.size(); i++) {
                if (ds.get(i).date.contentEquals(start)) k = i;
                if (ds.get(i).date.contentEquals(end)) m = i;
              }
              
              for (int i = 0; i < countryList.size(); i++) {
                if (countryList.get(i).name.contentEquals(country)) {
                  List<DayNode> dayList = countryList.get(i).getDays();
                  for (int j = k; j < m; j++) {
                    int numCases = dayList.get(j).numCases;
                    int numDeaths = dayList.get(j).numDeaths;
                    double deathsToCases = (double)numDeaths / numCases;
                    
                    dataSeries.getData().add(new XYChart.Data(j , deathsToCases));
                  } 
                }
              }

              lineChart.getData().add(dataSeries);
              
            
            
          } 
         };
         
         trackbutton2.setOnAction(deathsToCases);
      
       /**
        * Event Handler to graph the number of cases
        * with respect to population.
        */
       EventHandler<ActionEvent> casesToPopu = new EventHandler<ActionEvent>() { 
         public void handle(ActionEvent e) 
         {
           
           yAxis.setLabel("Cases to Population");
           
           String country = (String) countryComboBox.getValue();
           String start = (String) startDateComboBox.getValue();
           String end = (String) endDateComboBox.getValue();
           
           System.out.println(country);

           XYChart.Series dataSeries = new XYChart.Series();
           dataSeries.setName(country);
           
           int k = 0;
           int m = 1;
           for (int i = 0; i < ds.size(); i++) {
             if (ds.get(i).date.contentEquals(start)) k = i;
             if (ds.get(i).date.contentEquals(end)) m = i;
           }
           
           for (int i = 0; i < countryList.size(); i++) {
             if (countryList.get(i).name.contentEquals(country)) {
               List<DayNode> dayList = countryList.get(i).getDays();
               for (int j = k; j < m; j++) {
                 int numCases = dayList.get(j).numCases;
                 double casesToPop = ((double)numCases / countryList.get(i).population);
                 dataSeries.getData().add(new XYChart.Data(j , casesToPop));
                 
               } 
             }
           }

           lineChart.getData().add(dataSeries);
           
         } 
        };
        
        trackbutton3.setOnAction(casesToPopu);
        
        
        
        
         
         
         /**
          * Event Handler to clear the data on the graph.
          */
         EventHandler<ActionEvent> cleanChart = new EventHandler<ActionEvent>() { 
           public void handle(ActionEvent e) 
           {
             lineChart.getData().clear();
             
           } 
          };
          
          clearButton.setOnAction(cleanChart);
         
        
      
      Scene mainScene = new Scene(hbox, WINDOW_WIDTH, WINDOW_HEIGHT);
      
      BackgroundFill background_fill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
      Background background = new Background(background_fill);
      rightSide.setBackground(background);
      
      BackgroundFill background_fill_left = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
      Background background_left = new Background(background_fill_left);
      leftSide.setBackground(background_left);
      
      hbox.getChildren().addAll(leftSide, rightSide);
      hbox.setBackground(background_left);
      
      primaryStage.setTitle("COVID19 Visualizer");
      primaryStage.setScene(mainScene);
      primaryStage.show();
      
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
      try {
        countryList = getCSV("./application/time_series_covid19_confirmed_global.csv");
        countryList = pullDeaths(countryList, "./application/Covid19Deaths.csv");
        
      } catch (IOException e) {
        System.out.println("Base Input File Missing");
        e.printStackTrace();
      }
           launch(args);
           
    }
    
    /**
     * Reads from input file to compile a list of countries and the number of cases on each day.
     * @param file - csv file with countries and number of cases per day
     * @return - countryList - ArrayList of countries
     * @throws IOException
     */
    public static ArrayList<Country> getCSV(String file) throws IOException {
      ArrayList<Country> countryList = new ArrayList<>();
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        String line = csvReader.readLine();
        String[] values = line.split(",");
        int n = 0;
        while ((line = csvReader.readLine()) != null) {
          if (n ==0 ) System.out.println(line);
          n++;
            String[] value = line.split(",");
            String name = value[1] + " " + value[0];
            int pop = Integer.parseInt(value[2]);
            Country c = new Country(name, pop);
            
            for (int i = 3; i < values.length; i++) {
              String date = values[i];
              int numCases = Integer.parseInt(value[i]);
              c.addDay(date, numCases);
            }
            countryList.add(c);
        }

        csvReader.close();
        
        return countryList;
      
      
    }
    
    /**
     * Reads from input file to append the number of deaths for each country on any given day.
     * @param countryList - ArrayList of Countries
     * @param file - File with list of countries and number of deaths per day
     * @return - countryList with deaths added.
     * @throws IOException
     */
    public static ArrayList<Country> pullDeaths(ArrayList<Country> countryList, String file) throws IOException {
      
      BufferedReader csvReader = new BufferedReader(new FileReader(file));
      String line = csvReader.readLine();
      String[] values = line.split(",");
      
      int n = 0;
      while ((line = csvReader.readLine()) != null) {
          String[] value = line.split(",");
          String name = value[1] + " " + value[0]; // Country + Province
          int pop = Integer.parseInt(value[2]);
          Country c = countryList.get(n);
          
          for (int i = 3; i < values.length; i++) {
            String date = values[i];
            int numDead = Integer.parseInt(value[i]);
            c.getDays().get(i-3).addDeaths(numDead);
          }
          System.out.println(c.name + " " + c.getDays().get(206).numDeaths);
          n++;
      }

      csvReader.close();
      
      return countryList;
      
      
    }
    
    
}