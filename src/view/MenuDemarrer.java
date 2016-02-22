package view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuDemarrer extends UnMenu {
        // attribut(s)
    JMenuItem miConnecter;
    JMenuItem miQuitter;
        // methode(s)
    // constructeur(s)
    public MenuDemarrer() {
        this.setText("Démarrer");
        this.setActionCommand("Demarrer");
        this.setMnemonic((int)'D');
        
        this.miConnecter = new UnMenuItem("Se connecter", 'C', mil, true);
        this.miConnecter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK) );
        this.miQuitter = new UnMenuItem("Quitter", 'Q', mil, true);               
        this.miQuitter.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK) );
        
        this.add(miConnecter);
        this.addSeparator();
        this.add(this.miQuitter);
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)

}