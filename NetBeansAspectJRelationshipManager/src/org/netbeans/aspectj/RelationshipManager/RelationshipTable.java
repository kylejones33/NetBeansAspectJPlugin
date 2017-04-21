/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package org.netbeans.aspectj.RelationshipManager;


import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


 public class RelationshipTable {
    
   JTable table = new JTable();
   DefaultTableModel dtm = new DefaultTableModel();

   void createTable(){
   table.setModel(dtm);
   dtm.addColumn("Source");
   dtm.addColumn("Relationship");
   dtm.addColumn("Target");
   }
   
}   