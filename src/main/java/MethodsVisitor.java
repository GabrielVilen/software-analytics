import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.repodriller.persistence.PersistenceMechanism;

public class MethodsVisitor extends ASTVisitor {

    private final PersistenceMechanism writer;
    private int deprecated = 0;
    private int nonDeprecated = 0;

    public MethodsVisitor(PersistenceMechanism writer) {
        this.writer = writer;
    }

    /**
     * Checks if method is deprecated and if so writes it
     */
    @Override
    public boolean visit(MethodDeclaration node) {

        Javadoc doc = node.getJavadoc();
        if(doc != null) {
           if(doc.toString().contains(TagElement.TAG_DEPRECATED)) {
               System.out.println("Found deprecated method");

               writer.write(node.toString());
               deprecated++;
           } else {
               nonDeprecated++;
           }
        }

        return super.visit(node);
    }

    public int getDeprecated() {
        return deprecated;
    }

    public int getNonDeprecated() {
        return nonDeprecated;
    }
}