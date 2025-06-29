package pet.strategy;

import pet.enums.Action;
import pet.model.Pet;

/**
 * Neutral mood strategy for the pet.
 */
public class NeutralMoodStrategy implements MoodStrategy {
  @Override
  public void handleAction(Action action, Pet pet) {
    switch (action) {
      case FEED:
        pet.applyHealthImpact(-10, 0, 0, 0);
        break;
      case PLAY:
        pet.applyHealthImpact(0, 0, +10, -5);
        break;
      case CLEAN:
        pet.applyHealthImpact(0, +10, 0, 0);
        break;
      case SLEEP:
        if (!pet.isAsleep()) {
          pet.setAsleep(true);
          pet.applyHealthImpact(0, 0, 0, +10);
        } else {
          pet.setAsleep(false);
        }
        break;
      default:
        System.out.println("Unknown action received in NeutralMoodStrategy: " + action);
        break;
    }
  }
}