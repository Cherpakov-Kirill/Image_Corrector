package nsu.graphics.secondlab.parameters;

import javax.swing.*;

public class BoarderParameters extends ParametersWindow {
    public static final int SOBEL_OPERATOR = 1;
    public static final int ROBERTS_OPERATOR = 2;
    private final JTextField redLimit;
    private final JTextField greenLimit;
    private final JTextField blueLimit;
    private final JRadioButtonMenuItem buttonSobel;
    private final JRadioButtonMenuItem buttonRoberts;

    public BoarderParameters() {
        super(5);

        ButtonGroup operatorsButtons = new ButtonGroup();
        buttonSobel = new JRadioButtonMenuItem("Sobel");
        buttonRoberts = new JRadioButtonMenuItem("Roberts");
        operatorsButtons.add(buttonSobel);
        operatorsButtons.add(buttonRoberts);
        buttonSobel.setSelected(true);

        JLabel labelOperators = new JLabel("Choose filter operator");

        panel.add(labelOperators);
        panel.add(buttonSobel);
        panel.add(buttonRoberts);


        redLimit = new JTextField("128", 3);
        JLabel label1 = new JLabel("Red limit = ");
        JSlider redSlider = new JSlider(0, 255, 128);

        redLimit.addActionListener(new TextBoxListener(redLimit, redSlider, 0, 255));
        redSlider.addChangeListener(new SliderListener(redLimit, redSlider));
        panel.add(label1);
        panel.add(redLimit);
        panel.add(redSlider);

        greenLimit = new JTextField("128", 3);
        JLabel label2 = new JLabel("Green limit = ");
        JSlider greenSlider = new JSlider(0, 255, 128);

        greenLimit.addActionListener(new TextBoxListener(greenLimit, greenSlider, 0, 255));
        greenSlider.addChangeListener(new SliderListener(greenLimit, greenSlider));
        panel.add(label2);
        panel.add(greenLimit);
        panel.add(greenSlider);

        blueLimit = new JTextField("128", 3);
        JLabel label3 = new JLabel("Blue limit = ");
        JSlider blueSlider = new JSlider(0, 255, 128);

        blueLimit.addActionListener(new TextBoxListener(blueLimit, blueSlider, 0, 255));
        blueSlider.addChangeListener(new SliderListener(blueLimit, blueSlider));
        panel.add(label3);
        panel.add(blueLimit);
        panel.add(blueSlider);
    }

    public boolean showDialog() {
        int res = JOptionPane.showConfirmDialog(this, panel, "Boarder filter settings", JOptionPane.OK_CANCEL_OPTION);
        return res == 0 && checkValue(redLimit.getText(), 0, 255) && checkValue(greenLimit.getText(), 0, 255) && checkValue(blueLimit.getText(), 0, 255);
    }

    public int getRedLimit() {
        return (int) Double.parseDouble(redLimit.getText());
    }

    public int getGreenLimit() {
        return (int) Double.parseDouble(greenLimit.getText());
    }

    public int getBlueLimit() {
        return (int) Double.parseDouble(blueLimit.getText());
    }

    public int getOperatorType(){
        if(buttonSobel.isSelected()) return SOBEL_OPERATOR;
        if(buttonRoberts.isSelected()) return ROBERTS_OPERATOR;
        return 0;
    }
}
