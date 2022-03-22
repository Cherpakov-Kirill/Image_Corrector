package nsu.graphics.secondlab.filters;

import nsu.graphics.secondlab.parameters.DitheringParameters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class OrderedDithering {
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
    private int redMatrixSize;
    private int greenMatrixSize;
    private int blueMatrixSize;
    private double[][] redMatrix;
    private double[][] greenMatrix;
    private double[][] blueMatrix;

    private BufferedImage out;
    private final Map<Integer, Matrix> matrixMap;

    static class Matrix {
        private final double[][] matrix;

        Matrix(int size, double[][] prev) {
            if (size == 2) matrix = new double[][]{{0, 0.5}, {0.25, 0.75}};
            else {
                matrix = new double[size][size];
                double coefficient1 = Math.pow(2 * size, 2);
                int prevSize = size / 2;
                double[][] prevMatrix = new double[prevSize][prevSize];
                for (int h = 0; h < prevSize; h++) {
                    for (int w = 0; w < prevSize; w++) {
                        prevMatrix[h][w] = prev[h][w] * coefficient1;
                    }
                }
                for (int h = 0; h < prevSize; h++) {
                    for (int w = 0; w < prevSize; w++) {
                        matrix[h][w] = prevMatrix[h % prevSize][w % prevSize];
                    }
                    for (int w = prevSize; w < size; w++) {
                        matrix[h][w] = prevMatrix[h % prevSize][w % prevSize] + 2;
                    }
                }

                for (int h = prevSize; h < size; h++) {
                    for (int w = 0; w < prevSize; w++) {
                        matrix[h][w] = prevMatrix[h % prevSize][w % prevSize] + 3;
                    }
                    for (int w = prevSize; w < size; w++) {
                        matrix[h][w] = prevMatrix[h % prevSize][w % prevSize] + 1;
                    }
                }
                double coefficient2 = 1 / coefficient1;
                for (int h = 0; h < size; h++) {
                    for (int w = 0; w < size; w++) {
                        matrix[h][w] *= coefficient2;
                    }
                }
            }
        }

        public double[][] getMatrix() {
            return matrix;
        }
    }

    public OrderedDithering() {
        super();
        this.parameters = new DitheringParameters();
        redRange = 0;
        greenRange = 0;
        blueRange = 0;
        matrixMap = new HashMap<>();
    }

    private double[][] getMatrix(int size) {
        if (!matrixMap.containsKey(size)) {
            double[][] prevMatrix = null;
            if(matrixMap.size() != 0) prevMatrix = matrixMap.get((int) Math.pow(2, matrixMap.size())).getMatrix();
            for (int i = (int) Math.pow(2, matrixMap.size() + 1); i <= size; i *= 2) {
                Matrix matrix = new Matrix(i, prevMatrix);
                matrixMap.put(i, matrix);
                prevMatrix = matrix.getMatrix();
            }
        }
        return matrixMap.get(size).getMatrix();
    }

    public BufferedImage selectFilter(BufferedImage in) {
        if (parameters.showDialog()) {
            int newRedRange = parameters.getRedRange();
            int newGreenRange = parameters.getGreenRange();
            int newBlueRange = parameters.getBlueRange();
            if (redRange != newRedRange) {
                redRange = newRedRange;
                redDelta = 255.0 / (double) (redRange - 1);
                redMatrixSize = computeMatrixSize(redRange);
                redMatrix = getMatrix(redMatrixSize);
            }
            if (greenRange != newGreenRange) {
                greenRange = newGreenRange;
                greenDelta = 255.0 / (double) (greenRange - 1);
                greenMatrixSize = computeMatrixSize(greenRange);
                greenMatrix = getMatrix(greenMatrixSize);
            }
            if (blueRange != newBlueRange) {
                blueRange = newBlueRange;
                blueDelta = 255.0 / (double) (blueRange - 1);
                blueMatrixSize = computeMatrixSize(blueRange);
                blueMatrix = getMatrix(blueMatrixSize);
            }
            applyFilter(in);
            return out;
        }
        return null;
    }

    private void applyFilter(BufferedImage in) {
        this.out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = out.getGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
        int widthOfImage = out.getWidth();
        int heightOfImage = out.getHeight();

        for (int h = 0; h < heightOfImage; h++) {
            for (int w = 0; w < widthOfImage; w++) {
                int rgb = out.getRGB(w, h);
                int newRGB = (255 << ALPHA_OFFSET);
                for (int offset = 16; offset >= 0; offset -= 8) { // go across color channels
                    int color = (rgb >> offset) & 0xff;
                    int newColor = getNewColor(color, h, w, offset);
                    newRGB |= (newColor << offset);
                }
                out.setRGB(w, h, newRGB);
            }
        }
    }

    private int computeMatrixSize(int colorRange) {
        double min = 256.0 / (double) colorRange;
        int size = 2;
        int square = size * size;
        while (square < min) {
            size*=2;
            square = size * size;
        }
        return size;
    }

    private int getNewColor(int colorOld, int h, int w, int colorOffset){
        switch (colorOffset) {
            case RED_OFFSET -> {
                return nearestColor((int) Math.round((double)colorOld + redDelta*(redMatrix[h % redMatrixSize][w % redMatrixSize] - 0.5)),colorOffset);
            }
            case GREEN_OFFSET -> {
                return nearestColor((int) Math.round((double)colorOld + greenDelta*(greenMatrix[h % greenMatrixSize][w % greenMatrixSize] - 0.5)),colorOffset);
            }
            case BLUE_OFFSET -> {
                return nearestColor((int) Math.round((double)colorOld + blueDelta*(blueMatrix[h % blueMatrixSize][w % blueMatrixSize] - 0.5)),colorOffset);
            }
            default -> {
                return 0;
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
}
