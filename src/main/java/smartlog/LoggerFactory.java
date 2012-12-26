package smartlog;

import smartlog.front.StandardLoggerFactory;

public class LoggerFactory {
   private static final StandardLoggerFactory factory = StandardLoggerFactory.getInstance();

   private LoggerFactory() {
      // Not possible to instantiate
   }

   public static Logger createLogger(Class<?> clazz) {
      return factory.getLogger(clazz.getName());
   }
}
