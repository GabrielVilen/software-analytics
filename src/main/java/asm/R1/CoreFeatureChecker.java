package asm.R1;

import java.io.*;
import java.nio.file.*;
import java.text.Collator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gabriel on 2017-11-02.
 */
public class CoreFeatureChecker {

    private final Path path;
    private List<String> coreMethods;
    private TreeMap<String, String> lineCountMap = new TreeMap<>(Collator.getInstance());

    public CoreFeatureChecker(Path path) {
        this.path = path;

        TreeMap<String, File> fileMap = new TreeMap<>(Collator.getInstance());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path p : stream) {
                fileMap.put(p.toString(), p.toFile());
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
        Iterator<Map.Entry<String, File>> iterator = fileMap.entrySet().iterator();
        coreMethods = DeprecatedVersionRemovalChecker.getLines(iterator.next().getValue());

        while (iterator.hasNext()) {
            countCoreFeatures(iterator.next().getValue());
        }

        printMap(lineCountMap);
    }

    private void printMap(Map<String, String> lineCountMap) {
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("RQs_result/1.1/" + path.getFileName() + "-TOTAL-core-res.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.setOut(out);

        for (Map.Entry<String, String> entry : lineCountMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue()); //
        }
    }

    private void countCoreFeatures(File file) {
        List<String> cmpLines = DeprecatedVersionRemovalChecker.getLines(file);
        int coreMethodsCount = 0;
        int totalMethodsCount = 0;

        for (String coreMethod : coreMethods) {
            // check if file contains the core method
            if (cmpLines.contains(coreMethod)) {
                coreMethodsCount++;
            }
            totalMethodsCount++;
        }

        lineCountMap.put(file.getName(), coreMethodsCount + "/" + totalMethodsCount);
    }

    public static void main(String [] args ) {
        new CoreFeatureChecker(Paths.get(args[0]));
    }
}
