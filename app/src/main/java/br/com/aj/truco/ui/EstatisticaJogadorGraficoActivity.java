package br.com.aj.truco.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import br.com.aj.truco.BaseActivity;
import br.com.aj.truco.R;
import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.dao.AppRoomDatabase;


public class EstatisticaJogadorGraficoActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private static final String TAG = EstatisticaJogadorGraficoActivity.class.getSimpleName();

    private long jogadorID;
    private LineChart chart;
    private SeekBar seekBar;
    private TextView tvX;
    AppRoomDatabase dbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_estatistica_jogador_grafico);

            setDisplayHomeAsUpEnabled(true, true);


            dbs = AppRoomDatabase.getDatabase(getBaseContext());
            jogadorID = getIntent().getLongExtra(Jogador.EXTRA_KEY, 0);
            tvX = findViewById(R.id.tvXMax);
            seekBar = findViewById(R.id.seekBar);
            seekBar.setOnSeekBarChangeListener(this);
            chart = findViewById(R.id.chart_line);
            chart.setOnChartValueSelectedListener(this);


            // no description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            chart.setDragDecelerationFrictionCoef(0.9f);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            chart.setDrawGridBackground(false);
            chart.setHighlightPerDragEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            chart.setPinchZoom(true);

            // set an alternative background color
            chart.setBackgroundColor(Color.WHITE);

            // add data
            seekBar.setProgress(8);


            chart.animateX(1500);

            // get the legend (only possible after setting data)
            Legend l = chart.getLegend();

            // modify the legend ...
            l.setForm(Legend.LegendForm.LINE);
            l.setTypeface(tfExtraBold);
            l.setTextSize(11f);
            l.setTextColor(Color.BLACK);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);


            XAxis xAxis = chart.getXAxis();
            xAxis.setTypeface(tfBold);
            xAxis.setTextSize(11f);
            xAxis.setTextColor(Color.BLACK);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(false);

            xAxis.setGranularity(1f);
            //xAxis.setCenterAxisLabels(true);
            xAxis.setEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setTypeface(tfBold);
            leftAxis.setTextColor(ColorTemplate.getHoloBlue());
            leftAxis.setDrawGridLines(true);
            leftAxis.setGranularityEnabled(true);
//            leftAxis.setAxisMaximum(50f);
//            leftAxis.setAxisMinimum(-50f);
//            leftAxis.setLabelCount(25);

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setEnabled(false);


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }


    }


    private void setData(int count) {

        chart.resetTracking();

        ArrayList<Entry> pontos = new ArrayList<>();
        ArrayList<Entry> partidas = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>();
        int i = 0;
        int Max = 2;
        int Min = -2;


        List<PartidaJogador> partidaJogadorList = dbs.partidaJogadorDAO().getLastByJogador(jogadorID, count);
        for (PartidaJogador partidaJogador : partidaJogadorList) {

            Partida partida = dbs.partidaDAO().getPartida(partidaJogador.getPartidaID());
            if (partida != null) {
                xAxisValues.add(partida.getDiaMes());


                pontos.add(new Entry(i, partidaJogador.SaldoPontos()));
                partidas.add(new Entry(i, partidaJogador.SaldoVitorias()));

                if (Max < partidaJogador.SaldoPontos())
                    Max = partidaJogador.SaldoPontos();
                if (Max < partidaJogador.SaldoVitorias())
                    Max = partidaJogador.SaldoPontos();

                if (Min > partidaJogador.SaldoPontos())
                    Min = partidaJogador.SaldoPontos();
                if (Min > partidaJogador.SaldoVitorias())
                    Min = partidaJogador.SaldoPontos();

                i++;
            }
        }

        Max += 4;
        Min -= 4;

//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * (9 / 2f));
//            values1.add(new Entry(i, val));
//
//            float val2 = (float) (Math.random() * 9) ;
//
//            values2.add(new Entry(i, val2));
//
//        }


        LineDataSet set1, set2;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(pontos);
            set2.setValues(partidas);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {


            // create a dataset and give it a type
            set1 = new LineDataSet(pontos, "Pontos");

            //set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.BLUE);
            set1.setCircleColor(Color.BLUE);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.BLUE);
            set1.setHighLightColor(Color.BLUE);
            set1.setDrawCircleHole(false);
            set1.setValueFormatter(new LargeValueFormatter());


            // create a dataset and give it a type
            set2 = new LineDataSet(partidas, "Partidas");
            //set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.RED);
            set2.setLineWidth(1f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.RED);
            set2.setValueFormatter(new LargeValueFormatter());

            //set2.setFillFormatter(new MyFillFormatter(900f));

            //String setter in x-Axis
            chart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));


            int Step = Max / 2;

            if (Min < 0 && (Min * -1) > Max)
                Step = (Min * -1) / 2;


            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setAxisMaximum(Float.parseFloat(String.valueOf(Max)));
            leftAxis.setAxisMinimum(Float.parseFloat(String.valueOf(Min)));
            leftAxis.setLabelCount(Step);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            dataSets.add(set1);
            dataSets.add(set2);

            // create a data object with the data sets
            LineData data = new LineData(dataSets);
            data.setValueTextColor(Color.BLACK);

            data.setValueTextSize(9f);
            chart.setData(data);
            chart.invalidate();

        }

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvX.setText(String.valueOf(seekBar.getProgress()));


        setData(seekBar.getProgress());

        // redraw
        chart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        Log.i("Entry selected", e.toString());

        chart.centerViewToAnimated(e.getX(), e.getY(), chart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);

    }

    @Override
    public void onNothingSelected() {

    }
}