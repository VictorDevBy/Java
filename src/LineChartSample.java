import A.FFT;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.scene.chart.XYChart.*;

public class LineChartSample extends Application {

    public double koef = 0.1;
    Scene scene;
    FlowPane fp;
    FlowPane fp2;

    @Override public void start(Stage stage) {

        stage.setTitle("Fast Fourier Transform");

        fp = new FlowPane();
        Button btn = new Button("Display charts with a new coefficient");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ShowGrafics();
                koef += 0.1;
            }
        });
        Button btn2 = new Button("Clean");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fp2.getChildren().clear();
                koef = 0.1;
            }
        });

        fp.getChildren().add(btn);
        fp.getChildren().add(btn2);
        fp2 = new FlowPane();
        fp.getChildren().add(fp2);
        scene = new Scene(fp, 800, 800);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void SetGistagrammValues(double[] mass, Series series1){
        for (int i = 0; i < mass.length - 1; i++) {
            series1.getData().add(new Data(String.valueOf(i+1), mass[i]));
        }
    }

    private void ShowGrafics(){

//defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Amplitude");
//creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Amplitude-time characteristic");
        lineChart.setMinSize(800,200);
//defining a series
        Series series = new Series();
        series.setName("Signal");

        int n = 256;
        double[] output = new double[n];
        A.Complex[] buf = new A.Complex[n];

        for (int i = 1; i <= n; i++) {
            double value = koef * Math.cos(2 * Math.PI / n * koef * (i  + (n / koef)));
            //value = Math.cos((double) i/7);
            series.getData().add(new Data(i, value));
            buf[i-1] = new A.Complex(value,0);
        }

        buf = FFT.fft(buf);

        for (int i = 0; i < buf.length / 2; i++) {
            output[i] = Math.sqrt(buf[i].re()*buf[i].re() + buf[i].im()*buf[i].im());
        }

        final boolean add = lineChart.getData().add(series);

        final CategoryAxis xAxis1 = new CategoryAxis();
        final NumberAxis yAxis1 = new NumberAxis();
        xAxis1.setLabel("Frequency");
        yAxis1.setLabel("Amplitude");
        final BarChart<String,Number> bc =
                new BarChart<String,Number>(xAxis1,yAxis1);
        bc.setTitle("Amplitude frequency response");

        bc.setMinSize(800,200);

        Series series1 = new Series();
        series1.setName("Amplitude frequency response");

        SetGistagrammValues(output,series1);

        bc.getData().add(series1);

        fp2.getChildren().clear();
        fp2.getChildren().add(lineChart);
        fp2.getChildren().add(bc);
    }
}