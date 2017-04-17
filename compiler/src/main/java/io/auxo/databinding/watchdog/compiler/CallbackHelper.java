package io.auxo.databinding.watchdog.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import io.auxo.databinding.watchdog.Utils;
import io.auxo.databinding.watchdog.Watcher;
import io.auxo.databinding.watchdog.annotations.NotifyThis;
import io.auxo.databinding.watchdog.annotations.WatchThis;

public class CallbackHelper {

    static final String OBSERVABLE_FIELD = "observableField";
    static final String FIELD_ID = "fieldId";

    public static String getCallbackPackageName(Element element) {
        return Utils.getWatchdogPackage(element.getEnclosingElement().toString());
    }

    public static String getCallbackInterfaceName(Element element) {
        return Utils.getCallbackInterfaceName(element.getSimpleName().toString());
    }

    public static TypeSpec.Builder generateCallback(Element element) {
        return generateCallback(element, null);
    }

    public static TypeSpec.Builder generateCallback(Element element, TypeName superType) {
        TypeSpec.Builder callbacksInterface = generatePropertyChangeCallbacksInterface(getCallbackInterfaceName(element));
        for (Element field : element.getEnclosedElements()) {
            if (field.getAnnotation(WatchThis.class) != null) {
                // method name
                String methodName = field.getAnnotation(WatchThis.class).method();
                methodName = methodName.equals("") ? field.getSimpleName().toString() : methodName;
                // observable TypeName
                TypeName paramTypeName = TypeName.get(field.asType());

                callbacksInterface.addMethod(generatePropertyChangeCallbacksMethod(field, methodName, paramTypeName).build());
            }
        }
        if (superType == null) {
            callbacksInterface.addSuperinterface(ClassName.get(Watcher.class));
        } else {
            callbacksInterface.addSuperinterface(superType);
        }
        return callbacksInterface;
    }

    public static TypeSpec.Builder generatePropertyChangeCallbacksInterface(String className) {
        return TypeSpec.interfaceBuilder(className)
                .addModifiers(Modifier.PUBLIC);
    }

    public static MethodSpec.Builder generatePropertyChangeCallbacksMethod(Element element, String methodName, TypeName paramTypeName) {
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(TypeName.VOID)
                .addJavadoc("property change callback for {@link $T#$N}\n\n", element.getEnclosingElement().asType(), element.getSimpleName().toString())
                .addJavadoc("@param $N\n", OBSERVABLE_FIELD)
                .addJavadoc("@param $N\n", FIELD_ID)
                .addAnnotation(AnnotationSpec.builder(NotifyThis.class)
                        .addMember("field", CodeBlock.builder()
                                .add("$S", element.getSimpleName())
                                .build())
                        .build())
                .addParameter(paramTypeName, OBSERVABLE_FIELD)
                .addParameter(TypeName.INT, FIELD_ID);
    }
}
