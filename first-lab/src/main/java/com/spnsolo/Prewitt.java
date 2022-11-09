package com.spnsolo;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
@Setter
public class Prewitt implements Runnable {

    private final BufferedImage image;
    private static int count = 0;
    private int id = 0;
    private int startWidth;
    private int startHeight;
    private int endWidth;
    private int endHeight;

    private int[][] kernelX = {
            { -1, 0, 1 },
            { -1, 0, 1 },
            { -1, 0, 1 }
    };
    private int[][] kernelY = {
            { -1, -1, -1 },
            { 0, 0, 0 },
            { 1, 1, 1 }
    };

    public Prewitt(BufferedImage source, int startWidth, int startHeight, int endWidth, int endHeight) {
        this.image = source;
        this.startWidth = startWidth;
        this.startHeight = startHeight;

        this.endWidth = endWidth;
        this.endHeight = endHeight;
        ++count;
        id = count;
    }

    @Override
    public void run() {
        int i, j;
        int[][] pixels = new int[image.getWidth()][image.getHeight()];

        for(i = 0; i < image.getWidth(); i++) {
            for(j = 0; j < image.getHeight(); j++) {
                Color color = new Color(this.getImage().getRGB(i, j));
                pixels[i][j] = color.getRed();
            }
        }
        processImage(pixels);
    }
    public void processImage(int[][] pixels) {
        int i, j;
        endWidth = endWidth == image.getWidth() ? endWidth - 2 : endWidth;
        endHeight = endHeight == image.getHeight() ? endHeight - 2 : endHeight;

        for(i = startWidth; i < endWidth; i++) {
            for (j = startHeight; j < endHeight; j++) {
                int pixelX = (pixels[i][j] * this.kernelX[0][0]) + (pixels[i + 1][j] * this.kernelX[0][1]) + (pixels[i + 2][j] * this.kernelX[0][2]) +
                        (pixels[i][j + 1] * this.kernelX[1][0]) + (pixels[i + 1][j + 1] * this.kernelX[1][1]) + (pixels[i + 2][j + 1] * this.kernelX[1][2]) +
                        (pixels[i][j + 2] * this.kernelX[2][0]) + (pixels[i + 1][j + 2] * this.kernelX[2][1]) + (pixels[i + 2][j + 2] * this.kernelX[2][2]);

                int pixelY = (pixels[i][j] * this.kernelY[0][0]) + (pixels[i + 1][j] * this.kernelY[0][1]) + (pixels[i + 2][j] * this.kernelY[0][2]) +
                        (pixels[i][j + 1] * this.kernelY[1][0]) + (pixels[i + 1][j + 1] * this.kernelY[1][1]) + (pixels[i + 2][j + 1] * this.kernelY[1][2]) +
                        (pixels[i][j + 2] * this.kernelY[2][0]) + (pixels[i + 1][j + 2] * this.kernelY[2][1]) + (pixels[i + 2][j + 2] * this.kernelY[2][2]);

                int pixel = (int) Math.sqrt(pixelX * pixelX + pixelY * pixelY);

                if(pixel < 0) {
                    pixels[i][j] = 0;
                }
                else pixels[i][j] = Math.min(pixel, 255);
            }
        }

        for(i = startWidth; i < endWidth; i++) {
            for(j = startHeight; j < endHeight; j++) {
                Color color = new Color(pixels[i][j], pixels[i][j], pixels[i][j]);
                int rgb = color.getRGB();
                image.setRGB(i, j, rgb);
            }
        }
    }

}
