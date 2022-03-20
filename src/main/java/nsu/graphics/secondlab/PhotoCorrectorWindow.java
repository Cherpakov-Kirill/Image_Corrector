package nsu.graphics.secondlab;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import ru.nsu.cg.MainFrame;


public class PhotoCorrectorWindow extends MainFrame implements ComponentListener {
    private final ImagePanel photoPanel;
    private final JScrollPane scrollPane;
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

            //addSubMenu("Tools", KeyEvent.VK_F);


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

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "/About.png", "showAbout");

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

            scrollPane = new JScrollPane();
            photoPanel = new ImagePanel(scrollPane, 685, 395);
            scrollPane.setViewportView(photoPanel);

            add(scrollPane);
            addComponentListener(this);
            setBackground(Color.WHITE);
            //scrollPane.revalidate();
            //photoPanel.setMaxVisibleRectSize();
            /*String path = "/home/kirill/Desktop/Screenshot from 2022-03-15 00-23-44.png";
            File file = new File(path);
            try {
                photoPanel.setImage(ImageIO.read(file));
                System.out.println("Opened file " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        selectView("View/Real size");
        extensions = new String[4];
        extensions[0] = "png";
        extensions[1] = "jpeg";
        extensions[2] = "bmp";
        extensions[3] = "gif";
    }

    //File/Open - opens any image file
    public void openFile() {
        try {
            File file = getOpenFileName(extensions);
            photoPanel.setImage(ImageIO.read(file));
            selectFilter("Filters/Set original");
            System.out.println("Opened file " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //File/Save - saves image file
    public void saveFile() {
        File file = getSaveFileName(extensions);
        photoPanel.saveFile(file);
        System.out.println("Saving file to " + file.getAbsolutePath());
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
        if (photoPanel.applyFilter(title.substring(title.lastIndexOf("/") + 1))) {
            JRadioButtonMenuItem item = menuFiltersMap.get(title);
            item.setSelected(true);
            JToggleButton button = menuToolbarFiltersMap.get(title);
            button.setSelected(true);
        } else {
            setOriginalImage();
        }
    }

    public void selectView(String title) {
        JRadioButtonMenuItem item = menuViewMap.get(title);
        item.setSelected(true);
        JToggleButton button = menuToolbarViewMap.get(title);
        button.setSelected(true);
        selectFilter("Filters/Set original");
    }


    //Help/About... - shows program version and copyright information
    public void showAbout() {
        JOptionPane.showMessageDialog(this, "Image Corrector App. ver. 1.0\nCopyright 2022 Cherpakov Kirill, FIT, group 19201\nProgram for corrections any images.", "About Image Corrector App", JOptionPane.INFORMATION_MESSAGE);
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
