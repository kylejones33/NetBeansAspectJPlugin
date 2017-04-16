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
        category = "Source",
        id = "org.netbeans.aspectj.actions.AspectJRun"
)
@ActionRegistration(
        iconBase = "org/netbeans/aspectj/actions/icons/execute.gif",
        displayName = "#CTL_AspectJRun"
)
@ActionReference(path = "Menu/Tools/AspectJ Spring 2017", position = 2833)
@Messages("CTL_AspectJRun=AOP Run")
public final class AspectJRun implements ActionListener {
static Class class$org$netbeans$aspectj$actions$AspectJRun;
    
    public String getName() {
	return (NbBundle.getMessage
		((class$org$netbeans$aspectj$actions$AspectJRun == null
		  ? (class$org$netbeans$aspectj$actions$AspectJRun
		     = class$("org.aspectj.tools.ajde.netbeans.AJRunAction"))
		  : class$org$netbeans$aspectj$actions$AspectJRun),
		 "LBL_RunAction"));
    }
  public HelpCtx getHelpCtx() {
	return HelpCtx.DEFAULT_HELP;
    }

     protected boolean asynchronous() {
       return false;
    }
    /*synthetic*/ static Class class$(String string) {
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
       // AspectJPluginNBManager.INSTANCE.run();
    }
}
