package com.spnsolo;

public class Processor implements Runnable{

    private static int amount = 1;
    private final Float[]operands = new Float[2];
    private final Character operator;
    private final int id;
    private volatile Float result;
    public Processor(Float a, Float b, Character operator) {
        operands[0] = a;
        operands[1] = b;
        this.operator = operator;
        id = amount;
        ++amount;
    }

    @Override
    public void run() {
        System.out.println("Processor: " + id + " started");
        result = process();
        System.out.println("Result of processor " + id + ": " + operands[0] + " " + operator + " " + operands[1] + " = " + result);
        System.out.println("Processor " + id + " finished");
    }

    private Float multiply(){
        return operands[0] * operands[1];
    }

    private Float sum(){
        return operands[0] + operands[1];
    }

    private Float subtract(){
        return operands[0] - operands[1];
    }

    public Float process(){
        return switch (operator) {
            case '-' -> subtract();
            case '+' -> sum();
            case '*' -> multiply();
            default -> throw new IllegalArgumentException("You entered incorrect operator");
        };
    }

    public Float getResult() {
        return result;
    }
    public int getId() {
        return id;
    }

    public Float[] getOperands() {
        return operands;
    }

    public static void resetAmount(){amount = 1;}

}
