package asm.R1;

import java.util.ArrayList;
import java.util.List;

public class ASMAPIClass {
    private String className;
    private boolean isDeprecated;
    private List<ASMMethodDecl> methodDeclarations;
    private boolean isAnnotation;
    private boolean hasDeprecationAnnotation;
    private int bytecodeVersion;
    private String path;

    public ASMAPIClass(String className, boolean isDeprecated, List<ASMMethodDecl> methodDeclarations, boolean isAnnotation, boolean hasDeprecationAnnotation, int bytecodeVersion, String path) {
        this.className = className;
        this.isDeprecated = isDeprecated;
        this.methodDeclarations = methodDeclarations;
        this.isAnnotation = isAnnotation;
        this.hasDeprecationAnnotation = hasDeprecationAnnotation;
        this.bytecodeVersion = bytecodeVersion;
        this.path = path;
    }

    public ASMAPIClass(String path) {
        this.className = "";
        this.isAnnotation = false;
        this.isDeprecated = false;
        this.methodDeclarations = new ArrayList<ASMMethodDecl>();
        this.hasDeprecationAnnotation = false;
        this.bytecodeVersion = 0;
        this.path = path;
    }

    public String getClassName() {
        return className;
    }

    public boolean getIsDeprecated() {
        return isDeprecated;
    }

    public List<ASMMethodDecl> getMethodDeclarations() {
        return methodDeclarations;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean getIsAnnotation() {
        return this.isAnnotation;
    }

    public String toString() {
        return className;
    }

    public boolean getHasDeprecationAnnotation() {
        return hasDeprecationAnnotation;
    }

    public int getBytecodeVersion() {
        return bytecodeVersion;
    }

    public void setBytecodeVersion(int bytecodeVersion) {
        this.bytecodeVersion = bytecodeVersion;
    }
}