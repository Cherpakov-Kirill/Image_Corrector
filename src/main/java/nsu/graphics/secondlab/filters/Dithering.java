package nsu.graphics.secondlab.filters;

import nsu.graphics.secondlab.parameters.DitheringParameters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Dithering {
    private static final int ALPHA_OFFSET = 24;
    private static final int RED_OFFSET = 16;
    private static final int GREEN_OFFSET = 8;
    private static final int BLUE_OFFSET = 0;
    private final DitheringParameters parameters;
    private int redRange;
    private int greenRange;
    private int blueRange;
    private double redDelta;
    private double greenDelta;
    private double blueDelta;
    private BufferedImage out;

    public Dithering() {
        super();
        this.parameters = new DitheringParameters();
        redRange = 0;
        greenRange = 0;
        blueRange = 0;
    }

    public BufferedImage selectFilter(BufferedImage in) {
        if (parameters.showDialog()) {
            int newRedRange = parameters.getRedRange();
            int newGreenRange = parameters.getGreenRange();
            int newBlueRange = parameters.getBlueRange();
            if (redRange != newRedRange) {
                redRange = newRedRange;
                redDelta = 255.0 / (double) (redRange - 1);
            }
            if (greenRange != newGreenRange) {
                greenRange = newGreenRange;
                greenDelta = 255.0 / (double) (greenRange - 1);
            }
            if (blueRange != newBlueRange) {
                blueRange = newBlueRange;
                blueDelta = 255.0 / (double) (blueRange - 1);
            }
            applyFilter(in);
            return out;
        }
        return null;
    }

    private void applyFilter(BufferedImage in) {
        this.out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
        Graphics g = out.getGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        int widthOfImage = out.getWidth();
        int heightOfImage = out.getHeight();

        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                int rgb = out.getRGB(w, h);
                int newRGB = (255 << ALPHA_OFFSET);
                int newRGB1 = newRGB;
                int newRGB2 = newRGB;
                int newRGB3 = newRGB;
                int newRGB4 = newRGB;
                for (int offset = 16; offset >= 0; offset -= 8) { // go across color channels
                    int color = (rgb >> offset) & 0xff;
                    int newColor = nearestColor(color, offset);
                    double error = color - newColor;
                    newRGB |= (newColor << offset);

                    if (w < widthOfImage - 1)
                        newRGB1 |= (applyFilterToChannel(offset, w + 1, h, 7, error) << offset);
                    if (w > 0 && h < heightOfImage - 1)
                        newRGB2 |= (applyFilterToChannel(offset, w - 1, h + 1, 3, error) << offset);
                    if (h < heightOfImage - 1)
                        newRGB3 |= (applyFilterToChannel(offset, w, h + 1, 5, error) << offset);
                    if (w < widthOfImage - 1 && h < heightOfImage - 1)
                        newRGB4 |= (applyFilterToChannel(offset, w + 1, h + 1, 1, error) << offset);
                }
                out.setRGB(w, h, newRGB);
                if (w < widthOfImage - 1) out.setRGB(w + 1, h, newRGB1);
                if (w > 0 && h < heightOfImage - 1) out.setRGB(w - 1, h + 1, newRGB2);
                if (h < heightOfImage - 1) out.setRGB(w, h + 1, newRGB3);
                if (w < widthOfImage - 1 && h < heightOfImage - 1) out.setRGB(w + 1, h + 1, newRGB4);
            }
        }
    }

    private int nearestColor(int color, int colorOffset) {
        switch (colorOffset) {
            case RED_OFFSET -> {
                return (int) (Math.round((double) color / redDelta) * redDelta);
            }
            case GREEN_OFFSET -> {
                return (int) (Math.round((double) color / greenDelta) * greenDelta);
            }
            case BLUE_OFFSET -> {
                return (int) (Math.round((double) color / blueDelta) * blueDelta);
            }
            default -> {
                return 0;
            }

        }
    }

    private int applyFilterToChannel(int colorOffset, int w, int h, double mul, double error) {
        int rgb = out.getRGB(w, h);
        int color = (rgb >> colorOffset) & 0xff;
        color += mul * error / 16.0;
        if (color > 255) color = 255;
        if (color < 0) color = 0;
        return color;
    }
}
