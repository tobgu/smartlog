package smartlog.front;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import smartlog.LogLevel;
import smartlog.format.ClassicLogMessage;
import smartlog.format.Formatter;
import smartlog.format.LogMessage;
import smartlog.front.config.LogConfig;

// Logger levels

@RunWith(MockitoJUnitRunner.class)
public class StandardLoggerTest {
   @Mock
   private Formatter formatter;

   @Mock
   private MortumDumpBuffer buffer;

   private StandardLogger logger;

   @Before
   public void setup() throws Exception {
      logger = new StandardLogger(formatter, buffer, new LogConfig(LogLevel.INFO.getLevel(), 0, false), null);
   }

   @Test
   public void basic_Info() {
      String msg = "Test log";

      logger.log(LogLevel.INFO, 13, msg);
      checkCall(13, msg);
   }

   @Test
   public void templated_Info() {
      logger.log(LogLevel.INFO, 13, "Test log {} {}", 1, 2);
      checkCall(13, "Test log 1 2");
   }

   @Test
   public void logging_Info_At_The_Right_Level() {
      // Default
      logger.log(LogLevel.INFO, 13, "Test log");
      checkCall(13, "Test log");

      logger.setLogConfig(new LogConfig(LogLevel.TRACE.getLevel(), 0, false));
      logger.log(LogLevel.INFO, 13, "Test log");
      checkCall(13, "Test log");
      verifyZeroInteractions(buffer);

      logger.setLogConfig(new LogConfig(LogLevel.INFO.getLevel(), 0, false));
      logger.log(LogLevel.INFO, 13, "Test log");
      checkCall(13, "Test log");

      logger.setLogConfig(new LogConfig(LogLevel.WARN.getLevel(), 0, false));
      logger.log(LogLevel.INFO, 13, "Test log");
      verifyZeroInteractions(formatter);

      logger.setLogConfig(new LogConfig(LogLevel.ERROR.getLevel(), 0, false));
      logger.log(LogLevel.INFO, 13, "Test log");
      verifyZeroInteractions(formatter);
   }

   @Test
   public void buffering_At_The_Right_Level() {
      logger.setLogConfig(new LogConfig(LogLevel.INFO.getLevel(), LogLevel.WARN.getLevel(), true));

      logger.log(LogLevel.TRACE, 13, "Test log 1");
      verifyZeroInteractions(formatter);
      checkBuffer(13, "Test log 1");

      logger.log(LogLevel.INFO, 13, "Test log 2");
      checkCall(13, "Test log 2");
      checkBuffer(13, "Test log 2");

      logger.log(LogLevel.TRACE, 14, "Test log 3");
      verifyZeroInteractions(formatter);
      checkBuffer(14, "Test log 3");
   }

   private void checkBuffer(int id, String msg) {
      LogMessage message = new ClassicLogMessage(id, msg);
      verify(buffer).add(argThat(new MessagesArgumentMatcher(message)));
      reset(buffer);
   }

   private void checkCall(final int id, final String msg) {
      LogMessage message = new ClassicLogMessage(id, msg);
      verify(formatter).dispatch(argThat(new MessagesArgumentMatcher(message)));
      reset(formatter);
   }

   class MessagesArgumentMatcher extends ArgumentMatcher<LogMessage> {
      private final LogMessage message;

      public MessagesArgumentMatcher(final LogMessage message) {
         this.message = message;
      }

      @Override
      public boolean matches(final Object other) {
         assertTrue(other instanceof LogMessage);
         LogMessage otherMsg = (LogMessage) other;
         assertEquals(message.serializeContent(), otherMsg.serializeContent());
         assertEquals(message.getId(), otherMsg.getId());
         return true;
      }
   }

}
