package controller;

import pet.enums.Action;
import pet.enums.MoodEnum;
import pet.model.HealthStatus;
import pet.model.Pet;
import pet.model.mystery.MysteryBox;
import pet.model.mystery.MysteryBoxSystem;
import view.PetView;

/**
 * Controller for the Virtual Pet.
 * Handles user actions and serves as the intermediary between Model and View
 * Directly attaches action listeners to view components
 */
public class PetController {
  private final Pet pet;
  private final PetView view;
  private final MysteryBoxSystem mysteryBoxSystem;

  /**
   * Creates a new pet controller with both model and view references.
   *
   * @param pet  The pet model
   * @param view The pet view
   */
  public PetController(Pet pet, PetView view) {
    this.pet = pet;
    this.view = view;
    this.mysteryBoxSystem = new MysteryBoxSystem();

    // Attach action listeners to view buttons
    attachViewListeners();

    // Initialize the view with current model state
    updateView();
  }

  /**
   * Attaches action listeners to all view components.
   */
  private void attachViewListeners() {
    view.getFeedButton().addActionListener(e -> handleAction(Action.FEED));
    view.getPlayButton().addActionListener(e -> handleAction(Action.PLAY));
    view.getCleanButton().addActionListener(e -> handleAction(Action.CLEAN));
    view.getSleepButton().addActionListener(e -> handleAction(Action.SLEEP));
    view.getStepButton().addActionListener(e -> step());
    view.getMysteryBoxButton().addActionListener(e -> handleMysteryBox());
  }

  /**
   * Handles user actions on the pet.
   *
   * @param action The action to perform
   */
  private void handleAction(Action action) {
    if (pet.isAlive()) {
      if (pet.isAsleep() && action != Action.SLEEP) {
        view.displayMessage(pet.getName() + " is sleeping. Wake them up first!");
        return;
      }

      boolean wasAsleep = pet.isAsleep();

      // Perform the action
      pet.interactWith(action);

      if (action == Action.SLEEP) {
        if (wasAsleep && !pet.isAsleep()) {
          view.clearActionImage();
        } else if (!wasAsleep && pet.isAsleep()) {
          view.showActionImage(action);
        }
      } else if (!pet.isAsleep()) {
        view.showActionImage(action);
      }

      // Log the action based on its type
      String message;
      switch (action) {
        case FEED:
          message = "Fed " + pet.getName();
          break;
        case PLAY:
          message = "Played with " + pet.getName();
          break;
        case CLEAN:
          message = "Cleaned " + pet.getName();
          break;
        case SLEEP:
          if (pet.isAsleep()) {
            message = pet.getName() + " is now sleeping";
          } else {
            message = pet.getName() + " woke up";
          }
          break;
        default:
          message = "Unknown action performed";
          break;
      }
      view.displayMessage(message);

      // Check if the pet died after the action
      checkPetStatus();

      // Update the view to reflect changes
      updateView();
    }
  }

  /**
   * Handles the mystery box action.
   */
  private void handleMysteryBox() {
    if (pet.isAlive()) {
      if (pet.isAsleep()) {
        view.displayMessage(pet.getName() + " is sleeping. Wake them up first!");
        return;
      }

      // Generate a random mystery box
      MysteryBox box = mysteryBoxSystem.generateRandomBox();

      // Ask user if they want to open the box
      boolean openBox = view.displayMysteryBoxDialog(box.getName(), box.getDescription());

      if (openBox) {
        // Open the box and apply its effects
        String result = box.open(pet);
        view.displayMessage(result);

        // Check if the pet died after opening the box
        checkPetStatus();

        // Update the view to reflect changes
        updateView();
      } else {
        view.displayMessage("You decided not to open the mystery box.");
      }
    }
  }

  /**
   * Advances the pet's state by one time step.
   */
  private void step() {
    if (pet.isAlive()) {
      pet.step();
      view.displayMessage("Time passed");

      // Check if the pet died after the step
      checkPetStatus();

      // Update the view to reflect changes
      updateView();
    }
  }

  /**
   * Checks the pet's status and handles any important changes.
   */
  private void checkPetStatus() {
    // Check if the pet died
    if (!pet.isAlive()) {
      view.displayMessage(pet.getName() + " has passed away due to neglect");
      view.displayGameOver();
    }
  }

  /**
   * Checks pet conditions and shows appropriate speech bubbles.
   */
  private void updateSpeechBubble() {
    if (pet == null || view == null) {
      return;
    }

    // Check for various conditions
    if (pet.getHealth().getHunger() > 70) {
      view.showSpeechBubble("I'm starving!");
    } else if (pet.getHealth().getHygiene() < 30) {
      view.showSpeechBubble("I need a bath!");
    } else if (pet.getHealth().getSocial() < 30) {
      view.showSpeechBubble("I'm feeling lonely...");
    } else if (pet.getHealth().getSleep() < 30) {
      view.showSpeechBubble("*yawn* I'm tired...");
    }
  }

  /**
   * Updates all view elements to reflect the current model state.
   */
  private void updateView() {
    // Update health display
    HealthStatus health = pet.getHealth();
    view.updateHealth(String.format("Hunger: %d, Hygiene: %d, Social: %d, Sleep: %d",
        health.getHunger(), health.getHygiene(), health.getSocial(), health.getSleep()));

    // Update mood display
    view.updateMood(pet.getMood().name());

    // Update image
    view.updateImage(pet.getMood().name());

    // Update button states based on pet state
    boolean canInteract = pet.isAlive() && (!pet.isAsleep()); // Allow wake action when asleep
    view.updateButtonStates(canInteract, canInteract, canInteract,
        true, true);

    // Check and update speech bubble
    updateSpeechBubble();
  }

  /**
   * Sets the pet's mood (for debug/testing purposes).
   *
   * @param mood The mood to set
   */
  public void setMood(MoodEnum mood) {
    pet.setMood(mood);
    updateView();
  }

  /**
   * Gets the pet's name.
   *
   * @return The pet's name
   */
  public String getPetName() {
    return pet.getName();
  }

  /**
   * Gets the pet's current health status.
   *
   * @return The health status object
   */
  public HealthStatus getPetHealth() {
    return pet.getHealth();
  }

  /**
   * Gets the pet's current mood.
   *
   * @return The pet's mood
   */
  public MoodEnum getPetMood() {
    return pet.getMood();
  }

  /**
   * Checks if the pet is alive.
   *
   * @return True if the pet is alive, false otherwise
   */
  public boolean isPetAlive() {
    return pet.isAlive();
  }

  /**
   * Checks if the pet is asleep.
   *
   * @return True if the pet is asleep, false otherwise
   */
  public boolean isPetAsleep() {
    return pet.isAsleep();
  }
}