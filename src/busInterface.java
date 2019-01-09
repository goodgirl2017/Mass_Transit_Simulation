import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.stream.IntStream;
import static javax.swing.text.html.HTML.Tag.HEAD;

public class busInterface extends JFrame {
    public static final int CANVAS_WIDTH = 1150;
    public static final int CANVAS_HEIGHT = 700;
    public static final int MARGIN_WIDTH = 100;
    public static final int MARGIN_HEIGHT = 100;
    public static final Color LINE_COLOR;
    public static final Color CANVAS_BACKGROUND;
    private static working_system targetSystem;
    private static String sourceFilename;
    private static String sourceFilename1;
    private busInterface.DrawScene scene;
    boolean busChange = false;
    boolean busRouteChange = true;
    int busIDChange;
    String newAttributeChange;
    String attributeChange;

    public busInterface() {
        JPanel busPanel = new JPanel(new FlowLayout());
        JButton busReset = new JButton("Reset Buses");
        busPanel.add(busReset);
        busReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                busInterface.targetSystem.resetSystem();
//                re-start the program by reading the txt file again
                String [] x = new String[3];
                try{
                    x[0] = busInterface.sourceFilename;
                    x[1] = busInterface.sourceFilename1;
                    working_system.main(x);}
                catch (Exception e) {
                    System.out.println("Exception occurred1");
                    e.printStackTrace();
                }
                busInterface.this.scene.repaint();
                busInterface.this.requestFocus();
            }
        });

        JButton busNext = new JButton("Move Next Bus");
        busPanel.add(busNext);
        busNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                busInterface.targetSystem.eventList.performNext(working_system.eventList, working_system.busList);

                /*********change bus here***********/
                if (busChange && busInterface.targetSystem.busList.get(busIDChange).isStarted()) {
                    targetSystem.changeBus(busIDChange, attributeChange, newAttributeChange);
                    busChange = false;
                }

                busInterface.this.scene.repaint();
                busInterface.this.requestFocus();
            }
        });
// update Bus Attributes
        JTextField setBusID = new JTextField("BusID",10);
        JButton updateBus = new JButton("Update Bus Attributes");
        busPanel.add(updateBus);
        busPanel.add(setBusID);
        // constructor for JList
        String [] listEntries = {"route,stop", "speed", "capacity"};
        JList updateList = new JList(listEntries);
        updateList.setSelectedIndex(0);
        //Make it have a vertical scrollbar
        JScrollPane updateScroller = new JScrollPane(updateList);
        updateScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        updateScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        busPanel.add(updateScroller);
        //set the number of lines to show before scrolling
        updateList.setVisibleRowCount(1);
        //Restrict the user to selecting only one thing at a time
        updateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTextField updateBusAttribute = new JTextField("setNewAttribute",10);
        busPanel.add(updateBusAttribute);

        updateBus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String attribute = (String) updateList.getSelectedValue();
                String busIdTxt = setBusID.getText();
                //int newAttribute = Integer.valueOf(updateBusAttribute.getText());
                String newAttribute =  updateBusAttribute.getText();

                //To do list for updating bus attributes
                int newSpeed = 100000;
                int newCapacity = 1000000;
                int busID2= 1000000;
                int newRouteId = 1000000;
                int newStopIndex = 1000000;
                boolean poped = false;

                try
                { int busID = Integer.valueOf(busIdTxt);
                    busID2 = busID;
                busChange = true;
                busIDChange = busID;
                newAttributeChange = newAttribute;
                attributeChange = attribute;
                if (attribute== "speed"){
                    newSpeed = Integer.valueOf(newAttribute);
                }
                else if (attribute== "capacity"){
                        newCapacity = Integer.valueOf(newAttribute);
                }

                else if (attribute== "route,stop" && newAttribute.contains(",")){
                        String[] routeStop = newAttribute.split(",");
                        if (routeStop.length!=2){
                            busRouteChange = false;
                            JOptionPane.showMessageDialog(busPanel,
                                    "Oops, invalid input for Bus Update Attributes a");
                        }
                        else
                        {
                            newRouteId = Integer.valueOf(routeStop[0]);
                            newStopIndex = Integer.valueOf(routeStop[1]);
                        }
                    }

                    else{
                    busRouteChange = false;
                    JOptionPane.showMessageDialog(busPanel,
                            "Oops, invalid input for Bus Update Attributes b");
                }
                }
                catch(NumberFormatException nfe)
                {   poped = true;
                    busChange = false;
                    busRouteChange = false;
                    JOptionPane.showMessageDialog(busPanel,
                            "Oops, invalid input for Bus Update Attributes c");
                }

                if (!(busID2>0) ||(newStopIndex <0) || !(newRouteId>0)) {
                    busRouteChange = false;
                }

                if ( !poped && (!(newSpeed>0) || !(newCapacity>0) || !(busID2>0) || (newStopIndex <0) || !(newRouteId>0))){
                    busChange = false;
                    poped = true;
                    JOptionPane.showMessageDialog(busPanel,
                            "Input cannot be no-positive d");
                }


                if ( !poped &&!targetSystem.busList.containsKey(busID2)){
                    busChange = false;
                    poped = true;
                    busRouteChange = false;
                    JOptionPane.showMessageDialog(busPanel,
                            "Bus not in List e");
                }

                if (attribute== "route,stop") {

                    if (!poped && !targetSystem.routeList.containsKey(newRouteId)) {
                        busChange = false;
                        poped = true;
                        busRouteChange = false;
                        JOptionPane.showMessageDialog(busPanel,
                                "Route not in List f");
                    } else if (!poped && targetSystem.routeList.get(newRouteId).getStopList().size()-1<newStopIndex) {
                        busChange = false;
                        poped = true;
                        busRouteChange = false;
                        JOptionPane.showMessageDialog(busPanel,
                                "Stop not in Route g");
                    }

                    if (busRouteChange == true) {
                        targetSystem.changeBusRoute(busIDChange, attributeChange, newAttributeChange);
                    }
                }

                busRouteChange = true;

                if (busChange && !poped && (attribute== "capacity" || attribute== "speed")) {
                    if (!busInterface.targetSystem.busList.get(busIDChange).isStarted()){
                        targetSystem.changeBus(busIDChange, attributeChange, newAttributeChange);
                    }
                }

                if (busChange && !poped){
                    JOptionPane.showMessageDialog(busPanel,
                            "You successfully changed the bus attributes");
                }



                busInterface.this.scene.repaint();
                busInterface.this.requestFocus();
            }
        });
  // rewind bus
        JButton rewind = new JButton("Rewind");
        busPanel.add(rewind);
        rewind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (busInterface.targetSystem.eventList.prevInfo.size() < 1) {
                    JOptionPane.showMessageDialog(busPanel,
                            "Oops, ran out of maximum rewind time");
                }
                busInterface.targetSystem.eventList.rewind(working_system.eventList);
                busInterface.this.scene.repaint();
                busInterface.this.requestFocus();
            }
        });
 // calculate efficiency
        JButton efficiency = new JButton("System Efficiency");
        JTextField setNewKvalue = new JTextField("1",10);
        busPanel.add(efficiency);
        busPanel.add(setNewKvalue);

        String [] kvaluelist = {"Kspeed", "Kcapacity", "Kwaiting", "Kbuses", "Kcombined"};
        JList k_List = new JList(kvaluelist);
        JScrollPane k_Scroller = new JScrollPane(k_List);
        k_Scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        k_Scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        busPanel.add(k_Scroller);
        k_List.setVisibleRowCount(1);
        k_List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // outText
        JLabel calculatedValue = new JLabel("efficiency output");
        busPanel.add(calculatedValue);

        efficiency efficiencyCalculator = new efficiency(targetSystem.stopList,targetSystem.busList);
        k_List.setSelectedIndex(0);
        efficiency.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String kCategory = (String) k_List.getSelectedValue();
                //int newKValue = Integer.valueOf(setNewKvalue.getText());
                String inputNewKValue = setNewKvalue.getText();
                //To do list for calculation of Efficiency

                double newKValue = 1.0;
                if (kCategory != null && inputNewKValue != null) {
                    //double newKValue = Double.parseDouble(inputNewKValue);

                    try
                    {
                        newKValue = Double.parseDouble(inputNewKValue);

                        switch (kCategory) {
                            case "Kspeed":
                                efficiencyCalculator.setKspeed(newKValue);
                                break;
                            case "Kcapacity":
                                efficiencyCalculator.setKcapacity(newKValue);
                                break;
                            case "Kwaiting":
                                efficiencyCalculator.setKwaiting(newKValue);
                                break;
                            case "Kbuses":
                                efficiencyCalculator.setKbuses(newKValue);
                                break;
                            case "Kcombined":
                                efficiencyCalculator.setKcombined(newKValue);
                            default:
                        }
                    }
                    catch(NumberFormatException nfe)
                    {
                        JOptionPane.showMessageDialog(busPanel,
                                "Oops, invalid input for Efficiency Kvalue");
                    }

                }
                else {
                    JOptionPane.showMessageDialog(busPanel,
                            "Oops, invalid input for Efficiency Kvalue");
                }

                if ( !(newKValue>0)){
                    JOptionPane.showMessageDialog(busPanel,
                            "Input cannot be no-positive");
                }
                efficiencyCalculator.calcWaitingPassengers();
                efficiencyCalculator.calcBusCost();
                efficiencyCalculator.calculateEfficiency();

                double oU = 666.66; //default
                oU =  efficiencyCalculator.getEfficiency();
                String outputValue = String.valueOf(oU);
                calculatedValue.setText(outputValue);
                busInterface.this.scene.repaint();
                busInterface.this.requestFocus();
            }
        });

        // show route info
        JButton routeInfo = new JButton("routes");
        busPanel.add(routeInfo);
        routeInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                StringBuilder str = new StringBuilder();
                for (route singeRoute: targetSystem.routeList.values()) {
                    str.append(singeRoute.getRouteID() + ": ");
                    for (int i = 0; i < singeRoute.getStopInRoute().size(); i++) {
                        str.append(singeRoute.getStopInRoute().get(i));
                        if (i < singeRoute.getStopInRoute().size() - 1) {
                            str.append("->");
                        }
                    }
                    str.append("\n");
                }
                JOptionPane.showMessageDialog(busPanel,
                            String.valueOf(str));
            }
        });

        this.scene = new busInterface.DrawScene();
        this.scene.setPreferredSize(new Dimension(1150, 700));
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(this.scene, "Center");
        cp.add(busPanel, "South");

        this.setDefaultCloseOperation(3);
        this.setTitle("Mass Transit Simulation System");
        this.pack();
        this.setVisible(true);
        this.requestFocus();
    }

    public static void main(String[] args) {
        busInterface.targetSystem.resetSystem();
        sourceFilename = args[0];
        if(args.length>1){
        sourceFilename1 = args[1];
        }
        try{
            working_system.main(args);}
        catch (Exception e) {
            System.out.println("Exception occurred");
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new busInterface();
            }
        });
    }

    static {
        LINE_COLOR = Color.BLACK;
        CANVAS_BACKGROUND = Color.LIGHT_GRAY;
        targetSystem = new working_system();
    }

    class DrawScene extends JPanel {
        DrawScene() {
        }

        public void paintComponent(Graphics g) {
            ImageIcon busIcon = null;
            String imgFilename = "bus.png";
            URL imgURL = this.getClass().getClassLoader().getResource(imgFilename);
            if (imgURL != null) {
                busIcon = new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + imgFilename);
            }

            Image imgBus = busIcon.getImage();
            ImageIcon stopIcon = null;
            imgFilename = "bus_stop.png";
            imgURL = this.getClass().getClassLoader().getResource(imgFilename);
            if (imgURL != null) {
                stopIcon = new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + imgFilename);
            }

            Image imgStop = stopIcon.getImage();
            Font myFont1 = new Font("SansSerif", 1, 9);
            super.paintComponent(g);
            this.setBackground(busInterface.CANVAS_BACKGROUND);
            g.setColor(busInterface.LINE_COLOR);
            g.setFont(myFont1);
            busInterface.targetSystem.draw(g, imgBus, imgStop, 1150, 700, 100, 100);
        }
    }
}
