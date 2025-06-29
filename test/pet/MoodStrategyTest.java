package pet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import pet.enums.Action;
import pet.enums.MoodEnum;
import pet.model.Pet;

/**
 * Test class for the MoodStrategy.
 */
public class MoodStrategyTest {
  private Pet pet;

  /**
   * Sets up the Pet object for testing.
   */
  @Before
  public void setUp() {
    pet = new Pet();
  }

  /**
   * Test the pet's mood changes based on health.
   */
  @Test
  public void testSwitchBehaviourStrategies() {
    pet.interactWith(Action.FEED);
    int hungerAfterNeutral = pet.getHealth().getHunger();

    Pet petHappy = new Pet();
    petHappy.setMood(MoodEnum.HAPPY);
    petHappy.interactWith(Action.FEED);
    int hungerAfterHappy = petHappy.getHealth().getHunger();

    assertTrue(hungerAfterHappy < hungerAfterNeutral);
  }

  /**
   * Test the impacts of happy mood strategy on the pet.
   */
  @Test
  public void testHappyMoodStrategy() {
    Pet petHappy = new Pet();
    petHappy.setMood(MoodEnum.HAPPY);
    petHappy.interactWith(Action.FEED);
    assertEquals(35, petHappy.getHealth().getHunger());

    Pet petHappyPlay = new Pet();
    petHappyPlay.setMood(MoodEnum.HAPPY);
    petHappyPlay.interactWith(Action.PLAY);
    assertEquals(65, petHappyPlay.getHealth().getSocial());
    assertEquals(45, petHappyPlay.getHealth().getSleep());

    Pet petHappyClean = new Pet();
    petHappyClean.setMood(MoodEnum.HAPPY);
    petHappyClean.interactWith(Action.CLEAN);
    assertEquals(65, petHappyClean.getHealth().getHygiene());

    Pet petHappySleep = new Pet();
    petHappySleep.setMood(MoodEnum.HAPPY);
    petHappySleep.interactWith(Action.SLEEP);
    assertEquals(65, petHappySleep.getHealth().getSleep());
  }

  /**
   * Test the impacts of sad mood strategy on the pet.
   */
  @Test
  public void testSadMoodStrategy() {
    Pet petSad = new Pet();
    petSad.setMood(MoodEnum.SAD);
    petSad.interactWith(Action.FEED);
    assertEquals(45, petSad.getHealth().getHunger());

    Pet petSadPlay = new Pet();
    petSadPlay.setMood(MoodEnum.SAD);
    petSadPlay.interactWith(Action.PLAY);
    assertEquals(55, petSadPlay.getHealth().getSocial());
    assertEquals(45, petSadPlay.getHealth().getSleep());

    Pet petSadClean = new Pet();
    petSadClean.setMood(MoodEnum.SAD);
    petSadClean.interactWith(Action.CLEAN);
    assertEquals(55, petSadClean.getHealth().getHygiene());

    Pet petSadSleep = new Pet();
    petSadSleep.setMood(MoodEnum.SAD);
    petSadSleep.interactWith(Action.SLEEP);
    assertEquals(55, petSadSleep.getHealth().getSleep());
  }

  /**
   * Test the impacts of neutral mood strategy on the pet.
   */
  @Test
  public void testNeutralMoodStrategy() {
    pet.setMood(MoodEnum.NEUTRAL);
    pet.interactWith(Action.FEED);
    assertEquals(40, pet.getHealth().getHunger());

    Pet petPlay = new Pet();
    petPlay.setMood(MoodEnum.NEUTRAL);
    petPlay.interactWith(Action.PLAY);
    assertEquals(60, petPlay.getHealth().getSocial());
    assertEquals(45, petPlay.getHealth().getSleep());

    Pet petClean = new Pet();
    petClean.setMood(MoodEnum.NEUTRAL);
    petClean.interactWith(Action.CLEAN);
    assertEquals(60, petClean.getHealth().getHygiene());

    Pet petSleep = new Pet();
    petSleep.setMood(MoodEnum.NEUTRAL);
    petSleep.interactWith(Action.SLEEP);
    assertEquals(60, petSleep.getHealth().getSleep());
  }
}
