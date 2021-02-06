package de.leonhard.storage;

import de.leonhard.storage.internal.exceptions.LightningValidationException;
import de.leonhard.storage.internal.settings.DataType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigTest {

  static Config config;

  @BeforeEach
  @Test
  void setUp() {
    config = new Config("Example", "");
    Assertions.assertEquals("Example.yml", config.getName());
    Assertions.assertEquals(new ArrayList<>(), config.getHeader());
  }

  @Test
  void testGetDataType() {
    Assertions.assertEquals(config.getDataType(), DataType.SORTED);
  }

  @Test
  void testGetters() {
    Object anObject = config.get("Key"); // Default: null
    String aString = config.getString("Key"); // Default: ""
    int anInt = config.getInt("Key"); // Default: 0
    double aDouble = config.getDouble("Key"); // Default: 0.0
    float aFloat = config.getFloat("Key"); // Default: 0.0
    long aLong = config.getLong("Key"); // Default: 0.0

    Optional<String> optionalString = config.find(
        "Key",
        String.class); // If a key is not present an empty optional will be returned
    String getOrDefault = config.getOrDefault("Key", "Default-Value");
    String getOrSetDefault = config.getOrSetDefault(
        "Key",
        "Default-Value-To-Be-Set-If-Not-Yet-Present");

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> config.getEnum("Key", TimeUnit.class));
    Assertions.assertThrows(LightningValidationException.class,
        () -> config.getEnum("Key-1", TimeUnit.class));

    Assertions.assertNull(anObject);
    Assertions.assertEquals("", aString);
    Assertions.assertEquals(0, anInt);
    Assertions.assertEquals(0.0, aDouble);
    Assertions.assertEquals(0.0f, aFloat);
    Assertions.assertEquals(aLong, 0.0);
    Assertions.assertEquals(Optional.empty(), optionalString);
    Assertions.assertEquals("Default-Value", getOrDefault);
    Assertions.assertEquals("Default-Value-To-Be-Set-If-Not-Yet-Present", getOrSetDefault);
  }

  @Test
  void testSetHeader() {
    config.setHeader("Example-1", "Example-2");
    Assertions.assertEquals(Arrays.asList("#Example-1", "#Example-2"), config.getHeader());
  }

  @Test
  void testSet() {
    config.set("Test-Key-1", true);
    Assertions.assertTrue(config.getData().containsKey("Test-Key-1"));
    Assertions.assertTrue(config.contains("Test-Key-1"));
    Assertions.assertTrue(config.getBoolean("Test-Key-1"));
  }

  @AfterAll
  @Test
  static void tearDown() {
    config.clear();
    Assertions.assertTrue(config.getFile().delete());
  }
}