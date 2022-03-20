package nsu.graphics.secondlab.filters;

import nsu.graphics.secondlab.parameters.SharpnessParameters;

import java.awt.image.BufferedImage;

public class Sharpness extends FilterApplier {
    private final SharpnessParameters parameters;
    private int filterSize;

    public Sharpness() {
        super();
        this.parameters = new SharpnessParameters();
        filterSize = 0;
    }

    public BufferedImage selectFilter(BufferedImage image) {
        if (parameters.showDialog()) {
            int newFilterSize = parameters.getFilterSize();
            if (filterSize != newFilterSize) {
                filterSize = newFilterSize;
                System.out.println("New value: filterSize = " + filterSize);
                createFilter();
            }
            return applyFilter(image);
        }
        return null;
    }

    private void createFilter() {
        double[][] filter = null;
        //double[][] filter = new double[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        switch (filterSize) {
            case 3 -> filter = new double[][]{{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};
            case 5 -> filter = new double[][]{{-1, -1, -1, -1, -1}, {-1, 2, 2, 2, -1}, {-1, 2, 8, 2, -1}, {-1, 2, 2, 2, -1}, {-1, -1, -1, -1, -1}};
        }
        if(filter!= null) {
            double sum = 0;
            for (int y = 0; y < filterSize; y++) {
                for (int x = 0; x < filterSize; x++) {
                    sum += filter[y][x];
                }
            }
            System.out.println("Size = " + filterSize + " Sum = " + sum);
            double normalizer = 1 / sum;
            for (int y = 0; y < filterSize; y++) {
                for (int x = 0; x < filterSize; x++) {
                    filter[y][x] *= normalizer;
                }
            }
        }
        setFilter(filterSize, filter);
    }
}
