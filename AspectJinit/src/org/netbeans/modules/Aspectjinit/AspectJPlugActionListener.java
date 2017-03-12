/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.Aspectjinit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

/**
 *
 * @author sean
 */
@ActionID(category = "Edit", id = "org.netbeans.modules.Aspectjinit.AspectJPlugActionListener")
@ActionRegistration(iconBase = "org/netbeans/modules/Aspectjinit/AspectMenuIcon.png", displayName = "AspectJ Spring 2017")
@ActionReferences({
    @ActionReference(path = "Menu/Tools"),
    @ActionReference(path = "Toolbars/File")
        
})
public class AspectJPlugActionListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent ae) {
        JOptionPane.showMessageDialog(null, "AspectJ Spring");
        //To change body of generated methods, choose Tools | Templates.
    }
    
}
