
package org.netbeans.aspectj.actions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public class AspectJPluginClean extends CallableSystemAction
{
    /*synthetic*/ static Class class$org$netbeans$aspectj$actions$AspectJPluginDebug;
    
    @Override
    public void performAction() {
	AspectJPluginNBManager.INSTANCE.clean();
    }
    
    public String getName() {
	return (NbBundle.getMessage
		((class$org$netbeans$aspectj$actions$AspectJPluginDebug == null
		  ? (class$org$netbeans$aspectj$actions$AspectJPluginDebug
		     = class$("org.aspectj.tools.ajde.netbeans.AJCleanAction"))
		  : class$org$netbeans$aspectj$actions$AspectJPluginDebug),
		 "LBL_CleanAction"));
    }
    
    @Override
    protected String iconResource() {
	return "org/aspectj/tools/ajde/netbeans/resources/actions/cleanNB.png";
    }
    
    public HelpCtx getHelpCtx() {
	return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected void initialize() {
        super.initialize();
	super.setEnabled(false);
    }

    @Override
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
}
