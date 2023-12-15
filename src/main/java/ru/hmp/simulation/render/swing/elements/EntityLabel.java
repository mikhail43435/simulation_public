package ru.hmp.simulation.render.swing.elements;

import javax.swing.*;
import java.awt.*;

public final class EntityLabel extends JLabel {

    public EntityLabel(ImageIcon icon) {
        this.setPreferredSize(new Dimension(48, 48));
        this.setBackground(Color.WHITE);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.CENTER);
        this.setIcon(icon);
    }
}