package ninja.onewaysidewalks.ramler.core;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import ninja.onewaysidewalks.ramler.core.config.ConfigLoader;
import ninja.onewaysidewalks.ramler.core.config.RamlerConfig;
import ninja.onewaysidewalks.ramler.core.visitors.MethodVisitor;
import ninja.onewaysidewalks.ramler.core.visitors.ParameterVisitor;
import org.reflections.Reflections;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class RamlerProcessor extends AbstractProcessor {

    private final List<Class<? extends MethodVisitor>> methodVisitors;
    private final List<Class<? extends ParameterVisitor>> parameterVisitors;
    private final RamlerConfig config;

    private final Reflections reflections;


    public RamlerProcessor() {
        //TODO: must be better way for config loading?
        this.config = ConfigLoader.getRamlerConfigFromSystemProperty();

        Preconditions.checkNotNull(config, "config must not be null");
        Preconditions.checkNotNull(config.getBasePackageForReflection(), "basePackage name for scanning must not be null");
        Preconditions.checkNotNull(config.getSupportedAnnotationTypes(), "supportedAnnotations set must not be null");
        Preconditions.checkArgument(config.getSupportedAnnotationTypes().size() > 0, "supportedAnnotations must not be an empty collection");

        this.reflections = new Reflections(config.getBasePackageForReflection());

        log.info("Using base package {} for package scanning", config.getBasePackageForReflection());

        this.methodVisitors = scanForClasses(MethodVisitor.class);
        this.parameterVisitors = scanForClasses(ParameterVisitor.class);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> baseSet = super.getSupportedAnnotationTypes();

        Set<String> union = new HashSet<>();

        for (String annotationType : baseSet) {
            union.add(annotationType);
        }

        for (String annotationType : config.getSupportedAnnotationTypes()) {
            union.add(annotationType);
        }

        return union;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        log.info("{}", annotations);

        return false;
    }

    public <T> List<Class<? extends T>> scanForClasses(Class<T> classToLoad) {
        List<Class<? extends T>> retClasses = new ArrayList<>();

        Set<Class<? extends T>> classes = reflections.getSubTypesOf(classToLoad);

        retClasses.addAll(classes);

        return retClasses;
    }
}
