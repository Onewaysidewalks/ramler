package ninja.onewaysidewalks.ramler.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class ConfigLoader {

    public static final String RAMLER_CONFIG_SYSTEM_PROPERTY
            = "ninja.onewaysidewalks.ramler.config";

    public static RamlerConfig getRamlerConfigFromSystemProperty() {
        String ramlerConfigJson = System.getProperty(RAMLER_CONFIG_SYSTEM_PROPERTY);

        ObjectMapper objectMapper = new ObjectMapper();

        if (StringUtils.isBlank(ramlerConfigJson)) {
            return null;
        }

        try {
            return objectMapper.readValue(ramlerConfigJson, RamlerConfig.class);
        } catch (Exception ex) {
            log.error("Unable to serialize system property json config to class. Config: {}", ramlerConfigJson, ex);
            throw new RuntimeException(ex);
        }
    }
}
