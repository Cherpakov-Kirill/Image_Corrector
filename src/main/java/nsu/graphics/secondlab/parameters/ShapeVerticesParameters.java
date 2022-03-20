package nsu.graphics.secondlab.parameters;

import javax.swing.*;

public class ShapeVerticesParameters extends ParametersWindow {
    public ShapeVerticesParameters(PhotoForParameters draw) {
        super(3);
        JTextField radius = new JTextField("50", 4);
        JSlider radiusSlider = new JSlider(1,2000,50);
        radius.addActionListener(new TextBoxListener(radius,radiusSlider, 1, 2000));
        radiusSlider.addChangeListener(new SliderListener(radius,radiusSlider));
        JTextField rotation = new JTextField("0", 3);
        JSlider rotationSlider = new JSlider(0,2000,50);
        rotation.addActionListener(new TextBoxListener(rotation,rotationSlider, 0, 2000));
        rotationSlider.addChangeListener(new SliderListener(rotation,rotationSlider));
        JTextField vertices = new JTextField("5", 3);
        JSlider verticesSlider = new JSlider(3,20,5);
        vertices.addActionListener(new TextBoxListener(vertices,verticesSlider, 0, 20));
        verticesSlider.addChangeListener(new SliderListener(vertices,verticesSlider));
        JLabel label1 = new JLabel("Radius = ");
        JLabel label2 = new JLabel("Rotation = ");
        JLabel label3 = new JLabel("Vertices num. = ");

        panel.add(label1);
        panel.add(radius);
        panel.add(radiusSlider);
        panel.add(label2);
        panel.add(rotation);
        panel.add(rotationSlider);
        panel.add(label3);
        panel.add(vertices);
        panel.add(verticesSlider);

        int res = JOptionPane.showConfirmDialog(this, panel, "Shape Parameters", JOptionPane.OK_CANCEL_OPTION);
        if (res == 0 && checkValue(radius.getText(), 1, 2000) && checkValue(rotation.getText(), 0, 2000) && checkValue(vertices.getText(), 3, 20)) {
            draw.setParameters(Integer.parseInt(radius.getText()), Integer.parseInt(rotation.getText()), Integer.parseInt(vertices.getText()));
        }
    }
}
