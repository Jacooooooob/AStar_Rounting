import javax.swing.JPanel;
import java.awt.*;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class Grid extends JPanel {
    // Grid properties
    int Size, Height;
    double Spacing;

    Set<Node> openSet = ConcurrentHashMap.newKeySet();
    Set<Node> closedSet = ConcurrentHashMap.newKeySet();
    Set<Node> Path = ConcurrentHashMap.newKeySet();
    Set<Node> Walls = ConcurrentHashMap.newKeySet();
    static Node Wall1;
    static Node Wall2;
    Node Start, End;

    // Constructor
    public Grid(int Size, int Height) {
        this.Size = Size;
        this.Height = Height;
        Spacing = (double)(Height / Size);
    }

    // Methods for managing nodes
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawNodes(g, openSet, new Color(100, 255, 100, 180));
        drawNodes(g, closedSet, new Color(255, 100, 100, 130));
        drawNodes(g, Path, Color.ORANGE);
        drawNodes(g, Walls, Color.darkGray);
        drawSpecialNode(g, Start, new Color(0, 100, 255));
        drawSpecialNode(g, End, Color.magenta);
        drawGrid(g);
    }

    private void drawNodes(Graphics g, Set<Node> nodes, Color color) {
        for (Node node : nodes) {
            g.setColor(color);
            g.fillRect(getPixel(node.x), getPixel(node.y), (int) Spacing, (int) Spacing);
        }
    }


    private void drawSpecialNode(Graphics g, Node node, Color color) {
        if (node != null) {
            g.setColor(color);
            g.fillRect(getPixel(node.x), getPixel(node.y), (int) Spacing, (int) Spacing);
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.darkGray);
        for (int i = 1; i < Size; i++) {
            int pixel = (int) Math.ceil(Spacing * i);
            g.drawLine(0, pixel, Height, pixel);
            g.drawLine(pixel, 0, pixel, Height);
        }
    }

    public int getPixel(int x) {
        return (int) Spacing * (x);
    }

    public int[] getIndex(int x, int y) {
        int indexX = calculateIndex(x);
        int indexY = calculateIndex(y);
        return new int[]{indexX, indexY};
    }

    private int calculateIndex(int coordinate) {
        // Use Math.floorDiv to make sure you round down, for 0-based indexes
        return Math.floorDiv(coordinate, (int)Spacing);
    }

    // Version of getIndex with one input, adjusted for consistency
    public int getIndex(int x) {
        return calculateIndex(x);
    }


    void addStart(int x, int y) {
        if (PathFinding.isRunning || openSet.size() > 0 || End == null) return;

        Start = new Node(getIndex(x), getIndex(y));
        Start.addNeighbours();
        Start.Gcost = 0;
        Start.Hcost = PathFinding.heuristic(Start, End);
        Start.Fcost = Start.Gcost + Start.Hcost;
        openSet.add(Start);
        this.repaint();
    }

    void addEnd(int x, int y) {
        if (PathFinding.isRunning || PathFinding.Completed) return;

        End = new Node(getIndex(x), getIndex(y));
        this.repaint();
    }

    void addToOpen(Node n) {
        openSet.add(n); // Set.add() already ensures no duplicates
    }

    void addToClose(Node n) {
        closedSet.add(n);
    }

    void addToPath(Node n) {
        Path.add(n);
    }

    // The addToWalls method is modified to use thread-safe collection operations
    void addToWalls(Node n, boolean t) {
        if (PathFinding.isRunning || PathFinding.Completed) return;

        int[] index = getIndex(n.x, n.y);
        Node temp = t ? new Node(index[0], index[1]) : n;
        Walls.add(temp);
        this.repaint();
    }

    // The modified removeWalls method also uses thread-safe collection operations
    void removeWalls(Node n, boolean t) {
        if (!PathFinding.isRunning && !PathFinding.Completed) {
            Node target = t ? new Node(getIndex(n.x), getIndex(n.y)) : n;
            Walls.remove(target);
            this.repaint();
        }
    }


    void addSize(int x) {
        if ((Size + x > 0) && (Size + x <= 100)) {
            Size += x;
            Spacing = Height / Size;
        }
        this.repaint();
    }

    // Press the w key to automatically generate the wall
    void GenerateMaze() {
        if (!PathFinding.isRunning && !PathFinding.Completed) {
            Walls.clear(); // Clear existing walls

            // A random number generator was used to determine the location and size of the maze
            Random rand = new Random();
            int mazeWidth = rand.nextInt(Size / 2) + 10;
            int mazeHeight = rand.nextInt(Size / 2) + 10;
            int startX = rand.nextInt(Size - mazeWidth);
            int startY = rand.nextInt(Size - mazeHeight);

            // Generate the four boundaries of the maze
            for (int x = startX; x < startX + mazeWidth; x++) {
                // addToWalls(new Node(x, startY), false); // Top
                addToWalls(new Node(x, startY + mazeHeight), false); // Bottle
            }
            for (int y = startY; y <= startY + mazeHeight; y++) {
                addToWalls(new Node(startX, y), false); // Left
                addToWalls(new Node(startX + mazeWidth - 1, y), false); // right
            }

            // Make sure there is at least one pathway inside the maze, creating a gap randomly
//            int gapPosition = startY + rand.nextInt(mazeHeight); // Place a random spot on the wall
//            Walls.remove(new Node(startX, gapPosition)); // Create a gap in the left wall
//            Walls.remove(new Node(startX + mazeWidth - 1, gapPosition)); // Create a gap in the right wall

            repaint();
        }
    }

    public Node getNode(int x, int y) {
        Node searchNode = new Node(x, y);
        if (closedSet.contains(searchNode)) return searchNode;
        if (openSet.contains(searchNode)) return searchNode;
        if (Path.contains(searchNode)) return searchNode;
        return new Node(-1, -1);
    }
}