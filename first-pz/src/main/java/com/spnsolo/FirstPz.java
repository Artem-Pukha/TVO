package com.spnsolo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FirstPz {

    private static final Character MULTIPLY = '*';
    private static final Character SUM = '+';
    private static final Character SUBTRACT = '-';
    //6x^4 - 7x^3 - x^2-7x+6

    public static void main(String[] args) {

        Float x;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            System.out.print("Enter the number: ");
            x = Float.valueOf(reader.readLine());


        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }catch (NumberFormatException e){
            throw new RuntimeException("Wrong format of number");
        }

        System.out.printf("""
                Variant №16
                The equation: 6x^4 - 7x^3 - x^2-7x+6
                x = %f
                %n""", x);
        System.out.println("----------First stage----------");
        List<Processor> processors = new ArrayList<>();
        processors.add(new Processor(6F,x,MULTIPLY));
        processors.add(new Processor(x,x,MULTIPLY));
        processors.add(new Processor(7F,x,MULTIPLY));
        processors.add(new Processor(x,x,MULTIPLY));

        List<Float> results = new ArrayList<>();

        ExecutorService executor = Executors.newCachedThreadPool();
        for (Processor processor : processors) {
            executor.execute(processor);
        }
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(Processor p: processors){
            results.add(p.getResult());
        }
        Processor.resetAmount();

        System.out.println("----------Second stage----------");
        executor = Executors.newCachedThreadPool();
        processors = new ArrayList<>();
        processors.add(new Processor(results.get(0),results.get(1),MULTIPLY));
        processors.add(new Processor(results.get(2),results.get(3),MULTIPLY));
        processors.add(new Processor(x,x,MULTIPLY));
        processors.add(new Processor(7F,x,MULTIPLY));

        for (Processor processor : processors) {
            executor.execute(processor);
        }
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        results = new ArrayList<>();
        for(Processor p: processors){
            results.add(p.getResult());
        }
        Processor.resetAmount();
        System.out.println("----------Third stage----------");

        Processor singleStage = new Processor(results.get(0),x,MULTIPLY);
        System.out.println("Processor: " + singleStage.getId() + " started");
        Float singleResult = singleStage.process();
        System.out.println("Result of processor " + singleStage.getId() + ": " + results.get(0) + " " + MULTIPLY + " " + x + " = " + singleResult);
        System.out.println("Processor " + singleStage.getId() + " finished");
        Processor.resetAmount();

        System.out.println("----------Forth stage----------");

        executor = Executors.newCachedThreadPool();
        processors = new ArrayList<>();
        processors.add(new Processor(singleResult,results.get(1),SUBTRACT));
        processors.add(new Processor(results.get(2),results.get(3),SUM));
        System.out.println("Processor: " + singleStage.getId() + " started");

        for (Processor processor : processors) {
            executor.execute(processor);
        }
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        results = new ArrayList<>();
        for(Processor p: processors){
            results.add(p.getResult());
        }
        Processor.resetAmount();

        System.out.println("----------Fifth stage----------");

        singleStage = new Processor(results.get(0),results.get(1),SUBTRACT);
        System.out.println("Processor: " + singleStage.getId() + " started");
        singleResult = singleStage.process();
        System.out.println("Result of processor " + singleStage.getId() + ": " + singleStage.getOperands()[0] + " " + SUBTRACT + " " + results.get(1) + " = " + singleResult);
        System.out.println("Processor " + singleStage.getId() + " finished");
        Processor.resetAmount();

        System.out.println("----------Sixth stage----------");

        singleStage = new Processor(singleResult,6F,SUM);
        System.out.println("Processor: " + singleStage.getId() + " started");
        singleResult = singleStage.process();
        System.out.println("Result of processor " + singleStage.getId() + ": " + singleStage.getOperands()[0] + " " + SUM + " " + 6F + " = " + singleResult);
        System.out.println("Processor " + singleStage.getId() + " finished");
        Processor.resetAmount();

        System.out.println("Final answer: " + singleResult);
    }
}
