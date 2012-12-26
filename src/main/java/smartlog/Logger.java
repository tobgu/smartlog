package smartlog;

import java.util.Map;

public interface Logger {
   // If Throwable is last argument treat it as an exception and print log the
   // stack trace and message
   public void log(LogLevel level, long id, String msg);

   public void log(LogLevel level, long id, String msg, Object... params);

   public void log(LogLevel level, long id, Object data);

   public void log(LogLevel level, long id, Map<String, Object> data);

   public void log(LogLevel level, long id, Map<String, Object> data, Throwable t);

   public boolean isLoggable(LogLevel level);

   // public void debug(long id, String msg);
   // public void debug(long id, String msg, Object... params);
   // public void debug(long id, Map<String, Object> data);
   // public void debug(long id, Map<String, Object> data, Throwable t);
   //
   // public void info(long id, String msg);
   // public void info(long id, String msg, Object param);
   // public void info(long id, String msg, Object... params);
   // public void info(long id, Map<String, Object> data);
   // public void info(long id, Map<String, Object> data, Throwable t);
   //
   // public void warning(long id, String msg);
   // public void warning(long id, String msg, Object param);
   // public void warning(long id, String msg, Object param1, Object param2);
   // public void warning(long id, String msg, Object... params);
   // public void warning(long id, Map<String, Object> data);
   // public void warning(long id, Map<String, Object> data, Throwable t);
   //
   // public void error(long id, String msg);
   // public void error(long id, String msg, Object param);
   // public void error(long id, String msg, Object param1, Object param2);
   // public void error(long id, String msg, Object... params);
   // public void error(long id, Map<String, Object> data);
   // public void error(long id, Map<String, Object> data, Throwable t);

}
