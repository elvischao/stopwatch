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

    // data members
    private State state;
    private InitialState initialState = new InitialState();
    private RunningState runningState = new RunningState();
    private PausedState pausedState = new PausedState();

    // references to some controls
    private StopwatchView stopwatch;
    private RecordListAdapter adapter;
    private ListView recordListView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        // initialize
        recordListView = (ListView) findViewById(R.id.resultList);
        adapter = new RecordListAdapter(recordListView);
        recordListView.setAdapter(adapter);
        stopwatch = (StopwatchView) MainActivity.this.findViewById(R.id.stopwatch);

        // set state and visibility
        changeToState(initialState);

        // set button click listener
        Button btn = (Button) findViewById(R.id.init_start_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.informInput(INPUT.START);
            }
        });
        btn = (Button) findViewById(R.id.run_lap_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.informInput(INPUT.LAP);
            }
        });
        btn = (Button) findViewById(R.id.run_pause_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.informInput(INPUT.PAUSE);
            }
        });
        btn = (Button) findViewById(R.id.paused_start_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.informInput(INPUT.START);
            }
        });
        btn = (Button) findViewById(R.id.paused_reset_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.informInput(INPUT.RESET);
            }
        });

    }

    // change the visibility
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
        state = newState;
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
                    stopwatch.start();
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
                    Period period = stopwatch.getPeriod();
                    adapter.addRecord(period);
                    break;
                case PAUSE:
                    stopwatch.pause();
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
                    stopwatch.reset();
                    adapter.reset();
                    changeToState(initialState);
                    break;
                case START:
                    stopwatch.resume();
                    changeToState(runningState);
                    break;
                default:
                    break;
            }
        }
    }
}
