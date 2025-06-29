package pet.model;

import pet.enums.Action;
import pet.enums.MoodEnum;
import pet.strategy.HappyMoodStrategy;
import pet.strategy.MoodStrategy;
import pet.strategy.NeutralMoodStrategy;
import pet.strategy.SadMoodStrategy;

/**
 * Represents a pet that can be interacted with.
 */
public class Pet implements PetInterface {

  private static final int MAX_LEVEL = 100;
  private static final int MIN_LEVEL = 0;
  private static final int DECREMENT = 5;
  private static final int NEGLECT_THRESHOLD = 5;
  private static final int HIGH_THRESHOLD = 70;
  private static final int LOW_THRESHOLD = 30;

  private HealthStatus health;
  private MoodEnum mood;
  private MoodStrategy moodStrategy;
  private String name;

  private boolean alive = true;
  private boolean asleep = false;
  private boolean fedWhileSadAndHungry = false;
  private boolean playedWhileSadAndLonely = false;
  private int stepsSinceInteract = 0;

  /**
   * Constructs a new pet with default health status, mood, and name.
   */
  public Pet() {
    this.health = new HealthStatus(MAX_LEVEL / 2, MAX_LEVEL / 2,
        MAX_LEVEL / 2, MAX_LEVEL / 2);
    this.mood = MoodEnum.NEUTRAL;
    this.moodStrategy = new NeutralMoodStrategy();
    this.name = "Buddy";
  }

  /**
   * Constructs a new pet with a specified name.
   *
   * @param name the name for the pet
   */
  public Pet(String name) {
    this();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Updates the pet's health status by one unit of time.
   */
  @Override
  public void step() {
    if (!alive) {
      return;
    }
    stepsSinceInteract++;

    int actualDecrement = (this.mood == MoodEnum.HAPPY) ? DECREMENT / 2 : DECREMENT;

    this.health = new HealthStatus(
        Math.min(MAX_LEVEL, health.getHunger() + actualDecrement),
        Math.max(MIN_LEVEL, health.getHygiene() - actualDecrement),
        Math.max(MIN_LEVEL, health.getSocial() - actualDecrement),
        Math.max(MIN_LEVEL, health.getSleep() - actualDecrement)
    );

    if (health.getHunger() == MAX_LEVEL && health.getHygiene() == MIN_LEVEL
        && health.getSocial() == MIN_LEVEL && health.getSleep() == MIN_LEVEL) {
      alive = false;
      return;
    }

    updateMoodBasedOnHealth();
  }

  /**
   * Interacts with the pet by performing an action.
   *
   * @param action the action to perform
   */
  @Override
  public void interactWith(Action action) {
    if (!alive) {
      return;
    }

    if (asleep && action != Action.SLEEP) {
      return;
    }

    moodStrategy.handleAction(action, this);
    stepsSinceInteract = 0;
    updateMoodBasedOnHealth();
  }

  /**
   * Gets the current health status of the pet.
   *
   * @return the health status
   */
  @Override
  public HealthStatus getHealth() {
    return health;
  }

  /**
   * Gets the current mood of the pet.
   *
   * @return the mood
   */
  @Override
  public MoodEnum getMood() {
    return mood;
  }

  /**
   * Sets the mood of the pet.
   *
   * @param mood the new mood
   */
  @Override
  public void setMood(MoodEnum mood) {
    this.mood = mood;

    // Update the mood strategy based on the new mood.
    switch (mood) {
      case HAPPY:
        moodStrategy = new HappyMoodStrategy();
        break;
      case SAD:
        moodStrategy = new SadMoodStrategy();
        break;
      case NEUTRAL:
      default:
        moodStrategy = new NeutralMoodStrategy();
        break;
    }
  }

  /**
   * Applies the health impact of an action to the pet.
   *
   * @param hungerChange  the change in hunger
   * @param hygieneChange the change in hygiene
   * @param socialChange  the change in social
   * @param sleepChange   the change in sleep
   */
  public void applyHealthImpact(int hungerChange, int hygieneChange, int socialChange,
                                int sleepChange) {
    this.health = new HealthStatus(
        Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, health.getHunger() + hungerChange)),
        Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, health.getHygiene() + hygieneChange)),
        Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, health.getSocial() + socialChange)),
        Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, health.getSleep() + sleepChange))
    );
  }

  /**
   * Returns whether the pet was fed while sad and hungry.
   *
   * @return true if the pet was fed while sad and hungry, false otherwise
   */
  public boolean isFedWhileSadAndHungry() {
    return fedWhileSadAndHungry;
  }

  /**
   * Sets whether the pet was fed while sad and hungry.
   *
   * @param fed true if the pet was fed while sad and hungry, false otherwise
   */
  public void setFedWhileSadAndHungry(boolean fed) {
    this.fedWhileSadAndHungry = fed;
  }

  /**
   * Returns whether the pet was played with while sad and lonely.
   *
   * @return true if the pet was played with while sad and lonely, false otherwise
   */
  public boolean isPlayedWhileSadAndLonely() {
    return playedWhileSadAndLonely;
  }

  /**
   * Sets whether the pet was played with while sad and lonely.
   *
   * @param played true if the pet was played with while sad and lonely, false otherwise
   */
  public void setPlayedWhileSadAndLonely(boolean played) {
    this.playedWhileSadAndLonely = played;
  }

  /**
   * Returns whether the pet is alive.
   *
   * @return true if the pet is alive, false otherwise
   */
  public boolean isAlive() {
    return alive;
  }

  /**
   * Returns whether the pet is asleep.
   *
   * @return true if the pet is asleep, false otherwise
   */
  public boolean isAsleep() {
    return asleep;
  }

  /**
   * Sets whether the pet is asleep.
   *
   * @param asleep true if the pet is asleep, false otherwise
   */
  public void setAsleep(boolean asleep) {
    this.asleep = asleep;
  }

  /**
   * Update the mood based on the health status of the pet.
   */
  private void updateMoodBasedOnHealth() {
    // If the pet was fed while sad and hungry, set mood to HAPPY
    if (fedWhileSadAndHungry) {
      setMood(MoodEnum.HAPPY);
      fedWhileSadAndHungry = false;
      return;
      // If the pet was played with while sad and lonely, set mood to HAPPY
    } else if (playedWhileSadAndLonely) {
      setMood(MoodEnum.HAPPY);
      playedWhileSadAndLonely = false;
      return;
    }

    // If the pet hasn't been fed for too long, set mood to SAD
    if (stepsSinceInteract >= NEGLECT_THRESHOLD) {
      setMood(MoodEnum.SAD);
      return;
    }

    // Update the mood based on the health status of the pet.
    if (health.getHunger() > HIGH_THRESHOLD || health.getHygiene() < LOW_THRESHOLD
        || health.getSocial() < LOW_THRESHOLD || health.getSleep() < LOW_THRESHOLD) {
      setMood(MoodEnum.SAD);
    } else if (health.getHunger() < LOW_THRESHOLD && health.getHygiene() > HIGH_THRESHOLD
        && health.getSocial() > HIGH_THRESHOLD && health.getSleep() > HIGH_THRESHOLD) {
      setMood(MoodEnum.HAPPY);
    } else {
      setMood(MoodEnum.NEUTRAL);
    }
  }

}
