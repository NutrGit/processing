package cv.processing.pid.test.fx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;

import java.util.List;
import java.util.Random;


public class Controller {

    @FXML
    private AreaChart chartPID;

    @FXML
    private NumberAxis axisX;

    @FXML
    private NumberAxis axisY;

    @FXML
    private Button button1;

    @FXML
    void testButton() {
        addToChart(getRandomNumberUsingNextInt(0, 100));
    }

    public void addToChart(float value) {
        ObservableList<XYChart.Series> seriesObservableList = chartPID.getData();
        XYChart.Series series = seriesObservableList.get(0);
        ObservableList<XYChart.Data> dataObservableList = series.getData();
        int j = dataObservableList.size();
        dataObservableList.add(j, new XYChart.Data(j + 1, value));
    }

    public void init() {
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        chartPID.getData().add(series);
    }

    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
