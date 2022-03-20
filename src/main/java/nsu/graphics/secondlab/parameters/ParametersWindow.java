package nsu.graphics.secondlab.parameters;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import static nsu.graphics.secondlab.MathUtils.isNumeric;

public class ParametersWindow extends JFrame {
    protected JPanel panel;

    ParametersWindow(int numberOfParameters) {
        super("Parameters");
        setSize(300, 150);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);
        panel = new JPanel(new GridLayout(numberOfParameters, 3));
    }

    protected boolean checkValue(String val, double min, double max) {
        if (isNumeric(val)) {
            double value = Double.parseDouble(val);
            if (value >= min && value <= max) {
                return true;
            }
        }
        showMessage(val);
        return false;
    }

    protected boolean checkValue(String val, Set<Integer> items) {
        if (isNumeric(val)) {
            Integer value = Integer.parseInt(val);
            if (items.contains(value)) {
                return true;
            }
        }
        showMessage(val);
        return false;
    }

    protected void showMessage(String str) {
        JOptionPane.showMessageDialog(this, str + " is bad value", "Parameter error", JOptionPane.INFORMATION_MESSAGE);
    }

    class TextBoxListener implements ActionListener {
        JTextField textBox;
        int multiplier;
        JSlider slider;
        Set<Integer> items;
        int min;
        int max;

        public TextBoxListener(JTextField textBox, JSlider slider, int min, int max) {
            this.slider = slider;
            this.textBox = textBox;
            this.min = min;
            this.max = max;
            this.multiplier = 1;
        }

        public TextBoxListener(JTextField textBox, JSlider slider, Set<Integer> items) {
            this.slider = slider;
            this.textBox = textBox;
            this.items = items;
            this.multiplier = 1;
        }

        public TextBoxListener(JTextField textBox, JSlider slider, int min, int max, int multiplier) {
            this.slider = slider;
            this.textBox = textBox;
            this.min = min;
            this.max = max;
            this.multiplier = multiplier;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textBox.getText();
            System.out.println("New value: " + text);
            if (items == null) {
                if (checkValue(text, min, max)) {
                    double value = Double.parseDouble(text);
                    slider.setValue((int)value*multiplier);
                }
            } else {
                if (checkValue(text, items)) {
                    int value = Integer.parseInt(text);
                    slider.setValue(value*multiplier);
                }
            }
        }
    }

    class SliderListener implements ChangeListener {
        JTextField textBox;
        JSlider slider;
        Set<Integer> items;
        int multiplier;

        public SliderListener(JTextField textBox, JSlider slider) {
            this.slider = slider;
            this.textBox = textBox;
            this.multiplier = 1;
        }

        public SliderListener(JTextField textBox, JSlider slider, int multiplier) {
            this.slider = slider;
            this.textBox = textBox;
            this.multiplier = multiplier;
        }

        public SliderListener(JTextField textBox, JSlider slider, Set<Integer> items) {
            this.slider = slider;
            this.textBox = textBox;
            this.items = items;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            double value = ((JSlider) e.getSource()).getValue() / (double)multiplier;
            if (items == null) {
                textBox.setText(String.valueOf(value));
            } else {
                if (items.contains((int)value)) {
                    textBox.setText(String.valueOf(value));
                } else {
                    value++;
                    textBox.setText(String.valueOf(value));
                    slider.setValue((int)value);
                }
            }

        }
    }
}
