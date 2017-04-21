/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.RelationshipManager;

import javax.swing.table.DefaultTableModel;
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
import org.openide.nodes.Node;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;



@ActionID(
        category = "Build",
        id = "org.netbeans.aspectj.RelationshipManager.BuildTable"
)
@ActionRegistration(
        iconBase = "org/netbeans/aspectj/RelationshipManager/aspect.gif",
        displayName = "#CTL_BuildTable"
)
@ActionReference(path = "Menu/Tools/AspectJ Spring 2017", position = 2733)
@Messages("CTL_BuildTable=Build Relationships")




public final class BuildTable implements ActionListener {

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
    
   DefaultTableModel dtm = new DefaultTableModel();
   
   private static boolean hasRun = false;
   
    public void Build() {
            
        if(hasRun){return;}
        relationshipManagerTopComponent.jTable1.setModel(dtm);
            dtm.addColumn("Source");
            dtm.addColumn("Relationship");
            dtm.addColumn("Target");
            hasRun = true;
    }    

    
    @Override
    public void actionPerformed(ActionEvent e) {

        Build();
        
        try {
            File file = new File(getAJ());
            FileReader fileR = new FileReader(file);
            BufferedReader br = new BufferedReader(fileR);
            dtm.setRowCount(0);
            dtm.fireTableDataChanged();
            String line;
            String template = "(\\w*\\(\\)\\:) .* (\\S+\\w*\\(\\)).*";
            //String methodTemp = " .* (\\w*\\(\\)))";
            Pattern regexp = Pattern.compile(template);
            //Pattern regexp1 = Pattern.compile(methodTemp);
            
            List<String> aspMatches = new ArrayList<String>();
            List<String> methodMatches = new ArrayList<String>();
                                    
            while((line = br.readLine()) != null){
                Matcher matcher = regexp.matcher(line);
                if (matcher.find()){
                   aspMatches.add(matcher.group(1));
                   methodMatches.add(matcher.group(2));                 
                }
            }
            fileR.close();
            for (int i = 0; i < methodMatches.size(); i++ ){
                System.out.println("still working");
                dtm.addRow(new Object[] {classNem, aspMatches.get(i) , methodMatches.get(i)});
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } 

    }
}
