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
        category = "Build",
        id = "org.netbeans.aspectj.actions.AspectJPluginBuild"
)
@ActionRegistration( iconBase="org/netbeans/aspectj/actions/icons/method.gif",
        displayName = "#CTL_AspectJPluginBuild"
)
@ActionReference(path = "Menu/Tools/AspectJ Spring 2017", position = 3333)
@Messages("CTL_AspectJPluginBuild=Build AJ")
public final class AspectJPluginBuild implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "This will compile and build the Aspect project");
    }
}
