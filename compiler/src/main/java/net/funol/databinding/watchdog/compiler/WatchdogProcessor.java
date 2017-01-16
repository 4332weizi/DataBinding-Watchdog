package net.funol.databinding.watchdog.compiler;

import android.databinding.Observable;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
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

        Set<String> keys = mTypeSpecBuilderMap.keySet();

        for (String key : keys) {
            List<Element> elements = mTypeSpecBuilderMap.get(key);

            String outputPackageName = getPackageName(elements.get(0)) + Util.WATCHDOG_PACKAGE_NAME_SUFFIX;

            TypeSpec.Builder iPropertyChangeCallbacksBuilder = generatePropertyChangeCallbacksInterface(elements.get(0));
            TypeSpec.Builder injectorBuilder = generateInjectorClass(elements.get(0));
            MethodSpec.Builder injectMethod = generateInjectMethod(TypeName.get(elements.get(0).getEnclosingElement().asType()),
                    ClassName.get(outputPackageName, Util.getCallbackInterfaceName(elements.get(0).getEnclosingElement().getSimpleName().toString())));
            for (Element element : elements) {

                String method = element.getAnnotation(WatchThis.class).method();
                method = method.equals("") ? element.getSimpleName().toString() : method;

                iPropertyChangeCallbacksBuilder.addMethod(generatePropertyChangeCallbacksMethod(element).build());

                TypeSpec propertyChangeCallback = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(Observable.OnPropertyChangedCallback.class)
                        .addMethod(MethodSpec.methodBuilder("onPropertyChanged")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.VOID)
                                .addParameter(ClassName.get(Observable.class), "observableField")
                                .addParameter(TypeName.INT, "fieldId")
                                .addCode(CodeBlock.builder()
                                        .beginControlFlow("if(beNotified!=null)")
                                        .addStatement("beNotified." + method + "((" + element.asType() + ")observableField,fieldId)")
                                        .endControlFlow()
                                        .build())
                                .build())
                        .build();

                injectMethod.addStatement("beWatched." + element.getSimpleName() + ".addOnPropertyChangedCallback($L)", propertyChangeCallback);

            }
            injectorBuilder.addMethod(injectMethod.build());
            writeToFile(outputPackageName, iPropertyChangeCallbacksBuilder.build());
            writeToFile(outputPackageName, injectorBuilder.build());
        }

        return false;
    }

    public TypeSpec.Builder generatePropertyChangeCallbacksInterface(Element element) {
        return TypeSpec.interfaceBuilder(Util.getCallbackInterfaceName(element.getEnclosingElement().getSimpleName().toString()))
                .addModifiers(Modifier.PUBLIC);
    }

    public MethodSpec.Builder generatePropertyChangeCallbacksMethod(Element element) {
        String method = element.getAnnotation(WatchThis.class).method();
        method = method.equals("") ? element.getSimpleName().toString() : method;
        return MethodSpec.methodBuilder(method)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .addAnnotation(NotifyThis.class)
                .addParameter(TypeName.get(element.asType()), "observableField")
                .addParameter(TypeName.INT, "fieldId");
    }

    public TypeSpec.Builder generateInjectorClass(Element element) {
        return TypeSpec.classBuilder(Util.getInjectorClassName(element.getEnclosingElement().getSimpleName().toString()))
                .superclass(ClassName.get(WatchdogInjector.class))
                .addModifiers(Modifier.PUBLIC);
    }

    public MethodSpec.Builder generateInjectMethod(TypeName beWatched, TypeName beNotified) {
        return MethodSpec.methodBuilder("inject")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(beWatched, "beWatched").build())
                .addParameter(ParameterSpec.builder(beNotified, "beNotified", Modifier.FINAL).build());
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

        public static String getCallbackInterfaceName(String className) {
            return "I" + className + "Callbacks";
        }

        public static String getInjectorClassName(String className) {
            return className + "$$WatchdogInjector";
        }
    }

}
