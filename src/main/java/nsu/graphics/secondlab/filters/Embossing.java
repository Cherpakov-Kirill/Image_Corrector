package nsu.graphics.secondlab.filters;

import java.awt.image.BufferedImage;

public class Embossing extends FilterApplier{
    private static final int filterSize = 3;
    private static final int offset = 128;

    public Embossing() {
        super();
        createFilter();
    }

    public BufferedImage selectFilter(BufferedImage image) {
        return applyFilter(image, offset);
    }

    private void createFilter() {
        double[][] filter = new double[][]{{0, -1, 0}, {1, 0, -1}, {0, 1, 0}};
        setFilter(filterSize, filter);
    }
}
