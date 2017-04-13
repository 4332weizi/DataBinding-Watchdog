package io.auxo.databinding.watchdog.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import io.auxo.databinding.watchdog.annotations.WatchThis;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by ZHAOWEIWEI on 2017/1/9.
 */
@AutoService(Processor.class)
public class WatchdogProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;

    private Types mTypeUtils;
    private Elements mElementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(WatchThis.class.getName());
        // types.add(NotifyThis.class.getName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<Element> topWatchedElementsInEnv = getTopWatchedElementsInEnv(getEnclosingElements(roundEnv.getElementsAnnotatedWith(WatchThis.class)));
        Set<Element> topWatchedElementsOutOfEnv = getTopWatchedElementsOutOfEnv(roundEnv.getRootElements());

        for (Element element : roundEnv.getRootElements()) {

            Element superElement = getSuperElement(element, topWatchedElementsOutOfEnv);
            if (superElement == null) {
                superElement = getSuperElement(element, topWatchedElementsInEnv);
            }
            if (superElement == null) {
                continue;
            }

            TypeSpec.Builder injector;
            TypeSpec.Builder callback;

            if (mTypeUtils.isSameType(superElement.asType(), element.asType())) {
                callback = CallbackHelper.generateCallback(element);
                injector = InjectorHelper.generateInjector(element);
            } else {
                callback = CallbackHelper.generateCallback(element, ClassName.get(CallbackHelper.getCallbackPackageName(superElement), CallbackHelper.getCallbackInterfaceName(superElement)));
                injector = InjectorHelper.generateInjector(element, ClassName.get(InjectorHelper.getInjectorPackageName(superElement), InjectorHelper.getInjectorClassName(superElement)));
            }
            writeToFile(InjectorHelper.getInjectorPackageName(element), injector.build());
            writeToFile(CallbackHelper.getCallbackPackageName(element), callback.build());
        }

        return true;
    }

    protected Element getSuperElement(Element element, Set<Element> elements) {
        for (Element e : elements) {
            if (mTypeUtils.isSubtype(element.asType(), e.asType()) || mTypeUtils.isSameType(e.asType(), element.asType())) {
                return e;
            }
        }
        return null;
    }

    protected Set<Element> getEnclosingElements(Set<? extends Element> elements) {
        Set<Element> enclosingElements = new HashSet<>();
        for (Element element : elements) {
            enclosingElements.add(element.getEnclosingElement());
        }
        return enclosingElements;
    }

    protected Set<Element> getTopWatchedElementsOutOfEnv(Set<? extends Element> elements) {

        Set<Element> topElements = new HashSet<>();

        for (Element element : elements) {

            if (element.getKind().isInterface()) {
                break;
            }

            List<? extends TypeMirror> superTypes = mTypeUtils.directSupertypes(element.asType());

            if (!elements.contains(mTypeUtils.asElement(superTypes.get(0)))) {
                TypeName superType = InjectorHelper.getInjectorSuperType(mElementUtils, mTypeUtils, mTypeUtils.asElement(superTypes.get(0)));
                if (superType != null) {
                    // super annotated with WatchThis
                    topElements.add(element);
                }
            }
        }

        return topElements;
    }

    protected Set<Element> getTopWatchedElementsInEnv(Set<? extends Element> elements) {

        Set<Element> topElements = new HashSet<>();

        for (Element element : elements) {

            if (element.getKind().isInterface()) {
                break;
            }

            List<? extends TypeMirror> superTypes = mTypeUtils.directSupertypes(element.asType());

            if (!elements.contains(mTypeUtils.asElement(superTypes.get(0)))) {
                topElements.add(element);
            }
        }

        return topElements;
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

}
