package org.netbeans.aspectj.actions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;

/**
 *
 * @author sean
 */
@ActionID(category = "Edit", id = "org.netbeans.aspectj.actions.AspectJPluginToolbarAdd")
@ActionRegistration(iconBase="org/netbeans/aspectj/actions/icons/aspect.gif", displayName = "AspectJ Spring 2017")
@ActionReference(path = "Toolbars/File")
        
public class AspectJPluginToolbarAdd implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent ae) {
        JOptionPane.showMessageDialog(null, "This will parse and validate Aspects");
        //To change body of generated methods, choose Tools | Templates.
    }
    
}
