package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by bwelco on 2016/12/4.
 */

@SupportedAnnotationTypes({"Seriable"})
public class BeanProcesser extends AbstractProcessor {

    Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        // 元素操作的辅助类
        elementUtils = processingEnv.getElementUtils();

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elements = roundEnv
                .getElementsAnnotatedWith(Seriable.class);

        TypeElement classElement = null;
        ArrayList<VariableElement> fieldsElement = new ArrayList<VariableElement>();

        HashMap<String, ArrayList<VariableElement>> map = new HashMap<String, ArrayList<VariableElement>>();
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                classElement = (TypeElement) element;
                map.put(classElement.getQualifiedName().toString(), fieldsElement = new ArrayList<VariableElement>());
            } else if (element.getKind() == ElementKind.FIELD) {

                VariableElement variableElement = (VariableElement) element;
                TypeElement enclosingElement = (TypeElement) variableElement
                        .getEnclosingElement();
                String key = enclosingElement.getQualifiedName().toString();

                if (map.containsKey(key)) {
                    map.get(key).add(variableElement);
                } else {
                    map.put(key, fieldsElement = new ArrayList<VariableElement>());
                }
            }
        }


        for (String key : map.keySet()) {
            if (map.get(key).size() == 0) {
                TypeElement typeElement = elementUtils.getTypeElement(key);
                List<? extends Element> allMembers = elementUtils
                        .getAllMembers(typeElement);
                if (allMembers.size() > 0) {
                    map.get(key).addAll(ElementFilter.fieldsIn(allMembers));
                }
            }
        }


//        Gson gson = new Gson();
//        String json = gson.toJson(map);
        String json = "admin";
        File file = new File("f://apt_test.json");

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(json);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Annotation JSON : " + json);


        return true;
    }


}
