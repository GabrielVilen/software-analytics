
package asm.R1;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ASMClassReader {

    public ASMAPIClass readClass(String path) {
        try {
            FileInputStream stream = new FileInputStream(path);
            ClassReader reader = new ClassReader(stream);
            ASMClassVisitor visitor = new ASMClassVisitor(Opcodes.ASM5);
            reader.accept(visitor, 0);
            stream.close();
            return visitor.getProjectClass();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}