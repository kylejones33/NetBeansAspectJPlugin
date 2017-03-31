/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.compileonsave;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.*;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.aspectj.ajde.Ajde;
import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IHierarchy;
import org.aspectj.asm.IHierarchyListener;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.internal.ProgramElement;
import org.netbeans.aspectj.actions.AspectJPluginNBManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;

import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;



/**
 *
 * @author Ramos
 * if this action is enabled the listener is added to DataObject.getRegistry()
 */
public class CompileOnSave implements ChangeListener, IHierarchyListener {

   private static CompileOnSave INSTANCE = new CompileOnSave();
   private HashMap<DataObject, Date> lastModifiedRecord = new HashMap<DataObject, Date>();
   private boolean pause;

   private CompileOnSave() {

   }

   public static CompileOnSave getDefault() {
      return INSTANCE;
   }
   boolean compiling = false;

   public void finished() {
      //System.out.println("finished");
      synchronized (this) {
         compiling = false;
         notifyAll();
      }
   }

   public void pause(boolean b) {
      this.pause = b;
   }

   private Map<DataObject,PropertyChangeListener> listeningToMap = new HashMap<DataObject,PropertyChangeListener>();
   private void setListeningTo(DataObject fileDataObject, PropertyChangeListener pcl) {
      listeningToMap.put(fileDataObject, pcl);
   }

   private void setUpForCompileOnSave(DataObject fileDataObject) {
      
      if (!lastModifiedRecord.keySet().contains(fileDataObject)) {
          
         final FileObject fileObject = fileDataObject.getPrimaryFile();
         if (FileUtil.toFile(fileObject) != null) {
            lastModifiedRecord.put(fileDataObject, fileObject.lastModified());
            PropertyChangeListener pcl = new PropertyChangeListenerImpl(fileDataObject, fileObject);
            fileDataObject.addPropertyChangeListener(pcl);
            setListeningTo(fileDataObject, pcl);
         }
      }
   }
   
   public void unSetupForCompileOnSave(){
      for (DataObject key :  listeningToMap.keySet()) {
         key.removePropertyChangeListener(listeningToMap.get(key));
      }
      listeningToMap.clear();
      lastModifiedRecord.clear();
   }

   
   public static void disableCompileOnSave() {
      DataObject.getRegistry().removeChangeListener(CompileOnSave.getDefault());
      AsmManager.lastActiveStructureModel.removeStructureListener(CompileOnSave.getDefault());
      CompileOnSave.getDefault().unSetupForCompileOnSave();
   }

   public static  void enableCompileOnSave() {

      AsmManager.lastActiveStructureModel.addListener(CompileOnSave.getDefault());
      DataObject.getRegistry().addChangeListener(CompileOnSave.getDefault());
   }
   
   void doCompileOnSave(DataObject fileDataObject) {
      //System.out.println("doCompileOnSave " + fileDataObject);
      synchronized (this) {
         try {
            if (compiling) {
               wait();
            }
         } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
         }
         AspectJPluginNBManager.INSTANCE.compile();
         compiling = true;
      }

   }

   private boolean isInBuild(DataObject dataObject) {

      //System.out.println("isInBuild "+dataObject);
      if (!AsmManager.lastActiveStructureModel.getHierarchy().isValid()) {
         return false;
      }
      String path = FileUtil.toFile(dataObject.getPrimaryFile()).getAbsolutePath();
      Object found = AsmManager.lastActiveStructureModel.getHierarchy().findInFileMap(path);
      //System.out.println("PE + "+e);
      return found != null;
   }

   public void elementsUpdated(IHierarchy arg0) {
      Set<Map.Entry<String, IProgramElement>> set = AsmManager.lastActiveStructureModel.getHierarchy().getFileMapEntrySet();
      for (Map.Entry<String, IProgramElement> entry : set) {
         try {
            setUpForCompileOnSave(DataObject.find(FileUtil.toFileObject(new File(entry.getKey()))));
         } catch (DataObjectNotFoundException ex) {
            ex.printStackTrace();
         }
      }
   //System.out.println("set: "+set);

   }

    @Override
    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   private class PropertyChangeListenerImpl implements PropertyChangeListener {

      private DataObject fileDataObject;
      private FileObject fileObject;

      public PropertyChangeListenerImpl(DataObject fileDataObject, FileObject fileObject) {
         this.fileDataObject = fileDataObject;
         this.fileObject = fileObject;
      }

      public void propertyChange(PropertyChangeEvent evt) {
         if (isCompileTrigger(evt)) {
            lastModifiedRecord.put(fileDataObject, fileObject.lastModified());
            doCompileOnSave(fileDataObject);
         }
      }

      private boolean isCompileTrigger(PropertyChangeEvent evt) {
         return !pause && evt.getPropertyName().equals(DataObject.PROP_MODIFIED) && !fileObject.lastModified().equals(lastModifiedRecord.get(fileDataObject)) && evt.getOldValue().equals(true) && evt.getNewValue().equals(false);
         //sometimes triggered by making changes and then undo without
         //saving
         //inspect file on disk?
      }
   }
}
