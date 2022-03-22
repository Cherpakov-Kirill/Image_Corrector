package nsu.graphics.secondlab.parameters;

import javax.swing.*;

public class DitheringParameters extends ParametersWindow {
    private final JTextField redRange;
    private final JTextField greenRange;
    private final JTextField blueRange;

    public DitheringParameters() {
        super(3);

        redRange = new JTextField("2", 3);
        JLabel label1 = new JLabel("Red range = ");
        JSlider redSlider = new JSlider(2, 128, 2);

        redRange.addActionListener(new TextBoxListener(redRange, redSlider, 2, 128));
        redSlider.addChangeListener(new SliderListener(redRange, redSlider));
        panel.add(label1);
        panel.add(redRange);
        panel.add(redSlider);

        greenRange = new JTextField("2", 3);
        JLabel label2 = new JLabel("Green range = ");
        JSlider greenSlider = new JSlider(2, 128, 2);

        greenRange.addActionListener(new TextBoxListener(greenRange, greenSlider, 2, 128));
        greenSlider.addChangeListener(new SliderListener(greenRange, greenSlider));
        panel.add(label2);
        panel.add(greenRange);
        panel.add(greenSlider);

        blueRange = new JTextField("2", 3);
        JLabel label3 = new JLabel("Blue range = ");
        JSlider blueSlider = new JSlider(2, 128, 2);

        blueRange.addActionListener(new TextBoxListener(blueRange, blueSlider, 2, 128));
        blueSlider.addChangeListener(new SliderListener(blueRange, blueSlider));
        panel.add(label3);
        panel.add(blueRange);
        panel.add(blueSlider);
    }

    public boolean showDialog() {
        int res = JOptionPane.showConfirmDialog(this, panel, "Dithering filter settings", JOptionPane.OK_CANCEL_OPTION);
        return res == 0 && checkValue(redRange.getText(), 2, 128) && checkValue(greenRange.getText(), 2, 128) && checkValue(blueRange.getText(), 2, 128);
    }

    public int getRedRange() {
        return (int) Double.parseDouble(redRange.getText());
    }

    public int getGreenRange() {
        return (int) Double.parseDouble(greenRange.getText());
    }

    public int getBlueRange() {
        return (int) Double.parseDouble(blueRange.getText());
    }
}
