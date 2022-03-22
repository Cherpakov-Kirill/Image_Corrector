package nsu.graphics.secondlab.filters;

import nsu.graphics.secondlab.parameters.SmoothingParameters;

import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class Smoothing extends FilterApplier {
    private final SmoothingParameters parameters;
    private int filterSize;
    private double sigma;

    public Smoothing() {
        super();
        this.parameters = new SmoothingParameters();
        filterSize = 0;
        sigma = 0;
    }

    public BufferedImage selectFilter(BufferedImage image) {
        if (parameters.showDialog()) {
            int newFilterSize = parameters.getFilterSize();
            double newSigma = parameters.getSigma();
            if (filterSize != newFilterSize || sigma != newSigma) {
                filterSize = newFilterSize;
                sigma = newSigma;
                System.out.println("New values: filterSize = " + filterSize + " sigma = " + sigma);
                createFilter();
            }
            return applyFilter(image);
        }
        return null;
    }

    private void createFilter() {
        double[][] filter = new double[filterSize][filterSize];
        double sum = 0;
        for (int y = 0; y < filterSize; y++) {
            int yPoint = y - filterSize / 2;
            int yy = yPoint * yPoint;
            for (int x = 0; x < filterSize; x++) {
                int xPoint = x - filterSize / 2;
                int xx = xPoint * xPoint;
                double rr = yy + xx;
                double gaussian = gaussianDistribution(rr, sigma);
                filter[y][x] = gaussian;
                sum += gaussian;
            }
        }
        double normalizer = 1 / sum;
        for (int y = 0; y < filterSize; y++) {
            for (int x = 0; x < filterSize; x++) {
                filter[y][x] *= normalizer;
            }
        }
        setFilter(filterSize, filter);
    }

    private double gaussianDistribution(double rr, double sigma) {
        double res = 1 / (sigma * sigma * 2 * PI);
        res *= exp(-1 * (rr) / (2 * sigma * sigma));
        return res;
    }

}
