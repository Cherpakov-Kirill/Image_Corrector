package nsu.graphics.secondlab.parameters;

import javax.swing.*;

public class RotationParameters extends ParametersWindow {
    private final JTextField degree;

    public RotationParameters() {
        super(1);

        degree = new JTextField("0", 3);
        JLabel label1 = new JLabel("Degree = ");
        JSlider gammaSlider = new JSlider(-180, 180, 0);

        degree.addActionListener(new TextBoxListener(degree, gammaSlider, -180, 180));
        gammaSlider.addChangeListener(new SliderListener(degree, gammaSlider));
        panel.add(label1);
        panel.add(degree);
        panel.add(gammaSlider);
    }

    public boolean showDialog() {
        int res = JOptionPane.showConfirmDialog(this, panel, "Rotation settings", JOptionPane.OK_CANCEL_OPTION);
        return res == 0 && checkValue(degree.getText(), -180, 180);
    }

    public double getDegree() {
        return Double.parseDouble(degree.getText());
    }

}
