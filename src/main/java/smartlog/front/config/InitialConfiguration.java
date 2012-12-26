package smartlog.front.config;

import java.util.Properties;

import smartlog.LogLevel;

public class InitialConfiguration {

   private static final int DEFAULT_DUMP_BUFFER_SIZE = 1000;
   private static final String DUMPBUFFER_ENABLED_PROPERTY = "smartlog.dumpbuffer.enabled";
   private static final String DUMPBUFFER_ATLEVEL_PROPERTY = "smartlog.dumpbuffer.atlevel";
   private static final String DUMPBUFFER_SIZE_PROPERTY = "smartlog.dumpbuffer.size";

   private final Properties config;

   public InitialConfiguration(Properties configuration) {
      config = configuration;
   }

   public LogConfig getLogConfig(String path) {
      LogConfig result = null;
      String parameter = config.getProperty(path + ".level");
      if (parameter != null) {
         result = createLogConfig(parameter);
      }

      return result;
   }

   private LogConfig createLogConfig(String parameter) {
      LogLevel level = getLogLevel(parameter);
      LogLevel dumpLevel = getDumpLevel();
      return new LogConfig(level.getLevel(), dumpLevel.getLevel(), isDumpBufferEnabled());
   }

   private LogLevel getDumpLevel() {
      String level = config.getProperty(DUMPBUFFER_ATLEVEL_PROPERTY);
      return (level != null) ? getLogLevel(level) : LogLevel.WARN;
   }

   private boolean isDumpBufferEnabled() {
      try {
         String enabled = config.getProperty(DUMPBUFFER_ENABLED_PROPERTY);
         return (enabled != null) && (Integer.valueOf(enabled) != 0);
      } catch (NumberFormatException e) {
         throw new IllegalArgumentException("Non integer given for property '" + DUMPBUFFER_ENABLED_PROPERTY + "': "
               + e.getMessage());
      }
   }

   private LogLevel getLogLevel(String level) {
      try {
         return LogLevel.valueOf(level);
      } catch (IllegalArgumentException e) {
         throw new IllegalArgumentException("Invalid level specified: " + level);
      }
   }

   public int getDumpBufferSize() {
      try {
         int size = DEFAULT_DUMP_BUFFER_SIZE;
         String sizeString = config.getProperty(DUMPBUFFER_SIZE_PROPERTY);
         if (sizeString != null) {
            size = Integer.valueOf(sizeString);
            if (size < 0) {
               throw new IllegalArgumentException("Negative size not allowed for property '" + DUMPBUFFER_SIZE_PROPERTY);
            }
         }

         return size;
      } catch (NumberFormatException e) {
         throw new IllegalArgumentException("Non integer given for property '" + DUMPBUFFER_SIZE_PROPERTY + "': "
               + e.getMessage());
      }
   }
}
