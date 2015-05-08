package ninja.onewaysidewalks.ramler.core.visitors.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to determine the order of how visitors should be ran
 * The visitors with higher order's have higher precedence.
 * The order is scoped to a visitor of a particular type.
 * For example, all method visitors will be ordered in isolation,
 * unaffected by any Parameter visitors.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface VisitorOrder {
    int value();
}
