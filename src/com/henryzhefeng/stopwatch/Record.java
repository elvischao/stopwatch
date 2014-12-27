package com.henryzhefeng.stopwatch;

/**
 * Created by 哲 on 12/23/2014.
 */
public class Record {
    // the order in sequence.
    private int order = -1;
    private Period period = null;

    Record() {
    }

    Record(int order, Period period) {
        this.order = order;
        this.period = period;
    }
}
