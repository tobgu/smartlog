package smartlog.front.config;

public interface StaticConfiguration {
   public LogConfigNode getConfigNode(String path);

   public LogConfig getLogConfig(String path);
}
