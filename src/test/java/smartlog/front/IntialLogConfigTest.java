package smartlog.front;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static smartlog.LogLevel.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import smartlog.LogLevel;
import smartlog.Logger;
import smartlog.front.config.ConfigurationContainer;
import smartlog.front.config.InitialConfiguration;
import smartlog.front.config.LogConfig;

/**
 * Integrating test verifying the initial log configuration.
 */
@RunWith(MockitoJUnitRunner.class)
public class IntialLogConfigTest {

   @Mock
   private InitialConfiguration config;

   private StandardLoggerFactory factory;

   @Before
   public void setup() {
      factory = new StandardLoggerFactory(new ConfigurationContainer(config), null, null);
   }

   @Test
   public void configuration_For_Specific_Class_Exists() {
      set("test.Class", WARN);

      Logger logger = factory.getLogger("test.Class");

      assertTrue(logger.isLoggable(WARN));
      assertFalse(logger.isLoggable(INFO));
   }

   @Test
   public void gets_Most_Specific_Configuration() {
      set("test", INFO);
      set("test.some.more", WARN);

      Logger logger = factory.getLogger("test.some.more.Class");

      assertTrue(logger.isLoggable(WARN));
      assertFalse(logger.isLoggable(INFO));
   }

   @Test
   public void default_Config_Used_When_No_More_Specific_Configuration_Exists() {
      set("", TRACE);
      set("test", INFO);
      set("test.some.more", WARN);

      // Factory needs to be created here since the root configuration is read at creation
      factory = new StandardLoggerFactory(new ConfigurationContainer(config), null, null);
      Logger logger = factory.getLogger("another.package.Class");

      assertTrue(logger.isLoggable(TRACE));
   }

   @Test
   public void class_In_Default_Package_Receives_Correct_Log_Config() {
      set("", TRACE);

      factory = new StandardLoggerFactory(new ConfigurationContainer(config), null, null);
      Logger logger = factory.getLogger("Class");

      assertTrue(logger.isLoggable(TRACE));
   }

   private void set(String path, LogLevel level) {
      when(config.getLogConfig(path)).thenReturn(new LogConfig(level.getLevel(), WARN.getLevel(), false));
   }
}
