package org.netbeans.aspectj.actions;

//~--- non-JDK imports --------------------------------------------------------
import org.netbeans.aspectj.options.AspectjfornetbeansmodulePanel;

//import org.openide.windows.Workspace;

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.actions.BooleanStateAction;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

//~--- JDK imports ------------------------------------------------------------

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import org.aspectj.tools.ajbrowser.core.BrowserErrorHandler;

import org.aspectj.tools.ajbrowser.core.BrowserErrorHandler;
import org.netbeans.aspectj.compileonsave.CompileOnSave;

public class AspectJPluginBrowser extends BooleanStateAction {

   private boolean installed = false;
   private boolean firstInstall = true;
   private JToggleButton presenter = null;
   private Logger logger = Logger.getLogger(AspectJPluginBrowser.class.getName());

   // public StructureViewTab structureViewTab = null;
   private StructureViewComponent structureViewTab = null;

   public void performAction() {
      logger.log(Level.INFO, "AJStartBrowserAction#performAction");
      try {
         //get compile on save setting ONCE?
         boolean compileOnSave = NbPreferences.forModule(AspectjfornetbeansmodulePanel.class).getBoolean("compileOnSave",
                 false);
         

         //get ref to actions
         CallableSystemAction BUILD = (CallableSystemAction) SystemAction.get(AspectJPluginBuild.class);
         CallableSystemAction SELECT = (CallableSystemAction) SystemAction.get(AspectJPluginSelectConfigurationAction.class);
         CallableSystemAction RUN = (CallableSystemAction) SystemAction.get(AspectJPluginRun.class);
         CallableSystemAction CLEAN = (CallableSystemAction) SystemAction.get(AspectJPluginClean.class);
         CallableSystemAction DEBUG = (CallableSystemAction) SystemAction.get(AspectJPluginDebug.class);

         if (!installed) {
            logger.log(Level.INFO, "AJStartBrowserAction#performAction performing !installed");
            BUILD.setEnabled(true);
            SELECT.setEnabled(true);
            CLEAN.setEnabled(true);

            //mehrmals init
            AspectJPluginNBManager.init();
            //mehrmals erzeugt?
            structureViewTab // = new StructureViewTab(AspectJPluginNBManager.INSTANCE.getViewPanel())
                   = new StructureViewComponent(AspectJPluginNBManager.INSTANCE.getViewPanel());
            showStructureView();

            if (compileOnSave) {
               //enableCompileOnSave();
               CompileOnSave.disableCompileOnSave();
               CompileOnSave.enableCompileOnSave();
            }

            installed = true;
         } else {//installed
            logger.log(Level.INFO, "AJStartBrowserAction#performAction performing installed");
            System.out.println("AJStartBrowserAction#performAction performing installed");
            BUILD.setEnabled(false);
            SELECT.setEnabled(false);
            RUN.setEnabled(false);
            CLEAN.setEnabled(false);
            DEBUG.setEnabled(false);
            toggleStructureView(false);
            CompileOnSave.disableCompileOnSave();
            
            installed = false;
         }

         if (firstInstall) {

            // <editor-fold defaultstate="collapsed" desc="">
            // Repository.getDefault()
            // .addRepositoryListener(new RepositoryListener() {
            // public void fileSystemAdded
            // (RepositoryEvent repositoryevent) {
            // /* empty */
            // }
            //
            // public void fileSystemRemoved
            // (RepositoryEvent repositoryevent) {
            // /* empty */
            // }
            //
            // public void fileSystemPoolReordered
            // (RepositoryReorderedEvent repositoryreorderedevent) {
            // /* empty */
            // }
            // });
            // </editor-fold>
            firstInstall = false;
         }
      } catch (Throwable throwable) {
         BrowserErrorHandler.handleError("Could not start AJDE.", throwable);
         
      }
   }

   @Override
   public boolean getBooleanState() {
      return installed;
   }

   @Override
   public void setBooleanState(boolean bool) {
      performAction();
      if (EventQueue.isDispatchThread()){
         presenter.setSelected(installed);
      }
      else{
         EventQueue.invokeLater(new Runnable() {
            public void run() {
              presenter.setSelected(installed);
            }
         });
      }
   }

   @Override
   public Component getToolbarPresenter() {
      if (presenter == null) {
         presenter = new JToggleButton(this);
         presenter.setText(null);
         presenter.setToolTipText(this.getName());
      }

      return presenter;
   }

   public String getName() {
      return NbBundle.getMessage(org.netbeans.aspectj.actions.AspectJPluginBrowser.class,
              "LBL_StartBrowserAction");
   }

   @Override
   protected String iconResource() {
      return "org/aspectj/ajde/resources/actions/startAjde.gif";
   }

   public HelpCtx getHelpCtx() {
      return HelpCtx.DEFAULT_HELP;
   }

   private void showStructureView() {
      Runnable runnable = new Runnable() {

         public void run() {
            AspectJPluginBrowser.this.toggleStructureView(true);
            (structureViewTab).setVisible(true);
         }
      };

      if (SwingUtilities.isEventDispatchThread()) {
         runnable.run();
      } else {
         try {
            SwingUtilities.invokeAndWait(runnable);
         } catch (Exception exception) {
         }
      }
   }

   private void toggleStructureView(boolean flag) {
      try {

         // FileStructureViewWindowTopComponent fstc = FileStructureViewWindowTopComponent.findInstance();
         Mode mode = getExplorerMode();

         mode.dockInto(structureViewTab);

         if (flag) {
            structureViewTab.open();
            //only if name is "" ?
            structureViewTab.setName("AspectJ CrossRefs");
            structureViewTab.requestActive();
         } else {
            structureViewTab.close();
         }
      } catch (Exception exception) {
         BrowserErrorHandler.handleError("Could not toggle structure view.", exception);
         
      }
   }

   private static Mode getExplorerMode() {
      return WindowManager.getDefault().findMode("navigator");
   }

   @Override
   protected void initialize() {
      super.initialize();
      super.setEnabled(true);
   }

   protected boolean asynchronous() {
      return false;
   }

   static class StructureViewComponent extends TopComponent {

      private String PREFERRED_ID = "structureViewComponent";

      public StructureViewComponent(JPanel jpanel) {
         //this.setName("AspectJ Browser");//never seen, always reset
         this.setLayout(new BorderLayout());
         this.add(jpanel, "Center");
         this.add(jpanel);
         this.setBorder(null);
      }

     /* @Override
      public Image getIcon() {
         return ((ImageIcon) NbIconRegistry.INSTANCE.getStartAjdeIcon()).getImage();
      }*/

      // public SystemAction[] getSystemActions() {
//      return new SystemAction[0];
//        }
      @Override
      public int getPersistenceType() {
         return TopComponent.PERSISTENCE_NEVER;
      }

      @Override
      protected String preferredID() {
         return PREFERRED_ID;
      }
   }
}
//~ Formatted by Jindent --- http://www.jindent.com

