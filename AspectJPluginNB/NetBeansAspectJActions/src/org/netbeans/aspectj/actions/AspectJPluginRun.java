
package org.netbeans.aspectj.actions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public class AspectJPluginRun extends CallableSystemAction
{
    public static boolean PROP_ENABLED = false;
    /*synthetic*/ static Class class$org$netbeans$aspectj$actions$AspectJPluginRun;
    
    public void performAction() {
        //AspectJPluginNBManager.INSTANCE.build();
	AspectJPluginNBManager.INSTANCE.run();
    }
    
    public String getName() {
	return (NbBundle.getMessage
		((class$org$netbeans$aspectj$actions$AspectJPluginRun == null
		  ? (class$org$netbeans$aspectj$actions$AspectJPluginRun
		     = class$("org.aspectj.tools.ajde.netbeans.AJRunAction"))
		  : class$org$netbeans$aspectj$actions$AspectJPluginRun),
		 "LBL_RunAction"));
    }
    
    @Override
    protected String iconResource() {
	return "org/aspectj/tools/ajde/netbeans/resources/netbeans/runProject.png";
    }
    
    public HelpCtx getHelpCtx() {
	return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public boolean isEnabled() {
	return PROP_ENABLED;
    }
    
    @Override
    public void setEnabled(boolean bool) {
	PROP_ENABLED = bool;
	super.setEnabled(bool);
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
}
