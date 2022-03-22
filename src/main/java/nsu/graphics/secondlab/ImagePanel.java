package nsu.graphics.secondlab;

import nsu.graphics.secondlab.filters.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private final Dimension panelSize;          // visible image size
    private final JScrollPane spIm;
    private BufferedImage img = null;           // image to view
    private BufferedImage originalImg = null;   // original image
    private Dimension imSize = null;            // real image size
    private int lastX = 0, lastY = 0;           // last captured mouse coordinates
    private final double zoomK = 0.05;          // scroll zoom coefficient
    boolean fitToScreen;
    boolean filteredImageOnScreen;              //if true - filtered image is on screen
    private BufferedImage filteredImage;
    private Rectangle scalingRectangle;

    private final Smoothing smoothingFilter;
    private final Sharpness sharpnessFilter;
    private final Embossing embossingFilter;
    private final GammaCorrection gammaCorrection;
    private final Median medianFilter;
    private final WaterColourisation waterColourisationFilter;
    private final Rotation changeRotation;
    private final Dithering ditheringFilter;
    private final OrderedDithering orderedDithering;
    private final Boarder boarderFilter;

    /**
     * Creates default Image-viewer in the given JScrollPane.
     * Visible space will be painted in black.
     * <p>
     *
     * @param scrollPane - JScrollPane to add a new Image-viewer
     * @param width      - start width of panel
     * @param height     - start height of panel
     */
    public ImagePanel(JScrollPane scrollPane, int width, int height) {
        spIm = scrollPane;
        spIm.setWheelScrollingEnabled(false);
        spIm.setDoubleBuffered(true);
        spIm.setViewportView(this);
        panelSize = new Dimension();
        setPanelSize(width, height);
        spIm.validate();
        this.fitToScreen = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        this.smoothingFilter = new Smoothing();
        this.sharpnessFilter = new Sharpness();
        this.embossingFilter = new Embossing();
        this.gammaCorrection = new GammaCorrection();
        this.medianFilter = new Median();
        this.waterColourisationFilter = new WaterColourisation(medianFilter);
        this.changeRotation = new Rotation();
        ditheringFilter = new Dithering();
        orderedDithering = new OrderedDithering();
        boarderFilter = new Boarder();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, panelSize.width, panelSize.height);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(4, 4, panelSize.width - 4, 4);
        g2d.drawLine(4, 4, 4, panelSize.height - 4);
        g2d.drawLine(panelSize.width - 4, 4, panelSize.width - 4, panelSize.height - 4);
        g2d.drawLine(4, panelSize.height - 4, panelSize.width - 4, panelSize.height - 4);
        if (img != null) g.drawImage(img, 4, 4, panelSize.width - 8, panelSize.height - 8, null);
        //  Paint the Rectangle as the mouse is being dragged
        if (scalingRectangle != null) {
            g2d.draw(scalingRectangle);
        }
    }

    /**
     * Sets a new image to view.
     * If the given image is null, visible space will be painted in black.
     * Default view causes to "fit-screen" view.
     * If fitToScreen is set to false, viewer will show the last viewed part of the previous image.
     * But only if both of the images have the same size.
     *
     * @param newIm - new image to view
     */
    private void setImage(BufferedImage newIm) {
        // fitToScreen == true (means "fit screen (panel))"
        // fitToScreen == false (means "real size (panel))"
        img = new BufferedImage(newIm.getWidth(), newIm.getHeight(), newIm.getType());
        Graphics g = img.getGraphics();
        g.drawImage(newIm, 0, 0, null);
        g.dispose();
        imSize = new Dimension(img.getWidth(), img.getHeight());

        if (fitToScreen) {
            ///fit to screen
            setMaxVisibleRectSize();
            double kh = (double) imSize.height / panelSize.height;
            double kw = (double) imSize.width / panelSize.width;
            double k = Math.max(kh, kw);
            panelSize.width = (int) (imSize.width / k);
            panelSize.height = (int) (imSize.height / k);
        } else {
            ///real-size image
            setPanelSize(imSize);
        }
        revalidate();
        repaint();
        spIm.getViewport().setViewPosition(new Point(0, 0));
        spIm.revalidate();
        spIm.repaint();    // wipe off the old picture in "spare" space
        spIm.paintAll(spIm.getGraphics());
    }

    public void saveFile(File file) {
        try {
            String filename = file.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1);
            System.out.println(extension);
            ImageIO.write(img, extension, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFile(File file) {
        try {
            originalImg = ImageIO.read(file);
            setImage(originalImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean applyFilter(String selectedFilter) {
        imSize = new Dimension(originalImg.getWidth(), originalImg.getHeight());
        switch (selectedFilter) {
            case "Rotation" -> {
                BufferedImage filtered = changeRotation.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;

                imSize = new Dimension(img.getWidth(), img.getHeight());
                setPanelSize(img.getWidth(),img.getHeight());
            }
            case "Shades of grey" -> this.img = ShadesOfGrey.applyFilter(originalImg);
            case "Invert" -> this.img = Inverter.applyFilter(originalImg);
            case "Smoothing" -> {
                BufferedImage filtered = smoothingFilter.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Sharpness" -> {
                BufferedImage filtered = sharpnessFilter.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Embossing" -> {
                BufferedImage filtered = embossingFilter.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Gamma" -> {
                BufferedImage filtered = gammaCorrection.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Median" -> {
                BufferedImage filtered = medianFilter.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Water-colourisation" -> {
                BufferedImage filtered = waterColourisationFilter.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Gray World" -> this.img = GreyWorld.applyFilter(originalImg);
            case "Dithering" -> {
                BufferedImage filtered = ditheringFilter.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Ordered Dithering" -> {
                BufferedImage filtered = orderedDithering.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            case "Boarder" -> {
                BufferedImage filtered = boarderFilter.selectFilter(originalImg);
                if (filtered == null) return false;
                this.img = filtered;
            }
            default -> {
                return true;
            }
        }
        filteredImageOnScreen = true;
        filteredImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics g = filteredImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        if(fitToScreen) fitToScreen();
        else realSize();
        return true;
    }

    ///IMAGE VIEW

    /**
     * Sets original image on view.
     */

    public void setOriginalImage() {
        setImage(originalImg);
        filteredImage = null;
        filteredImageOnScreen = false;
    }

    /**
     * Sets "fit-screen" view.
     */
    public void fitToScreen() {
        fitToScreen = true;
        setImage(img);
    }

    /**
     * Sets "real-size" view.
     */
    public void realSize() {
        fitToScreen = false;
        setImage(img);
    }


    ///SIZE OF PANEL

    private void setPanelSize(int width, int height) {
        panelSize.width = width + 8;
        panelSize.height = height + 8;
    }

    private void setPanelSize(Dimension dimension) {
        setPanelSize(dimension.width, dimension.height);
    }

    /**
     * @return Dimension object with the current view-size
     */
    private Dimension getVisibleRectSize() {
        // maximum size for panel without scrolling (inner border of the ScrollPane)
        Dimension viewportSize = spIm.getViewport().getSize();
        if (viewportSize.height == 0) return new Dimension(spIm.getWidth() - 3, spIm.getHeight() - 3);
        else return viewportSize;
    }

    /**
     * Sets panelSize to the maximum available view-size with hidden scroll bars.
     */
    private void setMaxVisibleRectSize() {
        // maximum size for panel without scrolling (inner border of the ScrollPane)
        setPanelSize(getVisibleRectSize().width - 8, getVisibleRectSize().height - 8);    // max size, but possibly with enabled scroll-bars
        revalidate();
        spIm.validate();
        setPanelSize(getVisibleRectSize().width - 8, getVisibleRectSize().height - 8);    // max size, without enabled scroll-bars
        revalidate();
    }

    ///SCROLL OF IMAGE

    private void setView(Rectangle rect) {
        setView(rect, 10);
    }

    private void setView(Rectangle rect, int minSize) {
        // should also take into account ScrollBars size
        if (img == null) return;
        if (imSize.width < minSize || imSize.height < minSize) return;

        if (minSize <= 0) minSize = 10;

        if (rect.width < minSize) rect.width = minSize;
        if (rect.height < minSize) rect.height = minSize;
        if (rect.x < 0) rect.x = 0;
        if (rect.y < 0) rect.y = 0;
        if (rect.x > imSize.width - minSize) rect.x = imSize.width - minSize;
        if (rect.y > imSize.height - minSize) rect.y = imSize.height - minSize;
        if ((rect.x + rect.width) > imSize.width) rect.width = imSize.width - rect.x;
        if ((rect.y + rect.height) > imSize.height) rect.height = imSize.height - rect.y;

        Dimension viewSize = getVisibleRectSize();
        double kw = (double) rect.width / viewSize.width;
        double kh = (double) rect.height / viewSize.height;
        double k = Math.max(kh, kw);

        int newPW = (int) (imSize.width / k);
        int newPH = (int) (imSize.height / k);
        // Check for size whether we can still zoom out
        if (newPW == (int) (newPW * (1 - 2 * zoomK))) setView(rect, minSize * 2);
        setPanelSize(newPW, newPH);

        revalidate();
        spIm.validate();
        // сначала нужно, чтобы scroll понял новый размер, потом сдвигать

        int xc = rect.x + rect.width / 2, yc = rect.y + rect.height / 2;
        xc = (int) (xc / k);
        yc = (int) (yc / k);    // we need to center new view
        //int x0 = (int)(rect.x/k), y0 = (int)(rect.y/k);
        spIm.getViewport().setViewPosition(new Point(xc - viewSize.width / 2, yc - viewSize.height / 2));
        revalidate();    // spIm.validate();
        spIm.paintAll(spIm.getGraphics());
    }

    @Override
    public Dimension getPreferredSize() {
        return panelSize;
    }

    /**
     * Change zoom when scrolling
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (img == null)
            return;

        // Zoom
        double k = 1 - e.getWheelRotation() * zoomK;

        // Check for minimum size where we can still increase size
        int newPW = (int) (panelSize.width * k);
        if (newPW == (int) (newPW * (1 + zoomK))) return;

        if (k > 1) {
            int newPH = (int) (panelSize.height * k);
            Dimension viewSize = getVisibleRectSize();
            int pixSizeX = newPW / imSize.width;
            int pixSizeY = newPH / imSize.height;
            if (pixSizeX > 0 && pixSizeY > 0) {
                int pixNumX = viewSize.width / pixSizeX;
                int pixNumY = viewSize.height / pixSizeY;
                if (pixNumX < 2 || pixNumY < 2)
                    return;
            }
        }

        setPanelSize(newPW, (int) ((long) newPW * imSize.height / imSize.width));

        // Move so that mouse position doesn't visibly change
        int x = (int) (e.getX() * k);
        int y = (int) (e.getY() * k);
        Point scroll = spIm.getViewport().getViewPosition();
        scroll.x -= e.getX();
        scroll.y -= e.getY();
        scroll.x += x;
        scroll.y += y;

        repaint();    // можно и убрать
        revalidate();
        spIm.validate();
        // сначала нужно, чтобы scroll понял новый размер, потом сдвигать
        spIm.getHorizontalScrollBar().setValue(scroll.x);
        spIm.getVerticalScrollBar().setValue(scroll.y);
        spIm.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(filteredImage == null) return;
        if (filteredImageOnScreen) {
            img = originalImg;
            filteredImageOnScreen = false;
        }
        else {
            img = filteredImage;
            filteredImageOnScreen = true;
        }
        repaint();
    }


    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON3) scalingRectangle = new Rectangle();
    }

    /**
     * Move visible image part when dragging
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
            int x = Math.min(lastX, e.getX());
            int y = Math.min(lastY, e.getY());
            int width = Math.abs(lastX - e.getX());
            int height = Math.abs(lastY - e.getY());
            scalingRectangle.setBounds(x, y, width, height);
            repaint();
            return;
        }

        // move picture using scroll
        Point scroll = spIm.getViewport().getViewPosition();
        scroll.x += (lastX - e.getX());
        scroll.y += (lastY - e.getY());
        spIm.getHorizontalScrollBar().setValue(scroll.x);
        spIm.getVerticalScrollBar().setValue(scroll.y);
        spIm.repaint();
    }

    /**
     * When a rectangle is selected with pressed right button,
     * we zoom image to that rectangle
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON3) return;
        scalingRectangle = null;

        int x1 = e.getX();
        int y1 = e.getY();
        // Исключаем клик
        if (Math.abs(x1 - lastX) < 5 && Math.abs(y1 - lastY) < 5) return;

        double k = (double) imSize.width / panelSize.width;

        int x0 = (int) (k * lastX);
        int y0 = (int) (k * lastY);
        x1 = (int) (k * x1);
        y1 = (int) (k * y1);

        int w = Math.abs(x1 - x0);
        int h = Math.abs(y1 - y0);
        if (x1 < x0) x0 = x1;
        if (y1 < y0) y0 = y1;
        setView(new Rectangle(x0, y0, w, h));
    }

    ///NOT USED

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}