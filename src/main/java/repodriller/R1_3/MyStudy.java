package repodriller.R1_3;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class MyStudy implements Study {

    private static String REPO_URL = ""; // give as arg[0]
    private static String DEST = "RQs_result/1.1/";

    public static void main(String[] args) {
        if(args.length > 0) {
            REPO_URL = args[0];
            DEST += REPO_URL.split("/");
          //  DEST += s[s.length];
        }
     //   BasicConfigurator.configure(); // not needed with log4j.properties
        new RepoDriller().start(new MyStudy());
    }

    @Override
    public void execute() {
        System.out.println("MyStudy.execute");
        DeprecatedJavaParserVisitor parserVisitor = new DeprecatedJavaParserVisitor();
        new RepositoryMining()
                .in(GitRepository.singleProject(REPO_URL))
                .through(Commits.all())
                .withThreads(4)
                .process(parserVisitor, new CSVFile(DEST +".csv"))
                .mine();

        System.out.println("Total percentage: " + parserVisitor.getTotalDeprecated() + " / " + parserVisitor.getTotalNonDeprecated());
        parserVisitor.write("Total percentage: " + parserVisitor.getTotalDeprecated()/parserVisitor.getTotalNonDeprecated());
        System.out.println("done!");
    }
}