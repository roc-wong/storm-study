package org.roc.storm.flux.wordcount.spout;

import java.text.SimpleDateFormat;
import java.util.Date;

// Add unique identifier to each tuple, which is helpful for debugging
public class TimeStamped extends RandomSentenceSpout {
    private final String prefix;

    public TimeStamped() {
        this("");
    }

    public TimeStamped(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected String sentence(String input) {
        return prefix + currentDate() + " " + input;
    }

    private String currentDate() {
        return new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss.SSSSSSSSS").format(new Date());
    }
}