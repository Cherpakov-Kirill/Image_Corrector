package nsu.graphics.secondlab.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Inverter {
    private static final int ALPHA_OFFSET = 24;
    private static final int RED_OFFSET = 16;
    private static final int GREEN_OFFSET = 8;
    private static final int BLUE_OFFSET = 0;

    public static BufferedImage applyFilter(BufferedImage in){
        BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = out.getGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        int widthOfImage = out.getWidth();
        int heightOfImage = out.getHeight();
        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                int rgb = out.getRGB(w,h);
                int red = 255 - (rgb >> RED_OFFSET) & 0xff;
                int green = 255 - (rgb >> GREEN_OFFSET) & 0xff;
                int blue = 255 - (rgb >> BLUE_OFFSET) & 0xff;
                int newRGB = (255 << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
                out.setRGB(w, h, newRGB);
            }
        }
        return out;
    }
}
