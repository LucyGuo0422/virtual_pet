package pet.model;

import pet.enums.Action;
import pet.enums.MoodEnum;

/**
 * Interface for the pet.
 */
public interface PetInterface {
  void step();

  void interactWith(Action action);

  HealthStatus getHealth();

  MoodEnum getMood();

  void setMood(MoodEnum mood);
}
