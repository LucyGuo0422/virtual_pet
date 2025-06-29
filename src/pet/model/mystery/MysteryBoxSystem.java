package pet.model.mystery;

import java.util.Random;

/**
 * Manages the content of mystery boxes.
 */
public class MysteryBoxSystem {
  private final Random random = new Random();

  /**
   * Creates a random mystery box.
   *
   * @return a random MysteryBox instance
   */
  public MysteryBox generateRandomBox() {
    // Determine box rarity/type
    int roll = random.nextInt(100);

    if (roll < 60) {
      return new CommonBox(); // 60% chance - common box
    } else {
      return new RareBox(); // 40% chance - rare box
    }
  }
}