
package org.netbeans.aspectj.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.aspectj.ajde.Ajde;
import org.aspectj.ajde.ui.swing.BuildConfigPopupMenu;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public class AspectJPluginSelectConfigurationAction extends CallableSystemAction {

   public static boolean PROP_ENABLED = false;
   private JPopupMenu configsMenu = null;
   final AbstractAction BUILD_ACTION;

    public AspectJPluginSelectConfigurationAction() {
        this.BUILD_ACTION = new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent actionevent) {
                AspectJPluginNBManager.INSTANCE.saveAll();
                /*
                Project project = OpenProjects.getDefault().getMainProject();
                final ActionProvider ap = project.getLookup().lookup(ActionProvider.class);
                SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                ap.invokeAction(ActionProvider.COMMAND_BUILD,null);
                }
                });
                **/
                configsMenu.setVisible(false);
            }
        };
    }

   @Override
   public void performAction() {
   /* empty */
   }

   @Override
   public void actionPerformed(ActionEvent actionevent) {
      AspectJPluginNBManager.INSTANCE.getProjectProperties().setProject(null);//main project TODO
      configsMenu = new BuildConfigPopupMenu(BUILD_ACTION);
      configsMenu.setInvoker((Component) actionevent.getSource());
      if (actionevent.getSource() instanceof JButton) {
         JButton jbutton = (JButton) actionevent.getSource();
         System.out.println("button: " + jbutton);
         int x = (int) jbutton.getLocationOnScreen().getX();
         int y = (int) jbutton.getLocationOnScreen().getY() + 25;
         configsMenu.setLocation(x, y);
      } else {
         configsMenu.setLocation(30, 30);
      }
      configsMenu.setVisible(true);

   }

   @Override
   public String getName() {
      //super(NbBundle.getMessage(ClassBrowserAction.class, "CTL_ClassBrowserAction"));
      return NbBundle.getMessage(AspectJPluginSelectConfigurationAction.class,
              "LBL_AJSelectConfigurationAction");
   }

   @Override
   protected String iconResource() {
      return "org/aspectj/ajde/resources/actions/popup.gif";
   }

   @Override
   public HelpCtx getHelpCtx() {
      return HelpCtx.DEFAULT_HELP;
   }

   @Override
   public void setEnabled(boolean bool) {
      PROP_ENABLED = bool;
      super.setEnabled(bool);
   }

   @Override
   public boolean isEnabled() {
      return PROP_ENABLED;
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

   @Override
   public JMenuItem getMenuPresenter() {

      final JMenu menu = new JMenu(this);
      menu.setIcon(null);
      if (AspectJPluginNBManager.INSTANCE != null) {//ajde not started yet
         AspectJPluginNBManager.INSTANCE.getProjectProperties().setProject(null);
      }
      menu.addItemListener(new ItemListener() {

         boolean loaded = false;

         @Override
         public void itemStateChanged(ItemEvent e) {
            if (loaded) {
               return;
            }
            if (e.getStateChange() == ItemEvent.SELECTED) {
               System.out.println("AJSelectConfigurationAction WORKING ...");
               loadItems();
               //loaded = true; RAUS
            }
         }

         private void loadItems() {
            if (AspectJPluginNBManager.INSTANCE == null) return;//ajde not started yet
            List list = AspectJPluginNBManager.INSTANCE.getProjectProperties().getBuildConfigFiles();

            for (final Object object : list) {
               menu.add(new AbstractAction(object.toString()) {

                  @Override
                  public void actionPerformed(ActionEvent e) {
                     Ajde.getDefault().getBuildConfigManager().setActiveConfigFile((String) object);
                     AspectJPluginNBManager.INSTANCE.build();
                  }
               });
            }
         }
      });

      return menu;
   }


}
