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

    private Map<String, String> depMap = new HashMap<>();

    public static void main(String[] args) {
        CheckMethodsRunner runner = new CheckMethodsRunner();

        Path dir = Paths.get(args[0]);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            int i = 0;
            for (Path file : stream) {
              //  File[] fileMap = file.toFile().listFiles();
              //  assert fileMap != null;
               // for (int i = 0; i < fileMap.length; i++) {
                        runner.add(file);
                       // runner.compare(file, i);
                       // i++;
               // }
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x);
        }
        runner.compare();
    }

    private void compare() {
        for (Map.Entry<String,File> entry : fileMap.entrySet()) {
            compare(entry.getValue());
        }
        printMap();
    }

    TreeMap<String, File> fileMap = new TreeMap<>(Collator.getInstance());
    //private List<File> fileMap = new ArrayList<>();

    private void add(Path path) {
        fileMap.put(path.toString(), path.toFile());
    }

    private void printMap() {
        System.out.println("\nResults:");
        for (Map.Entry<String, String> line : depMap.entrySet()) {
            System.out.println(line.getKey() + " : " + line.getValue());

        }
    }

    private List<String> nonDepLines;

    private void compare(File cmpFile) {
        if(!cmpFile.isFile()) return;
       // System.out.println("comparing " + cmpFile);

        List<String> cmpLines = getLines(cmpFile);

        if(nonDepLines == null) {
            nonDepLines = new ArrayList<>();
            nonDepLines = cmpLines;
            System.out.println(cmpFile.getPath() + ": Added init lines " + nonDepLines);
            return;
        }

        for (Iterator<String> iterator = nonDepLines.iterator(); iterator.hasNext(); ) {
            String line = iterator.next();
          //  System.out.println("cmp line " + line);
            if(cmpLines.contains(line)) {
                iterator.remove();
                //nonDepLines.remove(line);
                depMap.put(line, cmpFile.getName());
              //  System.out.println("Found deprecated method '" + line + "' in " + cmpFile.getName());
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
