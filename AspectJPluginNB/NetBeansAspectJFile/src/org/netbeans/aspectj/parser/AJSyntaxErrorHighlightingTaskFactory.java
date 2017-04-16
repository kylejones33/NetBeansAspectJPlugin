package org.netbeans.aspectj.parser;
/**
 *
 * @author Steph
 */
import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.TaskFactory;

/**
 *
 * @author Steph
 */
@MimeRegistration(mimeType="text/x-apj",service=TaskFactory.class)
public class AJSyntaxErrorHighlightingTaskFactory extends TaskFactory {
 
    @Override
    public Collection create (Snapshot snapshot) {
        return Collections.singleton (new AJSyntaxErrorHighlightingTask());
    }
    
}
