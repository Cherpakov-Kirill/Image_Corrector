package nsu.graphics.secondlab.filters;

import nsu.graphics.secondlab.parameters.BoarderParameters;

import java.awt.image.BufferedImage;

public class Boarder extends FilterApplier {
    private static final int ALPHA_OFFSET = 24;
    private static final int RED_OFFSET = 16;
    private static final int GREEN_OFFSET = 8;
    private static final int BLUE_OFFSET = 0;
    private final BoarderParameters parameters;
    private int operatorType;
    private int redLimit;
    private int greenLimit;
    private int blueLimit;
    private BufferedImage out;
    private int widthOfImage;
    private int heightOfImage;

    public Boarder() {
        super();
        this.parameters = new BoarderParameters();
        operatorType = 0;
        redLimit = 0;
        greenLimit = 0;
        blueLimit = 0;
    }

    public BufferedImage selectFilter(BufferedImage in) {
        if (parameters.showDialog()) {
            int newOperatorType = parameters.getOperatorType();
            int newRedRange = parameters.getRedLimit();
            int newGreenRange = parameters.getGreenLimit();
            int newBlueRange = parameters.getBlueLimit();
            if (operatorType != newOperatorType) {
                operatorType = newOperatorType;
            }
            if (redLimit != newRedRange) {
                redLimit = newRedRange;
            }
            if (greenLimit != newGreenRange) {
                greenLimit = newGreenRange;
            }
            if (blueLimit != newBlueRange) {
                blueLimit = newBlueRange;
            }
            applyBoarderFilter(in);
            return out;
        }
        return null;
    }

    private void applyBoarderFilter(BufferedImage in) {
        this.out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        widthOfImage = out.getWidth();
        heightOfImage = out.getHeight();
        switch (operatorType) {
            case BoarderParameters.SOBEL_OPERATOR -> applySobelOperator(in);
            case BoarderParameters.ROBERTS_OPERATOR -> applyRobertsOperator(in);
            default -> throw new IllegalStateException("Unexpected value: " + operatorType);
        }
    }

    private void applyRobertsOperator(BufferedImage in) {
        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                int newRGB = (255 << ALPHA_OFFSET);
                // 1 2
                // 3 4
                int newRGB1 = in.getRGB(w, h);
                int newRGB2 = 0;
                int newRGB3 = 0;
                int newRGB4 = 0;
                if (w < widthOfImage - 1) newRGB2 = in.getRGB(w + 1, h);
                if (h < heightOfImage - 1) newRGB3 = in.getRGB(w, h + 1);
                if (w < widthOfImage - 1 && h < heightOfImage - 1) newRGB4 = in.getRGB(w + 1, h + 1);
                for (int offset = 16; offset >= 0; offset -= 8) { // go across color channels
                    int color1 = (newRGB1 >> offset) & 0xff;
                    int color2 = (newRGB2 >> offset) & 0xff;
                    int color3 = (newRGB3 >> offset) & 0xff;
                    int color4 = (newRGB4 >> offset) & 0xff;
                    int grad = Math.abs(color4 - color1) + Math.abs(color3 - color2);
                    newRGB |= (getNewColor(grad, offset) << offset);
                }
                out.setRGB(w, h, newRGB);
            }
        }
    }


    private void applySobelOperator(BufferedImage in) {
        setFilterSize(3);
        setImage(in);
        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                setFragment(w, h);
                int red = applySobelOperatorToChannel(RED_OFFSET);
                int green = applySobelOperatorToChannel(GREEN_OFFSET);
                int blue = applySobelOperatorToChannel(BLUE_OFFSET);
                int rgb = (255 << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
                out.setRGB(w, h, rgb);
            }
        }
    }

    private int applySobelOperatorToChannel(int colorOffset) {
        int sx = getColor(fragmentOfImage[0][2], colorOffset) + 2 * getColor(fragmentOfImage[1][2], colorOffset) + getColor(fragmentOfImage[2][2], colorOffset);
        sx -= getColor(fragmentOfImage[0][0], colorOffset) + 2 * getColor(fragmentOfImage[1][0], colorOffset) + getColor(fragmentOfImage[2][0], colorOffset);
        int sy = getColor(fragmentOfImage[2][0], colorOffset) + 2 * getColor(fragmentOfImage[2][1], colorOffset) + getColor(fragmentOfImage[2][2], colorOffset);
        sy -= getColor(fragmentOfImage[0][0], colorOffset) + 2 * getColor(fragmentOfImage[0][1], colorOffset) + getColor(fragmentOfImage[0][2], colorOffset);
        int s = Math.abs(sx) + Math.abs(sy);
        return getNewColor(s, colorOffset);
    }

    private int getColor(int rgb, int colorOffset) {
        return (rgb >> colorOffset) & 0xff;
    }

    private int getNewColor(int grad, int colorOffset) {
        int colorLimit;
        switch (colorOffset) {
            case RED_OFFSET -> colorLimit = redLimit;
            case GREEN_OFFSET -> colorLimit = greenLimit;
            case BLUE_OFFSET -> colorLimit = blueLimit;
            default -> throw new IllegalStateException("Unexpected value: " + colorOffset);
        }
        if (grad > colorLimit) return 255;
        else return 0;
    }
}
