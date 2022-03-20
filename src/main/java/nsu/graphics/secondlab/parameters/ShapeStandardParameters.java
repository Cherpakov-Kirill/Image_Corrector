package nsu.graphics.secondlab.parameters;

import javax.swing.*;

public class ShapeStandardParameters extends ParametersWindow {
    public ShapeStandardParameters(PhotoForParameters draw) {
        super(2);
        JTextField radius = new JTextField("50", 4);
        JSlider radiusSlider = new JSlider(1,2000,50);
        radius.addActionListener(new TextBoxListener(radius,radiusSlider, 1, 2000));
        radiusSlider.addChangeListener(new SliderListener(radius,radiusSlider));
        JTextField rotation = new JTextField("0", 3);
        JSlider rotationSlider = new JSlider(0,2000,50);
        rotation.addActionListener(new TextBoxListener(rotation,rotationSlider, 0, 2000));
        rotationSlider.addChangeListener(new SliderListener(rotation,rotationSlider));
        JLabel label1 = new JLabel("Radius = ");
        JLabel label2 = new JLabel("Rotation = ");

        panel.add(label1);
        panel.add(radius);
        panel.add(radiusSlider);
        panel.add(label2);
        panel.add(rotation);
        panel.add(rotationSlider);
        int res = JOptionPane.showConfirmDialog(this, panel, "Shape Parameters", JOptionPane.OK_CANCEL_OPTION);
        if (res == 0 && checkValue(radius.getText(), 1, 2000) && checkValue(rotation.getText(), 0, 2000)) {
            draw.setParameters(Integer.parseInt(radius.getText()), Integer.parseInt(rotation.getText()));
        }
    }
}
