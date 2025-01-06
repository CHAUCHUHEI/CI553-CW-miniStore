package clients.packing;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PackingView implements Observer {
    private static final String PACKED = "Packed";

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtPack = new JButton(PACKED);

    private OrderProcessing theOrder = null;
    private PackingController cont = null;

    public PackingView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theOrder = mf.makeOrderProcessing(); // Database access
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

        // Title setup
        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Packing Bought Order");
        pageTitle.setForeground(Color.WHITE);
        cp.add(pageTitle);

        // Button setup
        theBtPack.setBounds(16, 25, 90, 25);
        theBtPack.setBackground(new Color(200, 200, 200)); // Light grey
        theBtPack.setForeground(Color.BLACK);
        theBtPack.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtPack.addActionListener(e -> cont.doPacked());
        cp.add(theBtPack);

        // Action label setup
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(Color.WHITE);
        cp.add(theAction);

        // Output scroll pane setup
        theSP.setBounds(110, 50, 270, 200);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        rootWindow.setVisible(true);
    }

    public void setController(PackingController c) {
        cont = c;
    }

    /**
     * Update the view, called by notifyObservers(theAction) in model.
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        PackingModel model = (PackingModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        Basket basket = model.getBasket();
        if (basket != null) {
            theOutput.setText(basket.getDetails());
        } else {
            theOutput.setText("");
        }
    }
}