package pet.model.mystery;

import pet.model.Pet;

/**
 * Represents a mystery box that can affect the pet when opened.
 */
public interface MysteryBox {
  /**
   * Gets the name of this box type.
   *
   * @return the box name
   */
  String getName();

  /**
   * Gets the description of this box type.
   *
   * @return the box description
   */
  String getDescription();

  /**
   * Gets the image resource path for this box.
   *
   * @return the image path
   */
  String getImagePath();

  /**
   * Applies the box effects to the pet.
   *
   * @param pet the pet to affect
   * @return a description of what happened
   */
  String open(Pet pet);
}
