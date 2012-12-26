package smartlog.front;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import smartlog.Logger;
import smartlog.format.Formatter;
import smartlog.format.StandardFormatter;
import smartlog.front.config.ConfigurationContainer;
import smartlog.front.config.InitialConfiguration;
import smartlog.front.config.LogConfigNode;
import smartlog.front.config.StaticConfiguration;

import com.google.common.collect.Maps;

public class StandardLoggerFactory {

   private final StaticConfiguration configContainer;
   private final Map<String, Logger> loggers = Maps.newHashMap();
   private final Formatter formatter;
   private final MortumDumpBuffer dumpBuffer;

   // Package private, use factory method
   StandardLoggerFactory(StaticConfiguration configContainer, Formatter formatter, MortumDumpBuffer dumpBuffer) {
      this.configContainer = configContainer;
      this.formatter = formatter;
      this.dumpBuffer = dumpBuffer;
   }

   public static StandardLoggerFactory getInstance() {
      String configFile = System.getProperty("smartlog.config.file");
      InitialConfiguration configuration = new InitialConfiguration(readConfiguration(configFile));
      ConfigurationContainer configContainer = new ConfigurationContainer(configuration);
      StandardFormatter formatter = new StandardFormatter();
      MortumDumpBuffer dumpBuffer = new MortumDumpBuffer(configuration.getDumpBufferSize());
      return new StandardLoggerFactory(configContainer, formatter, dumpBuffer);
   }

   private static Properties readConfiguration(String configFile) {
      Properties config = new Properties();

      try {
         FileReader fileReader = new FileReader(configFile);
         BufferedReader bufferedReader = new BufferedReader(fileReader);
         config.load(bufferedReader);
      } catch (FileNotFoundException e) {
         log("Log configuration file " + configFile + " not found.");
      } catch (IOException e) {
         log("Failed reading configuration file " + configFile + ": " + e.getMessage());
      }

      return config;
   }

   private static void log(String message) {
      System.out.println(message);
   }

   public synchronized Logger getLogger(String name) {
      // We might want to find alternatives to synchronizing later but for now keep it simple
      Logger logger = loggers.get(name);
      if (logger == null) {
         logger = createLogger(name);
         loggers.put(name, logger);
      }

      return logger;
   }

   private Logger createLogger(String fullName) {
      // TODO: Input validation?
      int lastDotPos = Math.max(fullName.lastIndexOf("."), 0);
      String parentNode = fullName.substring(0, lastDotPos);
      String className = fullName.substring(lastDotPos + 1);

      LogConfigNode parent = configContainer.getConfigNode(parentNode);
      StandardLogger logger = new StandardLogger(formatter, dumpBuffer, configContainer.getLogConfig(fullName), parent);
      loggers.put(fullName, logger);
      parent.addChild(className, logger);

      return logger;
   }

}
