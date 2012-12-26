package smartlog.front.config;

public interface LogConfigNode {

   public LogConfig getLogConfig();

   public void setLogConfig(LogConfig config);

   public void addChild(String name, LogConfigNode child);

   public LogConfigNode getChild(String name);

}
