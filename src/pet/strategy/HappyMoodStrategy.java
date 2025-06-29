package pet.strategy;

import pet.enums.Action;
import pet.model.Pet;

/**
 * Strategy for the happy mood.
 */
public class HappyMoodStrategy implements MoodStrategy {
  @Override
  public void handleAction(Action action, Pet pet) {
    switch (action) {
      case FEED:
        pet.applyHealthImpact(-15, 0, 0, 0);
        break;
      case PLAY:
        pet.applyHealthImpact(0, 0, +15, -5);
        break;
      case CLEAN:
        pet.applyHealthImpact(0, +15, 0, 0);
        break;
      case SLEEP:
        if (!pet.isAsleep()) {
          pet.setAsleep(true);
          pet.applyHealthImpact(0, 0, 0, +15);
        } else {
          pet.setAsleep(false);
        }
        break;
      default:
        System.out.println("Unknown action received in HappyMoodStrategy: " + action);
        break;
    }
  }
}
