package ninja.onewaysidewalks.ramler.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.ramler.core.config.ConfigLoader;
import ninja.onewaysidewalks.ramler.core.config.RamlerConfig;
import ninja.onewaysidewalks.ramler.core.visitors.interfaces.MethodVisitor;
import ninja.onewaysidewalks.ramler.core.visitors.interfaces.ParameterVisitor;
import org.reflections.Reflections;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.*;

@Slf4j
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({ "ramllyConfig" })
public class RamlerProcessor extends AbstractProcessor {

    @Getter
    private List<Class<? extends MethodVisitor>> methodVisitors;

    @Getter
    private List<Class<? extends ParameterVisitor>> parameterVisitors;


    @Setter
    @VisibleForTesting
    private RamlerConfig config;

    private Reflections reflections;


    public RamlerProcessor() {
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        log.info("Loading supported annotations from annotation and configuration");
        Set<String> baseSet = super.getSupportedAnnotationTypes();

        Set<String> union = new HashSet<>();

        for (String annotationType : baseSet) {
            union.add(annotationType);
        }

        for (String annotationType : config.getSupportedAnnotationTypes()) {
            union.add(annotationType);
        }

        log.info("Looking for the following annotations: {}", union);

        return union;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (annotations.size() > 0) {
            log.info("{}", annotations);
            List<TypeElement> typeElements = new ArrayList<>();

            //Iterate through supported annotations, looking for any classes and interface with said annotations
            for (TypeElement annotation : annotations) {
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);

                for (Element element : elements) {
                    TypeElement typeElement = null;

                    //if the element is already a class/interface, use it
                    if (element instanceof TypeElement) {
                        typeElements.add((TypeElement) element);
                        continue;
                    }

                    //Otherwise, find elements that represent classes or interfaces, and pull out said element if it exists
                    while (!(element instanceof TypeElement)) {

                        element = element.getEnclosingElement();

                        if (element == null) {
                            break;
                        }

                        if (element instanceof TypeElement) {
                            typeElement = (TypeElement) element;
                        }
                    }

                    if (typeElement != null) {
                        typeElements.add(typeElement);
                    }
                }
            }

            log.info("Found elements: {}", typeElements);
        }
        return true;
    }

    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        this.config = ConfigLoader.getRamlerConfigFromOptions(processingEnvironment.getOptions());

        Preconditions.checkNotNull(config, "config must not be null");
        Preconditions.checkNotNull(config.getBasePackagesForReflection(), "basePackage name for scanning must not be null");
        Preconditions.checkNotNull(config.getSupportedAnnotationTypes(), "supportedAnnotations set must not be null");
        Preconditions.checkArgument(config.getSupportedAnnotationTypes().size() > 0, "supportedAnnotations must not be an empty collection");

        this.reflections = new Reflections(config.getBasePackagesForReflection());

        log.info("Using base package {} for package scanning", config.getBasePackagesForReflection());

        this.methodVisitors = scanForClasses(MethodVisitor.class);
        this.parameterVisitors = scanForClasses(ParameterVisitor.class);
    }

    public <T> List<Class<? extends T>> scanForClasses(Class<T> classToLoad) {
        List<Class<? extends T>> retClasses = new ArrayList<>();

        Set<Class<? extends T>> classes = reflections.getSubTypesOf(classToLoad);

        retClasses.addAll(classes);

        return retClasses;
    }
}
