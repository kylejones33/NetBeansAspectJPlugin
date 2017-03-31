
package org.netbeans.aspectj.actions;

import org.aspectj.ajde.Ajde;
import org.aspectj.ajde.core.IBuildProgressMonitor;

import static org.aspectj.ajde.ui.swing.DefaultBuildProgressMonitor.PROGRESS_HEADING;
import org.netbeans.aspectj.compileonsave.CompileOnSave;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;

/**
 *
 * @author Ramos
 */
public class ProgressMonitor implements IBuildProgressMonitor{

    private ProgressHandle handle;
    private Cancellable canceller = new MyCancellable();

    public Cancellable getCanceller() {
        return canceller;
    }
    private int max = 0;
    public ProgressMonitor(){
        
    }
    
    public void start(String configFilePath) {
  //      System.out.println("start: "+configFilePath);
        handle = ProgressHandleFactory.createHandle(PROGRESS_HEADING,canceller);
        handle.start(max);
    }

    public void setProgressText(String arg0) {
 //       System.out.println("setProgressText: "+arg0);
        handle.progress(arg0);
    }

    public void setProgressBarVal(int arg0) {
//        System.out.println("setProgressBarVal"+arg0);
        handle.progress(arg0);
    }

    public void incrementProgressBarVal() {
 //       System.out.println("incrementProgressBarVal");
    }

    public void setProgressBarMax(int arg0) {
        max = arg0;
 //       System.out.println("setProgressBarMax: "+arg0);
    }

    public int getProgressBarMax() {
//        System.out.println("getProgressBarMax");
        return max;
    }

    public void finish(boolean arg0) {
//        System.out.println("finish: "+arg0);
        handle.finish();
        CompileOnSave.getDefault().finished();
    }

    @Override
    public void begin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setProgress(double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCancelRequested() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    class MyCancellable implements Cancellable{

        @Override
        public boolean cancel() {
//            System.out.println("cancel   '''''''");
            //Ajde.getDefault().getBuildManager().abortBuild();
         
            finish(false);
            return true;
        }
        
    }

}
