package asm.R1;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class ASMClassVisitor extends ClassVisitor {

    private List<ASMMethodDecl> methods;
    private String className;
    private boolean isDeprecated;
    private boolean isAnnotation;
    private boolean hasdeprecatedAnnotation;

    public ASMClassVisitor(int arg0) {
        super(arg0);
        this.methods = new ArrayList<ASMMethodDecl>();
        this.isDeprecated = false;
        this.hasdeprecatedAnnotation = false;
    }

    /**
     * Called when a class is visited. This is the method called first
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName,
                      String[] interfaces) {
        this.className = name;
        this.isAnnotation = false;
        String accessString = Integer.toBinaryString(access);
        if (accessString.length() >= 17) {
            if (accessString.charAt(17) == '1') {
                this.isDeprecated = true;
            }
        }
        for (String s : interfaces) {
            if (s.equals("java/lang/annotation/Annotation")) {
                this.isAnnotation = true;
                break;
            }
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.contains("Deprecated")) {
            this.hasdeprecatedAnnotation = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
                                     String[] exceptions) {
        boolean isPrivate = true;
        boolean isDeprecated = false;

        String s = Integer.toBinaryString(access);

        if (s.length() >=1 ) {
            if (s.charAt(0) == '1') {
                isPrivate = false;
            }
        }
        if (!isPrivate) {
            if (s.length() >= 17) {
                if (s.charAt(17) == '1') {
                    isDeprecated = true;

                }
            }
            if(this.isDeprecated == true) {
                isDeprecated = true;
            }
            ASMMethodDecl methodDecl = new ASMMethodDecl(this.className, name, isDeprecated, false);
            this.methods.add(methodDecl);

        }
        MethodVisitor v = new MethodVisitor(Opcodes.ASM5) {

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (desc.contains("Deprecated")) {
                    for (ASMMethodDecl m : methods) {
                        if (m.getMethodName().equals(name)) {
                            m.setHasDeprecatedAnnotation(true);
                        }
                    }
                }
                return super.visitAnnotation(desc, visible);
            }
        };
        return v;
    }

    public ASMAPIClass getProjectClass() {
        return new ASMAPIClass(this.className, this.isDeprecated, this.methods, this.isAnnotation, this.hasdeprecatedAnnotation, 0,
                null);
    }
}