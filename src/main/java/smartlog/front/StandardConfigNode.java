package smartlog.front;

import java.util.Map;

import smartlog.front.config.LogConfig;
import smartlog.front.config.LogConfigNode;

import com.google.common.collect.Maps;

public class StandardConfigNode implements LogConfigNode {
   private final LogConfigNode parent;
   private final Map<String, LogConfigNode> children;
   private volatile LogConfig config;

   public StandardConfigNode(LogConfigNode parent, LogConfig config) {
      this.parent = parent;
      this.children = Maps.newHashMap();
      setLogConfig(config);
   }

   public LogConfig getLogConfig() {
      return config;
   }

   public void setLogConfig(LogConfig newConfig) {
      if (newConfig != null) {
         updateChildrenInNeed(newConfig);
         config = newConfig;
      } else {
         if (parent != null) {
            config = parent.getLogConfig();
         } else {
            throw new IllegalArgumentException("Can't set root node configuration to null");
         }
      }
   }

   private void updateChildrenInNeed(LogConfig newConfig) {
      for (LogConfigNode child : children.values()) {
         if (child.getLogConfig() == config) {
            // The child's configuration is the same as this node and must be updated
            child.setLogConfig(newConfig);
         }
      }
   }

   public void addChild(String name, LogConfigNode child) {
      children.put(name, child);
   }

   public LogConfigNode getChild(String name) {
      return children.get(name);
   }

}
