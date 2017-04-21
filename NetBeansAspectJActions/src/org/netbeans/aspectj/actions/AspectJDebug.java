/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

       

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
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
        category = "Debug",
        id = "org.netbeans.aspectj.actions.AspectJDebug"
)
@ActionRegistration(
        iconBase = "org/netbeans/aspectj/actions/icons/debug.gif",
        displayName = "#CTL_AspectJDebug"
)
@ActionReference(path = "Menu/Tools/AspectJ Spring 2017", position = 2833)
@Messages("CTL_AspectJDebug=AOP Debug")
public final class AspectJDebug implements ActionListener {
 
    Lookup lookup = Utilities.actionsGlobalContext();
    TopComponent activeTC = TopComponent.getRegistry().getActivated();
    DataObject dataLookup = activeTC.getLookup().lookup(DataObject.class);
    String filePath =  FileUtil.toFile(dataLookup.getPrimaryFile()).getAbsolutePath(); 
    String classNem = dataLookup.getPrimaryFile().getName();
    String nePath =  FileUtil.toFile(dataLookup.getPrimaryFile()).getParent();
    
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
    static Class class$org$netbeans$aspectj$actions$AspectJDebug;
    public String getName() {
	return (NbBundle.getMessage
		((class$org$netbeans$aspectj$actions$AspectJDebug == null
		  ? (class$org$netbeans$aspectj$actions$AspectJDebug
		     = class$("org.netbeans.aspectj.actions.AspectJDebug"))
		  : class$org$netbeans$aspectj$actions$AspectJDebug),
		 "LBL_DebugAction"));
    }

    public HelpCtx getHelpCtx() {
	return HelpCtx.DEFAULT_HELP;
    }

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
        
    @Override
    public void actionPerformed(ActionEvent e) {       
        try {
            File file = new File(getAJ());
            FileReader fileR = new FileReader(file);
            BufferedReader br = new BufferedReader(fileR);
            StringBuilder sb = new StringBuilder();
            String line;
            String template = "\\w*\\(\\)\\:";
            Pattern regexp = Pattern.compile(template);

            List<String> allMatches = new ArrayList<String>();
             
            while((line = br.readLine()) != null){
                Matcher matcher = regexp.matcher(line);
                if (matcher.find()){
                   allMatches.add(matcher.group());                   
                }
            }
            fileR.close();

            for (String temp : allMatches){
            System.out.println(temp);}

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }                
    }
}
