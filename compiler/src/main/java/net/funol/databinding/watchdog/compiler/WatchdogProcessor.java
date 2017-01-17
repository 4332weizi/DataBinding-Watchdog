package net.funol.databinding.watchdog.compiler;

import android.databinding.Observable;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.funol.databinding.watchdog.WatchdogInjector;
import net.funol.databinding.watchdog.annotations.NotifyThis;
import net.funol.databinding.watchdog.annotations.WatchThis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by ZHAOWEIWEI on 2017/1/9.
 */
@AutoService(Processor.class)
public class WatchdogProcessor extends AbstractProcessor {

    private Elements mElementUtils;
    private Filer mFiler;
    private Map<String, List<Element>> mTypeSpecBuilderMap;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mTypeSpecBuilderMap = new HashMap<>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(WatchThis.class.getName());
        types.add(NotifyThis.class.getName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(WatchThis.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                String className = element.getEnclosingElement().getSimpleName().toString();
                List<Element> elements = mTypeSpecBuilderMap.get(className);
                if (elements == null) {
                    elements = new ArrayList<>();
                }
                elements.add(element);
                mTypeSpecBuilderMap.put(className, elements);
            }
        }

        // classNames
        Set<String> keys = mTypeSpecBuilderMap.keySet();

        for (String key : keys) {

            // fields annotated with WatchThis
            List<Element> elements = mTypeSpecBuilderMap.get(key);

            // callbacks and injector package name
            String outputPackageName = getPackageName(elements.get(0)) + Util.WATCHDOG_PACKAGE_NAME_SUFFIX;

            // class name
            String originClassName = elements.get(0).getEnclosingElement().getSimpleName().toString();
            String callbackInterfaceName = Util.getCallbackInterfaceName(originClassName);

            // callback interface
            TypeSpec.Builder iPropertyChangeCallbacksBuilder = generatePropertyChangeCallbacksInterface(callbackInterfaceName);

            // injector
            String injectorClassName = Util.getInjectorClassName(originClassName);
            TypeName beWatchedTypeName = TypeName.get(elements.get(0).getEnclosingElement().asType());
            TypeName beNotifiedTypeName = ClassName.get(outputPackageName, callbackInterfaceName);
            // generate injector
            TypeSpec.Builder injectorBuilder = generateInjectorClass(injectorClassName, beWatchedTypeName, beNotifiedTypeName);
            // generate inject method
            MethodSpec.Builder injectMethod = generateInjectMethod(beWatchedTypeName, beNotifiedTypeName);

            for (Element element : elements) {

                // method name
                String methodName = element.getAnnotation(WatchThis.class).method();
                methodName = methodName.equals("") ? element.getSimpleName().toString() : methodName;
                // observable TypeName
                TypeName paramTypeName = TypeName.get(element.asType());

                // add callback method
                iPropertyChangeCallbacksBuilder.addMethod(generatePropertyChangeCallbacksMethod(methodName, paramTypeName).build());

                // generate anonymous callback
                TypeSpec propertyChangeCallback = generatePropertyChangeCallback(methodName, paramTypeName).build();
                // add property change callback
                injectMethod.addStatement("$N.$N.$N($L)", Util.BE_WATCHED_PARAM_NAME, element.getSimpleName(), Util.ADD_ON_PROPERTY_CHANGED_CALLBACK, propertyChangeCallback);

            }

            // add method to injector
            injectorBuilder.addMethod(injectMethod.build());

            // save to file
            writeToFile(outputPackageName, iPropertyChangeCallbacksBuilder.build());
            writeToFile(outputPackageName, injectorBuilder.build());
        }

        return false;
    }

    public TypeSpec.Builder generatePropertyChangeCallbacksInterface(String className) {
        return TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC);
    }

    public MethodSpec.Builder generatePropertyChangeCallbacksMethod(String methodName, TypeName paramTypeName) {
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .addAnnotation(NotifyThis.class)
                .addParameter(paramTypeName, Util.OBSERVABLE_FIELD)
                .addParameter(TypeName.INT, Util.FIELD_ID);
    }

    public TypeSpec.Builder generateInjectorClass(String className, TypeName beWatchedTypeName, TypeName beNotifiedTypeName) {
        return TypeSpec.classBuilder(className)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(WatchdogInjector.class), beWatchedTypeName, beNotifiedTypeName))
                .addModifiers(Modifier.PUBLIC);
    }

    public MethodSpec.Builder generateInjectMethod(TypeName beWatched, TypeName beNotified) {
        return MethodSpec.methodBuilder(Util.INJECT_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(beWatched, Util.BE_WATCHED_PARAM_NAME).build())
                .addParameter(ParameterSpec.builder(beNotified, Util.BE_NOTIFIED_PARAM_NAME, Modifier.FINAL).build());
    }

    public TypeSpec.Builder generatePropertyChangeCallback(String callbackMethodName, TypeName paramTypeName) {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Observable.OnPropertyChangedCallback.class)
                .addMethod(MethodSpec.methodBuilder("onPropertyChanged")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.VOID)
                        .addParameter(ClassName.get(Observable.class), Util.OBSERVABLE_FIELD)
                        .addParameter(TypeName.INT, Util.FIELD_ID)
                        .addCode(CodeBlock.builder()
                                .beginControlFlow("if ($N != null)", Util.BE_NOTIFIED_PARAM_NAME)
                                .addStatement("$N.$N(($T)$N,$N)", Util.BE_NOTIFIED_PARAM_NAME, callbackMethodName, paramTypeName, Util.OBSERVABLE_FIELD, Util.FIELD_ID)
                                .endControlFlow()
                                .build())
                        .build());
    }

    private void writeToFile(String packageName, TypeSpec typeSpec) {
        try {
            JavaFile.builder(packageName, typeSpec)
                    .build()
                    .writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPackageName(Element element) {
        return mElementUtils.getPackageOf(element).getQualifiedName().toString();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    public static class Util {

        private static final String WATCHDOG_PACKAGE_NAME_SUFFIX = ".watchdog";

        private static final String INJECT_METHOD_NAME = "inject";
        private static final String BE_WATCHED_PARAM_NAME = "beWatched";
        private static final String BE_NOTIFIED_PARAM_NAME = "beNotified";

        private static final String ADD_ON_PROPERTY_CHANGED_CALLBACK = "addOnPropertyChangedCallback";
        private static final String OBSERVABLE_FIELD = "observableField";
        private static final String FIELD_ID = "fieldId";

        public static String getCallbackInterfaceName(String className) {
            return "I" + className + "Callbacks";
        }

        public static String getInjectorClassName(String className) {
            return className + "$$WatchdogInjector";
        }
    }

}
