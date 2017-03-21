package org.netbeans.aspectj.lexer;

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.aspectj.jcclexer.JavaCharStream;
import org.netbeans.aspectj.jcclexer.JavaParserTokenManager;
import org.netbeans.aspectj.jcclexer.Token;
/**
 * @author stephanie.hammond
 */
public class AJLexer implements Lexer<AJTokenId> {
    private LexerRestartInfo<AJTokenId> info;
    private JavaParserTokenManager javaParserTokenManager;

    AJLexer(LexerRestartInfo<AJTokenId> info) {
        this.info = info;
        JavaCharStream stream = new JavaCharStream(info.input());
        javaParserTokenManager = new JavaParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<AJTokenId> nextToken() {
        Token token = javaParserTokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(AJLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
