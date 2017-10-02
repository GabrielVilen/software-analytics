import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRemoteRepository;

public class MyStudy implements Study {

    final static String REPO_URL = "https://github.com/hibernate/hibernate-orm"; // "https://github.com/spring-projects/spring-framework";

    public static void main(String[] args) {
     //   BasicConfigurator.configure(); // not needed with log4j.properties
        new RepoDriller().start(new MyStudy());
    }

    @Override
    public void execute() {
        System.out.println("MyStudy.execute");
        JavaParserVisitor parserVisitor = new JavaParserVisitor();
        new RepositoryMining()
                .in(GitRemoteRepository.singleProject(REPO_URL))
                .through(Commits.all())
                .withThreads(4)
                .process(parserVisitor, new CSVFile("test.csv"))
                .mine();

        System.out.println("Total percentage: " + parserVisitor.getTotalDeprecated() + " / " + parserVisitor.getTotalNonDeprecated());
        parserVisitor.write("Total percentage: " + parserVisitor.getTotalDeprecated()/parserVisitor.getTotalNonDeprecated());
        System.out.println("done!");
    }
}