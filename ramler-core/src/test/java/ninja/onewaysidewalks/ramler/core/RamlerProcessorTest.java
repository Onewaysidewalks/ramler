package ninja.onewaysidewalks.ramler.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import ninja.onewaysidewalks.ramler.core.config.ConfigLoader;
import ninja.onewaysidewalks.ramler.core.config.RamlerConfig;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Test
public class RamlerProcessorTest {

    private RamlerProcessor subject;

    @BeforeMethod
    public void setUp() throws Exception {
        RamlerConfig config = new RamlerConfig();
        config.setBasePackageForReflection("ninja.onewaysidewalks");
        config.setSupportedAnnotationTypes(new HashSet<>(Arrays.asList("lombok.Getter", "lombok.Setter")));

        System.setProperty(ConfigLoader.RAMLER_CONFIG_SYSTEM_PROPERTY, new ObjectMapper().writeValueAsString(config));

        subject = new RamlerProcessor();
    }

    @AfterMethod
    public void tearDown() {
        System.clearProperty(ConfigLoader.RAMLER_CONFIG_SYSTEM_PROPERTY);
    }

    @Test
    public void getSupportedAnnotationTypes_validInputFromConfig_returnsConfigValues() {
        Set<String> supportedTypes = subject.getSupportedAnnotationTypes();

        assertThat(supportedTypes.size(), is(2));
        assertThat(supportedTypes.contains("lombok.Getter"), is(true));
        assertThat(supportedTypes.contains("lombok.Setter"), is(true));
        assertThat(supportedTypes.contains("SomeUnknownAnnotation"), is(false));
    }
}
