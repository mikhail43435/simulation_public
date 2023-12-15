package ru.hmp.simulation.render.swing.panels;

import javax.swing.*;
import java.awt.*;

public final class MapPanel extends JPanel {

    private static final int GRID_SIZE = 7;

    public MapPanel(int xSize, int ySize) {
        this.setPreferredSize(new Dimension(xSize * GRID_SIZE, ySize * GRID_SIZE));
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new GridLayout(ySize, xSize));
    }
}