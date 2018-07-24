package com.sdf.myapplication.event;

public class SelectorVideoEvent {

    private boolean message;

    public boolean getMessage() {
        return message;
    }

    public SelectorVideoEvent(boolean message) {
        this.message = message;
    }
}
