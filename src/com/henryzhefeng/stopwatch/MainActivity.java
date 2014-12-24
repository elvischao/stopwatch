package com.henryzhefeng.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
        state = initialState;
        View initLayout = findViewById(R.id.init_btn_bar);
        initLayout.setVisibility(View.VISIBLE);
        View runLayout = findViewById(R.id.run_btn_bar);
        runLayout.setVisibility(View.INVISIBLE);
        View pausedLayout = findViewById(R.id.paused_btn_bar);
        pausedLayout.setVisibility(View.INVISIBLE);
    }

    // using State Pattern
    private abstract class State {
        public abstract void informInput(INPUT input);
    }

    private class InitialState extends State {
        @Override
        public void informInput(INPUT input) {
            // TODO
        }
    }

    private class RunningState extends State {
        @Override
        public void informInput(INPUT input) {
            // TODO
        }
    }

    private class PausedState extends State {
        @Override
        public void informInput(INPUT input) {
            // TODO
        }
    }
}
