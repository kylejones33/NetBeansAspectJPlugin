package org.netbeans.aspectj.lexer;

import java.util.*;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
/**
 * @author stephanie.hammond
 */
public class AJLanguageHierarchy extends LanguageHierarchy<AJTokenId> {
    
    private static List<AJTokenId> tokens;
    private static Map<Integer, AJTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<AJTokenId>asList(new AJTokenId[]{
              //copy list of tokens here
             
    new AJTokenId("EOF", "whitespace", 0),
    new AJTokenId("WHITESPACE", "whitespace", 1),
    new AJTokenId("SINGLE_LINE_COMMENT", "comment", 4),
    new AJTokenId("FORMAL_COMMENT", "comment", 5),
    new AJTokenId("MULTI_LINE_COMMENT", "comment", 6),
    new AJTokenId("ABSTRACT", "keyword", 8),
    new AJTokenId("ASSERT", "keyword", 9),
    new AJTokenId("BOOLEAN", "keyword", 10),
    new AJTokenId("BREAK", "keyword", 11),
    new AJTokenId("BYTE", "keyword", 12),
    new AJTokenId("CASE", "keyword", 13),
    new AJTokenId("CATCH", "keyword", 14),
    new AJTokenId("CHAR", "keyword", 15),
    new AJTokenId("CLASS", "keyword", 16),
    new AJTokenId("CONST", "keyword", 17),
    new AJTokenId("CONTINUE", "keyword", 18),
    new AJTokenId("_DEFAULT", "keyword", 19),
    new AJTokenId("DO", "keyword", 20),
    new AJTokenId("DOUBLE", "keyword", 21),
    new AJTokenId("ELSE", "keyword", 22),
    new AJTokenId("ENUM", "keyword", 23),
    new AJTokenId("EXTENDS", "keyword", 24),
    new AJTokenId("FALSE", "keyword", 25),
    new AJTokenId("FINAL", "keyword", 26),
    new AJTokenId("FINALLY", "keyword", 27),
    new AJTokenId("FLOAT", "keyword", 28),
    new AJTokenId("FOR", "keyword", 29),
    new AJTokenId("GOTO", "keyword", 30),
    new AJTokenId("IF", "keyword", 31),
    new AJTokenId("IMPLEMENTS", "keyword", 32),
    new AJTokenId("IMPORT", "keyword", 33),
    new AJTokenId("INSTANCEOF", "keyword", 34),
    new AJTokenId("INT", "keyword", 35),
    new AJTokenId("INTERFACE", "keyword", 36),
    new AJTokenId("LONG", "keyword", 37),
    new AJTokenId("NATIVE", "keyword", 38),
    new AJTokenId("NEW", "keyword", 39),
    new AJTokenId("NULL", "keyword", 40),
    new AJTokenId("PACKAGE", "keyword", 41),
    new AJTokenId("PRIVATE", "keyword", 42),
    new AJTokenId("PROTECTED", "keyword", 43),
    new AJTokenId("PUBLIC", "keyword", 44),
    new AJTokenId("RETURN", "keyword", 45),
    new AJTokenId("SHORT", "keyword", 46),
    new AJTokenId("STATIC", "keyword", 47),
    new AJTokenId("STRICTFP", "keyword", 48),
    new AJTokenId("SUPER", "keyword", 49),
    new AJTokenId("SWITCH", "keyword", 50),
    new AJTokenId("SYNCHRONIZED", "keyword", 51),
    new AJTokenId("THIS", "keyword", 52),
    new AJTokenId("THROW", "keyword", 53),
    new AJTokenId("THROWS", "keyword", 54),
    new AJTokenId("TRANSIENT", "keyword", 55),
    new AJTokenId("TRUE", "keyword", 56),
    new AJTokenId("TRY", "keyword", 57),
    new AJTokenId("VOID", "keyword", 58),
    new AJTokenId("VOLATILE", "keyword", 59),
    new AJTokenId("WHILE", "keyword", 60),
    new AJTokenId("ASPECT", "keyword", 61),
    new AJTokenId("AFTER", "keyword", 62),
    new AJTokenId("AROUND", "keyword", 63),
    new AJTokenId("BEFORE", "keyword", 64),
    new AJTokenId("POINTCUT", "keyword", 65),
    new AJTokenId("EXECUTION", "keyword", 66),
    new AJTokenId("RETURNING", "keyword", 67),
    new AJTokenId("PRIVILEGED", "keyword", 68),
    new AJTokenId("CALL", "keyword", 69),
    new AJTokenId("PERTARGET", "keyword", 70),
    new AJTokenId("PERTHIS", "keyword", 71),
    new AJTokenId("PERCFLOW", "keyword", 72),
    new AJTokenId("PERCFLOWBELOW", "keyword", 73),
    new AJTokenId("PERTYPEWITHIN", "keyword", 74),
    new AJTokenId("ISSINGLETON", "keyword", 75),
    new AJTokenId("THROWING", "keyword", 76),
    new AJTokenId("PROCEED", "keyword", 77),
    new AJTokenId("THISJOINPOINT", "keyword", 78),
    new AJTokenId("THISJOINPOINTSTATICPART", "keyword", 79),
    new AJTokenId("THISENCLOSINGJOINPOINTSTATICPART", "keyword", 80),
    new AJTokenId("DECLARE", "keyword", 81),
    new AJTokenId("PARENTS", "keyword", 82),
    new AJTokenId("WARNING", "keyword", 83),
    new AJTokenId("ERROR", "keyword", 84),
    new AJTokenId("SOFT", "keyword", 85),
    new AJTokenId("PRECEDENCE", "keyword", 86),
    new AJTokenId("_TYPE", "keyword", 87),
    new AJTokenId("_METHOD", "keyword", 88),
    new AJTokenId("_CONSTRUCTOR", "keyword", 89),
    new AJTokenId("_FIELD", "keyword", 90),
    new AJTokenId("INITIALIZATION", "keyword", 91),
    new AJTokenId("PREINITIALIZATION", "keyword", 92),
    new AJTokenId("STATICINITIALIZATION", "keyword", 93),
    new AJTokenId("GET", "keyword", 94),
    new AJTokenId("SET", "keyword", 95),
    new AJTokenId("HANDLER", "keyword", 96),
    new AJTokenId("ADVICEEXECUTION", "keyword", 97),
    new AJTokenId("WITHIN", "keyword", 98),
    new AJTokenId("WITHINCODE", "keyword", 99),
    new AJTokenId("CFLOW", "keyword", 100),
    new AJTokenId("CFLOWBELOW", "keyword", 101),
    new AJTokenId("TARGET", "keyword", 102),
    new AJTokenId("ARGS", "keyword", 103),
    new AJTokenId("_THIS", "keyword", 104),
    new AJTokenId("_TARGET", "keyword", 105),
    new AJTokenId("_ARGS", "keyword", 106),
    new AJTokenId("_WITHIN", "keyword", 107),
    new AJTokenId("_ANNOTATION", "keyword", 108),
    new AJTokenId("INTEGER_LITERAL", "literal", 109),
    new AJTokenId("DECIMAL_LITERAL", "literal", 110),
    new AJTokenId("HEX_LITERAL", "literal", 111),
    new AJTokenId("OCTAL_LITERAL", "literal", 112),
    new AJTokenId("FLOATING_POINT_LITERAL", "literal", 113),
    new AJTokenId("DECIMAL_FLOATING_POINT_LITERAL", "literal", 114),
    new AJTokenId("DECIMAL_EXPONENT", "number", 115),
    new AJTokenId("HEXADECIMAL_FLOATING_POINT_LITERAL", "literal", 116),
    new AJTokenId("HEXADECIMAL_EXPONENT", "number", 117),
    new AJTokenId("CHARACTER_LITERAL", "literal", 118),
    new AJTokenId("STRING_LITERAL", "literal", 119),
    new AJTokenId("IDENTIFIER", "identifier", 120),
    new AJTokenId("LETTER", "literal", 121),
    new AJTokenId("PART_LETTER", "literal", 122),
    new AJTokenId("LPAREN", "operator", 123),
    new AJTokenId("RPAREN", "operator", 124),
    new AJTokenId("LBRACE", "operator", 125),
    new AJTokenId("RBRACE", "operator", 126),
    new AJTokenId("LBRACKET", "operator", 127),
    new AJTokenId("RBRACKET", "operator", 128),
    new AJTokenId("SEMICOLON", "operator", 129),
    new AJTokenId("COMMA", "operator", 130),
    new AJTokenId("DOT", "operator", 131),
    new AJTokenId("AT", "operator", 132),
    new AJTokenId("ASSIGN", "operator", 133),
    new AJTokenId("LT", "operator", 134),
    new AJTokenId("BANG", "operator", 135),
    new AJTokenId("TILDE", "operator", 136),
    new AJTokenId("HOOK", "operator", 137),
    new AJTokenId("COLON", "operator", 138),
    new AJTokenId("EQ", "operator", 139),
    new AJTokenId("LE", "operator", 140),
    new AJTokenId("GE", "operator", 141),
    new AJTokenId("NE", "operator", 142),
    new AJTokenId("SC_OR", "operator", 143),
    new AJTokenId("SC_AND", "operator", 144),
    new AJTokenId("INCR", "operator", 145),
    new AJTokenId("DECR", "operator", 146),
    new AJTokenId("PLUS", "operator", 147),
    new AJTokenId("MINUS", "operator", 148),
    new AJTokenId("STAR", "operator", 149),
    new AJTokenId("SLASH", "operator", 150),
    new AJTokenId("BIT_AND", "operator", 151),
    new AJTokenId("BIT_OR", "operator", 152),
    new AJTokenId("XOR", "operator", 153),
    new AJTokenId("REM", "operator", 154),
    new AJTokenId("LSHIFT", "operator", 155),
    new AJTokenId("PLUSASSIGN", "operator", 156),
    new AJTokenId("MINUSASSIGN", "operator", 157),
    new AJTokenId("STARASSIGN", "operator", 158),
    new AJTokenId("SLASHASSIGN", "operator", 159),
    new AJTokenId("ANDASSIGN", "operator", 160),
    new AJTokenId("ORASSIGN", "operator", 161),
    new AJTokenId("XORASSIGN", "operator", 162),
    new AJTokenId("REMASSIGN", "operator", 163),
    new AJTokenId("LSHIFTASSIGN", "operator", 164),
    new AJTokenId("RSIGNEDSHIFTASSIGN", "operator", 165),
    new AJTokenId("RUNSIGNEDSHIFTASSIGN", "operator", 166),
    new AJTokenId("ELLIPSIS", "operator", 167),
    new AJTokenId("RUNSIGNEDSHIFT", "operator", 168),
    new AJTokenId("RSIGNEDSHIFT", "operator", 169),
    new AJTokenId("GT", "operator", 170)            
        });
        idToToken = new HashMap<Integer, AJTokenId>();
        for (AJTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized AJTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }
    
    @Override
    protected Collection<AJTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected Lexer<AJTokenId> createLexer(LexerRestartInfo<AJTokenId> info) {
        return new AJLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-aj";
    }
     
}
