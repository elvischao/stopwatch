package com.henryzhefeng.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

    //the input from buttons
    public enum INPUT {
        START, RESET, PAUSE, LAP
    }

    //data members
    private State state;
    private InitialState initialState = new InitialState();
    private RunningState runningState = new RunningState();
    private PausedState pausedState = new PausedState();

    //references to some controls
    private StopwatchView stopwatch;
    private RecordListAdapter adapter;
    private ListView recordList;
    private ViewGroup upLayout;
    private ViewGroup downLayout;

    //constants(we can't set them final)
    private int UP_MAX_HEIGHT;
    private int UP_MIN_HEIGHT;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        //initialize
        recordList = (ListView) findViewById(R.id.resultList);
        stopwatch = (StopwatchView) findViewById(R.id.stopwatch);
        upLayout = (ViewGroup) findViewById(R.id.up_half_layout);
        downLayout = (ViewGroup) findViewById(R.id.down_half_layout);
        //set adapter
        adapter = new RecordListAdapter(recordList);
        recordList.setAdapter(adapter);
        //set onTouch listener
        recordList.setOnTouchListener(new ListOnTouchListener());
        stopwatch.setOnTouchListener(new stopWatchOnTouchListener());

        //calculate layout size
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        UP_MAX_HEIGHT = metrics.heightPixels * 5 / 8;
        UP_MIN_HEIGHT = metrics.heightPixels * 1 / 8;
        stopwatch.setMaxHeight(UP_MAX_HEIGHT);
        stopwatch.setMinHeight(UP_MIN_HEIGHT);
        ViewGroup.LayoutParams params = upLayout.getLayoutParams();
        params.height = UP_MAX_HEIGHT;
        upLayout.setLayoutParams(params);

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

    //inner classes
    //the onTouch listener which is responsible to change layout size.
    private class ListOnTouchListener implements View.OnTouchListener {

        private class ListGestureListener extends GestureDetector.SimpleOnGestureListener {
            //reduce the frequent into half
            private boolean skip = false;

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!skip) {
                    ViewGroup.LayoutParams params = upLayout.getLayoutParams();
                    params.height -= distanceY * 2;
                    //put constrains on layout resizing.
                    if (params.height < UP_MIN_HEIGHT) {
                        params.height = UP_MIN_HEIGHT;
                    } else if (params.height > UP_MAX_HEIGHT) {
                        params.height = UP_MAX_HEIGHT;
                    }
                    upLayout.setLayoutParams(params);
                }
                skip = !skip;
                return true;
            }
        }

        private ListGestureListener gestureListener = new ListGestureListener();
        private GestureDetector gestureDetector = new GestureDetector(MainActivity.this, gestureListener);

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
    }

    private class stopWatchOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //TODO: change the view of stopwatch
            return true;
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
