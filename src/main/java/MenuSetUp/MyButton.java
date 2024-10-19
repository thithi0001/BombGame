package MenuSetUp;

import javax.swing.JButton;
import main.Main;
import javax.swing.ImageIcon;
import javax.swing.Icon;

public class MyButton extends JButton {
    int width,height;
    public MyButton(String name){
        Icon icon = new ImageIcon(Main.res+nameFileIcon(name));

        width = icon.getIconWidth();
        height = icon.getIconHeight();

        this.setIcon(icon);//them icon cho button
        this.setContentAreaFilled(false);//lam background cua button trong suot
        this.setBorder(null);//xoa border
    }
    public String nameFileIcon(String name){
        return "/button/" + name +"Button.png";
    }
    public void setLocateButton(int x,int y){
        setBounds(x, y, width, height);
    }
}
