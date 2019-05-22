package com.example.allison.bigfootsmallfoot;

        import android.support.constraint.ConstraintLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import com.jjoe64.graphview.GraphView;
        import com.jjoe64.graphview.series.DataPoint;
        import com.jjoe64.graphview.series.LineGraphSeries;

public class Graphs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        Button calculateButton = (Button) findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            int x = 1;
            DataPoint prevPoint = null;
                @Override
                public void onClick(View v) {
                    EditText mEdit;
                    TextView mText;
                    mEdit = (EditText) findViewById(R.id.distanceText);
                    String distance = mEdit.getText().toString();
                    int result = Integer.parseInt(distance);

                    GraphView graph = (GraphView) findViewById(R.id.graph);

                    DataPoint[] dataPoints = null;
                    DataPoint currPoint = new DataPoint(x, result);

                    if (x == 1) {
                        dataPoints = new DataPoint[1];
                    } else {
                        dataPoints = new DataPoint[2];
                        dataPoints[0] = prevPoint;
                    }

                    dataPoints[dataPoints.length - 1] = currPoint;
                    prevPoint = currPoint;

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                    graph.addSeries(series);

                    x++;
                }
            });

        }
    }



