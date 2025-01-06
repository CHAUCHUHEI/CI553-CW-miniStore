package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import java.util.Observable;

/**
 * Model for the Cashier Client.
 * It handles business logic and communicates with the middle layer.
 */
public class CashierModel extends Observable {
    private enum State {process, checked}

    private State theState = State.process; // Current state
    private Product theProduct = null;     // Product being processed
    private Basket theBasket = null;       // Basket for the current order

    private String pn = "";                // Product number being checked

    private StockReadWriter theStock = null;   // Stock system interface
    private OrderProcessing theOrder = null;  // Order processing interface

    /**
     * Constructor initializes the model with the middle layer factory.
     * @param mf The middle layer factory.
     */
    public CashierModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter();
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        theState = State.process;
    }

    public Basket getBasket() {
        return theBasket;
    }

    public void doCheck(String productNum) {
        String theAction = "";
        theState = State.process;
        pn = productNum.trim();
        int amount = 1;

        try {
            if (theStock.exists(pn)) {
                Product pr = theStock.getDetails(pn);
                if (pr.getQuantity() >= amount) {
                    theAction = String.format("%s : %7.2f (%2d)", pr.getDescription(), pr.getPrice(), pr.getQuantity());
                    theProduct = pr;
                    theProduct.setQuantity(amount);
                    theState = State.checked;
                } else {
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCheck", e.getMessage());
            theAction = e.getMessage();
        }

        setChanged();
        notifyObservers(theAction);
    }

    public void doBuy() {
        String theAction = "";
        int amount = 1;

        try {
            if (theState != State.checked) {
                theAction = "please check its availability";
            } else {
                boolean stockBought = theStock.buyStock(theProduct.getProductNum(), theProduct.getQuantity());
                if (stockBought) {
                    makeBasketIfReq();
                    theBasket.add(theProduct);
                    theAction = "Purchased " + theProduct.getDescription();
                } else {
                    theAction = "!!! Not in stock";
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s", "CashierModel.doBuy", e.getMessage());
            theAction = e.getMessage();
        }

        theState = State.process;
        setChanged();
        notifyObservers(theAction);
    }

    public void doBought() {
        String theAction = "";
        try {
            if (theBasket != null && theBasket.size() >= 1) {
                theOrder.newOrder(theBasket);  // Submit the order
                theBasket = null;
            }
            theAction = "Start New Order";
            theState = State.process;
            theBasket = null;
        } catch (OrderException e) {
            DEBUG.error("%s\n%s", "CashierModel.doCancel", e.getMessage());
            theAction = e.getMessage();
        }

        theBasket = null;  // Clear the basket
        setChanged();
        notifyObservers(theAction);
    }

    public void doClear() {
        theBasket = null;  // Reset the basket
        theState = State.process;  // Reset the state
        setChanged();
        notifyObservers("Basket cleared.");
    }

    public void askForUpdate() {
        setChanged();
        notifyObservers("Welcome");
    }

    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber();
                theBasket = makeBasket();
                theBasket.setOrderNum(uon);
            } catch (OrderException e) {
                DEBUG.error("Comms failure\nCashierModel.makeBasket()\n%s", e.getMessage());
            }
        }
    }

    protected Basket makeBasket() {
        return new Basket();
    }
}