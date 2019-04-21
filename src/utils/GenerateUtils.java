package utils;

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.xml.XmlFile;
import java.util.Objects;
import java.util.Optional;

/**
 * @creed: Here be dragons !
 * @author: EzioQAQ
 * @Time: 2019-04-15 14:45
 */
public class GenerateUtils {

    public static PsiDirectory genFolder(PsiDirectory directory, String folderName) {
        return Optional.ofNullable(directory.findSubdirectory(folderName))
            .orElseGet(() -> directory.createSubdirectory(folderName));
    }

    public static PsiClass genInterface(PsiDirectory directory, String className, String annotation) {

        return Optional.ofNullable(directory.findFile(className + ".java"))
            .map(psiFile -> {
                    PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
                    return psiJavaFile.getClasses()[0];
                }
            )
            .orElseGet(() -> {
                PsiClass anInterface = JavaDirectoryService.getInstance().createInterface(directory, className);
                genAnnotation(anInterface, annotation);
                return anInterface;
            });
    }

    public static PsiClass genClass(PsiDirectory directory, String className, String annotation) {
        return genClass(directory, className, annotation, null);
    }

    public static PsiClass genClass(PsiDirectory directory, String className, String annotation, PsiClass implClass) {

        return Optional.ofNullable(directory.findFile(className + ".java"))
            .map(psiFile -> {
                    PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
                    return psiJavaFile.getClasses()[0];
                }
            )
            .orElseGet(() -> {
                PsiClass aClass = JavaDirectoryService.getInstance().createClass(directory, className);
                genAnnotation(aClass, annotation);
                Optional.ofNullable(implClass).ifPresent(psiElement -> {
                    PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(aClass.getProject()).getElementFactory();
                    // import和impl
                    PsiImportStatement importStatement = psiElementFactory.createImportStatement(implClass);
                    PsiJavaCodeReferenceElement referenceElement = psiElementFactory.createClassReferenceElement(implClass);
                    Objects.requireNonNull(((PsiJavaFile) aClass.getContainingFile()).getImportList()).add(importStatement);
                    Objects.requireNonNull(aClass.getImplementsList()).add(referenceElement);
                });

                return aClass;
            });
    }

    private static void genAnnotation(PsiClass psiClass, String annotation) {

        if (annotation != null && annotation.length() > 0) {
            PsiModifierList modifierList = psiClass.getModifierList();
            assert modifierList != null;
            modifierList.addAnnotation(annotation);
        }
    }

    public static XmlFile genXML(PsiDirectory directory, String xmlName, PsiClass psiClass) {

        return Optional.ofNullable((XmlFile) directory.findFile(xmlName + ".xml"))
            .orElseGet(() -> {
                String xmlDefault = getXmlDefaultContent(psiClass.getQualifiedName());
                XmlFile xmlFile = (XmlFile) PsiFileFactory.getInstance(directory.getProject())
                    .createFileFromText(xmlName + ".xml", StdFileTypes.XML, xmlDefault);
                directory.add(xmlFile);
                return xmlFile;
            });
    }

    static String getXmlDefaultContent(String packagePath) {
        return ("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
            + "<!DOCTYPE mapper\n"
            + "    PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n"
            + "<mapper namespace=\"{packagePath}\">\n"
            + "</mapper>").replace("{packagePath}", packagePath);
    }
}
