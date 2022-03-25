package nsu.graphics.secondlab;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.*;

import ru.nsu.cg.MainFrame;


public class PhotoCorrectorWindow extends MainFrame implements ComponentListener {
    private final ImagePanel photoPanel;
    private final String[] extensions;

    public PhotoCorrectorWindow() {
        super(700, 500, "Image Corrector");
        try {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Open", "Open a file", KeyEvent.VK_O, "/Open.png", "openFile");
            addMenuItem("File/Save as", "Save your picture as file", KeyEvent.VK_S, "/Save.png", "saveFile");
            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X, "/Exit.png", "exit");

            addSubMenu("View", KeyEvent.VK_V);
            addRadioMenuItem("View/Fit to screen", "Fit image to the screen", KeyEvent.VK_F, "/FitImage.png", "fitToScreen");
            addRadioMenuItem("View/Real size", "Set real size", KeyEvent.VK_R, "/RealSize.png", "realSize");

            addSubMenu("Filters", KeyEvent.VK_F);
            addRadioMenuItem("Filters/Set original", "Set original image", KeyEvent.VK_O, "/Original.png", "setOriginalImage");
            addRadioMenuItem("Filters/Rotation", "Change rotation", KeyEvent.VK_R, "/Rotation.png", "changeRotation");
            addRadioMenuItem("Filters/Shades of grey", "Apply Shades of grey filter", KeyEvent.VK_B, "/ShadesOfGrey.png", "shadesOfGrey");
            addRadioMenuItem("Filters/Invert", "Invert all colors", KeyEvent.VK_I, "/Invert.png", "invertColors");
            addRadioMenuItem("Filters/Smoothing", "Smoothing of the image", KeyEvent.VK_Z, "/Smoothing.png", "smoothing");
            addRadioMenuItem("Filters/Sharpness", "Sharpness filter", KeyEvent.VK_S, "/Sharpness.png", "sharpness");
            addRadioMenuItem("Filters/Embossing", "Embossing filter", KeyEvent.VK_E, "/Embossing.png", "embossing");
            addRadioMenuItem("Filters/Gamma", "Gamma filter", KeyEvent.VK_G, "/Gamma.png", "gamma");
            addRadioMenuItem("Filters/Median", "Median filter", KeyEvent.VK_M, "/Median.png", "median");
            addRadioMenuItem("Filters/Boarder", "Boarder filter", KeyEvent.VK_B, "/Boarder.png", "boarder");
            addRadioMenuItem("Filters/Dithering", "Apply a dithering", KeyEvent.VK_D, "/Dithering.png", "dithering");
            addRadioMenuItem("Filters/Ordered Dithering", "Apply a ordered dithering", KeyEvent.VK_O, "/OrderedDithering.png", "orderedDithering");
            addRadioMenuItem("Filters/Water-colourisation", "Apply a water-colourisation", KeyEvent.VK_A, "/Water-colourisation.png", "watercolourisation");
            addRadioMenuItem("Filters/Gray World", "Apply a Gray World", KeyEvent.VK_W, "/GrayWorld.png", "grayWorld");

            addSubMenu("Rendering", KeyEvent.VK_R);
            addRadioMenuItem("Rendering/Bilinear interpolation", "Apply the bilinear interpolation algorithm", KeyEvent.VK_L, "/Linear.png", "linearInterpolation");
            addRadioMenuItem("Rendering/Bicubic interpolation", "Apply the bicubic interpolation algorithm", KeyEvent.VK_C, "/Cubic.png", "cubicInterpolation");
            addRadioMenuItem("Rendering/Nearest neighboring interpolation", "Apply the nearest neighboring interpolation algorithm", KeyEvent.VK_N, "/Nearest.png", "nearestInterpolation");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "/About.png", "showAbout");
            addMenuItem("Help/Usage", "Shows program usage information", KeyEvent.VK_U, "/Usage.png", "showUsage");

            addToolBarButton("File/Open");
            addToolBarButton("File/Save as");
            addToolBarSeparator();
            addToolBarToggleButton("View/Fit to screen");
            addToolBarToggleButton("View/Real size");
            addToolBarSeparator();
            addToolBarToggleButton("Filters/Set original");
            addToolBarToggleButton("Filters/Rotation");
            addToolBarToggleButton("Filters/Shades of grey");
            addToolBarToggleButton("Filters/Invert");
            addToolBarToggleButton("Filters/Smoothing");
            addToolBarToggleButton("Filters/Sharpness");
            addToolBarToggleButton("Filters/Embossing");
            addToolBarToggleButton("Filters/Gamma");
            addToolBarToggleButton("Filters/Median");
            addToolBarToggleButton("Filters/Boarder");
            addToolBarToggleButton("Filters/Dithering");
            addToolBarToggleButton("Filters/Ordered Dithering");
            addToolBarToggleButton("Filters/Water-colourisation");
            addToolBarToggleButton("Filters/Gray World");

            JScrollPane scrollPane = new JScrollPane();
            photoPanel = new ImagePanel(scrollPane, 685, 395);
            scrollPane.setViewportView(photoPanel);

            add(scrollPane);
            addComponentListener(this);
            setBackground(Color.WHITE);

            /*String path = "/home/kirill/Desktop/Screenshot from 2022-03-15 00-23-44.png";
            File file = new File(path);
            photoPanel.openFile(file);
            System.out.println("Opened file " + file.getAbsolutePath());*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setEnabledForAllFilters(false);
        setEnabledForAllView(false);
        setEnabledForAllRendering(false);
        setEnabledForSaveAs(false);
        selectView("View/Real size");
        menuRenderingMap.get("Rendering/Bilinear interpolation").setSelected(true);
        extensions = new String[4];
        extensions[0] = "png";
        extensions[1] = "jpeg";
        extensions[2] = "bmp";
        extensions[3] = "gif";
    }

    //File/Open - opens any image file
    public void openFile() {
        File file = getOpenFileName(extensions);
        if (file == null) return;
        photoPanel.openFile(file);
        selectFilter("Filters/Set original");
        setEnabledForAllFilters(true);
        setEnabledForAllView(true);
        setEnabledForAllRendering(true);
        setEnabledForSaveAs(true);
        System.out.println("Opened file " + file.getAbsolutePath());
    }

    //File/Save - saves image file
    public void saveFile() {
        File file = getSaveFileName(extensions);
        if (file == null) return;
        photoPanel.saveFile(file);
        System.out.println("Saving file to " + file.getAbsolutePath());
    }

    public void setEnabledForAllFilters(boolean b) {
        for (JToggleButton item : menuToolbarFiltersMap.values()) {
            item.setEnabled(b);
        }
        MenuElement element = getMenuElement("Filters");
        if (element instanceof JMenu)
            ((JMenu) element).setEnabled(b);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).getInvoker().setEnabled(b);
    }

    public void setEnabledForAllView(boolean b) {
        for (JToggleButton item : menuToolbarViewMap.values()) {
            item.setEnabled(b);
        }
        MenuElement element = getMenuElement("View");
        if (element instanceof JMenu)
            ((JMenu) element).setEnabled(b);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).getInvoker().setEnabled(b);
    }

    public void setEnabledForAllRendering(boolean b) {
        MenuElement element = getMenuElement("Rendering");
        if (element instanceof JMenu)
            ((JMenu) element).setEnabled(b);
        else if (element instanceof JPopupMenu)
            ((JPopupMenu) element).getInvoker().setEnabled(b);
    }

    public void setEnabledForSaveAs(boolean b) {
        AbstractButton button = getMenuToolbarElement("File/Save as");
        if (button instanceof JButton)
            button.setEnabled(b);
        MenuElement element = getMenuElement("File/Save as");
        if (element instanceof JMenuItem)
            ((JMenuItem) element).setEnabled(b);
    }

    //View/Real size
    public void realSize() {
        photoPanel.realSize();
        selectView("View/Real size");
    }

    //View/Fit to screen
    public void fitToScreen() {
        photoPanel.fitToScreen();
        selectView("View/Fit to screen");
    }

    public void linearInterpolation() {
        photoPanel.setLinearInterpolation();
    }

    public void cubicInterpolation() {
        photoPanel.setCubicInterpolation();
    }

    public void nearestInterpolation() {
        photoPanel.setNearestInterpolation();
    }

    //Filters/Set original - set original image
    public void setOriginalImage() {
        photoPanel.setOriginalImage();
        selectFilter("Filters/Set original");
    }

    //Filters/Rotation - change rotation
    public void changeRotation() {
        selectFilter("Filters/Rotation");
    }

    //Filter/Shades of grey
    public void shadesOfGrey() {
        selectFilter("Filters/Shades of grey");
    }

    //Filter/Invert
    public void invertColors() {
        selectFilter("Filters/Invert");
    }

    //Filters/Smoothing
    public void smoothing() {
        selectFilter("Filters/Smoothing");
    }

    //Filter/Sharpness
    public void sharpness() {
        selectFilter("Filters/Sharpness");
    }

    //Filter/Embossing
    public void embossing() {
        selectFilter("Filters/Embossing");
    }

    //Filter/Gamma
    public void gamma() {
        selectFilter("Filters/Gamma");
    }

    //Filter/Median
    public void median() {
        selectFilter("Filters/Median");
    }

    //Filter/Boarder
    public void boarder() {
        selectFilter("Filters/Boarder");
    }

    //Filter/Dithering
    public void dithering() {
        selectFilter("Filters/Dithering");
    }

    //Filter/Ordered Dithering
    public void orderedDithering() {
        selectFilter("Filters/Ordered Dithering");
    }

    //Filters/Water-colourisation
    public void watercolourisation() {
        selectFilter("Filters/Water-colourisation");
    }

    //Filter/Gray World
    public void grayWorld() {
        selectFilter("Filters/Gray World");
    }


    //File/Exit - exits application
    public void exit() {
        System.exit(0);
    }

    public void selectFilter(String title) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (photoPanel.applyFilter(title.substring(title.lastIndexOf("/") + 1))) {
            JRadioButtonMenuItem item = menuFiltersMap.get(title);
            item.setSelected(true);
            JToggleButton button = menuToolbarFiltersMap.get(title);
            button.setSelected(true);
        } else {
            setOriginalImage();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void selectView(String title) {
        JRadioButtonMenuItem item = menuViewMap.get(title);
        item.setSelected(true);
        JToggleButton button = menuToolbarViewMap.get(title);
        button.setSelected(true);
    }


    //Help/About... - shows program version and copyright information
    public void showAbout() {
        JOptionPane.showMessageDialog(this, "Image Corrector App. ver. 1.0\nCopyright 2022 Cherpakov Kirill, FIT, group 19201\nProgram for corrections any images.", "About Image Corrector App", JOptionPane.INFORMATION_MESSAGE);
    }

    //Help/Usage - shows program usage information
    public void showUsage() {
        JOptionPane.showMessageDialog(this, "Open image-file to start correcting your image.\nPush on the Open File button or find the same menu item in the menu \"File\"", "Usage", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        PhotoCorrectorWindow mainFrame = new PhotoCorrectorWindow();
        mainFrame.setVisible(true);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        //photoPanel.componentResized();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
