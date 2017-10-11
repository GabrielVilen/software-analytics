package asm.R1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.text.Collator;
import java.util.*;

/**
 * Created by gabriel on 2017-10-05.
 */
public class CheckMethodsRunner {

    public static void main(String[] args) {
        new CheckMethodsRunner(Paths.get(args[0]));
    }

    private TreeMap<String, Integer> lineCountMap;
    private List<String> initLines;

    public CheckMethodsRunner(Path path) {
        TreeMap<String, File> fileMap = new TreeMap<>(Collator.getInstance());
        lineCountMap = new TreeMap<>(Collator.getInstance());
        initLines = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path p : stream) {
                fileMap.put(p.toString(), p.toFile());
                lineCountMap.put(p.toFile().getName(), 0);
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }

        int currCount = 0;
        while (!fileMap.isEmpty()) {
            boolean hasRemovedFirst = false;
            for (Iterator<Map.Entry<String, File>> iterator = fileMap.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, File> fileEntry = iterator.next();

                checkForDeprecation(fileEntry.getValue());

                if (!hasRemovedFirst) {
                    System.out.println("Removed " + fileEntry.getKey());
                    iterator.remove();
                    hasRemovedFirst = true;
                }
            }
            resetMap(lineCountMap, currCount);
            initLines.clear();
            currCount++;
        }
    }

    private void resetMap(Map<String, Integer> lineCountMap, int currCount) {
        System.out.println("\nResults: ");
        int i = 0;
        for (Map.Entry<String, Integer> entry : lineCountMap.entrySet()) {
       //     if(i > currCount)
                System.out.println(entry.getValue()); // line.getKey() + " : " + line.getValue()

            entry.setValue(0);  // reset count to -1
            i++;
        }
        System.out.println("-----------------------------------");
    }

    private void checkForDeprecation(File file) {
        List<String> cmpLines = getLines(file);

        if(initLines.isEmpty()) {
            initLines = cmpLines;
            System.out.println(file.getPath() + ": Added init lines " + initLines);
            return;
        }

        for (Iterator<String> iterator = initLines.iterator(); iterator.hasNext(); ) {
            String currLine = iterator.next();

            // if the dep method is not in this api it has been removed before release
            if(!cmpLines.contains(currLine)) {
                int count = lineCountMap.get(file.getName());
                lineCountMap.put(file.getName(), count+1);
                iterator.remove();
            }
        }
    }
    private List<String> getLines(File file) {

        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
