
package asm.R1;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.repodriller.persistence.PersistenceMechanism;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ASMClassReader{


    private int nonDepMethods, depMethods;
    private PersistenceMechanism writer;

    public ASMClassReader(PersistenceMechanism writer) {
        this.writer = writer;
    }

    public ASMAPIClass readClass(String path) {
        try {
            FileInputStream stream = new FileInputStream(path);
            ClassReader reader = new ClassReader(stream);
            ASMClassVisitor visitor = new ASMClassVisitor(Opcodes.ASM5);
            reader.accept(visitor, 0);
            stream.close();
            return visitor.getProjectClass();
        } catch (FileNotFoundException e) {
          //  System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
          //  System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void writeMethods(List<ASMMethodDecl> methodDeclarations) {
        for (ASMMethodDecl m : methodDeclarations) {
            if (m.getIsDeprecated()) { // TODO: check only public
                depMethods++;
                writer.write(m.toString());
            } else {
                nonDepMethods++;
            }
        }
    }


    public void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                try {
                    if(file.getName().contains(".class")) {
                        System.out.println("File: " + file.getName());
                        ASMAPIClass asmapiClass = readClass(file.getPath());
                        // mapMethods.put(file, asmapiClass.getMethodDeclarations());
                        writeMethods(asmapiClass.getMethodDeclarations());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public int getNonDepMethods() {
        return nonDepMethods;
    }

    public void setNonDepMethods(int nonDepMethods) {
        this.nonDepMethods = nonDepMethods;
    }

    public int getDepMethods() {
        return depMethods;
    }

    public void setDepMethods(int depMethods) {
        this.depMethods = depMethods;
    }
}