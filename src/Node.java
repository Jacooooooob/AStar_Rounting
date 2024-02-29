import java.util.*;

public class Node implements Comparable<Node> {
    // Node attributes like x, y, costs, etc.
    // Ensure to override equals() and hashCode() for proper use in HashSet and PriorityQueue
    int x = -1, y = -1;
    double Gcost = Double.MAX_VALUE, Hcost = -1, Fcost = -1;

    Node CameFrom;
    // List<Node> Neighbours = new ArrayList<Node>();
    Set<Node> Neighbours = new HashSet<>();

    // Constructor
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Implement the compareTo method for PriorityQueue
    @Override
    public int compareTo(Node other) {
        return Double.compare(this.getFcost(), other.getFcost());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double getGcost() {
        return Gcost;
    }

    public void setGcost(double Gcost) {
        this.Gcost = Gcost;
    }

    public double getHcost() {
        return Hcost;
    }

    public void setHcost(double Hcost) {
        this.Hcost = Hcost;
    }

    public double getFcost() {
        return getGcost() + getHcost();
    }

    public void setFcost(double Fcost) {
        this.Fcost = Fcost;
    }

    public void updateFcost() {
        this.Fcost = this.Gcost + this.Hcost;
    }

    // Add a method to update the node's neighbors
    public void addNeighbours() {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            // Check bounds
            if (newX >= 0 && newX < PathFinding.Size && newY >= 0 && newY < PathFinding.Size) {
                Node potentialNeighbour = new Node(newX, newY);
                // Check if the potential neighbour is not a wall
                if (!PathFinding.Grid.Walls.contains(potentialNeighbour)) {
                    Neighbours.add(potentialNeighbour);
                }
            }
        }
    }
}
