package pet.strategy;

import pet.enums.Action;
import pet.enums.MoodEnum;
import pet.model.Pet;

/**
 * Strategy for the sad mood.
 */
public class SadMoodStrategy implements MoodStrategy {
  @Override
  public void handleAction(Action action, Pet pet) {
    switch (action) {
      case FEED:
        boolean isHungry = pet.getHealth().getHunger() > 70;
        pet.applyHealthImpact(-5, 0, 0, 0);
        if (isHungry && pet.getMood() == MoodEnum.SAD) {
          pet.setFedWhileSadAndHungry(true);
        }
        break;
      case PLAY:
        boolean isLonely = pet.getHealth().getSocial() < 30;
        pet.applyHealthImpact(0, 0, +5, -5);
        if (isLonely && pet.getMood() == MoodEnum.SAD) {
          pet.setPlayedWhileSadAndLonely(true);
        }
        break;
      case CLEAN:
        pet.applyHealthImpact(0, +5, 0, 0);
        break;
      case SLEEP:
        if (!pet.isAsleep()) {
          pet.setAsleep(true);
          pet.applyHealthImpact(0, 0, 0, +5);
        } else {
          pet.setAsleep(false);
        }
        break;
      default:
        System.out.println("Unknown action received in SadMoodStrategy: " + action);
        break;
    }
  }
}


