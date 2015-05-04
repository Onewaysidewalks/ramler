package ninja.onewaysidewalks.ramler.core.visitors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used as a generic way to force the RamlerProcessor to be invoked
 * if this annotation is present, with the information it has at that time
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlerVisit {
}
