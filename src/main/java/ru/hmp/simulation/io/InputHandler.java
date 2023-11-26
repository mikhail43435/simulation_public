package ru.hmp.simulation.io;

import java.util.List;

public final class InputHandler {

    private InputHandler() {
    }

    public static int handleSelectItems(Input input,
                                        Output output,
                                        String caption,
                                        List<String> selectItems,
                                        String question) {
        int select;
        while (true) {
            if (!caption.isEmpty()) {
                output.println(caption);
            }

            for (int i = 0; i < selectItems.size(); i++) {
                output.println(String.format("%d. %s", i + 1, selectItems.get(i)));
            }
            select = input.askInt(question);
            if (select >= 1 && select <= selectItems.size()) {
                break;
            }
            output.println(String.format("Wrong input, you can select: 1...%d", selectItems.size()));
        }
        return select;
    }

    public static int handleSingleIntInput(Input input,
                                           Output out,
                                           String question,
                                           int minValue,
                                           int maxValue) {
        int select;
        while (true) {
            select = input.askInt(question);
            if (select >= minValue && select <= maxValue) {
                break;
            }
            out.println(String.format("Wrong input, you can select: %d...%d", minValue, maxValue));
        }
        return select;
    }

    public static char handleSingleCharInput(Input input,
                                             Output out,
                                             String question) {
        String select;
        while (true) {
            select = input.askStr(question);
            if (select.length() == 1 && Character.isLetter(select.charAt(0))) {
                break;
            }
            out.println("Wrong input, you can input only one letter");
        }
        return select.charAt(0);
    }
}