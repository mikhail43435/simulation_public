package ru.hmp.simulation.render.swing;

import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.PositionFactory;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.entities.*;
import ru.hmp.simulation.render.SimMapRenderer;
import ru.hmp.simulation.render.swing.elements.EntityLabelPool;
import ru.hmp.simulation.render.swing.panels.ButtonsPanel;
import ru.hmp.simulation.render.swing.panels.InfoPanel;
import ru.hmp.simulation.render.swing.panels.MapPanel;
import ru.hmp.simulation.simulation.Simulation;
import ru.hmp.simulation.util.ResourcesLoadHandler;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class SwingSimMapRenderer implements SimMapRenderer {

    private static final EntityLabelPool ENTITY_CONTAINER_POOL = new EntityLabelPool();
    private final Map<Position, JLabel> renderMap = new HashMap<>();
    private final MapPanel simulationPanel;
    private final Map<String, ImageIcon> iconsMap;
    private final InfoPanel infoPanel;
    private final SimulationMap simulationMap;

    public SwingSimMapRenderer(SimulationMap simulationMap,
                               Simulation simulation,
                               String instructionText) {
        this.simulationMap = simulationMap;
        infoPanel = new InfoPanel();
        simulationPanel = new MapPanel(simulationMap.getXMapSize(), simulationMap.getYMapSize());

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Simulation 2.0");

        window.setLayout(new BorderLayout());

        GridBagConstraints constraintsForLabelPanel = new GridBagConstraints();
        constraintsForLabelPanel.gridy = 0;
        window.add(infoPanel, BorderLayout.LINE_START);

        GridBagConstraints simulationPanelConstraints = new GridBagConstraints();
        simulationPanelConstraints.fill = GridBagConstraints.BOTH;
        simulationPanelConstraints.gridy = 0;
        simulationPanelConstraints.anchor = GridBagConstraints.PAGE_END;
        simulationPanelConstraints.anchor = GridBagConstraints.SOUTHEAST;
        window.add(simulationPanel, BorderLayout.LINE_END);

        GridBagConstraints buttonsPanelConstraints = new GridBagConstraints();
        buttonsPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonsPanelConstraints.gridy = 1;
        buttonsPanelConstraints.weightx = 1;
        window.add(new ButtonsPanel(simulation, instructionText), BorderLayout.PAGE_END);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        this.iconsMap = initIconsMap();
    }

    @Override
    public void renderMap() {
        infoPanel.setText(formStatisticText(simulationMap.getStatistic()));
        if (simulationMap.getCycleNumber() == 0) {
            ENTITY_CONTAINER_POOL.reset();
            simulationPanel.removeAll();
            fillRenderMap();
            updateSimulationPanel();
            simulationPanel.revalidate();
        } else {
            Map<Position, Entity> map = simulationMap.getChangeTrackerMap();

            updateRenderMap(map);
        }
        simulationPanel.repaint();
    }

    private void updateRenderMap(Map<Position, Entity> changeMap) {
        for (Map.Entry<Position, Entity> entry : changeMap.entrySet()) {
            this.renderMap.get(entry.getKey()).
                    setIcon(getIcon(entry.getValue() != null ? entry.getValue().getClass().getSimpleName() : null));
        }
    }

    private void fillRenderMap() {
        renderMap.clear();
        buildRenderMap(simulationMap);
    }

    private void updateSimulationPanel() {
        this.renderMap.entrySet().
                stream().
                sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey())).
                forEach(e -> this.simulationPanel.add(e.getValue()));
    }

    private void buildRenderMap(SimulationMap map) {
        for (int row = 0; row < map.getYMapSize(); row++) {
            for (int col = 0; col < map.getXMapSize(); col++) {
                Position position = PositionFactory.of(col, row);
                Entity entity = map.getEntityAtPosition(position).orElse(null);
                ImageIcon imageIcon = getIcon(entity != null ? entity.getClass().getSimpleName() : null);
                JLabel entityLabel = ENTITY_CONTAINER_POOL.getUnused(imageIcon);
                renderMap.put(position, entityLabel);
            }
        }
    }

    private String formStatisticText(Map<String, String> statisticMap) {
        StringBuilder sb = new StringBuilder(550);
        sb.append("<html>");
        statisticMap.
                forEach((key, value) -> sb.
                        append(key).
                        append(": ").
                        append(value).
                        append("<br>"));
        sb.append("</html>");
        return sb.toString();
    }

    private Map<String, ImageIcon> initIconsMap() {
        Map<String, ImageIcon> map = new HashMap<>();
        map.put(Grass.class.getSimpleName(),
                ResourcesLoadHandler.loadImageIcon("/images/grass.png"));
        map.put(Herbivore.class.getSimpleName(),
                ResourcesLoadHandler.loadImageIcon("/images/herbivore.png"));
        map.put(Predator.class.getSimpleName(),
                ResourcesLoadHandler.loadImageIcon("/images/predator.png"));
        map.put(Rock.class.getSimpleName(),
                ResourcesLoadHandler.loadImageIcon("/images/rock.png"));
        map.put(Tree.class.getSimpleName(),
                ResourcesLoadHandler.loadImageIcon("/images/tree.png"));
        map.put(null, ResourcesLoadHandler.
                loadImageIcon("/images/void.png"));
        return map;
    }

    private ImageIcon getIcon(String entityClass) {
        return iconsMap.get(entityClass);
    }
}