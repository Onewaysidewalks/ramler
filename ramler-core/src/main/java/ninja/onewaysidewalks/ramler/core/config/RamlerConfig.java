package ninja.onewaysidewalks.ramler.core.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RamlerConfig {
    /**
     * Annotations to look for when processing all visitors. Normally supported
     * by the @SupportedAnnotationTypes annotation, but is being extended here as source code access
     * may not be available. If both are present, the union of the two sets (from annotation and from config)
     * will be used
     */
    private List<String> supportedAnnotationTypes;


    /**
     * used to determine what the base package name for reflection scanning is
     * use something that is more specific, as it will speed up processing time
     * as there will be less to reflect over
     */
    private List<String> basePackagesForReflection;
}
