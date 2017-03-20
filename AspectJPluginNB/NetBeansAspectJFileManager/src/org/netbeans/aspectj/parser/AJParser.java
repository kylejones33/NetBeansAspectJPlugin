package org.netbeans.aspectj.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.Parser.Result;
import org.netbeans.modules.csl.spi.ParserResult;
import java.util.*;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.netbeans.aspectj.jccparser.JavaParser;

/**
 *
 * @author Steph
 */
public class AJParser extends Parser {
    private Snapshot snapshot;
    private JavaParser javaParser;

    @Override
    public void parse (Snapshot snapshot, Task task, SourceModificationEvent event) {
        this.snapshot = snapshot;
        Reader reader = new StringReader(snapshot.getText().toString ());
        javaParser = new JavaParser(reader);
        try {
            javaParser.CompilationUnit ();
        } catch (org.netbeans.aspectj.jccparser.ParseException ex) {
            Logger.getLogger (AJParser.class.getName()).log (Level.WARNING, null, ex);
        }
    }

    @Override
    public Result getResult (Task task) {
        return new AJParserResult (snapshot, javaParser);
    }

    @Override
    public void cancel () {
    }

    @Override
    public void addChangeListener (ChangeListener changeListener) {
    }

    @Override
    public void removeChangeListener (ChangeListener changeListener) {
    }

    //=======================================================================
    public static class AJParserResult extends ParserResult {
        private JavaParser javaParser;
        private boolean valid = true;

        AJParserResult (Snapshot snapshot, JavaParser javaParser) {
            super (snapshot);
            this.javaParser = javaParser;
        }

        public JavaParser getJavaParser () throws org.netbeans.modules.parsing.spi.ParseException {
            if (!valid) {
                throw new org.netbeans.modules.parsing.spi.ParseException ();
            }   
            return javaParser;
        }
        
        @Override
        protected void invalidate () {
            valid = false;
        }
        
        @Override
        public List<? extends Error> getDiagnostics() {
            List<Error> err = new ArrayList<Error>();
            return err;
        }
        
    }
          
}
