package pet.model;

/**
 * Class representing the health status of the pet.
 */
public class HealthStatus {
  private final int hunger;
  private final int hygiene;
  private final int social;
  private final int sleep;

  /**
   * Constructor for the health status.
   *
   * @param hunger  the hunger level
   * @param hygiene the hygiene level
   * @param social  the social level
   * @param sleep   the sleep level
   */
  public HealthStatus(int hunger, int hygiene, int social, int sleep) {
    this.hunger = hunger;
    this.hygiene = hygiene;
    this.social = social;
    this.sleep = sleep;
  }

  /**
   * Gets the hunger level.
   *
   * @return the hunger level
   */
  public int getHunger() {
    return hunger;
  }

  /**
   * Gets the hygiene level.
   *
   * @return the hygiene level
   */
  public int getHygiene() {
    return hygiene;
  }

  /**
   * Gets the social level.
   *
   * @return the social level
   */
  public int getSocial() {
    return social;
  }

  /**
   * Gets the sleep level.
   *
   * @return the sleep level
   */
  public int getSleep() {
    return sleep;
  }

  /**
   * Returns a string representation of the health status.
   *
   * @return the string representation
   */
  @Override
  public String toString() {
    return "HealthStatus{" + "hunger=" + hunger + ", hygiene=" + hygiene
        + ", social=" + social + ", sleep=" + sleep + '}';
  }
}
