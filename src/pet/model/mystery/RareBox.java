package pet.model.mystery;

import java.util.Random;
import pet.model.Pet;

/**
 * An uncommon mystery box with moderate effects.
 */
public class RareBox implements MysteryBox {
  private static final String[] OUTCOMES = {
      "Found a healthy meal! (-10 hunger)",
      "Found a fun puzzle! (+10 social, -5 sleep)",
      "Discovered a shower kit! (+10 hygiene)",
      "Found a comfy pillow! (+10 sleep)",
      "Oh no! Box contained a stinky surprise! (-10 hygiene, +5 social)"
  };

  private final Random random = new Random();

  /**
   * Constructor for the RareBox.
   *
   * @return the name of the box
   */
  @Override
  public String getName() {
    return "Uncommon Mystery Box";
  }

  /**
   * Gets the description of the box.
   *
   * @return the description of the box
   */
  @Override
  public String getDescription() {
    return "An uncommon mystery box with more significant effects.";
  }

  /**
   * Gets the image path of the box.
   *
   * @return the image path of the box
   */
  @Override
  public String getImagePath() {
    return "res/images/boxes/uncommon_box.png";
  }

  /**
   * Opens the mystery box and applies its effects to the pet.
   *
   * @param pet the pet to affect
   * @return the result of opening the box
   */
  @Override
  public String open(Pet pet) {
    int outcomeIndex = random.nextInt(OUTCOMES.length);

    // Apply effects based on the outcome
    switch (outcomeIndex) {
      case 0: // Meal
        pet.applyHealthImpact(-10, 0, 0, 0);
        break;
      case 1: // Puzzle
        pet.applyHealthImpact(0, 0, 10, -5);
        break;
      case 2: // Shower kit
        pet.applyHealthImpact(0, 10, 0, 0);
        break;
      case 3: // Pillow
        pet.applyHealthImpact(0, 0, 0, 10);
        break;
      case 4: // Stinky surprise
        pet.applyHealthImpact(0, -10, 5, 0);
        break;
      default:
        // This case should never happen due to the random range
        throw new IllegalStateException("Unexpected outcome index: " + outcomeIndex);
    }

    return OUTCOMES[outcomeIndex];
  }
}
