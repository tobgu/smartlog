package smartlog.format;

import org.slf4j.helpers.MessageFormatter;

public class ClassicLogMessage implements LogMessage {

   private final String message;
   private final long id;
   private final Object[] args;

   public ClassicLogMessage(final long id, final String message, final Object... args) {
      this.id = id;
      this.message = message;
      this.args = args;
   }

   public String serializeContent() {
      return MessageFormatter.arrayFormat(message, args).getMessage();
   }

   public long getId() {
      return id;
   }

}
