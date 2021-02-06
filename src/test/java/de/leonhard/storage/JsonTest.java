package de.leonhard.storage;

import de.leonhard.storage.internal.exceptions.LightningValidationException;
import de.leonhard.storage.internal.settings.DataType;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTest {

  static Json json;

  @BeforeEach
  @Test
  void setUp() {
    json = new Json("Example", "");
    Assertions.assertEquals("Example.yml", json.getName());
  }

  @Test
  void testGetDataType() {
    Assertions.assertEquals(json.getDataType(), DataType.SORTED);
  }

  @Test
  void testGetters() {
    Object anObject = json.get("Key"); // Default: null
    String aString = json.getString("Key"); // Default: ""
    int anInt = json.getInt("Key"); // Default: 0
    double aDouble = json.getDouble("Key"); // Default: 0.0
    float aFloat = json.getFloat("Key"); // Default: 0.0
    long aLong = json.getLong("Key"); // Default: 0.0

    Optional<String> optionalString = json.find(
        "Key",
        String.class); // If a key is not present an empty optional will be returned
    String getOrDefault = json.getOrDefault("Key", "Default-Value");
    String getOrSetDefault = json.getOrSetDefault(
        "Key",
        "Default-Value-To-Be-Set-If-Not-Yet-Present");

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> json.getEnum("Key", TimeUnit.class));
    Assertions.assertThrows(
        LightningValidationException.class,
        () -> json.getEnum("Key-1", TimeUnit.class));

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
    json.set("Test-Key-1", true);
    Assertions.assertTrue(json.getData().containsKey("Test-Key-1"));
    Assertions.assertTrue(json.contains("Test-Key-1"));
    Assertions.assertTrue(json.getBoolean("Test-Key-1"));
  }

  @AfterAll
  @Test
  static void tearDown() {
    json.clear();
    Assertions.assertTrue(json.getFile().delete());
  }
}
