package com.veve.shotsandsugar.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.veve.shotsandsugar.Constants;
import com.veve.shotsandsugar.DaoAccess;
import com.veve.shotsandsugar.model.SugarLevel;

import com.veve.shotsandsugar.R;

public class SugarActivity extends DatabaseActivity {

    private static final int PADDING = 5;
    private static final int COLUMNS_NUM = 4;
    private static final int ROWS_NUM = 5;
    private static final int MAX_SUGAR_LEVEL = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentOne);
            }
        });

        final GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                gridLayout.setColumnCount(
                        ((CoordinatorLayout)gridLayout.getParent()).getWidth()/
                                (Constants.BUTTON_WIDTH + Constants.PADDING));
                for (float i=0; i<60.f; i++) {
                    Button button = new Button(getApplicationContext());
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                            new ViewGroup.MarginLayoutParams(
                                    Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
                    params.setMargins(0, Constants.PADDING, Constants.PADDING, 0);
                    button.setLayoutParams(params);
                    button.setTextColor(Color.WHITE);
                    button.setOnClickListener(new SugarActivity.NumberListener(i/2));
                    button.setText(String.valueOf(i/2));
                    gridLayout.addView(button);
                }
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    static class AddSugarTask extends AsyncTask<Float, Void, Void> {

        DaoAccess daoAccess;

        AddSugarTask(DaoAccess daoAccess) {
            this.daoAccess = daoAccess;
        }

        @Override
        protected Void doInBackground(Float... floats) {
            daoAccess.insertSugarLevel(new SugarLevel(floats[0], System.currentTimeMillis()));
            return null;
        }

    }

    class NumberListener implements View.OnClickListener {

        float level;

        public NumberListener(float level) {
            this.level = level;
        }

        @Override
        public void onClick(View v) {
            new SugarActivity.AddSugarTask(daoAccess).execute(level);
            Intent intentOne = new Intent(getApplicationContext(), MainActivity.class);
            intentOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intentOne);
        }

    }

}