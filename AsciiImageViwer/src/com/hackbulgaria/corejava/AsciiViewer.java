package com.hackbulgaria.corejava;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class AsciiViewer {

    private static final int CONSOLE_WIDTH = 130;

    private static void printPixel(int intensity) {
        if (intensity > 240) {
            System.out.print(" ");
        } else if (intensity > 200 && intensity <= 240) {
            System.out.print(".");
        } else if (intensity > 160 && intensity <= 200) {
            System.out.print("^");
        } else if (intensity > 120 && intensity <= 160) {
            System.out.print("*");
        } else if (intensity > 80 && intensity <= 120) {
            System.out.print("%");
        } else if (intensity > 40 && intensity <= 80) {
            System.out.print("#");
        } else {
            System.out.print("@");
        }
    }

    private static int getStep(BufferedImage image) {
        final int width = image.getWidth();
        int step = 1;
        if (CONSOLE_WIDTH < width) {
            step = width / CONSOLE_WIDTH + 1;
        }
        return step;
    }

    private static int getAverage(int xCoordinate, int yCoordinate, int step, BufferedImage image) {
        int averageIntensity = 0;
        for (int y = yCoordinate; y < yCoordinate + step; y++) {
            for (int x = xCoordinate; x < xCoordinate + step; x++) {
                final int redGreenBlue = image.getRGB(x, y);
                final Color color = new Color(redGreenBlue, false);
                averageIntensity += intensity(color.getRed(), color.getGreen(), color.getBlue());
            }
        }
        return averageIntensity / (step * step);
    }

    public static void createAsciiViewer(BufferedImage image) {
        final int step = getStep(image);
        final int imageHeight = image.getHeight();
        final int imageWidth = image.getWidth();
        for (int y = 0; y < imageHeight && y + step < imageHeight; y += step) {
            for (int x = 0; x < imageWidth && x + step < imageWidth; x += step) {
                printPixel(getAverage(x, y, step, image));
            }
            System.out.println();
        }
    }

    private static int intensity(int red, int green, int blue) {
        return (red + green + blue) / 3;
    }

    public static void main(String[] args) {
        final Path path = Paths.get(args[0]);
        try {
            final BufferedImage image = ImageIO.read(path.toFile());
            createAsciiViewer(image);
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }
}
