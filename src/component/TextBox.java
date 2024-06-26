package component;

import java.awt.Color;
import java.awt.Insets;
import static java.awt.image.ImageObserver.WIDTH;
import javax.swing.JTextField;
import static javax.swing.SwingConstants.LEFT;

/**
 *
 * @author usu
 */
public class TextBox extends JTextField {

    public TextBox() {
        super();
        setFont(new java.awt.Font("Inter", 0, 12));
        setSelectionColor(new Color(230, 230, 172));
        setSelectedTextColor(new Color(255, 0, 0));
        setForeground(new Color(0, 131, 62));
        setBackground(new Color(255, 255, 255));
        setHorizontalAlignment(LEFT);
        setSize(WIDTH, 35);

//        setRoundRect(false);
    }

}
