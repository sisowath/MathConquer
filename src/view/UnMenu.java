package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class UnMenu extends JMenu {
        // attribut(s)
    ActionListener menuListener;
    MenuItemListener mil;   
        // methode(s)
    // constructeur(s)
    public UnMenu() { super(); }
    public UnMenu(String arg0) { super(arg0); }
    public UnMenu(String arg0, boolean arg1) { super(arg0, arg1); }
    public UnMenu(Action arg0) {super(arg0); }    
    // accesseur(s)

    // mutateur(s)
    public void setMenuListener(ActionListener al) { this.menuListener = al; }
    public void setEnabled(String nomItem, boolean b) {
        Component[] c = this.getMenuComponents();
        for(int i=0; i < c.length; i++) {
            if(c[i] instanceof JMenuItem) {
                JMenuItem jmi = (JMenuItem)c[i];
                if(jmi.getText().equals(nomItem)) {
                    jmi.setEnabled(b);
                }                    
            }
        }
    }
    // autre(s)
    class MenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(menuListener != null) {
                menuListener.actionPerformed(e);
            }
        }        
    }
}