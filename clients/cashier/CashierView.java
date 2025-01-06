package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CashierView implements Observer {
    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought/Pay";
    private static final String CLEAR = "Clear"; // New button label

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(CHECK);
    private final JButton theBtBuy = new JButton(BUY);
    private final JButton theBtBought = new JButton(BOUGHT);
    private final JButton theBtClear = new JButton(CLEAR); // New Clear button

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierController cont = null;

    public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter();
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        cp.setBackground(Color.DARK_GRAY); // Set background color to grey
        Font f = new Font("Monospaced", Font.PLAIN, 12);

        // Page title
        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Cashier System");
        pageTitle.setForeground(Color.WHITE);
        pageTitle.setFont(new Font("Arial", Font.BOLD, 16));
        cp.add(pageTitle);

        // Buttons
        theBtCheck.setBounds(16, 25, 90, 25);
        theBtCheck.setBackground(new Color(200, 200, 200)); // Light grey
        theBtCheck.setForeground(Color.BLACK);
        theBtCheck.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText()));
        cp.add(theBtCheck);

        theBtBuy.setBounds(16, 65, 90, 25);
        theBtBuy.setBackground(new Color(200, 200, 200)); // Light grey
        theBtBuy.setForeground(Color.BLACK);
        theBtBuy.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtBuy.addActionListener(e -> cont.doBuy());
        cp.add(theBtBuy);

        theBtBought.setBounds(16, 105, 90, 25);
        theBtBought.setBackground(new Color(200, 200, 200)); // Light grey
        theBtBought.setForeground(Color.BLACK);
        theBtBought.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtBought.addActionListener(e -> cont.doBought());
        cp.add(theBtBought);

        // Clear button
        theBtClear.setBounds(16, 145, 90, 25); // Positioned below the "Bought/Pay" button
        theBtClear.setBackground(new Color(200, 200, 200)); // Light grey
        theBtClear.setForeground(Color.BLACK);
        theBtClear.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtClear.addActionListener(e -> {
            theInput.setText("");     // Clear the input field
            theOutput.setText("");    // Clear the output area
            theAction.setText("");    // Clear the action label
        });
        cp.add(theBtClear);

        // Action label
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(Color.WHITE);
        theAction.setFont(new Font("Arial", Font.PLAIN, 14));
        cp.add(theAction);

        // Input field
        theInput.setBounds(110, 50, 270, 25);
        theInput.setText("");
        theInput.setFont(f);
        cp.add(theInput);

        // Output area
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        theSP.getViewport().add(theOutput);
        cp.add(theSP);

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    public void setController(CashierController c) {
        cont = c;
    }

    @Override
    public void update(Observable modelC, Object arg) {
        CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        Basket basket = model.getBasket();
        if (basket == null) {
            theOutput.setText("Customer's order");
        } else {
            theOutput.setText(basket.getDetails());
        }

        theInput.requestFocus();
    }
}
