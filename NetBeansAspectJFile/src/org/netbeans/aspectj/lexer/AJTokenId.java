package org.netbeans.aspectj.lexer;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.Language;
/**
 * @author stephanie.hammond
 */
public class AJTokenId implements TokenId {
    private final String name;
    private final String primaryCategory;
    private final int id;
    
     AJTokenId(
            String name,
            String primaryCategory,
            int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }
     
    public static Language<AJTokenId> getLanguage() {
        return new AJLanguageHierarchy().language();
    }     
     
    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }
    
}
