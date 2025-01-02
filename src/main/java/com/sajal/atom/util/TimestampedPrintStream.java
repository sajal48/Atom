package com.sajal.atom.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampedPrintStream extends PrintStream {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TimestampedPrintStream(PrintStream original) {
        super(original);
    }

    @Override
    public void println(String message) {
        super.println(getTimestamp() + " " + message);
    }

    @Override
    public void println(Object message) {
        super.println(getTimestamp() + " " + message);
    }

    private String getTimestamp() {
        return dateFormat.format(new Date());
    }
}