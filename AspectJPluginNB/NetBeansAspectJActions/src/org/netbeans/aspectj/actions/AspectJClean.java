/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "org.netbeans.aspectj.actions.AspectJClean"
)
@ActionRegistration(
        iconBase = "org/netbeans/aspectj/actions/icons/method.gif",
        displayName = "#CTL_AspectJClean"
)
@ActionReference(path = "Menu/Tools/AspectJ Spring 2017", position = 2933)
@Messages("CTL_AspectJClean=AOP Clean")
public final class AspectJClean implements ActionListener {
static Class class$org$netbeans$aspectj$actions$AspectJClean;

    
    public String getName() {
	return (NbBundle.getMessage
		((class$org$netbeans$aspectj$actions$AspectJClean == null
		  ? (class$org$netbeans$aspectj$actions$AspectJClean
		     = class$("org.aspectj.tools.ajde.netbeans.AJCleanAction"))
		  : class$org$netbeans$aspectj$actions$AspectJClean),
		 "LBL_CleanAction"));
    }

    
    public HelpCtx getHelpCtx() {
	return HelpCtx.DEFAULT_HELP;
    }


     protected boolean asynchronous() {
       return false;
    }
    /*synthetic*/ 
     static Class class$(String string) {
	Class var_class;
	try {
	    var_class = Class.forName(string);
	} catch (ClassNotFoundException classnotfoundexception) {
	    throw new NoClassDefFoundError(classnotfoundexception
					       .getMessage());
	}
	return var_class;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        AspectJPluginNBManager.INSTANCE.clean();
    }
}
