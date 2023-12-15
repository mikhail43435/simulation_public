package ru.hmp.simulation.render.swing.panels;

import javax.swing.*;

public class StartButton extends JButton {

    public static final String CUSTOM_PROP_NAME = "ExceptionOccurred";

    public StartButton(String s) {
        super(s);
    }

    public void setPropExceptionOccurred(Throwable throwable) {
        firePropertyChange(CUSTOM_PROP_NAME, null, throwable);
    }
}
