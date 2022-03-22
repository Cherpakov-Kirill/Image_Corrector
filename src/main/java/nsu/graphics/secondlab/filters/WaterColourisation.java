package nsu.graphics.secondlab.filters;

import java.awt.image.BufferedImage;

public class WaterColourisation extends FilterApplier {
    private final Median medianFilter;

    public WaterColourisation(Median medianFilter) {
        super();
        this.medianFilter = medianFilter;
        createSharpnessFilter();
    }

    public BufferedImage selectFilter(BufferedImage image) {
        return applyFilter(medianFilter.selectFilter(image));
    }

    private void createSharpnessFilter() {
        double[][] filter = new double[][]{{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};
        setFilter(3, filter);
    }
}
