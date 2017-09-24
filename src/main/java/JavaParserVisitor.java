import br.com.metricminer2.parser.jdt.JDTRunner;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.io.ByteArrayInputStream;


public class JavaParserVisitor implements CommitVisitor {

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {

        for (Modification m : commit.getModifications()) {

            if (m.wasDeleted()) continue;

            MethodsVisitor visitor = new MethodsVisitor(writer);
            new JDTRunner().visit(visitor, new ByteArrayInputStream(m.getSourceCode().getBytes()));

          //  writer.write("\nFound " + visitor.getQty() + " methods in " + repo.getOrigin());

        }

    }

    @Override
    public String name() {
        return "java-parser";
    }
}
