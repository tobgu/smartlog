package smartlog.front;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

import smartlog.format.LogMessage;

public class MortumDumpBuffer {

   private static class BufferedLogMessage implements LogMessage {

      private final String content;
      private final long id;

      public BufferedLogMessage(LogMessage message) {
         this.id = message.getId();
         this.content = message.serializeContent();
      }

      public String serializeContent() {
         return content;
      }

      public long getId() {
         return id;
      }

   }

   private final Buffer buffer;

   public MortumDumpBuffer(int bufferSize) {
      buffer = BufferUtils.synchronizedBuffer(new CircularFifoBuffer(bufferSize));
   }

   @SuppressWarnings("unchecked")
   public synchronized void add(LogMessage message) {
      // This may become a performance bottle neck but lets not do anything
      // about it until proven

      // Need to serialize the content when buffering the message to avoid that
      // any of the objects part of the output are later modified before a
      // potential flush since they may be mutable.
      buffer.add(new BufferedLogMessage(message));
   }

   public synchronized List<LogMessage> removeAll() {
      // This may become a performance bottle neck but lets not do anything
      // about it until proven
      List<LogMessage> result = new ArrayList<LogMessage>(buffer.size());
      while (!buffer.isEmpty()) {
         result.add((LogMessage) buffer.remove());
      }

      return result;
   }
}
