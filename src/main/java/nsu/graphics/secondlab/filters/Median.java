package nsu.graphics.secondlab.filters;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Median extends FilterApplier {
    public BufferedImage selectFilter(BufferedImage image) {
        setFilterSize(5);
        return applyMedianFilter(image);
    }

    private BufferedImage applyMedianFilter(BufferedImage image) {
        setImage(image);

        BufferedImage out = new BufferedImage(widthOfImage, heightOfImage, BufferedImage.TYPE_INT_ARGB);

        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                setFragment(w, h);
                int red = applyMedianFilterToChannel(RED_OFFSET);
                int green = applyMedianFilterToChannel(GREEN_OFFSET);
                int blue = applyMedianFilterToChannel(BLUE_OFFSET);
                int rgb = (255 << ALPHA_OFFSET) | (red << RED_OFFSET) | (green << GREEN_OFFSET) | blue;
                out.setRGB(w, h, rgb);
            }
        }
        return out;
    }

    private int applyMedianFilterToChannel(int colorOffset) {
        List<Integer> colors = new LinkedList<>();
        for (int h = 0; h < filterSize; h++) {
            for (int w = 0; w < filterSize; w++) {
                int color = (fragmentOfImage[h][w] >> colorOffset) & 0xff;
                colors.add(color);
            }
        }
        colors.sort(Comparator.naturalOrder());
        return colors.get(12);
    }
}
