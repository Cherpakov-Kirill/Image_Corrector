package nsu.graphics.secondlab.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FilterApplier {
    protected static final int ALPHA_OFFSET = 24;
    protected static final int RED_OFFSET = 16;
    protected static final int GREEN_OFFSET = 8;
    protected static final int BLUE_OFFSET = 0;

    protected int filterSize;
    private int filterStep;
    private double[][] filter;
    protected int[][] fragmentOfImage;

    private BufferedImage in;
    protected int widthOfImage;
    protected int heightOfImage;

    protected FilterApplier() {
    }

    protected void setFilterSize(int filterSize){
        this.filterSize = filterSize;
        this.filterStep = filterSize / 2;
        this.fragmentOfImage = new int[filterSize][filterSize];
    }

    protected void setFilter(int filterSize, double[][] filter) {
        setFilterSize(filterSize);
        this.filter = filter;
    }

    /// Ox axis is width of Image;
    /// Oy axis is height of Image;

    protected BufferedImage applyFilter(BufferedImage image) {
        setImage(image);

        BufferedImage out = new BufferedImage(widthOfImage, heightOfImage, BufferedImage.TYPE_INT_ARGB);

        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                setFragment(w, h);
                int red = applyFilterToChannel(RED_OFFSET);
                int green = applyFilterToChannel(GREEN_OFFSET);
                int blue = applyFilterToChannel(BLUE_OFFSET);
                int rgb = (255 << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
                out.setRGB(w, h, rgb);
            }
        }
        return out;
    }

    protected BufferedImage applyFilter(BufferedImage image, int offset) {
        //Embossing filter
        setImage(image);

        BufferedImage out = new BufferedImage(widthOfImage, heightOfImage, BufferedImage.TYPE_INT_ARGB);
        int maxVal = 255 - offset;
        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                setFragment(w, h);
                int red = applyFilterToChannel(RED_OFFSET);
                int green = applyFilterToChannel(GREEN_OFFSET);
                int blue = applyFilterToChannel(BLUE_OFFSET);
                red = red <= maxVal ? red + offset : 255;
                green = green <= maxVal ? green + offset : 255;
                blue = blue <= maxVal ? blue + offset : 255;
                int rgb = (255 << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
                out.setRGB(w, h, rgb);
            }
        }
        return out;
    }

    private int applyFilterToChannel(int colorOffset) {
        int result = 0;
        for (int h = 0; h < filterSize; h++) {
            for (int w = 0; w < filterSize; w++) {
                int color = (fragmentOfImage[h][w] >> colorOffset) & 0xff;
                result += color * filter[h][w];
            }
        }
        if (result > 255) result = 255;
        if (result < 0) result = 0;
        return result;
    }

    protected void setImage(BufferedImage image) {
        widthOfImage = image.getWidth();
        heightOfImage = image.getHeight();

        int widthWithOffset = widthOfImage + filterStep * 2;
        int heightWithOffset = heightOfImage + filterStep * 2;

        this.in = new BufferedImage(widthWithOffset, heightWithOffset, image.getType());
        Graphics g = in.getGraphics();
        g.drawImage(image, filterStep, filterStep, image.getWidth(), image.getHeight(), null);
        g.dispose();

        int rgb = image.getRGB(0, 0);
        for (int h = 0; h < filterStep; h++) {
            for (int w = 0; w < filterStep; w++) {
                in.setRGB(w, h, rgb);
            }
        }

        rgb = image.getRGB(widthOfImage - 1, 0);
        for (int h = 0; h < filterStep; h++) {
            for (int w = widthOfImage + filterStep; w < widthWithOffset; w++) {
                in.setRGB(w, h, rgb);
            }
        }

        rgb = image.getRGB(widthOfImage - 1, heightOfImage - 1);
        for (int h = heightOfImage + filterStep; h < heightWithOffset; h++) {
            for (int w = widthOfImage + filterStep; w < widthWithOffset; w++) {
                in.setRGB(w, h, rgb);
            }
        }

        rgb = image.getRGB(0, heightOfImage - 1);
        for (int h = heightOfImage + filterStep; h < heightWithOffset; h++) {
            for (int w = 0; w < filterStep; w++) {
                in.setRGB(w, h, rgb);
            }
        }

        for (int h = filterStep; h < heightOfImage + filterStep; h++) {
            rgb = image.getRGB(0, h - filterStep);
            for (int w = 0; w < filterStep; w++) {
                in.setRGB(w, h, rgb);
            }
        }

        for (int h = filterStep; h < heightOfImage + filterStep; h++) {
            rgb = image.getRGB(widthOfImage - 1, h - filterStep);
            for (int w = widthOfImage + filterStep; w < widthWithOffset; w++) {
                in.setRGB(w, h, rgb);
            }
        }

        for (int w = filterStep; w < widthOfImage + filterStep; w++) {
            rgb = image.getRGB(w - filterStep, 0);
            for (int h = 0; h < filterStep; h++) {
                in.setRGB(w, h, rgb);
            }
        }

        for (int w = filterStep; w < widthOfImage + filterStep; w++) {
            rgb = image.getRGB(w - filterStep, heightOfImage - 1);
            for (int h = heightOfImage + filterStep; h < heightWithOffset; h++) {
                in.setRGB(w, h, rgb);
            }
        }
    }

    protected void setFragment(int x, int y) {
        for (int h = 0; h < filterSize; h++) {
            for (int w = 0; w < filterSize; w++) {
                //(x+filterOffset) - coordinate in image with offset
                //(x+filterOffset) - filterOffset + w - coordinate in image with offset for fragment[0][0] position
                //x+ w - coordinate in image with offset for fragment[0][0] position
                fragmentOfImage[h][w] = in.getRGB(x + w, y + h);
            }
        }
    }

}
