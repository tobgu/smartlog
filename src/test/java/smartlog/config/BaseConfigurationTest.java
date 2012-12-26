package smartlog.config;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

import smartlog.LogLevel;
import smartlog.front.config.InitialConfiguration;
import smartlog.front.config.LogConfig;

public class BaseConfigurationTest {

   private static final String DUMPBUFFER_ENABLED_PROPERTY = "smartlog.dumpbuffer.enabled";
   private static final String DUMPBUFFER_ATLEVEL_PROPERTY = "smartlog.dumpbuffer.atlevel";
   private static final String DUMPBUFFER_SIZE_PROPERTY = "smartlog.dumpbuffer.size";

   @Test
   public void well_Formed_Properties_Are_Read_Correctly_Mortum_Buffer_Enabled() {
      Properties props = new Properties();
      props.setProperty(DUMPBUFFER_ENABLED_PROPERTY, "1");
      props.setProperty(DUMPBUFFER_ATLEVEL_PROPERTY, "ERROR");
      props.setProperty(DUMPBUFFER_SIZE_PROPERTY, "1000");
      props.setProperty("smartlog.test.level", "INFO");

      InitialConfiguration baseConfig = new InitialConfiguration(props);

      assertEquals(1000, baseConfig.getDumpBufferSize());

      LogConfig logConfig = baseConfig.getLogConfig("smartlog.test");
      assertTrue(logConfig.isMortumDumpEnabled());
      assertEquals(LogLevel.INFO.getLevel(), logConfig.getLogLevel());
      assertEquals(LogLevel.ERROR.getLevel(), logConfig.getMortumDumpLevel());
   }

   @Test
   public void well_Formed_Properties_Are_Read_Correctly_Mortum_Buffer_Disabled() {
      Properties props = new Properties();
      props.setProperty(DUMPBUFFER_ENABLED_PROPERTY, "0");
      props.setProperty(DUMPBUFFER_ATLEVEL_PROPERTY, "WARN");
      props.setProperty(DUMPBUFFER_SIZE_PROPERTY, "1000");
      props.setProperty("smartlog.test.level", "TRACE");
      props.setProperty(".level", "WARN");

      InitialConfiguration baseConfig = new InitialConfiguration(props);

      assertEquals(1000, baseConfig.getDumpBufferSize());

      LogConfig logConfig = baseConfig.getLogConfig("smartlog.test");
      assertFalse(logConfig.isMortumDumpEnabled());
      assertEquals(LogLevel.TRACE.getLevel(), logConfig.getLogLevel());
      assertEquals(LogLevel.WARN.getLevel(), logConfig.getMortumDumpLevel());

      LogConfig logConfig2 = baseConfig.getLogConfig("");
      assertEquals(LogLevel.WARN.getLevel(), logConfig2.getLogLevel());

   }

   @Test(expected = IllegalArgumentException.class)
   public void does_Not_Accept_Non_Integers_As_Dump_Buffer_Size() {
      Properties props = new Properties();
      props.setProperty(DUMPBUFFER_SIZE_PROPERTY, "abcd");

      InitialConfiguration baseConfig = new InitialConfiguration(props);
      baseConfig.getDumpBufferSize();
   }

   @Test(expected = IllegalArgumentException.class)
   public void does_Not_Accept_Negative_Integers_As_Dump_Buffer_Size() {
      Properties props = new Properties();
      props.setProperty(DUMPBUFFER_SIZE_PROPERTY, "-1");
      InitialConfiguration baseConfig = new InitialConfiguration(props);
      baseConfig.getDumpBufferSize();
   }

   @Test(expected = IllegalArgumentException.class)
   public void does_Not_Accept_Unknown_Log_Levels() {
      Properties props = new Properties();
      props.setProperty("smartlog.test.level", "ABCD");

      InitialConfiguration baseConfig = new InitialConfiguration(props);
      baseConfig.getLogConfig("smartlog.test");
   }

   @Test(expected = IllegalArgumentException.class)
   public void does_Not_Accept_Unknown_Buffer_Dump_Levels() {
      Properties props = new Properties();
      props.setProperty("smartlog.test.level", "WARN");
      props.setProperty(DUMPBUFFER_ATLEVEL_PROPERTY, "ABCD");

      InitialConfiguration baseConfig = new InitialConfiguration(props);
      baseConfig.getLogConfig("smartlog.test");
   }

   @Test
   public void returns_Null_When_No_Level_Is_Specified_For_Node() {
      Properties props = new Properties();
      InitialConfiguration baseConfig = new InitialConfiguration(props);

      assertNull(baseConfig.getLogConfig("smartlog.test"));
   }

   @Test
   public void sensible_Default_Values_For_Non_Mandatory_Parameters() {
      Properties props = new Properties();
      props.setProperty("smartlog.test.level", "WARN");

      InitialConfiguration baseConfig = new InitialConfiguration(props);
      LogConfig config = baseConfig.getLogConfig("smartlog.test");

      assertFalse(config.isMortumDumpEnabled());
      assertEquals(LogLevel.WARN.getLevel(), config.getMortumDumpLevel());
      assertEquals(1000, baseConfig.getDumpBufferSize());
   }

}
