package ru.hmp.simulation.render.swing.elements;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityLabelPool {

    private static final Map<ImageIcon, List<JLabel>> POOL = new HashMap<>();
    private static final Map<ImageIcon, Integer> MAP_INDEX = new HashMap<>();

    public JLabel getUnused(ImageIcon key) {
        POOL.computeIfAbsent(key, imageIcon -> new ArrayList<>());

        List<JLabel> list = POOL.get(key);
        int index = MAP_INDEX.getOrDefault(key, 0);

        JLabel entityLabel;
        if (index < list.size()) {
            entityLabel = list.get(index);
        } else {
            entityLabel = new EntityLabel(key);
            list.add(entityLabel);
        }
        MAP_INDEX.merge(key, 1, Integer::sum);
        return entityLabel;
    }

    public void reset() {
        MAP_INDEX.replaceAll((key, value) -> value = 0);

        for (Map.Entry<ImageIcon, List<JLabel>> entry : POOL.entrySet()) {
            entry.getValue().forEach(listElement -> listElement.setIcon(entry.getKey()));
        }
    }
}