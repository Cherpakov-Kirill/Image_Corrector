package nsu.graphics.secondlab.filters;

import nsu.graphics.secondlab.parameters.RotationParameters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rotation {
    private final RotationParameters parameters;
    private double degree;

    public Rotation() {
        super();
        this.parameters = new RotationParameters();
        degree = 1;
    }

    public BufferedImage selectFilter(BufferedImage image) {
        if (parameters.showDialog()) {
            double newGamma = parameters.getDegree();
            if (degree != newGamma) {
                degree = newGamma;
                System.out.println("New value: gamma = " + degree);
            }
            return rotateImage(image, degree);
        }
        return null;
    }

    private BufferedImage rotateImage(BufferedImage image, double degree) {
        int w = image.getWidth();
        int h = image.getHeight();
        double degreeRad = Math.toRadians(degree);
        double sin = Math.abs(Math.sin(degreeRad)), cos = Math.abs(Math.cos(degreeRad));
        int wRotated = (int) Math.floor(w * cos + h * sin), hRotated = (int) Math.floor(h * cos + w * sin);

        BufferedImage out = new BufferedImage(wRotated, hRotated, image.getType());
        Graphics2D graphics2d = (Graphics2D) out.getGraphics();
        graphics2d.translate((wRotated - w) / 2, (hRotated - h) / 2);
        graphics2d.rotate(degreeRad, w / 2, h / 2);
        graphics2d.drawImage(image, 0, 0, null);
        graphics2d.dispose();
        return out;
    }
}
