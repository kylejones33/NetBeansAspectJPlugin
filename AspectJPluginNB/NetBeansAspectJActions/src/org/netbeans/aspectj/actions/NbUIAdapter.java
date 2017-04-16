
package org.netbeans.aspectj.actions;
import org.aspectj.ajde.IdeUIAdapter;
import org.openide.awt.StatusDisplayer;

public class NbUIAdapter implements IdeUIAdapter
{
    @Override
    public void displayStatusInformation(String string) {
	StatusDisplayer.getDefault().setStatusText(string);
    }
    
    public void resetGUI() {

    }
}
