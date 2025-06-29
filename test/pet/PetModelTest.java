package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import pet.enums.Action;
import pet.enums.MoodEnum;
import pet.model.HealthStatus;
import pet.model.Pet;

/**
 * A JUnit test class for the Pet class.
 */
public class PetModelTest {
  private Pet pet;

  /**
   * Sets up the Pet object for testing.
   */
  @Before
  public void setUp() {
    pet = new Pet();
  }

  /**
   * Test the hunger increases over time.
   */
  @Test
  public void testHungerIncreasesOverTime() {
    int initialHunger = pet.getHealth().getHunger();

    pet.step();
    int hungerAfterStep = pet.getHealth().getHunger();
    assertTrue(hungerAfterStep > initialHunger);
  }

  /**
   * Test the hunger decreases after feeding.
   */
  @Test
  public void testHungerDecreasesAfterFeeding() {
    int initialHunger = pet.getHealth().getHunger();

    pet.interactWith(Action.FEED);
    int hungerAfterFeed = pet.getHealth().getHunger();
    assertTrue(hungerAfterFeed < initialHunger);
  }

  /**
   * Test the hygiene decreases over time.
   */
  @Test
  public void testHygieneDecreasesOverTime() {
    int initialHygiene = pet.getHealth().getHygiene();

    pet.step();
    int hygieneAfterStep = pet.getHealth().getHygiene();
    assertTrue(hygieneAfterStep < initialHygiene);
  }

  /**
   * Test the hygiene increases after cleaning.
   */
  @Test
  public void testHygieneIncreasesAfterCleaning() {
    int initialHygiene = pet.getHealth().getHygiene();

    pet.interactWith(Action.CLEAN);
    int hygieneAfterClean = pet.getHealth().getHygiene();
    assertTrue(hygieneAfterClean > initialHygiene);
  }

  /**
   * Test the social decreases over time.
   */
  @Test
  public void testSocialDecreasesOverTime() {
    int initialSocial = pet.getHealth().getSocial();

    pet.step();
    int socialAfterStep = pet.getHealth().getSocial();
    assertTrue(socialAfterStep < initialSocial);
  }

  /**
   * Test the social increases after playing.
   */
  @Test
  public void testSocialIncreasesAfterPlaying() {
    int initialSocial = pet.getHealth().getSocial();

    pet.interactWith(Action.PLAY);
    int socialAfterPlay = pet.getHealth().getSocial();
    assertTrue(socialAfterPlay > initialSocial);
  }

  /**
   * Test the sleep decreases over time.
   */
  @Test
  public void testSleepDecreasesOverTime() {
    int initialSleep = pet.getHealth().getSleep();

    pet.step();
    int sleepAfterStep = pet.getHealth().getSleep();
    assertTrue(sleepAfterStep < initialSleep);
  }

  /**
   * Test the sleep increases after sleeping.
   */
  @Test
  public void testSleepIncreasesAfterSleeping() {
    int initialSleep = pet.getHealth().getSleep();

    pet.interactWith(Action.SLEEP);
    int sleepAfterSleep = pet.getHealth().getSleep();
    assertTrue(sleepAfterSleep > initialSleep);
  }

  /**
   * Test a sad and hungry pet becomes happy after feeding.
   */
  @Test
  public void testSadAndHungryPetBecomeHappyAfterFeeding() {
    Pet petSad = new Pet();
    petSad.setMood(MoodEnum.SAD);

    for (int i = 0; i < 5; i++) {
      petSad.step();
    }
    assertEquals(75, petSad.getHealth().getHunger());
    petSad.interactWith(Action.FEED);
    assertEquals(MoodEnum.HAPPY, petSad.getMood());
  }

  /**
   * Test a sad and lonely pet becomes happy after playing.
   */
  @Test
  public void testSadAndLonelyPetBecomeHappyAfterPlaying() {
    Pet petSad = new Pet();
    petSad.setMood(MoodEnum.SAD);

    for (int i = 0; i < 5; i++) {
      petSad.step();
    }
    assertEquals(25, petSad.getHealth().getSocial());
    petSad.interactWith(Action.PLAY);
    assertEquals(MoodEnum.HAPPY, petSad.getMood());
  }

  /**
   * Test a pet that is not fed for too long becomes sad.
   */
  @Test
  public void testPetNotFedTooLongBecomesSad() {
    Pet pet = new Pet();
    for (int i = 0; i < 5; i++) {
      pet.step();
    }
    assertEquals(MoodEnum.SAD, pet.getMood());
  }

  /**
   * Test the pet is asleep after sleeping and awake after waking up.
   */
  @Test
  public void testSleepToggle() {
    assertFalse(pet.isAsleep());

    pet.interactWith(Action.SLEEP);
    assertTrue(pet.isAsleep());
    assertEquals(60, pet.getHealth().getSleep());

    pet.interactWith(Action.SLEEP);
    assertFalse(pet.isAsleep());
    assertEquals(60, pet.getHealth().getSleep());
  }

  /**
   * Test the pet's health decreases if neglected.
   */
  @Test
  public void testNeglectDecreasesHealth() {
    for (int i = 0; i < 2; i++) {
      pet.step();
    }
    assertEquals(60, pet.getHealth().getHunger());
    assertEquals(40, pet.getHealth().getHygiene());
    assertEquals(40, pet.getHealth().getSocial());
    assertEquals(40, pet.getHealth().getSleep());
  }

  /**
   * Test proper care improves mood and health.
   */
  @Test
  public void testProperCareImprovesMoodAndHealth() {
    for (int i = 0; i < 5; i++) {
      pet.interactWith(Action.FEED);
      pet.interactWith(Action.PLAY);
      pet.interactWith(Action.CLEAN);
      pet.interactWith(Action.SLEEP);
      pet.interactWith(Action.SLEEP);
    }

    assertEquals(MoodEnum.HAPPY, pet.getMood());

    HealthStatus health = pet.getHealth();
    assertTrue(health.getHunger() < 30);
    assertTrue(health.getHygiene() > 70);
    assertTrue(health.getSocial() > 70);
    assertTrue(health.getSleep() > 70);
  }

  /**
   * Test pet dies when health reaches zero.
   */
  @Test
  public void testPetDiesWhenHealthReachesZero() {
    while (pet.isAlive()) {
      pet.step();
    }
    HealthStatus health = pet.getHealth();
    assertEquals(100, health.getHunger());
    assertEquals(0, health.getHygiene());
    assertEquals(0, health.getSocial());
    assertEquals(0, health.getSleep());
    assertFalse(pet.isAlive());
  }
}
