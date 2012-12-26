package smartlog.front;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import smartlog.format.ClassicLogMessage;
import smartlog.format.LogMessage;

public class MortumDumpBufferTest {
   private final int TEST_SIZE = 3;
   private final MortumDumpBuffer buffer = new MortumDumpBuffer(TEST_SIZE);

   @Test
   public void messages_Are_Serialized_When_Buffered() {
      AtomicInteger i = new AtomicInteger();
      i.set(55);
      LogMessage message = new ClassicLogMessage(1, "Testing {}", i);
      buffer.add(message);

      i.set(66);
      List<LogMessage> messages = buffer.removeAll();
      assertEquals("Testing 55", messages.get(0).serializeContent());
   }

   @Test
   public void oldest_Messages_Are_Dropped_When_Buffer_Is_Full() {
      LogMessage message1 = new ClassicLogMessage(1, "A");
      LogMessage message2 = new ClassicLogMessage(2, "B");
      LogMessage message3 = new ClassicLogMessage(3, "C");
      LogMessage message4 = new ClassicLogMessage(4, "D");

      buffer.add(message1);
      buffer.add(message2);
      buffer.add(message3);
      buffer.add(message4);

      List<LogMessage> messages = buffer.removeAll();

      // Only the last three are stored
      assertEquals(2, messages.get(0).getId());
      assertEquals(3, messages.get(1).getId());
      assertEquals(4, messages.get(2).getId());
   }

}
