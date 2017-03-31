package net.funol.databinding.watchdog.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.funol.databinding.watchdog.annotations.NotifyThis;
import net.funol.databinding.watchdog.annotations.WatchThis;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by ZHAOWEIWEI on 2017/3/31.
 */

public class CallbackHelper {

    public static String getCallbackPackageName(Element element) {
        return element.getEnclosingElement().toString() + Constants.WATCHDOG_PACKAGE_NAME_SUFFIX;
    }

    public static String getCallbackInterfaceName(Element element) {
        return "I" + element.getSimpleName() + "Callbacks";
    }

    public static TypeSpec.Builder generateCallback(Element element) {
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
        return callbacksInterface;
    }

    public static TypeSpec.Builder generateCallback(Element element, TypeName superType) {
        return generateCallback(element).addSuperinterface(superType);
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
                .addJavadoc("property change callback for {@link $T#$N}\n", element.getEnclosingElement().asType(), element.getSimpleName().toString())
                .addAnnotation(AnnotationSpec.builder(NotifyThis.class)
                        .addMember("field", CodeBlock.builder()
                                .add("$S", element.getSimpleName())
                                .build())
                        .build())
                .addParameter(paramTypeName, Constants.OBSERVABLE_FIELD)
                .addParameter(TypeName.INT, Constants.FIELD_ID);
    }
}
