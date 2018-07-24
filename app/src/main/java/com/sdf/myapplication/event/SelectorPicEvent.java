package com.sdf.myapplication.event;

public class SelectorPicEvent {

    private boolean message;

    public boolean getMessage() {
        return message;
    }

    public SelectorPicEvent(boolean message) {
        this.message = message;
    }
}
