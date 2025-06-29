package pet.model.mystery;

import java.util.Random;
import pet.model.Pet;

/**
 * A common mystery box with minor effects.
 */
public class CommonBox implements MysteryBox {
  private static final String[] OUTCOMES = {
      "Found a small snack! (-5 hunger)",
      "Discovered a minor toy! (+5 social)",
      "Got a bit dirty... (-5 hygiene)",
      "Took a little extra energy (-5 sleep)",
      "Small treat! (-3 hunger, +3 social)"
  };

  private final Random random = new Random();

  /**
   * Constructor for the CommonBox.
   *
   * @return the name of the box
   */
  @Override
  public String getName() {
    return "Common Mystery Box";
  }

  /**
   * Gets the description of the box.
   *
   * @return the description of the box
   */
  @Override
  public String getDescription() {
    return "A common mystery box. Contains minor helpful (or sometimes harmful) surprises.";
  }

  /**
   * Gets the image path of the box.
   *
   * @return the image path of the box
   */
  @Override
  public String getImagePath() {
    return "res/images/boxes/common_box.png";
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
      case 0: // Snack
        pet.applyHealthImpact(-5, 0, 0, 0);
        break;
      case 1: // Toy
        pet.applyHealthImpact(0, 0, 5, 0);
        break;
      case 2: // Dirty
        pet.applyHealthImpact(0, -5, 0, 0);
        break;
      case 3: // Tired
        pet.applyHealthImpact(0, 0, 0, -5);
        break;
      case 4: // Small treat
        pet.applyHealthImpact(-3, 0, 3, 0);
        break;
      default:
        // This case should never happen due to the random range
        throw new IllegalStateException("Unexpected outcome index: " + outcomeIndex);
    }

    return OUTCOMES[outcomeIndex];
  }
}
