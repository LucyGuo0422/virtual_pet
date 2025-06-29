package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import pet.model.HealthStatus;
import pet.model.Pet;
import pet.model.mystery.CommonBox;
import pet.model.mystery.MysteryBox;
import pet.model.mystery.MysteryBoxSystem;
import pet.model.mystery.RareBox;

/**
 * JUnit test class for the MysteryBoxSystem.
 */
public class MysteryBoxTest {

  private MysteryBoxSystem mysteryBoxSystem;

  /**
   * Sets up the test environment before each test.
   */
  @Before
  public void setUp() {
    mysteryBoxSystem = new MysteryBoxSystem();
  }

  /**
   * Test the creation of the MysteryBoxSystem.
   */
  @Test
  public void testMysteryBoxSystemCreation() {
    assertNotNull("Mystery box system should be created", mysteryBoxSystem);
  }

  /**
   * Test the generation of a random box.
   */
  @Test
  public void testGenerateRandomBox() {
    MysteryBox box = mysteryBoxSystem.generateRandomBox();
    assertNotNull("Should generate a box", box);
    assertTrue("Box should be either CommonBox or RareBox",
        box instanceof CommonBox || box instanceof RareBox);
  }

  /**
   * Test the properties of the CommonBox.
   */
  @Test
  public void testCommonBoxProperties() {
    MysteryBox box = new CommonBox();
    assertEquals("Common Mystery Box", box.getName());
    assertEquals("A common mystery box. Contains minor helpful (or sometimes harmful) surprises.",
        box.getDescription());
    assertEquals("res/images/boxes/common_box.png", box.getImagePath());
  }

  /**
   * Test the properties of the RareBox.
   */
  @Test
  public void testRareBoxProperties() {
    MysteryBox box = new RareBox();
    assertEquals("Uncommon Mystery Box", box.getName());
    assertEquals("An uncommon mystery box with more significant effects.",
        box.getDescription());
    assertEquals("res/images/boxes/uncommon_box.png", box.getImagePath());
  }

  /**
   * Test the effects of opening a CommonBox.
   */
  @Test
  public void testCommonBoxEffects() {
    Pet pet = new Pet("TestPet");
    CommonBox box = new CommonBox();

    // Save initial health state
    HealthStatus initialHealth = pet.getHealth();
    int initialHunger = initialHealth.getHunger();
    int initialHygiene = initialHealth.getHygiene();
    int initialSocial = initialHealth.getSocial();
    int initialSleep = initialHealth.getSleep();

    String result = box.open(pet);

    // Verify the result is one of the possible outcomes
    boolean validOutcome = false;
    String[] expectedOutcomes = {
        "Found a small snack! (-5 hunger)",
        "Discovered a minor toy! (+5 social)",
        "Got a bit dirty... (-5 hygiene)",
        "Took a little extra energy (-5 sleep)",
        "Small treat! (-3 hunger, +3 social)"
    };

    for (String expected : expectedOutcomes) {
      if (result.equals(expected)) {
        validOutcome = true;
        break;
      }
    }

    assertTrue("Result should be a valid outcome", validOutcome);

    // Verify that health values have changed appropriately
    HealthStatus newHealth = pet.getHealth();

    // We don't know which outcome was selected, so we can only check that something changed
    boolean healthChanged =
        initialHunger != newHealth.getHunger() || initialHygiene != newHealth.getHygiene()
            || initialSocial != newHealth.getSocial() || initialSleep != newHealth.getSleep();

    assertTrue("At least one health value should have changed", healthChanged);
  }

  /**
   * Test the effects of opening a RareBox.
   */
  @Test
  public void testRareBoxEffects() {
    Pet pet = new Pet("TestPet");
    RareBox box = new RareBox();

    // Save initial health state
    HealthStatus initialHealth = pet.getHealth();
    int initialHunger = initialHealth.getHunger();
    int initialHygiene = initialHealth.getHygiene();
    int initialSocial = initialHealth.getSocial();
    int initialSleep = initialHealth.getSleep();

    String result = box.open(pet);

    // Verify the result is one of the possible outcomes
    boolean validOutcome = false;
    String[] expectedOutcomes = {
        "Found a healthy meal! (-10 hunger)",
        "Found a fun puzzle! (+10 social, -5 sleep)",
        "Discovered a shower kit! (+10 hygiene)",
        "Found a comfy pillow! (+10 sleep)",
        "Oh no! Box contained a stinky surprise! (-10 hygiene, +5 social)"
    };

    for (String expected : expectedOutcomes) {
      if (result.equals(expected)) {
        validOutcome = true;
        break;
      }
    }

    assertTrue("Result should be a valid outcome", validOutcome);

    // Verify that health values have changed appropriately
    HealthStatus newHealth = pet.getHealth();

    // We don't know which outcome was selected, so we can only check that something changed
    boolean healthChanged =
        initialHunger != newHealth.getHunger() || initialHygiene != newHealth.getHygiene()
            || initialSocial != newHealth.getSocial() || initialSleep != newHealth.getSleep();

    assertTrue("At least one health value should have changed", healthChanged);
  }

  /**
   * Test the outcome of opening a CommonBox with a specific effect.
   */
  @Test
  public void testCommonBoxSnackOutcome() {
    Pet pet = new Pet("TestPet");
    int initialHunger = pet.getHealth().getHunger();

    // Create a box with a fixed outcome for snack
    CommonBox box = new CommonBox() {
      @Override
      public String open(Pet pet) {
        // Force the first outcome (snack)
        pet.applyHealthImpact(-5, 0, 0, 0);
        return "Found a small snack! (-5 hunger)";
      }
    };

    String result = box.open(pet);
    assertEquals("Found a small snack! (-5 hunger)", result);

    // Verify the hunger was reduced by 5
    assertEquals(initialHunger - 5, pet.getHealth().getHunger());

    // Other health values should remain unchanged
    assertEquals(50, pet.getHealth().getHygiene()); // Default is 50
    assertEquals(50, pet.getHealth().getSocial());  // Default is 50
    assertEquals(50, pet.getHealth().getSleep());   // Default is 50
  }

  /**
   * Test the outcome of opening a RareBox with a specific effect.
   */
  @Test
  public void testRareBoxShowerKitOutcome() {
    Pet pet = new Pet("TestPet");
    int initialHygiene = pet.getHealth().getHygiene();

    // Create a box with a fixed outcome for shower kit
    RareBox box = new RareBox() {
      @Override
      public String open(Pet pet) {
        // Force the shower kit outcome
        pet.applyHealthImpact(0, 10, 0, 0);
        return "Discovered a shower kit! (+10 hygiene)";
      }
    };

    String result = box.open(pet);
    assertEquals("Discovered a shower kit! (+10 hygiene)", result);

    // Verify the hygiene was increased by 10
    assertEquals(initialHygiene + 10, pet.getHealth().getHygiene());

    // Other health values should remain unchanged
    assertEquals(50, pet.getHealth().getHunger()); // Default is 50
    assertEquals(50, pet.getHealth().getSocial());  // Default is 50
    assertEquals(50, pet.getHealth().getSleep());   // Default is 50
  }

  /**
   * Test the outcome of opening a RareBox with multiple effects.
   */
  @Test
  public void testRareBoxMultipleEffects() {
    Pet pet = new Pet("TestPet");
    int initialHygiene = pet.getHealth().getHygiene();
    int initialSocial = pet.getHealth().getSocial();

    // Create a box with a fixed outcome for stinky surprise
    RareBox box = new RareBox() {
      @Override
      public String open(Pet pet) {
        // Force the stinky surprise outcome
        pet.applyHealthImpact(0, -10, 5, 0);
        return "Oh no! Box contained a stinky surprise! (-10 hygiene, +5 social)";
      }
    };

    String result = box.open(pet);
    assertEquals("Oh no! Box contained a stinky surprise! (-10 hygiene, +5 social)", result);

    // Verify multiple health effects were applied
    assertEquals(initialHygiene - 10, pet.getHealth().getHygiene());
    assertEquals(initialSocial + 5, pet.getHealth().getSocial());

    // Other health values should remain unchanged
    assertEquals(50, pet.getHealth().getHunger()); // Default is 50
    assertEquals(50, pet.getHealth().getSleep());   // Default is 50
  }
}