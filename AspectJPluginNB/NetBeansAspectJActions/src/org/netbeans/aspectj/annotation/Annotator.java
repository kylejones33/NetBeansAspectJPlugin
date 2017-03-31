/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aspectj.annotation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.aspectj.asm.AsmManager;

import org.aspectj.asm.IHierarchy;
import org.aspectj.asm.IHierarchyListener;
import org.aspectj.asm.IProgramElement;
import org.aspectj.asm.IRelationship;
import org.aspectj.asm.IRelationshipMap;
import org.aspectj.asm.internal.Relationship;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.lookup.*;
import org.openide.nodes.Node;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Annotation;
import org.openide.text.Line;

/**
 *
 * @author ramos
 */
public class Annotator implements IHierarchyListener {

    private List<AspectJAnnotation> attachedAnnotations = new ArrayList<AspectJAnnotation>();
    private List<DataObject> annotatedComponents = new ArrayList<DataObject>();
    private static Annotator instance = new Annotator();

    public static Annotator getDefault() {
        return instance;
    }

    private Annotator() {
        updateAnnotations(AsmManager.lastActiveStructureModel.getHierarchy());
    }

    public void annotateFile(String fileName) throws IndexOutOfBoundsException {
        try {
            DataObject dataObject = null;            
                    DataObject.find(FileUtil.toFileObject(new File(fileName)));
            //System.out.println("annotating " + fileName);
            if (annotatedComponents.contains(dataObject)) {
                return;
            }
            Map<Integer, List<IProgramElement>> annotationsMapOfFile =
                    AsmManager.lastActiveStructureModel.getInlineAnnotations(fileName, true, true);
            if (annotationsMapOfFile == null) {
                return;
            }

            for (Integer lineNumber : annotationsMapOfFile.keySet()) {

                List<IProgramElement> annotationsOnLine = annotationsMapOfFile.get(lineNumber);

                for (IProgramElement programmElement : annotationsOnLine) {

                    IRelationshipMap relationshipMap = AsmManager.lastActiveStructureModel.getRelationshipMap();
                    List<IRelationship> relationships = relationshipMap.get(programmElement);
                    AspectJAnnotation annotation = annotationByKind(programmElement);
                    annotateLine(lineNumber, annotation, dataObject);
                }
            }
            annotatedComponents.add(dataObject);
        System.out.println("annotatedComponents + "+dataObject);
        } catch (DataObjectNotFoundException ex) {
            //Exceptions.printStackTrace(ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void elementsUpdated(final IHierarchy rootNode) {
        //System.out.println("Annotator.elementsUpdated");
        //inspectRelMap();
        //new RelationshipTable();
        //System.out.println("HierarchyAdapter: elementsUpdated");

        // Make sure that this work is done on the UI thread
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        elementsUpdated(rootNode);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        updateAnnotations(rootNode);
    }

    // TODO: consider looping over open textcomps once in outer loop, and 
    public void updateAnnotationsIfOpenInEditor(String fileName) {
        //System.out.println("updateAnnotationsIfOpenInEditor: " + fileName);
        List<JTextComponent> textComponents = (List<JTextComponent>) EditorRegistry.componentList();
        //System.out.println("number of text components: " + textComponents.size());
        //coild remove found file from a cloned copy of the list
        for (JTextComponent jTextComponent : textComponents) {
            if (isThatFile(jTextComponent, fileName)) {
                annotateFile(fileName);
                return;//fertig
            }
        }

    }

    private void annotateLine(Integer lineNumber, AspectJAnnotation annotation, DataObject dataObject)
            throws IndexOutOfBoundsException {
        LineCookie cookie = dataObject.getLookup().lookup(LineCookie.class);
        Line.Set lineSet = cookie.getLineSet();
        
        final Line line = lineSet.getOriginal(lineNumber.intValue() - 1);
        annotation.attach(line);
        System.out.println("attached annotation "+annotation+" to line "+line);
        attachedAnnotations.add(annotation);
    //System.out.println("attached annotations + "+annotation);
    }

    private AspectJAnnotation annotationByKind(List<Relationship> relationships, IProgramElement programmElement) {
        return new AspectJAnnotation(relationships, programmElement);

    }

    private void clearAnnotations() {
        //detach annotations
        for (Annotation annotation : attachedAnnotations) {
            annotation.detach();
        //System.out.println("annotation "+annotation+" detached");
        }
        attachedAnnotations.clear();
        //System.out.println("attachedAnnotations cleared");
        annotatedComponents.clear();
    //System.out.println("annotatedComponents cleared");
    }

    private boolean isThatFile(JTextComponent jTextComponent, String filename) {
      
            Document doc = jTextComponent.getDocument();
            if (doc == null) return false;
           DataObject dataObject = NbEditorUtilities.getDataObject(doc);
            if (dataObject == null) return false;
            FileObject fo = dataObject.getPrimaryFile();
            if (fo == null) return false;
            File fileToFind = FileUtil.toFile(fo);
            if (fileToFind == null) return false;
            //System.out.println("file.getAbsPath?: " + fileToFind.getAbsolutePath());
            return filename.equals(fileToFind.getAbsolutePath());

    }

    public AspectJAnnotation annotationForLine(Line line) {
        for (AspectJAnnotation annotation : attachedAnnotations) {
            if (annotation.getAttachedAnnotatable().equals(line)) {
                return annotation;
            }
        }
        return null;
    }

    private void updateAnnotations(final IHierarchy rootNode) {


        // Work through each file in the set, looking for editors to update
        if (rootNode != null && rootNode.isValid()) {

            //clear annotaded comps list
            clearAnnotations();
            //System.out.println("rootNode: " + rootNode);
            //System.out.println("files: " + rootNode.getFileMapEntrySet());
            //all files in project?
            Set<Map.Entry<String, IProgramElement>> entrySet = rootNode.getFileMapEntrySet(); //absoliutePath und fileName as Strings
            if (entrySet != null) {

                try {
                    for (Map.Entry nextEntry : entrySet) {
                        String fileName = (String) nextEntry.getKey();
                        updateAnnotationsIfOpenInEditor(fileName);
                    }
                } catch (Exception ex) {
                    // Stop exception that you get if you happen to have built this project
                    // before
                    ex.printStackTrace();
                }
            }
        }
    }

    private AspectJAnnotation annotationByKind(IProgramElement programmElement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
