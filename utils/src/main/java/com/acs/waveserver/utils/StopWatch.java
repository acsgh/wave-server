package com.acs.waveserver.utils;

import org.slf4j.Logger;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class StopWatch {
    // The default formatter
    public static final DecimalFormat DEFAULT_FORMATTER = new DecimalFormat("#.###");
    // The start time, atomic just in case
    private final AtomicLong startTime = new AtomicLong(-1);

    /**
     * Start the stop watch. Called it again you restart the stop watch
     */
    public StopWatch start() {
        startTime.set(System.currentTimeMillis());
        return this;
    }

    /**
     * Get the elapse time takes since you call the start method.
     *
     * @return The elapse time in milli seconds.
     */
    public long getElapseTime() {
        long start = startTime.get();
        if (start > 0) {
            return System.currentTimeMillis() - start;
        } else {
            throw new IllegalArgumentException("The stop watch haven't started yet");
        }
    }

    /**
     * Get the elapse time takes since you call the start method.
     */
    public String getDividedElapseTime() {
        return TimeUtils.getIntervalInfo(Math.round(getElapseTime(TimeUnit.MILLISECONDS)), TimeUnit.MILLISECONDS);
    }

    /**
     * Get the elapse time takes since you call the start method.
     *
     * @param unit The time unit preferred to get the calculation
     * @return The elapse time in the unit provided
     */
    public double getElapseTime(TimeUnit unit) {
        CheckUtils.checkNull("unit", unit);

        return convertMillisecondTime(unit, getElapseTime());

    }

    /**
     * Get the elapse time takes since you call the start method.
     *
     * @param unit The time unit preferred to get the calculation
     * @return The elapse time in the unit provided in a pretty way using the default formatter: #.###
     */
    public String getElapseTimeFormatted(TimeUnit unit) {
        return getElapseTimeFormatted(unit, DEFAULT_FORMATTER);

    }

    /**
     * Get the elapse time takes since you call the start method.
     *
     * @param unit      The time unit preferred to get the calculation
     * @param formatter The decimal formated designated to enhance the number presentation.
     * @return The elapse time in the unit provided in a pretty way
     */
    public String getElapseTimeFormatted(TimeUnit unit, DecimalFormat formatter) {
        CheckUtils.checkNull("formatter", formatter);

        return formatter.format(getElapseTime(unit));
    }

    /**
     * Print into a given log the elapse time
     *
     * @param eventName The event name.
     * @param log       The log interface through the message is going to be sent.
     * @param level     The log level of this trace.
     */
    public void printElapseTime(String eventName, Logger log, LogLevel level) {
        printElapseTime(eventName, log, level, null);
    }

    /**
     * Print into a given log the elapse time
     *
     * @param eventName The event name.
     * @param log       The log interface through the message is going to be sent.
     * @param level     The log level of this trace.
     * @param unit      The time units that have to be showed.
     */
    public void printElapseTime(String eventName, Logger log, LogLevel level, TimeUnit unit) {
        CheckUtils.checkString("eventName", eventName);
        CheckUtils.checkNull("log", log);
        CheckUtils.checkNull("level", level);

        String time;
        String text = "{} in {}";

        if (unit == null) {
            time = TimeUtils.getIntervalInfo(Math.round(getElapseTime(TimeUnit.MILLISECONDS)), TimeUnit.MILLISECONDS);
        } else {
            time = getElapseTimeFormatted(unit);
            time = time + " " + unit.toString().toLowerCase();
        }

        log(log, level, text, eventName, time);
    }

    @Override
    public String toString() {
        return "in " + TimeUtils.getIntervalInfo(Math.round(getElapseTime(TimeUnit.MILLISECONDS)), TimeUnit.MILLISECONDS);
    }

    /**
     * Convert a given nanosecond time into the given Time Unit
     *
     * @param unit The time units
     * @param time The time in nanosecond scale
     * @return The time converted into the specified unit
     */
    private double convertMillisecondTime(TimeUnit unit, long time) {
        double scale = TimeUnit.MILLISECONDS.convert(1, unit);

        return time / scale;
    }

    /**
     * Print a log trace.
     *
     * @param log    The log interface through the message is going to be sent.
     * @param level  The log level of this trace.
     * @param text   The base test message could have insertion string: '{}'
     * @param params The params to be inserted into the message
     */
    @SuppressWarnings("incomplete-switch")
    private void log(Logger log, LogLevel level, String text, Object... params) {
        switch (level) {
            case TRACE:
                log.trace(text, params);
                break;
            case DEBUG:
                log.debug(text, params);
                break;
            case INFO:
                log.info(text, params);
                break;
            case WARN:
                log.warn(text, params);
                break;
            case ERROR:
                log.error(text, params);
                break;
        }
    }
}
