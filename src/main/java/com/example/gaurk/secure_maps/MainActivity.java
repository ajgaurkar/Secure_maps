package com.example.gaurk.secure_maps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static com.github.mikephil.charting.components.Legend.LegendPosition.ABOVE_CHART_LEFT;

//Main launcher activity
public class MainActivity extends AppCompatActivity {

    //UI components declaration
    private TextView mTextMessage;
    private Button goto_maps_btn;
    private RelativeLayout statistics_layout;
    private RelativeLayout settings_layout;
    private RelativeLayout home_layout;

    //bottom navigation bar listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    statistics_layout.setVisibility(View.INVISIBLE);
                    settings_layout.setVisibility(View.INVISIBLE);
                    home_layout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    statistics_layout.setVisibility(View.VISIBLE);
                    settings_layout.setVisibility(View.INVISIBLE);
                    home_layout.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    statistics_layout.setVisibility(View.INVISIBLE);
                    settings_layout.setVisibility(View.VISIBLE);
                    home_layout.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //UI components initialization
        statistics_layout = (RelativeLayout) findViewById(R.id.statistics_layout);
        settings_layout = (RelativeLayout) findViewById(R.id.settings_layout);
        home_layout = (RelativeLayout) findViewById(R.id.home_layout);

        statistics_layout.setVisibility(View.INVISIBLE);
        settings_layout.setVisibility(View.INVISIBLE);
        home_layout.setVisibility(View.VISIBLE);

        mTextMessage = (TextView) findViewById(R.id.message);
        goto_maps_btn = (Button) findViewById(R.id.goto_maps_button);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent login_intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login_intent);

        goto_maps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maps_Intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(maps_Intent);
            }
        });


        //statistics bar chart Initialization
        BarChart barChart = (BarChart) findViewById(R.id.statistics_barchart);


        ArrayList<BarEntry> entries = new ArrayList();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries, "xyz");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("2007");
        labels.add("2008");
        labels.add("2009");
        labels.add("2010");
        labels.add("2011");
        labels.add("2012");
        labels.add("2013");
        labels.add("2014");
        labels.add("2015");
        labels.add("2016");

        barChart.setDescription("Drug Abuse Statistics");
        barChart.getLegend().setTextSize(15f);
        barChart.setDescriptionTextSize(16f);
        barChart.setDescriptionPosition(420f, 35f);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.setGridBackgroundColor(Color.TRANSPARENT);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //Opioid death statistics values (y,x)
        ArrayList<BarEntry> BarEntry = new ArrayList<>();
        BarEntry.add(new BarEntry(620f, 0));
        BarEntry.add(new BarEntry(510f, 1));
        BarEntry.add(new BarEntry(582f, 2));
        BarEntry.add(new BarEntry(500f, 3));
        BarEntry.add(new BarEntry(514f, 4));
        BarEntry.add(new BarEntry(618f, 5));
        BarEntry.add(new BarEntry(742f, 6));
        BarEntry.add(new BarEntry(858f, 7));
        BarEntry.add(new BarEntry(1131f, 8));
        BarEntry.add(new BarEntry(1823f, 9));
        // create BarEntry for group 1

        //Non-Opioid death statistics values (y,x)
        ArrayList<BarEntry> BarEntry2 = new ArrayList<>();
        BarEntry2.add(new BarEntry(195f, 0));
        BarEntry2.add(new BarEntry(186f, 1));
        BarEntry2.add(new BarEntry(149f, 2));
        BarEntry2.add(new BarEntry(149f, 3));
        BarEntry2.add(new BarEntry(157f, 4));
        BarEntry2.add(new BarEntry(181f, 5));
        BarEntry2.add(new BarEntry(116f, 6));
        BarEntry2.add(new BarEntry(183f, 7));
        BarEntry2.add(new BarEntry(128f, 8));
        BarEntry2.add(new BarEntry(266f, 9));

        //2 bar set of multiline bar graph
        BarDataSet barDataSet1 = new BarDataSet(BarEntry2, "Non-Opioid Death ");  // creating dataset for group1
        BarDataSet barDataSet2 = new BarDataSet(BarEntry, "Opioid Death"); // creating dataset for group1
        barDataSet2.setColor(Color.parseColor("#CD853F"));

        //combining both the bar sets
        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        //adding labels to the chart
        BarData data = new BarData(labels, dataSets); // initialize the Bardata with argument labels and dataSet
        barChart.setData(data);

    }


}
