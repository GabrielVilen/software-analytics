package asm.R1;

import java.io.Serializable;

public class ASMMethodDecl implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 6519273949227618982L;
    private String className;
    private String methodName;
    private Boolean isDeprecated;
    private Boolean hasDeprecatedAnnotation;

    public ASMMethodDecl(String className, String methodName, Boolean isDeprecated, Boolean hasDeprecatedAnnotation) {
        this.className = className;
        this.methodName = methodName;
        this.isDeprecated = isDeprecated;
        this.hasDeprecatedAnnotation = hasDeprecatedAnnotation;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String toString() {
        return className + " " + methodName + " " + isDeprecated;
    }

    public Boolean getIsDeprecated() {
        return isDeprecated;
    }

    public void setIsDeprecated(Boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
    }

    public Boolean getHasDeprecatedAnnotation() {
        return hasDeprecatedAnnotation;
    }

    public void setHasDeprecatedAnnotation(Boolean hasDeprecatedAnnotation) {
        this.hasDeprecatedAnnotation = hasDeprecatedAnnotation;
    }

}