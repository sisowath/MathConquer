package view;

import com.sun.glass.events.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuTaches extends UnMenu {
        // attribut(s)
    JMenuItem miConfigurerServeur;
        // methode(s)
    // constructeur(s)
    public MenuTaches() {
        this.setText("Tâches");
        this.setActionCommand("Taches");
        this.setMnemonic((int)'T');
        
        this.miConfigurerServeur = new UnMenuItem("Configurer serveur", 'S', mil, true);
        this.miConfigurerServeur.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK) );
        
        this.add(this.miConfigurerServeur);
    }
    // accesseur(s)

    // mutateur(s)

    // autre(s)

}