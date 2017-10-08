package repodriller.R1_3;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.repodriller.persistence.PersistenceMechanism;

public class DeprecatedMethodsVisitor extends ASTVisitor {

    private final PersistenceMechanism writer;
    private int deprecated = 0;
    private int nonDeprecated = 0;
    private static List<String> deprecatedMethods = new ArrayList<String>();
    private static List<String> nonDeprecatedMethods = new ArrayList<String>();

    public DeprecatedMethodsVisitor(PersistenceMechanism writer) {
        this.writer = writer;
    }

    /**
     * Checks if method is deprecated and if so writes it
     */
    @Override
    public boolean visit(MethodDeclaration node) {
//  System.out.println(node.modifiers().get(0).getClass());
        Javadoc doc = node.getJavadoc();
        if(doc != null) {
           if(doc.toString().contains(TagElement.TAG_DEPRECATED)) {
               for (Object m : node.modifiers()) {
                   if(m instanceof Modifier && ((Modifier) m).isPublic()) {
                       System.out.println("Found deprecated method");

                       String deprecatedmethod = node.toString().split("@Deprecated ")[1];
                       if(!deprecatedMethods.contains(deprecatedmethod)) {
                    	   deprecatedMethods.add(deprecatedmethod);
                    	   writer.write(deprecatedmethod);                   
                    	   deprecated++;
                       }
                   }
               }

           } else {
        	   String nondeprecatedmethod = node.toString().split("[*]/")[1];
        	   if(!nonDeprecatedMethods.contains(nondeprecatedmethod)) {
        		   nonDeprecatedMethods.add(nondeprecatedmethod);
        		   nonDeprecated++;
        	   }
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