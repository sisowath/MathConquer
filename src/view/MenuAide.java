package view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuAide extends UnMenu {
        // attribut(s)
    JMenuItem miAPropos;
        // methode(s)
    // constructeur(s)
    public MenuAide() {
        this.setText("Aide");
        this.setActionCommand("Aide");
        this.setMnemonic((int)'A');
        
        this.miAPropos = new UnMenuItem("À propos...", 'A', mil, true);
        this.miAPropos.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK) );
        
        this.add(this.miAPropos);
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)

}