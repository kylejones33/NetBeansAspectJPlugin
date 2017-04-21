package org.netbeans.aspectj.file;

import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.aspectj.lexer.AJTokenId;
import org.netbeans.aspectj.parser.AJParser;

/**
 * @author stephanie.hammond
 */

@LanguageRegistration(mimeType = "text/x-aj")
public class AspectJSpringLanguage extends DefaultLanguageConfig {

    @Override
    public Language getLexerLanguage() {
        return AJTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "AJ";
    }
    
    @Override
    public Parser getParser() {
        return new AJParser();
    }    
}
