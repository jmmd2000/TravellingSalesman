// James Doyle
// 19496366
// CS211 Final Project - 26/5/2021
// Travelling Salesman Problem

import java.awt.Font;
import java.awt.Color;
import java.awt.Panel;
import java.awt.Cursor;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.util.*;

public class tsp {

    private static final int WIN_WIDTH = 500;
    private static final int WIN_HEIGHT = 400;
    private static Window window;

    public static void main(String[] args) {
        window = new Window(WIN_WIDTH, WIN_HEIGHT, "Apache Pizza Route Planner");
    }

}

class Window {
    // Declaration of GUI components
    JFrame window;
    Panel outputPanel, inputPanel, minutesPanel, buttonPanel;
    JLabel header1, header2;
    JTextArea inputField, outputField1, outputField2;
    Button submit, reset;
    Font font;
    ImageIcon img = new ImageIcon("LOGO.png");

    // Arraylists
    static ArrayList<String> orderNum = new ArrayList<>();
    static ArrayList<String> address = new ArrayList<>();
    static ArrayList<String> mins = new ArrayList<>();
    static ArrayList<String> northGPS = new ArrayList<>();
    static ArrayList<String> westGPS = new ArrayList<>();
    static ArrayList<String> finalRoute = new ArrayList<>();
    static ArrayList<String> unvisited = new ArrayList<>();
    static ArrayList<String> visited = new ArrayList<>();
    static ArrayList<Double> routeDistance = new ArrayList<>();

    // Setup of window
    Window(int width, int height, String title) {
        window = new JFrame();
        window.setBounds(0, 0, width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setCursor(new Cursor(Cursor.HAND_CURSOR));
        window.setTitle(title);
        window.setResizable(false);
        window.setLayout(null);
        window.setIconImage(img.getImage());

        createOutputPanel();
        createInputPanel();
        createMinutesPanel();
        createButtonPanel();

        window.add(outputPanel);
        window.add(inputPanel);
        window.add(minutesPanel);
        window.add(buttonPanel);

        window.setVisible(true);
    }

    // PANEL FOR MAIN OUTPUT FIELD
    private void createOutputPanel() {
        outputPanel = new Panel();
        outputPanel.setBackground(new Color(223, 29, 37));
        outputPanel.setBounds(0, 160, 150, 240);
        outputPanel.setLayout(null);

        // String to be displayed as title for this panel
        String routeHeader = "<html>Optimal Route</html>";

        header1 = new JLabel(routeHeader);
        header1.setBounds(25, 10, 100, 20);
        header1.setFont(new Font("SansSerif", Font.BOLD, 14));
        header1.setFocusable(false);
        header1.setForeground(Color.WHITE);

        // Create the text field
        outputField1 = new JTextArea();
        outputField1.setBounds(10, 50, 130, 130);
        outputField1.setFont(new Font("SansSerif", Font.BOLD, 8));
        outputField1.setFocusable(true);
        outputPanel.add(header1);
        outputPanel.add(outputField1);
        outputField1.setLineWrap(true);
        outputField1.setWrapStyleWord(true);

        header1.setVisible(true);
        outputField1.setVisible(true);
        outputPanel.setVisible(true);

    }

    // PANEL FOR ANGRY MINUTES TEXT FIELD
    private void createMinutesPanel() {
        minutesPanel = new Panel();
        minutesPanel.setBackground(new Color(223, 29, 37));
        minutesPanel.setBounds(150, 160, 150, 240);
        minutesPanel.setLayout(null);

        // String to be displayed as title for this panel
        String minutesHeader = "<html>Angry Minutes</html>";

        header1 = new JLabel(minutesHeader);
        header1.setBounds(25, 10, 100, 20);
        header1.setFont(new Font("SansSerif", Font.BOLD, 14));
        header1.setFocusable(false);
        header1.setForeground(Color.WHITE);

        // Create the text field
        outputField2 = new JTextArea();
        outputField2.setBounds(10, 50, 130, 130);
        outputField2.setFont(new Font("SansSerif", Font.BOLD, 18));
        outputField2.setFocusable(true);
        minutesPanel.add(header1);
        minutesPanel.add(outputField2);

        header1.setVisible(true);
        outputField2.setVisible(true);
        minutesPanel.setVisible(true);

    }

    // PANEL FOR MAIN INPUT FIELD
    private void createInputPanel() {
        inputPanel = new Panel();
        inputPanel.setBounds(0, 0, 500, 160);
        inputPanel.setBackground(new Color(223, 29, 37));
        inputPanel.setLayout(null);

        // String to be displayed as title for this panel
        String inputHeader = "<html>Input</html>";

        header2 = new JLabel(inputHeader);
        header2.setBounds(210, 10, 50, 30);
        header2.setFont(new Font("SansSerif", Font.BOLD, 20));
        header2.setFocusable(false);
        header2.setForeground(Color.WHITE);

        // Create the text field
        inputField = new JTextArea();
        inputField.setBounds(10, 50, 460, 100);
        inputField.setFont(new Font("SansSerif", Font.BOLD, 18));
        inputPanel.add(header2);
        inputPanel.add(inputField);

        inputPanel.setVisible(true);
        inputField.setVisible(true);
    }

    // PANEL FOR BUTTONS
    private void createButtonPanel() {
        buttonPanel = new Panel();
        buttonPanel.setBackground(new Color(223, 29, 37));
        buttonPanel.setBounds(300, 160, 200, 240);
        buttonPanel.setLayout(null);

        String buttonHeader = "<html>Controls</html>";

        header1 = new JLabel(buttonHeader);
        header1.setBounds(60, 10, 100, 20);
        header1.setFont(new Font("SansSerif", Font.BOLD, 14));
        header1.setFocusable(false);
        header1.setForeground(Color.WHITE);

        submit = new Button("Submit");
        submit.setBounds(10, 50, 165, 60);
        submit.setFocusable(true);
        submit.setFont(new Font("SansSerif", Font.BOLD, 24));

        submit.addActionListener(new ActionListener() {
            // when the button is clicked, begin the sorting of the input
            @Override
            public void actionPerformed(ActionEvent e) {
                String string = inputField.getText();
                chop(string);
            }
        });

        reset = new Button("Reset");
        reset.setBounds(10, 120, 165, 60);
        reset.setFocusable(true);
        reset.setFont(new Font("SansSerif", Font.BOLD, 24));
        reset.addActionListener(new ActionListener() {
            // when the button is clicked, clear the input boxes
            @Override
            public void actionPerformed(ActionEvent e) {
                inputField.setText("");
                outputField1.setText("");
                outputField2.setText("");
            }
        });

        // creates the panel and adds all the above inner components
        buttonPanel.add(header1);
        buttonPanel.add(submit);
        buttonPanel.add(reset);

        header1.setVisible(true);
        submit.setVisible(true);
        reset.setVisible(true);
        buttonPanel.setVisible(true);

    }

    // This keeps track of what arraylist the info is being added to
    int list = 0;

    public void chop(String x) {
        // Replaces each linebreak at the end of each line with a comma
        String cleaned = x.replaceAll("\n", ",");
        int pos = cleaned.indexOf(",");
        String main = cleaned.substring(0, pos);
        String leftover = x.substring(pos + 1);
        if (list == 0) {
            orderNum.add(main);
            unvisited.add(main);
            list++;
        }

        else if (list == 1) {
            address.add(main);
            list++;
        }

        else if (list == 2) {
            mins.add(main);
            list++;
        }

        else if (list == 3) {
            northGPS.add(main);
            list++;
        }

        else if (list == 4) {
            westGPS.add(main);
            list = 0;
        }

        try {
            chop(leftover);
        } catch (StringIndexOutOfBoundsException e) {
            westGPS.add(leftover);
            getFirstStop();
            return;
        }

    }

    // This method uses the Haversine
    // formula to get the distance between two GPS points
    public double distance(double lat1, double lon1, double lat2, double lon2) {

        int R = 6371;

        double o1 = lat1 * Math.PI / 180;
        double o2 = lat1 * Math.PI / 180;

        double deltaO = (lat2 - lat1) * Math.PI / 180;
        double deltaA = (lon2 - lon1) * Math.PI / 180;

        double a = Math.sin(deltaO / 2) * Math.sin(deltaO / 2)
                + Math.cos(o1) * Math.cos(o2) * Math.sin(deltaA / 2) * Math.sin(deltaA / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c;

        return d;
    }

    double angryMinsFirstStop;

    // The method to get the distances for the points is recursive so I needed
    // to get the first stop in its own method as the starting point is always fixed
    public void getFirstStop() {
        double startLat = 53.38197;
        double startLon = -6.59274;
        double nextLat;
        double nextLon;
        double timeWaiting;

        String shortestRoute = " ";
        double tempDistance;
        double shortestDistance = 1000.0;

        for (int i = 0; i < northGPS.size(); i++) {
            nextLat = Double.parseDouble(northGPS.get(i));
            nextLon = Double.parseDouble(westGPS.get(i));
            tempDistance = distance(startLat, startLon, nextLat, nextLon);

            if (tempDistance < shortestDistance) {
                shortestDistance = tempDistance;
                shortestRoute = orderNum.get(i);

            }

        }
        int f = Integer.parseInt(shortestRoute);
        timeWaiting = Double.parseDouble(mins.get(f - 1));
        if ((timeWaiting + shortestDistance) > 30) {
            angryMinsFirstStop = (timeWaiting + shortestDistance) - 30;
        } else {
            angryMinsFirstStop = 0;
        }
        visited.add(shortestRoute);
        finalRoute.add(shortestRoute);
        routeDistance.add(shortestDistance);

        planRoute();

    }

    // This method compares each house to the last house in the solution route to
    // see which is the closest
    // once we find the closest, we save it and get the order number of that house
    // from the orderNum arraylist
    // This is then added to the solution route and becomes the base house that
    // everything is compared to and the cycle continues
    public void planRoute() {
        // Get the most recent house we visited
        // Get the value and the index of that value in orderNum
        // And get the gps coords of that value
        int pos = finalRoute.size() - 1;
        String currentHouse = finalRoute.get(pos);
        int currentHouseIndex = orderNum.indexOf(currentHouse);

        double startLat = Double.parseDouble(northGPS.get(currentHouseIndex));
        double startLon = Double.parseDouble(westGPS.get(currentHouseIndex));
        double nextLat;
        double nextLon;

        String shortestRoute = "";
        double tempDistance = 0;
        double shortestDistance = 1000.0;

        for (int i = 0; i < unvisited.size(); i++) {
            if (finalRoute.contains(unvisited.get(i))) {
            } else {

                // We get the value of the next house in unvisited,
                // and get the index of it in orderNum
                // This way we can access the gps coords of the house
                String nextHouse = unvisited.get(i);
                int nextHouseIndex = orderNum.indexOf(nextHouse);

                nextLat = Double.parseDouble(northGPS.get(nextHouseIndex));
                nextLon = Double.parseDouble(westGPS.get(nextHouseIndex));

                tempDistance = distance(startLat, startLon, nextLat, nextLon);

                if (tempDistance < shortestDistance) {
                    shortestDistance = tempDistance;
                    finalRoute.add(unvisited.get(i));
                    System.out.println("FINAL: " + Arrays.deepToString(finalRoute.toArray()));
                }
                shortestRoute = unvisited.get(i);
                visited.add(shortestRoute);
            }
        }

        // If all the houses have been included in the route, end, else go again
        if (finalRoute.size() == orderNum.size()) {
            outputField1.setText(Arrays.deepToString(finalRoute.toArray()));
            addDistance();
            return;
        } else {
            planRoute();
        }

    }

    // This gets the distances between each house in the solution route to use in
    // the calculation of the angry minutes. The deliver driver drives 60km/h which
    // means that the distance between the houses is also the time in minutes it
    // takes to get there
    public void addDistance() {
        try {
            for (int i = 0; i < finalRoute.size(); i++) {
                int x = orderNum.indexOf(finalRoute.get(i));
                int y = orderNum.indexOf(finalRoute.get(i + 1));

                double lat1 = Double.parseDouble(northGPS.get(x));
                double lon1 = Double.parseDouble(westGPS.get(x));
                double lat2 = Double.parseDouble(northGPS.get(y));
                double lon2 = Double.parseDouble(westGPS.get(y));

                double dist = distance(lat1, lon1, lat2, lon2);
                routeDistance.add(dist);
            }
        } catch (java.lang.IndexOutOfBoundsException e) {
            angryMinutes();
        }

    }

    // This takes the distances from addDistance() and compares with the time the
    // customer has already been waiting to get the total angry minutes
    public void angryMinutes() {
        double totalAMins = 0;
        double aMins;
        double minsWaited = 0;
        double timeTaken = 0;
        for (int i = 0; i < finalRoute.size(); i++) {
            int x = orderNum.indexOf(finalRoute.get(i));
            minsWaited = Double.parseDouble(mins.get(x));
            timeTaken += routeDistance.get(i);

            if ((timeTaken + minsWaited) > 30) {
                aMins = (timeTaken + minsWaited) - 30;
            } else {
                aMins = 0;
            }

            totalAMins = totalAMins + aMins;

        }
        String x = String.valueOf(totalAMins);
        String y = x.substring(0, 6);
        outputField2.setText(y);
    }

}
