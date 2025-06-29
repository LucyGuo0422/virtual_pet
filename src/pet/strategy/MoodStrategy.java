package pet.strategy;

import pet.enums.Action;
import pet.model.Pet;

/**
 * Interface for the mood strategies.
 */
public interface MoodStrategy {
  /**
   * Handles the action based on the mood strategy.
   *
   * @param action the action to handle
   * @param pet    the pet to apply the action to
   */
  void handleAction(Action action, Pet pet);

}
