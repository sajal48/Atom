package com.sajal.atom.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampedPrintStream extends PrintStream {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final boolean isErrorStream;

    public TimestampedPrintStream(PrintStream original, boolean isErrorStream) {
        super(original);
        this.isErrorStream = isErrorStream;
    }

    @Override
    public void println(String message) {
        if (isErrorStream) {
            super.println("\u001B[31m[" + getTimestamp() + "] \u001B[0m" + message);
        } else {
            super.println("\u001B[32m[" + getTimestamp() + "] \u001B[0m" + message);
        }
    }

    @Override
    public void println(Object message) {
        if (isErrorStream) {
            super.println("\u001B[31m[" + getTimestamp() + "] \u001B[0m" + message);
        } else {
            super.println("\u001B[32m[" + getTimestamp() + "] \u001B[0m" + message);
        }
    }

    private String getTimestamp() {
        return dateFormat.format(new Date());
    }
}