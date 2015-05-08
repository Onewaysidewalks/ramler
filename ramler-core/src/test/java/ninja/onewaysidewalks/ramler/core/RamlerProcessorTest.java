package ninja.onewaysidewalks.ramler.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import ninja.onewaysidewalks.ramler.core.config.ConfigLoader;
import ninja.onewaysidewalks.ramler.core.config.RamlerConfig;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

@Test
public class RamlerProcessorTest {

    private RamlerProcessor subject;

    private ObjectMapper objectMapper;

    @Mock
    private ProcessingEnvironment processingEnvironment;

    @Mock
    private RoundEnvironment roundEnvironment;

    @BeforeMethod
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        subject = new RamlerProcessor();

        objectMapper = new ObjectMapper();
    }

    @AfterMethod
    public void tearDown() {

    }

    @Test
    public void getSupportedAnnotationTypes_validInputFromConfig_returnsConfigValues() {

        initProcessor();

        Set<String> supportedTypes = subject.getSupportedAnnotationTypes();

        assertThat(supportedTypes.size(), is(2));
        assertThat(supportedTypes.contains("lombok.Getter"), is(true));
        assertThat(supportedTypes.contains("lombok.Setter"), is(true));
        assertThat(supportedTypes.contains("SomeUnknownAnnotation"), is(false));
    }

    @Test
    public void getMethodVisitors_scannedPathPicksUpVisitors_success() {

        initProcessor();

        assertThat(subject.getMethodVisitors().size(), is(1));
    }

    @Test
    public void getParameterVisitors_scannedPathPicksUpVisitors_success() {
        initProcessor();

        assertThat(subject.getParameterVisitors().size(), is(1));
    }

    @Test
    public void process_noSupportedAnnotations_nothingDone() {
        initProcessor();

        verifyNoMoreInteractions(roundEnvironment);

        subject.process(new HashSet<TypeElement>(), roundEnvironment);
    }

//    @Test
//    public void process_noTypesFoundWithAnnotations_nothingDone() {
//        initProcessor();
//
//        when(roundEnvironment.getElementsAnnotatedWith(any(TypeElement.class)))
//                .thenReturn();
//
//        subject.process(new HashSet<TypeElement>(), roundEnvironment);
//    }

    private void initProcessor() {
        RamlerConfig config = new RamlerConfig();
        config.setBasePackagesForReflection(Collections.singletonList("ninja.onewaysidewalks"));
        config.setSupportedAnnotationTypes(Arrays.asList("lombok.Getter", "lombok.Setter"));

        try {
            when(processingEnvironment.getOptions())
                    .thenReturn(Collections.singletonMap(
                            ConfigLoader.RAMLER_CONFIG_OPTION, objectMapper.writeValueAsString(config)));

            subject.init(processingEnvironment);
        } catch (Exception e) {
            fail("Could not setup mock for initializing processor", e);
        }
    }
}
