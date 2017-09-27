import br.com.metricminer2.parser.jdt.JDTRunner;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.io.ByteArrayInputStream;


public class JavaParserVisitor implements CommitVisitor {

    private int totalDeprecated = 0;
    private int totalNonDeprecated = 0;
    private PersistenceMechanism writer;

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        this.writer = writer;
        MethodsVisitor visitor = new MethodsVisitor(writer);

        for (Modification m : commit.getModifications()) {

            if (m.wasDeleted()) continue;
            new JDTRunner().visit(visitor, new ByteArrayInputStream(m.getSourceCode().getBytes()));
        }
        totalDeprecated += visitor.getDeprecated();
        totalNonDeprecated += visitor.getNonDeprecated();

      //  write("deprecated methods: " + totalDeprecated + "/" + totalNonDeprecated);

    }

    public void write(String toWrite) {
        writer.write(toWrite);
    }

    public int getTotalDeprecated() {
        return totalDeprecated;
    }

    public int getTotalNonDeprecated() {
        return totalNonDeprecated;
    }

    @Override
    public String name() {
        return "java-parser";
    }
}
