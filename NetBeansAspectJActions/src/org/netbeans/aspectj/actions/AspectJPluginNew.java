/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;


@ActionID(
        category = "File",
        id = "org.netbeans.aspectj.actions.AspectJPluginNew"
)
@ActionRegistration( iconBase="org/netbeans/aspectj/actions/icons/aspect.gif",
        displayName = "#CTL_AspectJPluginNew"
)
@ActionReference(path = "Menu/Tools/AspectJ Spring 2017", position = 3233)
@Messages("CTL_AspectJPluginNew=New AJ File")


public class AspectJPluginNew implements ActionListener {

    
    @Override
    public void actionPerformed(ActionEvent e) {     

        AspectJEditorTopComponent window = new AspectJEditorTopComponent();
        window.open();
        JOptionPane.showMessageDialog(null, "This creates a new AspectJ file and adds to the editor screen");
       // AspectJPluginNBManager.init();
        

    }
}
