package nsu.graphics.secondlab.filters;

import nsu.graphics.secondlab.parameters.GammaParameters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GammaCorrection {
    private final GammaParameters parameters;
    private double gamma;

    public GammaCorrection() {
        super();
        this.parameters = new GammaParameters();
        gamma = 1;
    }

    public BufferedImage selectFilter(BufferedImage image) {
        if (parameters.showDialog()) {
            double newGamma = parameters.getGamma();
            if (gamma != newGamma) {
                gamma = newGamma;
            }
            return applyFilter(image);
        }
        return null;
    }

    private static final int ALPHA_OFFSET = 24;
    private static final int RED_OFFSET = 16;
    private static final int GREEN_OFFSET = 8;
    private static final int BLUE_OFFSET = 0;

    public BufferedImage applyFilter(BufferedImage in){
        BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = out.getGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        int widthOfImage = out.getWidth();
        int heightOfImage = out.getHeight();
        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                int rgb = out.getRGB(w,h);
                int red = (int) Math.round(255 * Math.pow((double)((rgb >> RED_OFFSET) & 0xff) / 255.0, gamma));
                int green = (int) Math.round(255 * Math.pow((double)((rgb >> GREEN_OFFSET) & 0xff) / 255.0, gamma));
                int blue = (int) Math.round(255 * Math.pow((double)((rgb >> BLUE_OFFSET) & 0xff) / 255.0, gamma));
                int newRGB = (255 << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
                out.setRGB(w, h, newRGB);
            }
        }
        return out;
    }
}
