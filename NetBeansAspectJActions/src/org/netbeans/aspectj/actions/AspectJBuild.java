/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import org.aspectj.tools.ajc.Main;
import org.aspectj.bridge.*;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Build",
        id = "org.netbeans.aspectj.actions.AspectJBuild"
)
@ActionRegistration(
        iconBase = "org/netbeans/aspectj/actions/icons/build.gif",
        displayName = "#CTL_AspectJBuild"
)
@ActionReference(path = "Menu/Tools/AspectJ Spring 2017", position = 3033)
@Messages("CTL_AspectJBuild=AOP Build")

public final class AspectJBuild implements ActionListener  {
  
    Lookup lookup = Utilities.actionsGlobalContext();
    Project project = OpenProjects.getDefault().getOpenProjects()[0];
    FileObject projectDir = project.getProjectDirectory();
    String projectLocation = projectDir.getPath();
    TopComponent activeTC = TopComponent.getRegistry().getActivated();
    DataObject dataLookup = activeTC.getLookup().lookup(DataObject.class);
    String newPath =  FileUtil.toFile(dataLookup.getPrimaryFile()).getPath();
    String nePath =  FileUtil.toFile(dataLookup.getPrimaryFile()).getParent();
    String filePath =  FileUtil.toFile(dataLookup.getPrimaryFile()).getAbsolutePath();

    String[] args = {projectLocation, filePath};
  
    public String getAJ() throws IOException {
        String ajPath = "This loads first";
        File folder = new File(nePath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".aj")) {
                String content = file.getPath();
                ajPath = content;
            } 
        }
         return ajPath;   
    }   
    public String getName() {
	return NbBundle.getMessage(AspectJBuild.class,"LBL_BuildAction");
    }
  
    public void mainRun (String[] args) {
        try {Main compiler = new Main();

        MessageHandler m = new MessageHandler();
        compiler.run(args, m);

        IMessage[] ms = m.getMessages(null, true);
        System.out.println("messages:" + ms);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public HelpCtx getHelpCtx() {
	return HelpCtx.DEFAULT_HELP;
    }
    protected boolean asynchronous() {
       return false;
    }    
    @Override
    public void actionPerformed(ActionEvent e) {    
        System.out.println("AJBuildAction#performAction");
        System.out.println("Project directory: " + projectLocation);
        System.out.println(".java file: " + filePath);
        System.out.println("Project directory of .aj: " + nePath);        
        try {
           System.out.println(".aj file: " + getAJ());
           String[] arys = {"org/aspectj/jrt/aspectjrt.jar", getAJ(), filePath};
           mainRun(arys);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
