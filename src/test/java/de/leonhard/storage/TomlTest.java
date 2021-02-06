package de.leonhard.storage;

import de.leonhard.storage.internal.exceptions.LightningValidationException;
import de.leonhard.storage.internal.settings.DataType;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TomlTest {

  static Toml toml;

  @BeforeEach
  @Test
  void setUp() {
    toml = new Toml("Example", "");
    Assertions.assertEquals("Example.yml", toml.getName());
  }

  @Test
  void testGetDataType() {
    Assertions.assertEquals(toml.getDataType(), DataType.SORTED);
  }

  @Test
  void testGetters() {
    Object anObject = toml.get("Key"); // Default: null
    String aString = toml.getString("Key"); // Default: ""
    int anInt = toml.getInt("Key"); // Default: 0
    double aDouble = toml.getDouble("Key"); // Default: 0.0
    float aFloat = toml.getFloat("Key"); // Default: 0.0
    long aLong = toml.getLong("Key"); // Default: 0.0

    Optional<String> optionalString = toml.find(
        "Key",
        String.class); // If a key is not present an empty optional will be returned
    String getOrDefault = toml.getOrDefault("Key", "Default-Value");
    String getOrSetDefault = toml.getOrSetDefault(
        "Key",
        "Default-Value-To-Be-Set-If-Not-Yet-Present");

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> toml.getEnum("Key", TimeUnit.class));
    Assertions.assertThrows(
        LightningValidationException.class,
        () -> toml.getEnum("Key-1", TimeUnit.class));

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
  void testSet() {
    toml.set("Test-Key-1", true);
    Assertions.assertTrue(toml.getData().containsKey("Test-Key-1"));
    Assertions.assertTrue(toml.contains("Test-Key-1"));
    Assertions.assertTrue(toml.getBoolean("Test-Key-1"));
  }

  @AfterAll
  @Test
  static void tearDown() {
    toml.clear();
    Assertions.assertTrue(toml.getFile().delete());
  }
}
