package smartlog.front.config;

import java.util.List;
import java.util.Map;

import smartlog.LogLevel;
import smartlog.front.StandardConfigNode;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class ConfigurationContainer implements StaticConfiguration, DynamicConfiguaration {

   private final InitialConfiguration initialConfig;
   private final LogConfigNode rootNode;

   public ConfigurationContainer(InitialConfiguration initialConfig) {
      this.initialConfig = initialConfig;
      this.rootNode = createRootNode();
   }

   private LogConfigNode createRootNode() {
      LogConfig config = initialConfig.getLogConfig("");
      if (config == null) {
         log("No default configuration specified, setting hard coded value");
         config = new LogConfig(LogLevel.WARN.getLevel(), LogLevel.WARN.getLevel(), false);
      }

      return new StandardConfigNode(null, config);
   }

   private static void log(String message) {
      System.out.println(message);
   }

   public Map<String, LogConfig> getAllConfigurations() {
      // TODO Auto-generated method stub
      return null;
   }

   public void setConfig(String path, LogConfig config) {
      // TODO Auto-generated method stub

   }

   public LogConfigNode getConfigNode(String path) {
      Iterable<String> nodeNames = Splitter.on('.').split(path);

      LogConfigNode node = rootNode;
      List<String> processedNodes = Lists.newArrayList();
      for (String nodeName : nodeNames) {
         processedNodes.add(nodeName);
         node = getNode(nodeName, node, Joiner.on('.').join(processedNodes));
      }

      return node;
   }

   public LogConfig getLogConfig(String path) {
      return initialConfig.getLogConfig(path);
   }

   private LogConfigNode getNode(String nodeName, LogConfigNode parent, String fullName) {
      LogConfigNode newNode = parent.getChild(nodeName);
      if (newNode == null) {
         newNode = new StandardConfigNode(parent, initialConfig.getLogConfig(fullName));
         parent.addChild(nodeName, newNode);
      }

      return newNode;
   }
}
