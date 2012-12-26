package smartlog.front;

import java.util.Map;

import smartlog.LogLevel;
import smartlog.Logger;
import smartlog.format.ClassicLogMessage;
import smartlog.format.Formatter;
import smartlog.front.config.LogConfig;
import smartlog.front.config.LogConfigNode;

class StandardLogger implements Logger, LogConfigNode {

   // private static final String CLASS_NAME = StandardLogger.class.getName();
   private final Formatter formatter;
   private final MortumDumpBuffer buffer;
   private final LogConfigNode configParent;
   private volatile LogConfig logConfig;

   // Package private
   StandardLogger(Formatter formatter, MortumDumpBuffer buffer, LogConfig logConfig, LogConfigNode configParent) {
      assert (configParent != null);
      this.formatter = formatter;
      this.buffer = buffer;
      this.configParent = configParent;
      setLogConfig(logConfig);
   }

   public void log(smartlog.LogLevel level, long id, String msg) {
      if (isLoggable(level)) {
         formatter.dispatch(new ClassicLogMessage(id, msg));
      }

      if (logConfig.isMortumDumpEnabled()) {
         buffer.add(new ClassicLogMessage(id, msg));
      }
   }

   public void log(smartlog.LogLevel level, long id, String msg, Object... params) {
      if (isLoggable(level)) {
         formatter.dispatch(new ClassicLogMessage(id, msg, params));
      }

      if (logConfig.isMortumDumpEnabled()) {
         buffer.add(new ClassicLogMessage(id, msg, params));
      }
   }

   public void log(smartlog.LogLevel level, long id, Object data) {
      // TODO Auto-generated method stub

   }

   public void log(smartlog.LogLevel level, long id, Map<String, Object> data) {
      // TODO Auto-generated method stub

   }

   public void log(smartlog.LogLevel level, long id, Map<String, Object> data, Throwable t) {
      // TODO Auto-generated method stub

   }

   public LogConfig getLogConfig() {
      return logConfig;
   }

   public void setLogConfig(LogConfig newConfig) {
      if (newConfig != null) {
         logConfig = newConfig;
      } else {
         logConfig = configParent.getLogConfig();
      }
   }

   public void addChild(String name, LogConfigNode child) {
      // This is a bit hacky but should never occur
      throw new UnsupportedOperationException("Loggers cannot have children");
   }

   public LogConfigNode getChild(String name) {
      // This is a bit hacky but should never occur
      throw new UnsupportedOperationException("Loggers have no children");
   }

   public boolean isLoggable(LogLevel level) {
      return logConfig.getLogLevel() <= level.getLevel();
   }
}
