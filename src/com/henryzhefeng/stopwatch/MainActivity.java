package com.henryzhefeng.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

    // the input from buttons
    public enum INPUT {
        START, RESET, PAUSE, LAP
    }

    private State state;
    private InitialState initialState = new InitialState();
    private RunningState runningState = new RunningState();
    private PausedState pausedState = new PausedState();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        // initialize
        ListView recordList = (ListView) findViewById(R.id.resultList);
        recordList.setAdapter(new RecordListAdapter());

        // set state and visibility
        changeToState(initialState);

        // set button click listener
        Button btn = (Button) findViewById(R.id.init_start_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToState(runningState);
            }
        });
        btn = (Button) findViewById(R.id.run_lap_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add new records
            }
        });
        btn = (Button) findViewById(R.id.run_pause_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToState(pausedState);
            }
        });
        btn = (Button) findViewById(R.id.paused_start_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToState(runningState);
            }
        });
        btn = (Button) findViewById(R.id.paused_reset_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToState(initialState);
            }
        });
    }

    private void changeToState(State newState) {
        View initLayout = findViewById(R.id.init_btn_bar);
        View runLayout = findViewById(R.id.run_btn_bar);
        View pausedLayout = findViewById(R.id.paused_btn_bar);
        initLayout.setVisibility(View.INVISIBLE);
        runLayout.setVisibility(View.INVISIBLE);
        pausedLayout.setVisibility(View.INVISIBLE);
        if (newState == initialState) {
            initLayout.setVisibility(View.VISIBLE);
        } else if (newState == runningState) {
            runLayout.setVisibility(View.VISIBLE);
        } else {
            pausedLayout.setVisibility(View.VISIBLE);
        }
    }

    // using State Pattern
    private abstract class State {
        public abstract void informInput(INPUT input);
    }

    private class InitialState extends State {
        @Override
        public void informInput(INPUT input) {
            switch (input) {
                case START:
                    changeToState(runningState);
                    break;
                default:
                    break;
            }
        }
    }

    private class RunningState extends State {
        @Override
        public void informInput(INPUT input) {
            switch (input) {
                case LAP:
                    break;
                case PAUSE:
                    changeToState(pausedState);
                    break;
                default:
                    break;
            }
        }
    }

    private class PausedState extends State {
        @Override
        public void informInput(INPUT input) {
            switch (input) {
                case RESET:
                    changeToState(initialState);
                    break;
                case START:
                    changeToState(runningState);
                    break;
                default:
                    break;
            }
        }
    }
}
