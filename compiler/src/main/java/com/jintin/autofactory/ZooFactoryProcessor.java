package com.jintin.autofactory;

import com.squareup.javapoet.ClassName;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes({"com.jintin.autofactory.ZooFactory", "com.jintin.autofactory.ZooElement"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ZooFactoryProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<ClassName, List<ElementInfo>> result = new HashMap<>();
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ZooFactory.class)) {
            if (annotatedElement.getKind() != ElementKind.INTERFACE) {
                error("Only interfaces can be annotated with @ZooFactory", annotatedElement);
                return true;
            }
            TypeElement typeElement = (TypeElement) annotatedElement;
            ClassName className = ClassName.get(typeElement);
            result.putIfAbsent(className, new ArrayList<>());
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ZooElement.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error("Only classes can be annotated with @ZooElement", annotatedElement);
                return true;
            }
            ZooElement zooElement = annotatedElement.getAnnotation(ZooElement.class);
            TypeElement typeElement = (TypeElement) annotatedElement;
            ClassName className = ClassName.get(typeElement);
            List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
            for (TypeMirror typeMirror : interfaces) {
                ClassName typeName = getName(typeMirror);
                if (result.containsKey(typeName)) {
                    result.get(typeName).add(new ElementInfo(zooElement.value(), className));
                    break;
                }
            }
        }

        try {
            new ZooFactoryBuilder(filer, result).generate();
        } catch (IOException e) {
            error("Failed to generate factory class: " + e.getMessage());
        }

        return true;
    }

    private ClassName getName(TypeMirror typeMirror) {
        String rawString = typeMirror.toString();
        int dotPosition = rawString.lastIndexOf('.');
        String packageName = rawString.substring(0, dotPosition);
        String className = rawString.substring(dotPosition + 1);
        return ClassName.get(packageName, className);
    }

    private void error(String message, Element element) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }
}
