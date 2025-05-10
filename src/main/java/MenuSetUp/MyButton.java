package MenuSetUp;

import javax.swing.JButton;

import main.Main;

import java.awt.Dimension;

import javax.swing.ImageIcon;


public class MyButton extends JButton {
    int width, height;
    public ImageIcon icon;

    public MyButton(String name) {
        icon = new ImageIcon(Main.res + nameFileIcon(name));

        width = icon.getIconWidth();
        height = icon.getIconHeight();

        this.setContentAreaFilled(false);//
        this.setBorder(null);//delete button border
        this.setPreferredSize(new Dimension(width, height));
    }

    public String nameFileIcon(String name) {
        return "/button/" + name + "Button.png";
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setLocateButton(int x, int y) {
        setBounds(x, y, width, height);
    }
}
