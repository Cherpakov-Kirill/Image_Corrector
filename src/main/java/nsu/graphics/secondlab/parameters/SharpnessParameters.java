package nsu.graphics.secondlab.parameters;

import javax.swing.*;

public class SharpnessParameters extends ParametersWindow {
    private final ButtonGroup filterSizesButtons;
    private final JRadioButtonMenuItem button3;
    private final JRadioButtonMenuItem button5;

    public SharpnessParameters() {
        super(3);
        filterSizesButtons = new ButtonGroup();
        button3 = new JRadioButtonMenuItem("3");
        button5 = new JRadioButtonMenuItem("5");
        filterSizesButtons.add(button3);
        filterSizesButtons.add(button5);
        button3.setSelected(true);

        JLabel label1 = new JLabel("Choose filter size");

        panel.add(label1);
        panel.add(button3);
        panel.add(button5);
    }

    public boolean showDialog(){
        int res = JOptionPane.showConfirmDialog(this, panel, "Sharpness filter settings", JOptionPane.OK_CANCEL_OPTION);
        return res == 0;
    }

    public int getFilterSize(){
        if(button3.isSelected()) return 3;
        if(button5.isSelected()) return 5;
        return 0;
    }


}
