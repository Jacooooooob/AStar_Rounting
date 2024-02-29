import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.JPanel;

public class PathFinding extends JPanel{
    // Path finding attributes like grid, controls, etc.
    static int height = 700;	// Dimensions of the content pane.
    static int Size = 100;     //Grid size.
    static Grid Grid = new Grid(Size, height); // the grid to work with.
    static JPanel panel = Grid;    //Used the grid as JPanel too !!!
    static boolean Completed, isRunning = false;
    static int Sleep = 1;
    static JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    static JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    static int p2size = 30;
    static int p3size = 450;
    static JLabel label = new JLabel();
    static JLabel label2 = new JLabel();
    static boolean showStat;
    static double diagonalDistance;
    static double distance;
    static long starttime;
    static float elapsedtime;

    static void addToGrid(int x) {
        Grid.addSize(x);
        if (Size + x > 0 && Size + x <= 100) {
            Size += x;
        }
    }

    // Method to set up the path finding environment
    public void setUp() {
        // Initialize controls listener
        Controls lis = new Controls();

        // Setup JFrame
        JFrame jframe = new JFrame("PathFinding");
        jframe.setSize(height, height); // Assuming 'height' is defined elsewhere
        jframe.getContentPane().setPreferredSize(new Dimension(height + p3size, height + p2size));

        // Configure panels
        configurePanel(panel, new Color(158, 143, 255), lis);
        configureInfoPanel(panel2, label, Color.DARK_GRAY, new Dimension(height, p2size));
        configureInfoPanel(panel3, label2, Color.DARK_GRAY, new Dimension(p3size, height));

        // Setup JFrame
        jframe.addKeyListener(lis);
        jframe.add(panel, BorderLayout.CENTER);
        jframe.add(panel2, BorderLayout.SOUTH);
        jframe.add(panel3, BorderLayout.EAST);
        jframe.pack();
        jframe.setLocationRelativeTo(null);

        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start pathfinding in a new thread
        new Thread(PathFinding::findPath).start(); // Adjusted for static method call
    }

    // Helper method to configure panels
    private void configurePanel(JPanel panel, Color bgColor, Controls listener) {
        panel.setBackground(bgColor);
        panel.addMouseListener(listener);
        panel.addMouseMotionListener(listener);
        panel.addMouseWheelListener(listener);
    }

    // Helper method to configure information panels
    private void configureInfoPanel(JPanel panel, JLabel label, Color bgColor, Dimension prefSize) {
        panel.setBackground(bgColor);
        panel.setPreferredSize(prefSize);
        label.setForeground(Color.CYAN);
        panel.add(label);
    }

    // A* algorithm implementation
    public static void findPath() {
        starttime = System.currentTimeMillis(); // Algorithm start time
        distance = Grid.Spacing; // Default distance of one grid
        diagonalDistance = Math.sqrt(2 * (distance * distance)); // The square root of 2 multiplies the diagonal
        updateLabel();

        // PathFinding
        new Thread(() -> {
            while (!Completed) { // Finding the end
                updateLabel(); // Update the path color
                if (!Grid.openSet.isEmpty()) { // If there are still elements in the openset, we continue to expand

                    isRunning = true;
                    // Find the node with the minimum Fcost
                    // Synchronization prevents errors
//                    Set<Node> openSetCopy;
//                    synchronized (Grid.openSet) {
//                        openSetCopy = new HashSet<>(Grid.openSet);
//                    }
                    // Node Current = Collections.min(openSetCopy, Comparator.comparing(node -> node.Fcost));
                    Node Current = Collections.min(Grid.openSet, Comparator.comparing(Node::getFcost));
                    double tempG;

                    // Check if the destination node has been reached
                    if (Current.equals(Grid.End)) {
                        elapsedtime = (System.currentTimeMillis() - starttime) / 10;
                        Current.Fcost = 0;
                        isRunning = false;
                        Completed = true;
                        panel.setBackground(new Color(10, 200, 255));
                        // Retrace the way
                        tracePath(Current);

                        Grid.repaint();
                        break;
                    }

                    for (Node n : Current.Neighbours) {
                        if (Current.x - n.x != 0 && Current.y - n.y != 0) {
                            tempG = Current.Gcost + diagonalDistance;
                        } else {
                            tempG = Current.Gcost + distance;
                        }
                        // Ignore nodes that are already in closedSet or Walls
                        if (Grid.closedSet.contains(n) || Grid.Walls.contains(n)) {
                            continue;
                        }

                        if (!Grid.openSet.contains(n) || n.Gcost > tempG) {
                            n.Gcost = tempG;
                            n.addNeighbours();
                            n.CameFrom = Current;
                            n.Hcost = heuristic(n, Grid.End);
                            n.Fcost = n.Gcost + n.Hcost;
                            Grid.addToOpen(n);
                        }
                    }
                    Grid.openSet.remove(Current);
                    Grid.addToClose(Current);
                    Grid.repaint();
                } else {
                    if (Grid.Start != null) {
                        elapsedtime = (System.currentTimeMillis() - starttime) / 10;
                        isRunning = false;
                        panel.setBackground(Color.RED);
                        Completed = true;
                        break;
                    }
                }
                // Time out
                try {
                    Thread.sleep(Sleep);
                    SwingUtilities.invokeLater(() -> Grid.repaint());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //Calculate distance from node 1 to node 2 aka Hcost
    static public int heuristic(Node start, Node end) {
        // Manhattan distance
        return Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
    }

    // Method to trace back the path from end node to start node
    public static void tracePath(Node n) {
        Grid.Path.clear();

        Node current = n;
        while (current != null) {
            Grid.addToPath(current);
            current = current.CameFrom;
        }
        Grid.repaint();
    }

    static void updateLabel() {
        String cssStyles = "<style>"
                + ".color-code {margin-bottom: 10px;}"
                + ".color-label {margin-right: 5px; font-weight: bold;}"
                + ".control-item {margin-bottom: 5px;}"
                + "</style>";

        String htmlContent = "<html>"
                + cssStyles
                + "<div class='color-code'><h2><u>Colour Codes:</u></h2>"
                + "<p><span class='color-label' style='color:#9e8fff'>Clear path</span></p>"
                + "<p><span class='color-label' style='color:#303030'>Obstacle/Walls</span></p>"
                + "<p><span class='color-label' style='color:#0066ff'>Start Block</span></p>"
                + "<p><span class='color-label' style='color:#ff00ff'>Destination Block</span></p>"
                + "<p><span class='color-label' style='color:#64ff64'>OpenSet</span></p>"
                + "<p><span class='color-label' style='color:#ff6464'>ClosedSet</span></p>"
                + "<p><span class='color-label' style='color:#ff9500'>Traced Path</span></p>"
                + "</div>"
                + "<div class='controls'><h2><u>Controls:</u></h2>"
                + "<p class='control-item'>- Press w to create a U-shaped wall at random</p>"
                + "<p class='control-item'>- Hold down the left mouse button and drag to create walls freely</p>"
                + "<p class='control-item'>- &lt; and &gt; for speed control</p>"
                + "<p class='control-item'>- Right Click to mark a block as Target Block</p>"
                + "<p class='control-item'>- Left Click to mark a block as Start Block and start PathFinding</p>"
                + "<p class='control-item'>- Press r to refresh all</p>"
                + "<p class='control-item'>- Press f to start PathFinding</p>"
                + "</div>"
                + "</html>";

        label2.setText(htmlContent);

        if (showStat) {
            return;
        }

        String elapsedTimeHtml = "<html>"
                + "<span style='color:#9e8fff'>Elapsed Time:</span>"
                + Math.round(elapsedtime / Sleep) + "ms"
                + "<span style='color:#9e8fff'> | Delay:</span>" + Sleep
                + "</html>";

        if (isRunning) {
            label.setText(elapsedTimeHtml);
        } else {
            label.setText(elapsedTimeHtml);
        }
    }


    // Suppose that the pathfinding class has a reference to the controls class or can access the d variable directly
    static void updateLabel(int x, int y) {
        if(!showStat) {
            return;
        }
        if(isRunning) {
            int xy[] = Grid.getIndex(x,y);
            Node n = Grid.getNode(xy[0], xy[1]);
            label.setText("<Html><span style=\"color:#9e8fff\">Delay:</span>:"+Sleep+" <span style=\"color:#9e8fff\"> | Gcost: </span>"+n.Gcost+" | <span style=\"color:#9e8fff\">Hcost: </span>"+n.Hcost+" | <span style=\"color:#9e8fff\">Fcost: </span>"+n.Fcost+"</Html>");
        } else {
            int xy[] = Grid.getIndex(x,y);
            Node n = Grid.getNode(xy[0], xy[1]);
            label.setText("<Html><span style=\"color:#9e8fff\">Delay:</span>"+Sleep+" <span style=\"color:#9e8fff\"> | Gcost: </span>"+n.Gcost+" | <span style=\"color:#9e8fff\">Hcost: </span>"+n.Hcost+" | <span style=\"color:#9e8fff\">Fcost: </span>"+n.Fcost+"</Html>");
        }
        panel2.repaint();
        Grid.repaint();
        panel.repaint();
    }


    static void reset() {
        Grid.openSet.clear();
        Grid.closedSet.clear();
        Grid.Path.clear();
        Grid.Walls.clear();
        Completed = false;
        isRunning = false;
        Grid.Start = null;
        Grid.End = null;
        panel.setBackground(new Color(158, 143, 255));
        Grid.repaint();
    }

    public static void speedControl(int x) {
        Sleep += x;
        Sleep = Math.max(1, Math.min(Sleep, 1000)); // Ensure Sleep is between 1 and 1000 milliseconds
        // Provide feedback about the current speed
        System.out.println("Current speed delay: " + Sleep + "ms");
    }

}
