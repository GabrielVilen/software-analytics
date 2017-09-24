import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRemoteRepository;

public class MyStudy implements Study {

    final static String REPO_URL = "https://github.com/spring-projects/spring-framework";

    public static void main(String[] args) {
     //   BasicConfigurator.configure(); // not needed with log4j.properties
        new RepoDriller().start(new MyStudy());
    }

    @Override
    public void execute() {
        System.out.println("MyStudy.execute");
        new RepositoryMining()
                .in(GitRemoteRepository.singleProject(REPO_URL))
                .through(Commits.all())
                .process(new JavaParserVisitor(), new CSVFile("test.csv"))
                .mine();

        System.out.println("done!");
    }
}