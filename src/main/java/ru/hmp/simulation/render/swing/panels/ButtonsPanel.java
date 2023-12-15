package ru.hmp.simulation.render.swing.panels;

import ru.hmp.simulation.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public final class ButtonsPanel extends JPanel {

    private final Simulation simulation;
    private final StartButton startButton = new StartButton("Start (continue)");
    private final JButton stopButton = new JButton("Stop");
    private final JButton resetButton = new JButton("Reset");
    private final JButton helpButton = new JButton("Help");
    private final String instructionText;

    public ButtonsPanel(Simulation simulation, String instructionText) {
        this.simulation = simulation;
        this.instructionText = instructionText;
        this.add(startButton);
        this.add(stopButton);
        this.add(resetButton);
        this.add(helpButton);

        this.setBackground(Color.LIGHT_GRAY);
        this.setPreferredSize(new Dimension(300, 35));
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        startButtonClicked();
        stopButtonClicked();
        resetButtonClicked();
        helpButtonClicked();

        stopButton.setEnabled(false);
    }

    public void startButtonClicked() {
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            new StartButtonWorker().execute();
        });
    }

    public void stopButtonClicked() {
        stopButton.addActionListener(e -> simulation.stop());
    }

    public void resetButtonClicked() {
        resetButton.addActionListener(e -> {
            simulation.stop();
            simulation.reset();
            simulation.render();
        });
    }

    public void helpButtonClicked() {
        helpButton.addActionListener(e -> {
            simulation.stop();
            JOptionPane.showMessageDialog(this,
                    instructionText,
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private class StartButtonWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            simulation.run();
            return null;
        }

        @Override
        protected void done() {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            try {
                get();
            } catch (Exception ignored) {
            }
        }
    }
}