import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PathFinding pf = new PathFinding();
            pf.setUp();
        });
    }
}
