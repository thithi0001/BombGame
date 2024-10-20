package MenuSetUp;

import java.awt.Font;

import javax.swing.*;

import javax.swing.JSlider;
import javax.swing.SwingConstants;


public class MySlider {
    JLabel nameSlider;
    JSlider slider;

    public MySlider(int width, int height, int value, String name) {
        slider = new JSlider(-24, 6, value);
        slider.setUI(new SliderUI(slider));
        slider.setOpaque(false);
        slider.setSize(width - 55, height);


        //SET JLABEL
        nameSlider = new JLabel(name);
        nameSlider.setSize(60, 35);
        nameSlider.setFont(new Font("Courier New", Font.ITALIC, 16));
        nameSlider.setHorizontalAlignment(SwingConstants.CENTER);
        nameSlider.setVerticalAlignment(SwingConstants.CENTER);
        nameSlider.setOpaque(false);
    }

    public void setLocateMySlider(int x, int y) {
        slider.setLocation(x + 60, y);
        nameSlider.setLocation(x, y - 5);
    }
    public void addMySlider(JPanel panel){
        panel.add(slider);
        panel.add(nameSlider);
    }
}
