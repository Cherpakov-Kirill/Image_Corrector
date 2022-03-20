package nsu.graphics.secondlab.parameters;

import javax.swing.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmoothingParameters extends ParametersWindow {
    private final Set<Integer> filterSizeItems;
    private final JTextField filterSize;
    private final JTextField sigma;

    public SmoothingParameters() {
        super(2);
        filterSizeItems = new HashSet<>(List.of(3, 5, 7, 9, 11));

        filterSize = new JTextField("5", 2);
        JLabel label1 = new JLabel("Filter size = ");
        JSlider filterSizeSlider = new JSlider(3, 11, 5);

        filterSize.addActionListener(new TextBoxListener(filterSize, filterSizeSlider, filterSizeItems));
        filterSizeSlider.addChangeListener(new SliderListener(filterSize, filterSizeSlider, filterSizeItems));
        panel.add(label1);
        panel.add(filterSize);
        panel.add(filterSizeSlider);

        sigma = new JTextField("1", 2);
        JLabel label2 = new JLabel("Sigma = ");
        JSlider sigmaSlider = new JSlider(1, 10, 1);

        sigma.addActionListener(new TextBoxListener(sigma, sigmaSlider, 1, 10));
        sigmaSlider.addChangeListener(new SliderListener(sigma, sigmaSlider));
        panel.add(label2);
        panel.add(sigma);
        panel.add(sigmaSlider);
    }

    public boolean showDialog(){
        int res = JOptionPane.showConfirmDialog(this, panel, "Smoothing filter settings", JOptionPane.OK_CANCEL_OPTION);
        return res == 0 && checkValue(filterSize.getText(), filterSizeItems) && checkValue(sigma.getText(), 1, 10);
    }

    public int getFilterSize(){
        return Integer.parseInt(filterSize.getText());
    }

    public double getSigma(){
        return Double.parseDouble(sigma.getText());
    }


}
