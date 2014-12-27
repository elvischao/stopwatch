package com.henryzhefeng.stopwatch;

/**
 * Created by 哲 on 12/23/2014.
 */
public class Record {
    // the order in sequence.
    public int order = -1;
    public Period period = null;

    Record() {
    }

    Record(int order, Period period) {
        this.order = order;
        this.period = period;
    }
}
