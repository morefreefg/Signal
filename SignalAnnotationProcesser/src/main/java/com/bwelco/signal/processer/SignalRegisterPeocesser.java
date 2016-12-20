package com.bwelco.signal.processer;

import com.bwelco.signal.SignalReceiver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by bwelco on 2016/12/19.
 */

public class SignalRegisterPeocesser extends AbstractProcessor {

    Elements elementUtils;

    private final HashMap<TypeElement, ExecutableElement> methodsByClass = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<String>();
        ret.add("com.bwelco.signal.SignalReceiver");
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Messager messager = processingEnv.getMessager();

        Set<? extends Element> annotations =
                roundEnvironment.getElementsAnnotatedWith(SignalReceiver.class);

        for (Element element : annotations) {

            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) element;
                if (checkHasNoErrors(method, messager)) {
                    String methodName = method.getSimpleName().toString();
                    TypeElement targetClass = (TypeElement) method.getEnclosingElement();
                    methodsByClass.put(targetClass, method);

                    messager.printMessage(Diagnostic.Kind.WARNING, "className = " +
                            targetClass.getSimpleName(), element);
                    messager.printMessage(Diagnostic.Kind.WARNING, "methodName = " +
                            methodName, element);
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "@Subscribe is only valid for methods", element);
            }
//            if (element instanceof ExecutableElement) {
//                ExecutableElement method = (ExecutableElement) element;
//                if (checkHasNoErrors(method, messager)) {
//                    TypeElement classElement = (TypeElement) method.getEnclosingElement();
//                    messager.printMessage(Diagnostic.Kind.WARNING, "simplenametest = " +
//                            classElement.getSimpleName(), element);
//                } else {
//                    messager.printMessage(Diagnostic.Kind.ERROR, "@Subscribe is only valid for methods", element);
//                }
//            }
        }

        return true;
    }

    private boolean checkHasNoErrors(ExecutableElement element, Messager messager) {
        if (element.getModifiers().contains(Modifier.STATIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "SignalRegister method must not be static", element);
            return false;
        }

        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "SignalRegister method must be public", element);
            return false;
        }

        return true;
    }
}
