package org.netbeans.aspectj.actions;

import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import org.aspectj.asm.IHierarchy;
import org.netbeans.aspectj.annotation.Annotator;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import org.aspectj.ajde.Ajde;
import org.aspectj.ajde.ui.UserPreferencesAdapter;
import org.netbeans.api.project.Project;
import org.openide.LifecycleManager;
import org.openide.windows.WindowManager;
import org.netbeans.api.editor.EditorRegistry;
import java.awt.Frame;

import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.aspectj.ajde.IconRegistry;
import org.aspectj.ajde.internal.BuildConfigListener;
import org.aspectj.ajde.ui.FileStructureView;
import org.aspectj.ajde.ui.IStructureViewNode;
//import org.aspectj.ajde.ui.swing.AjdeUIManager;
import org.aspectj.ajde.ui.swing.StructureViewPanel;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IHierarchyListener;
import org.aspectj.asm.IProgramElement;
import org.aspectj.tools.ajbrowser.core.BrowserErrorHandler;
import org.netbeans.aspectj.compileonsave.CompileOnSave;
//import org.aspectj.tools.ajde.netbeans.configeditor.NetBeansLstBuildConfigManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.spi.project.ActionProvider;
import org.openide.loaders.DataObject;
import org.openide.text.Line;


public class AspectJPluginNBManager {

   public static AspectJPluginNBManager INSTANCE = null;
   static String restoredConfigFile = null;
   AspectJPluginNBManager.BuildListenerImpl buildListener;
   
   private NbProjectProperties projectProperties = null;
   private StructureViewPanel multiViewPanel;
   private UserPreferencesAdapter preferencesAdapter = null;
   private PropertyChangeListener documentSwitchListener = new DocumentSwitchListener();
   private Logger logger = Logger.getLogger(AspectJPluginNBManager.class.getName());

   /**
    * Creates a new NbManager object.
    */
   protected AspectJPluginNBManager() {

      logger.log(Level.INFO, "AJDE AspectJPluginNBManager starting");
      preferencesAdapter = new UserPreferencesStore();
      projectProperties = new NbProjectProperties(preferencesAdapter);
      NbEditorAdapter nbeditoradapter = new NbEditorAdapter();
      NbCompilerMessages nbcompilermessages = new NbCompilerMessages();
      NbUIAdapter nbuiadapter = new NbUIAdapter();
     /*AjdeUIManager.getDefault().init(nbeditoradapter, nbcompilermessages,
              projectProperties, preferencesAdapter, nbuiadapter,
              new NbIconRegistry(), getRootFrame(), new ProgressMonitor(),
              new BrowserErrorHandler(), true); */
     //Ajde.getDefault().init(compilerConfig, uiBuildMessageHandler, monitor, nbeditoradapter, nbuiadapter, iconRegistry, rootFrame, runtimeProperties, ignoreGotoLines);
      Ajde.getDefault().init(null, null, null, nbeditoradapter, nbuiadapter, null, null, null, ignoreGotoLines);
      logger.log(Level.INFO, "AJDEUIManager initialized");
      //JPanel browser = modifyBrowserPanel();
      multiViewPanel = new LayoutHelper().modifyFilePanel2();
      nbeditoradapter.setTreeRef(treeRef);

      //multiViewPanel = new NetBeansMultiViewPanel(browser, filePanel);
//        System.out.println("BEFORE: "+Ajde.getDefault().getStructureViewManager().getDefaultFileView().getSourceFile());
//        Ajde.getDefault().getStructureViewManager().getDefaultFileView().setSourceFile("");
//        System.out.println("AFTER: "+Ajde.getDefault().getStructureViewManager().getDefaultFileView().getSourceFile());
      //String string = NetBeansAjdeUIManager.getDefault().getBuildOptions().getNonStandardOptions();
      //NetBeansAjdeUIManager.getDefault().getBuildOptions().setComplianceLevel(AjcBuildOptions.VERSION_15);

      AsmManager.lastActiveStructureModel.addListener(Annotator.getDefault());
      expandTreePathsWhenModelreloaded();
      EditorRegistry.addPropertyChangeListener(documentSwitchListener);
           //   Ajde.getDefault().getConfigurationManager()
           //    .setActiveConfigFile("<all project files>");

      buildListener = new BuildListenerImpl();
      Ajde.getDefault().getBuildConfigManager().addListener((BuildConfigListener) buildListener);
      AsmManager.attemptIncrementalModelRepairs = true;
     // Ajde.getDefault().getBuildManager().setBuildModelMode(false);
      logger.log(Level.INFO, "AJDE AspectJPluginNBManager started");
   }

   public static void init() {
      System.out.println("AspectJPluginNBManager#init");
     if (INSTANCE == null) {
       INSTANCE = new AspectJPluginNBManager();
      }
   }

   public void build() {
       System.out.println("Worked!");
    /*  logger.log(Level.INFO, "AJDE AspectJPluginNBManager build");
      //System.out.println("AspectJPluginNBManager#build");
      CompileOnSave.getDefault().pause(true);
      saveAll();
      if (projectProperties.getOutputPath() != null) {
         buildListener.build = true;
         Ajde.getDefault().getBuildConfigManager().buildModel(restoredConfigFile);
         
      } else {
         handleNoOutputPath();
      }
      CompileOnSave.getDefault().pause(false);*/
   }

   public void compile() {
      logger.log(Level.INFO, "AJDE AspectJPluginNBManager compile");
//        System.out.println("AspectJPluginNBManager#compile");
      CompileOnSave.getDefault().pause(true);
      saveAll();
      if (projectProperties.getOutputPath() != null) {
         buildListener.build = false;
         
         Ajde.getDefault().getBuildConfigManager().buildModel(restoredConfigFile);
      
      } else {
         handleNoOutputPath();
      }
      CompileOnSave.getDefault().pause(false);
   }

   public void compileTests() {
      throw new UnsupportedOperationException("not implemented yet");
   //we have test source roots?
   //test output path
   //test class path
   //.lst file?
   }

   /**
    * DOCUMENT ME!
    */
   public void debug() {
      
      logger.log(Level.INFO, "AJDE AspectJPluginNBManager debug");
      Project project = OpenProjects.getDefault().getMainProject();
      AspectJPluginNBManager.INSTANCE.getProjectProperties().setProject(project);
      if (project == null) {
         handleNoMainProject();
         return;
      }
      ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);
      actionProvider.invokeAction(ActionProvider.COMMAND_DEBUG, project.getLookup());

   }

   public IconRegistry getIcons() {
      return NbIconRegistry.INSTANCE;
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Frame getRootFrame() {
      //System.out.println("NbManager#getRootFrame");
      return WindowManager.getDefault().getMainWindow();
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public StructureViewPanel getViewPanel() {
      //System.out.println("AspectJPluginNBManager#getViewPanel");
      return multiViewPanel;
   }

   public void run() {
      //System.out.println("AspectJPluginNBManager#run");
      logger.log(Level.INFO, "AJDE AspectJPluginNBManager run");
      Project project = OpenProjects.getDefault().getMainProject();
      AspectJPluginNBManager.INSTANCE.getProjectProperties().setProject(project);
      if (project == null) {
         handleNoMainProject();
         return;
      }
      final ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);
      actionProvider.invokeAction(ActionProvider.COMMAND_RUN, project.getLookup());
   }

   /**
    * DOCUMENT ME!
    */
   public void saveAll() {
      //System.out.println("AspectJPluginNBManager#saveAll");
      LifecycleManager.getDefault().saveAll();
   }

   void clean() {
      //System.out.println("AspectJPluginNBManager#clean");
      logger.log(Level.INFO, "AJDE AspectJPluginNBManager clean");
      Project project = OpenProjects.getDefault().getMainProject();
      AspectJPluginNBManager.INSTANCE.getProjectProperties().setProject(project);
      if (project == null) {
         handleNoMainProject();
         return;
      }
      final ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);
      actionProvider.invokeAction(ActionProvider.COMMAND_CLEAN, project.getLookup());
      enableOtherActions(false);
   }

   public NbProjectProperties getProjectProperties() {
      return projectProperties;
   }

   private void expandTreePathsWhenModelreloaded() {
      AsmManager.lastActiveStructureModel.addListener(new IHierarchyListener()
      {

         @Override
         public void elementsUpdated(IHierarchy arg0) {
            for (int j = 0; j < treeRef.getRowCount(); j++) {
               treeRef.expandRow(j);
            }
         }
      });
   }

   private void handleNoMainProject() {
      BrowserErrorHandler.handleWarning("Please set a project as main project");
   }

   private void handleNoOutputPath() {
      BrowserErrorHandler.handleWarning("No output path defined, please select an output path.");
   }

   private class BuildListenerImpl /*implements BuildListener */{

      private boolean build;

      public void compileAborted(String lstFileName, String string0) {
         logger.log(Level.INFO, "AJDE AspectJPluginNBManager compileAborted: " + lstFileName + " xxx: " + string0);
      }

      public void compileFinished(String lstFileName, int i, boolean success, boolean b0) {
         //            System.out.println("compileFinished ");
         logger.log(Level.INFO, "AJDE AspectJPluginNBManager compileFinished: " + lstFileName);
         if (build && success) {
            Project project = AspectJPluginNBManager.INSTANCE.getProjectProperties().getProject();
            if (project == null) {
               handleNoMainProject();
               return;
            }
            final ActionProvider actionProvider = project.getLookup().lookup(ActionProvider.class);
            actionProvider.invokeAction(ActionProvider.COMMAND_BUILD, project.getLookup());
            enableOtherActions(true);
         }
      //dumpRelationships();
      }

      public void compileStarted(String lstFileName) {
         logger.log(Level.INFO, "AJDE AspectJPluginNBManager compileStarted: " + lstFileName);
      }
   }

   private void enableOtherActions(final boolean flag) {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(new Runnable() {

               @Override
               public void run() {
                  enableOtherActions(flag);
               }
            });
         } catch (InterruptedException ex) {
         } catch (InvocationTargetException ex) {
          }
         return;
      }

   }
   private boolean ignoreGotoLines;

   public boolean isIgnoreGotoLines() {
      return ignoreGotoLines;
   }

   private class DocumentSwitchListener implements PropertyChangeListener {

      public void propertyChange(PropertyChangeEvent evt) {
         FileStructureView fileStructureView = Ajde.getDefault().getStructureViewManager().getDefaultFileView();
         String filePath = null;

         JTextComponent textComp = null;
         DataObject dataObj = null;
         if (fileStructureView != null) {
            if (evt.getPropertyName().equals(EditorRegistry.FOCUS_GAINED_PROPERTY)) {
               try {
                  textComp = (JTextComponent) evt.getNewValue();
                  Document doc = textComp.getDocument();
                  dataObj = NbEditorUtilities.getDataObject(doc);
                  File f = null; /*FileUtil.toFile(dataObj.getPrimaryFile());*/
                  filePath = f.getAbsolutePath(); // NPE !!!

               } catch (NullPointerException e) {
                  //System.out.println("propertyChange: problems with doc " + e.getMessage());
                  //Exceptions.printStackTrace(e);
                  return;
               }
               if (filePath == null || filePath.equals(fileStructureView.getSourceFile()) || !(filePath.endsWith(".java") || filePath.endsWith(".aj"))) {
                  return;
               }
               int lineNumber = 0;
               try {

                  //lineNumber = NbEditorUtilities.getLine(textComp, true).getLineNumber();
//                        System.out.println("MODIFIED: "+dataObj.isModified());

                  lineNumber = NbEditorUtilities.getLine(textComp, true).getLineNumber();
               //AsmManager.getDefault().getHierarchy().findElementForSourceLine(filename, lineNumber);
               //org.aspectj.ajde.ui.StructureView.

               } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                  indexOutOfBoundsException.printStackTrace();//if file is new and editing?
               //return;
               }

//                    System.out.println("will fire nav with line number " + lineNumber);
//                    System.out.println("other line number " + NbEditorUtilities.getLine(textComp, false).getLineNumber());


               Ajde.getDefault().getStructureViewManager().fireNavigationAction(filePath, +1);
               ignoreGotoLines = true;
//                    System.out.println("fireNavigationAction(" + filePath);
               final IProgramElement findElementForSourceLine = AsmManager.lastActiveStructureModel.getHierarchy().findElementForSourceLine(filePath, lineNumber + 1);
//                    System.out.println("elem for source line " + lineNumber + " in " + filePath + " = " + findElementForSourceLine);
               IStructureViewNode n = Ajde.getDefault().getStructureViewManager().getDefaultFileView().findCorrespondingViewNode(findElementForSourceLine);
               multiViewPanel.setActiveNode(n);
               multiViewPanel.highlightActiveNode();
               ignoreGotoLines = false;




               for (int j = 0; j < treeRef.getRowCount(); j++) {
                  treeRef.expandRow(j);
               }
               //WindowManager.getDefault().findTopComponent("structureViewComponent").setName(filename+" - AspectJ CrossRefs");
               //System.out.println("changed source file? " + fsv.getSourceFile());
               Annotator.getDefault().annotateFile(filePath);
            }
         }
      }
   }
   private JTree treeRef;

   private class LayoutHelper {

      private StructureViewPanel modifyFilePanel2() {

         StructureViewPanel fileStructurePanel = Ajde.getDefault().getFileStructurePanel();
         //get oben bar
         BorderLayout layoutStructureView = (BorderLayout) fileStructurePanel.getLayout();
         JPanel obenBar = (JPanel) layoutStructureView.getLayoutComponent(BorderLayout.NORTH);

         //move oben bar
         fileStructurePanel.remove(obenBar);
         BorderLayout layoutObenBar = (BorderLayout) obenBar.getLayout();
         //get buttonsPanel
         JPanel buttons_panel = (JPanel) layoutObenBar.getLayoutComponent(BorderLayout.EAST);
         //get navigationPanel
         BorderLayout buttonsPanelLayout = (BorderLayout) buttons_panel.getLayout();
         JPanel navigation_panel = (JPanel) buttonsPanelLayout.getLayoutComponent(BorderLayout.CENTER);
         //nun zu den buttons
         BorderLayout navigationLayout = (BorderLayout) navigation_panel.getLayout();
         JButton back = (JButton) navigationLayout.getLayoutComponent(BorderLayout.CENTER);
         JButton forward_button = (JButton) navigationLayout.getLayoutComponent(BorderLayout.EAST);
         navigation_panel.removeAll();
         //new filter here as test
         obenBar.removeAll();

         StructureFilterPanel sfp = new StructureFilterPanel(Ajde.getDefault().getStructureViewManager().getDefaultFileView());
         sfp.addNavButtons(back, forward_button);
         TapPanel tapPanel = new TapPanel();
         tapPanel.setOrientation(TapPanel.DOWN);
         tapPanel.add(sfp);
         fileStructurePanel.add(tapPanel, BorderLayout.SOUTH);

         //tree component
         JScrollPane center = (JScrollPane) layoutStructureView.getLayoutComponent(BorderLayout.CENTER); //scroll?
         center.getViewport().setBorder(null);//geaendert
         center.setBorder(null);
         JTree tree = (JTree) center.getViewport().getView();
         tree.setCellRenderer(new SwingTreeViewNodeRenderer());
         tree.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
         tree.setRowHeight(18);
         treeRef = tree;
         //ende
         return fileStructurePanel;
      }

      private JPanel modifyBrowserPanel() {
         JPanel oben = Ajde.getDefault().getViewManager().getBrowserPanel();
         oben.setBorder(null);
         BorderLayout l = (BorderLayout) oben.getLayout();
         JPanel obenBar = (JPanel) l.getLayoutComponent(BorderLayout.NORTH);
         oben.remove(obenBar);
         JScrollPane center = (JScrollPane) l.getLayoutComponent(BorderLayout.CENTER); //scroll?
         center.getViewport().getView();
         center.setBorder(null);
         JTree tree = (JTree) center.getViewport().getView();
         tree.setCellRenderer(new SwingTreeViewNodeRenderer());
         tree.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
         return oben;
      }
   }

   private class ClickOnTextListener extends MouseAdapter implements ChangeListener {

      private List<DataObject> listened = new ArrayList<DataObject>();

      @Override
      public void mouseReleased(MouseEvent e) {

         ignoreGotoLines = true;
         Line line = NbEditorUtilities.getLine(EditorRegistry.lastFocusedComponent(), true);
         String filePath = NbEditorAdapter.getDefault().getCurrFile();
         IProgramElement findElementForSourceLine = AsmManager.lastActiveStructureModel.getHierarchy().findElementForSourceLine(filePath, line.getLineNumber() + 1);
         IStructureViewNode n = Ajde.getDefault().getStructureViewManager().getDefaultFileView().findCorrespondingViewNode(findElementForSourceLine);
         multiViewPanel.setActiveNode(n);
         multiViewPanel.highlightActiveNode();
         ignoreGotoLines = false;
      }

     /* @Override
      public void stateChanged(ChangeEvent e) {
         DataObject[] dataObjects = DataObject.getRegistry().getModified(); 
         for (DataObject dataObject : dataObjects) {
            if (isInBuild(dataObject)) {
               addListener(dataObject);
            }
         }

      }*/

      private void addListener(DataObject dataObject) {
         if (!listened.contains(dataObject)) {
            listened.add(dataObject);
         }
         throw new UnsupportedOperationException("Not yet implemented");
      }

      private boolean isInBuild(DataObject dataObject) {

         //System.out.println("isInBuild "+dataObject);
         if (!AsmManager.lastActiveStructureModel.getHierarchy().isValid()) {
            return false;
         }
         String path = System.in.toString();
                 //FileUtil.toFile(dataObject.getPrimaryFile()).getAbsolutePath();
         
//       System.out.println("AsmManager.getDefault().getHierarchy().isValid: "+AsmManager.getDefault().getHierarchy().isValid());
//        System.out.println("AsmManager.getDefault().getHierarchy().getConfigFile: "+AsmManager.getDefault().getHierarchy().getConfigFile());
         Object e = AsmManager.lastActiveStructureModel.getHierarchy().findInFileMap(path);
         //System.out.println("PE + "+e);
         
         return e != null;
      }

        @Override
        public void stateChanged(ChangeEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
   }

   // <editor-fold defaultstate="collapsed" desc="Old Code"> 
   private class Unused {

      /**
       * DOCUMENT ME!
       *
       * @return DOCUMENT ME!
       */
      private String findClassNameToRun() {
         //System.out.println("NbManager#findClassNameToRun: ");
         return "";
      }

      /**
       * DOCUMENT ME!
       */
      public void run_old() {
         //System.out.println("NbManager#run");

         try {
            String classToExecute = projectProperties.getClassToExecute();

            if ((classToExecute == null) || (classToExecute.length() == 0)) {
               BrowserErrorHandler.handleWarning("No main class specified, please select a class to run.");
               //AjdeUIManager.getDefault().getOptionsFrame().showPanel(BuildOptionsPanel.getDefault());
            } else {
               //		String outputPathAndClassPath
               //		    = (projectProperties.getOutputPath() + File.pathSeparator
               //		       + projectProperties.getClasspath());
               //		Enumeration enumeration = Executor.executors();
               //		boolean bool = false;
               //		while (enumeration.hasMoreElements()) {
               //		    Object object = enumeration.nextElement();
               //		    if (object instanceof ProcessExecutor
               //			&& (object.getClass().toString().equals
               //			    ("class org.netbeans.modules.java.JavaProcessExecutor"))) {
               //			((ProcessExecutor) object)
               //			    .setClassPath(new NbClassPath(outputPathAndClassPath));
               //			((ProcessExecutor) object)
               //			    .execute(new ExecInfo(classToExecute));
               //			bool = true;
               //		    }
               //		}
            }
         } catch (Exception exception) {
            BrowserErrorHandler.handleError("Main class not found.", exception);
         }
      }

      private void test() {
         //        JFrame f = new JFrame("browser");
//        StructureViewManager m = Ajde.getDefault().getStructureViewManager();
//        GlobalStructureView gsv = m.createGlobalView(new GlobalViewProperties(Hierarchy.CROSSCUTTING));
//        f.add(NetBeansAjdeUIManager.getDefault().getViewManager().getBrowserPanel());
//        NetBeansAjdeUIManager.getDefault().getViewManager().getBrowserPanel().setCurrentView(gsv);
//        f.pack();
//        f.setVisible(true);

         //NetBeansAjdeUIManager.getDefault().getFileStructurePanel();

         //        JFrame fg = new JFrame("global");
//        StructureViewPanel fileStructureView2 = NetBeansAjdeUIManager.getDefault().getFileStructurePanel();
//        StructureViewManager m = Ajde.getDefault().getStructureViewManager();
//        GlobalStructureView gsv = m.createGlobalView(new GlobalViewProperties(Hierarchy.CROSSCUTTING));
//        fileStructureView2.setCurrentView(gsv);
//        fg.add(fileStructureView2);
//        fg.pack();
//        fg.setVisible(true);
         //Ajde.getDefault().getStructureViewManager().createViewForSourceFile("file path", null);
         //Ajde.getDefault().getStructureViewManager().getDefaultFileView();
         //System.out.println(Ajde.getDefault().getStructureViewManager().getAvailableRelations());
         //Ajde.getDefault().getStructureViewManager().refreshView(null);
         //System.out.println(Ajde.getDefault().getStructureModelManager().getHierarchy().getRoot().getChildren());
      }

      /**
       * DOCUMENT ME!
       *
       * @param string DOCUMENT ME!
       */
      public void setEditorStatusText(String string) {
         //System.out.println("NbManager#setEditorStatusText: " + string);
      }

      /**
       * DOCUMENT ME!
       */
      public void showMessages() {
         //System.out.println("NbManager#showMessages: ");
    /* empty */
      }

      /**
       * DOCUMENT ME!
       *
       * @param string DOCUMENT ME!
       */
      public void editConfigFile(String string) {
         //System.out.println("NbManager#editConfigFile");
    /* empty */
      }

      /**
       * doesn't get called
       *
       * @return DOCUMENT ME!
       */
      public List getBuildOptions() {
         //System.out.println("NbManager#getBuildOptions");

         ArrayList arraylist = new ArrayList();
         arraylist.add("-g");
         arraylist.add("-1.5");

         String classPath = projectProperties.getClasspath();
         if (!classPath.equals("")) {
            arraylist.add("-classpath");
            arraylist.add(classPath);
         }

         String outputPath = projectProperties.getOutputPath();

         if (!outputPath.equals("")) {
            arraylist.add("-d");
            arraylist.add(outputPath);
         }

         return arraylist;
      }

      /**
       * DOCUMENT ME!
       *
       * @return DOCUMENT ME!
       */
      public String getDumpFilePath() {
         //System.out.println("NbManager#getDumpFilePath");
         return null;
      }

      /**
       * DOCUMENT ME!
       *
       * @return DOCUMENT ME!
       */
      /**
       * DOCUMENT ME!
       */
      public void hideMessages() {
         //System.out.println("NbManager#hideMessages");
    /* empty */
      }

      /**
       * DOCUMENT ME!
       *
       * @param string DOCUMENT ME!
       */
      public void mountConfigFile(String string) {
         //System.out.println("NbManager#mountConfigFile");
    /* empty */
      }

      /**
       * DOCUMENT ME!
       *
       * @param string DOCUMENT ME!
       */
      public void removeConfigFile(String string) {
         /* empty */
         //System.out.println("NbManager#removeConfigFile");
      }
   }// </editor-fold>
}
