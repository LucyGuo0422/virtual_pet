package main;

import controller.PetController;
import javax.swing.SwingUtilities;
import pet.model.Pet;
import view.PetView;

/**
 * Main class for the Virtual Pet Simulator Application.
 */
public class MyPetMain {
  /**
   * Main method to start the application.
   *
   * @param args Command line arguments (not used)
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      // Create view
      PetView view = new PetView();

      // Display welcome message
      view.displayWelcomeMessage();

      // Get pet name from the view
      String petName = view.promptForPetName();

      // Create model
      Pet pet = new Pet(petName);

      // Create controller with model and view
      PetController controller = new PetController(pet, view);

      // Set controller in view
      view.setController(controller);

      // Show the view
      view.setVisible(true);
    });
  }
}