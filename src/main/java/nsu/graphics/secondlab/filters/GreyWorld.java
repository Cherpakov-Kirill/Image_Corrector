package nsu.graphics.secondlab.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GreyWorld {
    private static final int ALPHA_OFFSET = 24;
    private static final int RED_OFFSET = 16;
    private static final int GREEN_OFFSET = 8;
    private static final int BLUE_OFFSET = 0;

    public static BufferedImage applyFilter(BufferedImage in) {
        BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = out.getGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();

        int widthOfImage = out.getWidth();
        int heightOfImage = out.getHeight();
        int numberOfPixels = widthOfImage * heightOfImage;
        long redSum = 0;
        long greenSum = 0;
        long blueSum = 0;
        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                int rgb = out.getRGB(w, h);
                redSum += (rgb >> RED_OFFSET) & 0xff;
                greenSum += (rgb >> GREEN_OFFSET) & 0xff;
                blueSum += (rgb >> BLUE_OFFSET) & 0xff;
            }
        }
        double redAvg = (double) redSum / (double) numberOfPixels;
        double greenAvg = (double) greenSum / (double) numberOfPixels;
        double blueAvg = (double) blueSum / (double) numberOfPixels;
        double avg = (redAvg + greenAvg + blueAvg) / 3.0;
        double redCoefficient = avg / redAvg;
        double greenCoefficient = avg / greenAvg;
        double blueCoefficient = avg / blueAvg;

        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                int rgb = out.getRGB(w, h);
                int red = (int) (((rgb >> RED_OFFSET) & 0xff) * redCoefficient);
                int green = (int) (((rgb >> GREEN_OFFSET) & 0xff) * greenCoefficient);
                int blue = (int) (((rgb >> BLUE_OFFSET) & 0xff) * blueCoefficient);
                red = Math.min(red,255);
                green = Math.min(green,255);
                blue = Math.min(blue,255);
                int newRGB = (255 << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
                out.setRGB(w, h, newRGB);
            }
        }
        return out;
    }
}
