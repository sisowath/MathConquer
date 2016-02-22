package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.Client;

public class MainPanel extends JPanel implements Observer {    
        // attribut(s)
    Client unClient;
    JPanel centerPanel;
    JButton casesQuestions[];
    MainPanelActionListener mpal;
        // methode(s)
    // constructeur(s)
    public MainPanel() {
        super();        
        this.mpal = new MainPanelActionListener();
        this.centerPanel = new JPanel();
        this.centerPanel.setLayout(new GridLayout(8, 8) );        
        for(int i=0; i < 64; i++) {
            JButton btn = new JButton( "Case " + (i++) );
            btn.setActionCommand("Case");
            btn.addActionListener(mpal);
            this.centerPanel.add(btn);
        }
        this.add(this.centerPanel);
        this.setVisible(true);
    }
    // accesseur(s)
    
    // mutateur(s)
    
    // autre(s)
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Client) {
            
        }
    }
    class MainPanelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
        }        
    }
}