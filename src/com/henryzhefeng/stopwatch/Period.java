package com.henryzhefeng.stopwatch;

/**
 * Created by 哲 on 12/27/2014.
 */
public class Period {
    private int minutes = 0;
    private int seconds = 0;
    private int tenOfSec = 0;

    Period() {
    }


    Period(int minutes, int seconds, int tenOfSec) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.tenOfSec = tenOfSec;
    }
}
