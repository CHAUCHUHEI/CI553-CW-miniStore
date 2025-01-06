package clients.cashier;

/**
 * Controller for the Cashier Client.
 * It handles user input and communicates with the model.
 */
public class CashierController {
    private CashierModel model = null;
    private CashierView view = null;

    /**
     * Constructor to initialize the controller.
     * @param model The model for managing data and logic.
     * @param view The view for the user interface.
     */
    public CashierController(CashierModel model, CashierView view) {
        this.view = view;
        this.model = model;
    }

    /**
     * Handles the "Check" action.
     * Plays a welcome sound when the button is clicked.
     * @param pn The product number to check.
     */
    public void doCheck(String pn) {
        model.doCheck(pn);
        SoundPlayer.playSound("resources/welcome.wav"); // Path to "welcome" sound file
    }

    /**
     * Handles the "Buy" action.
     */
    public void doBuy() {
        model.doBuy();
    }

    /**
     * Handles the "Bought/Pay" action.
     * Plays a thank-you sound when the button is clicked.
     */
    public void doBought() {
        model.doBought();
        SoundPlayer.playSound("resources/thank_you.wav"); // Path to "thank you" sound file
    }

    /**
     * Handles the "Clear" action.
     */
    public void doClear() {
        model.doClear();
    }
}