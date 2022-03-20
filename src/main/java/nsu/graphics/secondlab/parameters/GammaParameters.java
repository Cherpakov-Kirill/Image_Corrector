package nsu.graphics.secondlab.parameters;

import javax.swing.*;

public class GammaParameters extends ParametersWindow {
    private final JTextField gamma;

    public GammaParameters() {
        super(1);

        gamma = new JTextField("1", 2);
        JLabel label1 = new JLabel("Gamma = ");
        JSlider gammaSlider = new JSlider(1, 100, 10);

        gamma.addActionListener(new TextBoxListener(gamma, gammaSlider, 1, 100, 10));
        gammaSlider.addChangeListener(new SliderListener(gamma, gammaSlider, 10));
        panel.add(label1);
        panel.add(gamma);
        panel.add(gammaSlider);
    }

    public boolean showDialog(){
        int res = JOptionPane.showConfirmDialog(this, panel, "Gamma correction settings", JOptionPane.OK_CANCEL_OPTION);
        return res == 0 && checkValue(gamma.getText(), 0.1, 10);
    }

    public double getGamma(){
        return Double.parseDouble(gamma.getText());
    }

}
