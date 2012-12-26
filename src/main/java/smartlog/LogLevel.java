package smartlog;

public enum LogLevel {
   ALL(0), TRACE(20), INFO(50), WARN(70), ERROR(100), DISABLED(Integer.MAX_VALUE);

   private final int level;

   private LogLevel(int level) {
      this.level = level;
   }

   public int getLevel() {
      return level;
   }
}