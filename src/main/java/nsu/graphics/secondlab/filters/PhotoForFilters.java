package nsu.graphics.secondlab.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface PhotoForFilters {
    Dimension getPreferredSize();
    Graphics2D getImageGraphics();
    BufferedImage getImage();
    Color getSelectedColor();
}
