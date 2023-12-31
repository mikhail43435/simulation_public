package ru.hmp.simulation.io;

public final class ValidateInput implements Input {
    private final Output out;
    private final Input in;

    public ValidateInput(Output output, Input input) {
        this.out = output;
        this.in = input;
    }

    @Override
    public String askStr(String question) {
        return in.askStr(question);
    }

    @Override
    public int askInt(String question) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
                value = in.askInt(question);
                invalid = false;
            } catch (NumberFormatException nfe) {
                out.println("Invalid input. Please enter again.");
            }
        } while (invalid);
        return value;
    }
}