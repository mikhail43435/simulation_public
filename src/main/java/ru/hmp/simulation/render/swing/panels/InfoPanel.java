package ru.hmp.simulation.render.swing.panels;

import ru.hmp.simulation.render.swing.elements.InfoLabel;

import javax.swing.*;
import java.awt.*;

public final class InfoPanel extends JPanel {

    private final JLabel label = new InfoLabel();

    public InfoPanel() {
        this.add(label);
        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(220, 35));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    public void setText(String text) {
        label.setText(text);
    }
}