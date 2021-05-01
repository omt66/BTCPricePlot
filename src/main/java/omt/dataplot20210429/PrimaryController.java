package omt.dataplot20210429;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class PrimaryController {
    private List<BTCPriceReader.Data> allData;
            
    @FXML
    private LineChart<?, ?> lineChart1;

    @FXML
    private Slider slider1;
    
    @FXML
    private Label lblNofDays;
    
    @FXML
    private CheckBox cbCircleDataPoints;
    
    @FXML
    void handleSliderChange(MouseEvent event) {
        System.out.println("slider " + slider1.getValue());
        updatePlot();
    }
    
    @FXML
    void handleCheckbox(ActionEvent event) {
        updatePlot();
    }
    
    void updatePlot() {
        int nofDays = (int)slider1.getValue();
        lblNofDays.setText("Last " + nofDays + " days");
        var subset = new ArrayList<BTCPriceReader.Data>(allData.subList(0, nofDays));
        Collections.reverse(subset); // We need to reverse the data here!
        
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series1.setName("High");
        series2.setName("Low");
        
//        NumberAxis xAxis = new NumberAxis(0, nofDays, 100); 
//        NumberAxis yAxis = new NumberAxis(); 
//        xAxis.setLabel("Days"); 
//        yAxis.setLabel("BTC Price");
        
        lineChart1.getData().clear();
        boolean circleDataPoints = cbCircleDataPoints.isSelected();
        for (var d: subset) {
            var high = new XYChart.Data(d.dateStr, d.high);
            var low = new XYChart.Data(d.dateStr, d.low);
            if (!circleDataPoints) {
                double size = 3;
                Rectangle r1 = new Rectangle(size, size);
                Rectangle r2 = new Rectangle(size, size);
                r1.setVisible(false);
                r2.setVisible(false);
                high.setNode(r1);
                low.setNode(r2);
            }
            series1.getData().add(high);
            series2.getData().add(low);
        }
        lineChart1.getData().add(series1);
        lineChart1.getData().add(series2);
    }
    
    @FXML
    void initialize() {
        System.out.println("Init...");
        lineChart1.getXAxis().setLabel("Days");
        lineChart1.getYAxis().setLabel("BTC Price ($)");
        lineChart1.setAnimated(false);
        allData = BTCPriceReader.loadBTCData();
        updatePlot();
    }
    
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
