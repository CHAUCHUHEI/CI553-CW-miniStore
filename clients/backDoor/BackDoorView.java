package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the BackDoor view.
 */
public class BackDoorView implements Observer {
    private static final String RESTOCK = "Add";
    private static final String CLEAR = "Clear";
    private static final String QUERY = "Query";

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width  of window pixels

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextField theInputNo = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtClear = new JButton(CLEAR);
    private final JButton theBtRStock = new JButton(RESTOCK);
    private final JButton theBtQuery = new JButton(QUERY);

    private StockReadWriter theStock = null;
    private BackDoorController cont = null;

    /**
     * Construct the view
     *
     * @param rpc Window in which to construct
     * @param mf  Factory to deliver order and stock objects
     * @param x   x-coordinate of position of window on screen
     * @param y   y-coordinate of position of window on screen
     */
    public BackDoorView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
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

        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Staff check and manage stock");
        pageTitle.setForeground(Color.WHITE);
        cp.add(pageTitle);

        theBtQuery.setBounds(16, 25 + 40 * 0, 90, 25);
        theBtQuery.setBackground(new Color(200, 200, 200)); // Light grey
        theBtQuery.setForeground(Color.BLACK);
        theBtQuery.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtQuery.addActionListener(e -> cont.doQuery(theInput.getText()));
        cp.add(theBtQuery);

        theBtRStock.setBounds(16, 25 + 40 * 1, 90, 25);
        theBtRStock.setBackground(new Color(200, 200, 200)); // Light grey
        theBtRStock.setForeground(Color.BLACK);
        theBtRStock.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtRStock.addActionListener(e -> cont.doRStock(theInput.getText(), theInputNo.getText()));
        cp.add(theBtRStock);

        theBtClear.setBounds(16, 25 + 40 * 2, 90, 25);
        theBtClear.setBackground(new Color(200, 200, 200)); // Light grey
        theBtClear.setForeground(Color.BLACK);
        theBtClear.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtClear.addActionListener(e -> cont.doClear());
        cp.add(theBtClear);

        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(Color.WHITE);
        cp.add(theAction);

        theInput.setBounds(110, 50, 120, 25);
        theInput.setText("");
        cp.add(theInput);

        theInputNo.setBounds(260, 50, 120, 25);
        theInputNo.setText("0");
        cp.add(theInputNo);

        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);
        theSP.getViewport().add(theOutput);
        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    public void setController(BackDoorController c) {
        cont = c;
    }

    /**
     * Update the view, called by notifyObservers(theAction) in model,
     *
     * @param modelC The observed model
     * @param arg    Specific args
     */
    @Override
    public void update(Observable modelC, Object arg) {
        BackDoorModel model = (BackDoorModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }
}
