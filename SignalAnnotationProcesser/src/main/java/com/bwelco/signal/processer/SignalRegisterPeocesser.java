package com.bwelco.signal.processer;

import com.bwelco.signal.SignalPackage.ThreadMode;
import com.bwelco.signal.SignalReceiver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by bwelco on 2016/12/19.
 */

public class SignalRegisterPeocesser extends AbstractProcessor {

    ProcessingEnvironment processingEnv;
    Element element;

    class PendingTargetInfo {
        TypeElement targetClass;
        ExecutableElement method;
        ThreadMode threadMode;

        public PendingTargetInfo(TypeElement targetClass, ExecutableElement method, ThreadMode threadMode) {
            this.targetClass = targetClass;
            this.method = method;
            this.threadMode = threadMode;
        }
    }

    private final LinkedList<PendingTargetInfo> methodInfos = new LinkedList<PendingTargetInfo>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnv = processingEnvironment;
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

                    SignalReceiver subscribe = method.getAnnotation(SignalReceiver.class);

                    methodInfos.add(new PendingTargetInfo(targetClass, method, subscribe.threadMode()));
                    this.element = method;
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "@Subscribe is only valid for methods", element);
            }
        }

        writeJavaCode();

        return true;
    }

    private void writeJavaCode() {
        BufferedWriter writer;
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile("MySignalIndex");
            writer = new BufferedWriter(jfo.openWriter());

            StringBuffer sb = new StringBuffer("");

            sb.append("package com.bwelco.signalsperf;\n" +
                    "\n" +
                    "import com.bwelco.signal.MethodFinder.GetRegisterInfoInterface;\n" +
                    "import com.bwelco.signal.MethodFinder.IndexMethodInfo;\n" +
                    "import com.bwelco.signal.SignalPackage.RegisterMethodInfo;\n" +
                    "import com.bwelco.signal.SignalPackage.ThreadMode;\n" +
                    "\n" +
                    "import java.lang.reflect.Method;\n" +
                    "import java.util.HashMap;\n" +
                    "import java.util.LinkedList;\n" +
                    "import java.util.List;\n" +
                    "import java.util.Map;\n" +
                    "\n" +
                    "/**\n" +
                    " * This code is generated by Signal, do not edit.\n" +
                    " */\n" +
                    "\n" +
                    "public class MySignalIndex implements GetRegisterInfoInterface{\n" +
                    "\n" +
                    "    private static Map<Class<?>, List<RegisterMethodInfo>> INDEX_MAP = null;\n" +
                    "\n" +
                    "    static {\n" +
                    "        INDEX_MAP = new HashMap<Class<?>, List<RegisterMethodInfo>>();");

            sb.append(writeMapPut());

            sb.append("\n    }\n\n" +
                    "\n" +
                            "    private static RegisterMethodInfo putIndex(IndexMethodInfo indexMethodInfo) {\n" +
                            "        RegisterMethodInfo registerMethodInfo = null;\n" +
                            "        try {\n" +
                            "            Method method = indexMethodInfo.getTargetClass().getDeclaredMethod(\n" +
                            "                    indexMethodInfo.getMethodName(), indexMethodInfo.getParams());\n" +
                            "            registerMethodInfo = new RegisterMethodInfo(indexMethodInfo.getMethodName(),\n" +
                            "                    indexMethodInfo.getThreadMode(), method, indexMethodInfo.getParams());\n" +
                            "        } catch (NoSuchMethodException e) {\n" +
                            "            e.printStackTrace();\n" +
                            "        }\n" +
                            "        return registerMethodInfo;\n" +
                            "    }\n" +
                            "\n" +
                            "    @Override\n" +
                            "    public List<RegisterMethodInfo> getRegisterByClass(Class<?> clazz) {\n" +
                            "        return INDEX_MAP.get(clazz);\n" +
                            "    }\n");
            sb.append("\n\n}\n");
            writer.write(sb.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String writeMapPut() {
        // private final HashMap<TypeElement, ExecutableElement> methodsByClass = new HashMap<>();
        StringBuffer sb = new StringBuffer("");

        for (PendingTargetInfo pendingTargetInfo : methodInfos) {
            String targetClassName = pendingTargetInfo.targetClass.getQualifiedName() + ".class";
            String methodName = pendingTargetInfo.method.getSimpleName().toString();
            List<? extends VariableElement> paramElements = pendingTargetInfo.method.getParameters();

            //  processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "length = " + paramElements.size(), element);

            String[] params = new String[paramElements.size()];
            for (int i = 0; i < paramElements.size(); i++) {
                VariableElement variableElement = paramElements.get(i);
                TypeMirror typeMirror = getParamTypeMirror(variableElement, processingEnv.getMessager());
                params[i] = getType(typeMirror);
            }

            sb.append(writePutIndex(targetClassName, methodName, params, pendingTargetInfo.threadMode));
        }

        return sb.toString();
    }

    private String writePutIndex(String targetClassName, String methodName, String[] params, ThreadMode threadMode) {
        StringBuffer sb = new StringBuffer("");
        sb.append("\n        if (INDEX_MAP.containsKey(");
        sb.append(targetClassName);
        sb.append(")){\n");
        sb.append("            INDEX_MAP.get(");
        sb.append(targetClassName);
        sb.append(").add(\n                    ");

        sb.append(addPutIndexParam(methodName, threadMode, targetClassName, params));
        sb.append(");\n");
        sb.append("        } else {\n" +
                "            List<RegisterMethodInfo> list = new LinkedList<RegisterMethodInfo>();\n" +
                "            list.add(");
        sb.append(addPutIndexParam(methodName, threadMode, targetClassName, params));
        sb.append(");\n" +
                "            INDEX_MAP.put(");
        sb.append(targetClassName);
        sb.append(", list);\n");
        sb.append("        }\n");
        return sb.toString();

    }

    private String addPutIndexParam(String methodName, ThreadMode threadMode, String classname, String[] params) {
        StringBuffer sb = new StringBuffer("");
        sb.append("putIndex(new IndexMethodInfo(\n");
        sb.append("                    \"" + methodName + "\", " + getThreadMode(threadMode) + ",\n");
        sb.append("                    " + classname + ", \n");

        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            sb.append("                    " + param);

            if (i != (params.length - 1)) {
                sb.append(", \n");
            }
        }

        sb.append("            ))");

        return sb.toString();

    }

    private String getThreadMode(ThreadMode threadMode) {
        switch (threadMode) {
            case MAINTHREAD:
                return "ThreadMode.MAINTHREAD";

            case ASYNC:
                return "ThreadMode.ASYNC";

            case BACKGROUND:
                return "ThreadMode.BACKGROUND";

            case POSTERTHREAD:
                return "ThreadMode.POSTERTHREAD";

            default:
                return "";
        }
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


    // 获取元素参数类型
    private TypeMirror getParamTypeMirror(VariableElement param, Messager messager) {
        TypeMirror typeMirror = param.asType();
        // Check for generic type
        if (typeMirror instanceof TypeVariable) {
            TypeMirror upperBound = ((TypeVariable) typeMirror).getUpperBound();
            if (upperBound instanceof DeclaredType) {
                if (messager != null) {
                    messager.printMessage(Diagnostic.Kind.NOTE, "Using upper bound type " + upperBound +
                            " for generic parameter", param);
                }
                typeMirror = upperBound;
            }
        }
        return typeMirror;
    }

    // 普通类型手动装箱
    private String getType(TypeMirror typeMirror) {
        TypeKind typeKind = typeMirror.getKind();

        switch (typeKind) {
            case BOOLEAN:
                return "boolean.class";
            case BYTE:
                return "byte.class";
            case INT:
                return "int.class";
            case FLOAT:
                return "float.class";
            case LONG:
                return "long.class";
            case DOUBLE:
                return "double.class";
            case CHAR:
                return "char.class";
            case SHORT:
                return "short.class";
            default:
                return ((TypeElement)
                        ((DeclaredType) typeMirror).asElement()).getQualifiedName() + ".class";
        }
    }
}
