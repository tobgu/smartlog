package smartlog.front.config;

import java.util.Map;

public interface DynamicConfiguaration {

   public Map<String, LogConfig> getAllConfigurations();

   public void setConfig(String path, LogConfig config);
}
