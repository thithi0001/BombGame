package MenuSetUp;

import javax.swing.JButton;

import main.Main;

import javax.swing.ImageIcon;


public class MyButton extends JButton {
    int width, height;
    ImageIcon icon;

    public MyButton(String name) {
        icon = new ImageIcon(Main.res + nameFileIcon(name));

        width = icon.getIconWidth();
        height = icon.getIconHeight();

        this.setIcon(icon);//them icon cho button
        this.setContentAreaFilled(false);//lam background cua button trong suot
        this.setBorder(null);//xoa border
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
