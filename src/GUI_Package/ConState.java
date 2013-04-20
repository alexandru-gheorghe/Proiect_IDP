package GUI_Package;


import Mediator.Mediator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class ConState extends State {
    static final String acceptOffer = "Accept Offer";
    static final String refuseOffer = "Refuse Offer";
    public ConState(String name, Mediator med, ServListModel servList ) {
        this.med = med;
        this.servList = servList;
        this.servName = servList.servName.getText();
        this.name = new JLabel(name);
        this.state = ServListModel.noOffer;
        this.price = "";
        
    }
  
    @Override
        public void showPopup(int x, int y, MouseEvent lie) {
        JPopupMenu jp = new JPopupMenu("Menu");
        if(servList.isAcceptOfferAvailable()) {
            JMenuItem jm = new JMenuItem(acceptOffer);
            addAcceptOfferActionListener(jm);
            jp.add(jm);
        }
        if(servList.isRefuseOfferAvailable()) {
            JMenuItem jm1 = new JMenuItem(refuseOffer);
            refuseOfferActionListener(jm1);
            jp.add(jm1);
        }

        System.out.println("" + x + " " + y);
        jp.show((JComponent)lie.getSource(), lie.getX(), lie.getY());
    }
    
    void addAcceptOfferActionListener(JMenuItem jm) {
        jm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                if(med.sendAcceptOffer(name.getText(), servName)) {
                    changeState(ServListModel.offerAccept);
                }                    
            }
        });
    }
    
    void refuseOfferActionListener(JMenuItem jm) {
        jm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(med.sendRefuseOffer(name.getText(), servName)) 
                    changeState(ServListModel.offerRefused);
            }
        });
    }
}
