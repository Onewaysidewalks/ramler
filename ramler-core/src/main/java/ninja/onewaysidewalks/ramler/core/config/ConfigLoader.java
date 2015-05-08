package ninja.onewaysidewalks.ramler.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

@Slf4j
public class ConfigLoader {

    public static final String RAMLER_CONFIG_OPTION
            = "ninja.onewaysidewalks.ramler.config";

    public static RamlerConfig getRamlerConfigFromOptions(Map<String, String> options) {
        log.info("Options: {}", options);
        String ramlerConfigJson = options.get(RAMLER_CONFIG_OPTION);

        if (StringUtils.isBlank(ramlerConfigJson)) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(ramlerConfigJson, RamlerConfig.class);
        } catch (Exception ex) {
            log.error("Unable to serialize system property json config to class. Config: {}", ramlerConfigJson, ex);
            throw new RuntimeException(ex);
        }
    }
}
