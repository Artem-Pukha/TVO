package com.spnsolo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("9_shaft.jpg");
        BufferedImage source = ImageIO.read(file);

        convertToGrayscale(source);

        int width = source.getWidth();
        int height = source.getHeight();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter count of threads: ");
        int countThreads = scanner.nextInt();

        List<Prewitt> prewitts = new ArrayList<>();
        for(int i = 1; i <= countThreads; ++i){
            int startHeight = height/countThreads * i - height/countThreads;
            int endHeight = height/countThreads * i;
            if(i==1){
                prewitts.add(new Prewitt(source, 0,0, width, endHeight));
            }
            else if(i==countThreads){
                prewitts.add(new Prewitt(source, 0, startHeight, width, height));
            }
            else{
                prewitts.add(new Prewitt(source, 0, startHeight, width, endHeight));
            }
        }


        Instant start = Instant.now();
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i = 0; i < countThreads; ++i){
            executor.execute(prewitts.get(i));
        }
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Instant finish = Instant.now();
        double timeElapsed = Duration.between(start, finish).toMillis();

        double timeSecond = timeElapsed / 100;
        System.out.println("Elapsed time in seconds: " + timeSecond);

        File output = new File("9_shaft_processed.jpg");
        ImageIO.write(source, "jpg", output);
    }

    private static void convertToGrayscale(BufferedImage image)
    {
        for(int i = 0; i < image.getWidth(); i++) {
            for(int j = 0; j < image.getHeight(); j++) {
                int pixel = image.getRGB(i, j);
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                int average = (red + green + blue) / 3;

                pixel = (alpha << 24) | (average << 16) | (average << 8) | average;

                image.setRGB(i, j, pixel);
            }
        }
    }
}
