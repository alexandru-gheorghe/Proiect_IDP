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
public class ProdState extends State {
    static final String makeOffer = "Make Offer";
    static final String dropAction = "Drop Action";
    
            
            
    public ProdState(String name, Mediator med, ServListModel servList) {
        this.servList = servList;
        this.med = med;
        this.servName = servList.servName.getText();
        this.name = new JLabel(name);
        this.state = ServListModel.noOffer;
        this.price = "";
        
    }
    @Override
    public void showPopup(int x, int y, MouseEvent lie) {
        JPopupMenu jp = new JPopupMenu("Menu");
        if(servList.isMakeOfferAvailable()) {
            JMenuItem jm = new JMenuItem(makeOffer);
            addMakeOfferActionListener(jm);
            jp.add(jm);
        }
        if(servList.isDropActionAvailable()) {
            JMenuItem jm1 = new JMenuItem(dropAction);
            addDropActionListener(jm1);
            jp.add(jm1);
        }
        System.out.println("" + x + " " + y);
        jp.show((JComponent)lie.getSource(), lie.getX(), lie.getY());
    }
    
    public void addMakeOfferActionListener(JMenuItem jm) {
        jm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                price = JOptionPane.showInputDialog("Make an offer (RON):");
                if (price == null)
                    price = "";
                
                if(med.sendMakeOffer(name.getText(), servName, price))
                    changeState(ServListModel.offerMade);
            }
        });
    }
    public void addDropActionListener(JMenuItem jm) {
        jm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(med.sendDropOffer(name.getText(), servName))
                        ;
                    }
        });
    }
}
