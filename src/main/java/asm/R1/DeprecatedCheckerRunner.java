package asm.R1;

import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Created by gabriel on 2017-10-05.
 */
public class DeprecatedCheckerRunner {

    private static final String RQS_RESULT = "RQs_result/1.1/";

    public static void main(String[] args) {

        Path dir = Paths.get(args[0]);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {
                System.out.println(file.getFileName());

                PersistenceMechanism writer = new CSVFile(RQS_RESULT + file.getFileName() + ".csv");

                File[] files = file.toFile().listFiles();
                ASMClassReader reader = new ASMClassReader(writer);
                reader.showFiles(files);

                // reader.writeMethods(asmapiClass.getMethodDeclarations());

                String toWrite = file.getFileName() + ": dep methods = " + reader.getDepMethods() + " / " + reader.getNonDepMethods();
                System.out.println(toWrite);
                System.out.println();
                writer.write(toWrite);
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
    }
}
