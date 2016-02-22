package view;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

public class UnMenuItem extends JMenuItem {
        // attribut(s)
    
        // methode(s)
    // constructeur(s)
    public UnMenuItem(String s, char touche, ActionListener al, boolean b) {
        super(s, (int)touche);
        this.setActionCommand(s);
        this.setEnabled(b);
        this.addActionListener(al);
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)

}