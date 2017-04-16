/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
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
    String filePath =  FileUtil.toFile(dataLookup.getPrimaryFile()).getAbsolutePath();
    
    
    public String getName() {
	return NbBundle.getMessage(AspectJBuild.class,"LBL_BuildAction");
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
        System.out.println(filePath);
	AspectJPluginNBManager.INSTANCE.build();
    }
}
