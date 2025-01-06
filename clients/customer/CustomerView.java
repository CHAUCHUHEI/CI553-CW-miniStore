package clients.customer;

import catalogue.Basket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CustomerView implements Observer {
    class Name {
        public static final String CHECK = "Check";
        public static final String CLEAR = "Clear";
    }

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private final JLabel pageTitle = new JLabel();
    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(Name.CHECK);
    private final JButton theBtClear = new JButton(Name.CLEAR);

    private Picture thePicture = new Picture(80, 80);
    private StockReader theStock = null;
    private CustomerController cont = null;

    public CustomerView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReader();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        cp.setBackground(Color.DARK_GRAY); // Background color updated
        Font f = new Font("Monospaced", Font.PLAIN, 12);

        // Title
        pageTitle.setBounds(110, 0, 270, 20);
        pageTitle.setText("Customer Search Stock");
        pageTitle.setForeground(Color.WHITE);
        cp.add(pageTitle);

        // Action label
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(Color.WHITE);
        cp.add(theAction);

        // Input field
        theInput.setBounds(110, 50, 270, 25);
        theInput.setFont(f);
        theInput.setText("");
        cp.add(theInput);

        // Buttons
        theBtCheck.setBounds(16, 25, 90, 25);
        theBtCheck.setBackground(new Color(200, 200, 200)); // Light grey
        theBtCheck.setForeground(Color.BLACK);
        theBtCheck.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText()));
        cp.add(theBtCheck);

        theBtClear.setBounds(16, 65, 90, 25);
        theBtClear.setBackground(new Color(200, 200, 200)); // Light grey
        theBtClear.setForeground(Color.BLACK);
        theBtClear.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        theBtClear.addActionListener(e -> cont.doClear());
        cp.add(theBtClear);

        // Picture area
        thePicture.setBounds(16, 110, 80, 80);
        cp.add(thePicture);

        // Output area
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    public void setController(CustomerController c) {
        cont = c;
    }

    @Override
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        ImageIcon image = model.getPicture();
        if (image == null) {
            thePicture.clear();
        } else {
            thePicture.set(image);
        }

        Basket basket = model.getBasket();
        if (basket != null) {
            theOutput.setText(basket.getDetails());
        } else {
            theOutput.setText("");
        }

        theInput.requestFocus();
    }
}