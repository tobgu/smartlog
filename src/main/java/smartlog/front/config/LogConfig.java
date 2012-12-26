package smartlog.front.config;

public class LogConfig {
   private final int logLevel;
   private final int mortumDumpLevel;
   private final boolean mortumDumpEnabled;

   public LogConfig(int logLevel, int mortumDumpLevel, boolean mortumDumpEnabled) {
      this.logLevel = logLevel;
      this.mortumDumpLevel = mortumDumpLevel;
      this.mortumDumpEnabled = mortumDumpEnabled;
   }

   public int getLogLevel() {
      return logLevel;
   }

   public int getMortumDumpLevel() {
      return mortumDumpLevel;
   }

   public boolean isMortumDumpEnabled() {
      return mortumDumpEnabled;
   }
}
