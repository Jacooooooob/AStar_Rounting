import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Controls implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener {
    static boolean d ;
    boolean isDragging = false; // Flag to track dragging

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 1) {
            PathFinding.Grid.addStart(e.getX(), e.getY());
        }
        else {
            PathFinding.Grid.addEnd(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            isDragging = true; // Start dragging
            Grid grid = PathFinding.Grid;
            grid.addToWalls(new Node(e.getX(), e.getY()), true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false; // Stop dragging
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Grid grid = PathFinding.Grid; // 假设有一个静态访问方式
        if (SwingUtilities.isLeftMouseButton(e)) {
            grid.addToWalls(new Node(e.getX(), e.getY()), true);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            grid.removeWalls(new Node(e.getX(), e.getY()), true);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(d) {
            PathFinding.updateLabel(e.getX(),e.getY());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar() == 'r') {
            PathFinding.reset();
        }
        if(e.getKeyChar() == 'w') {
            PathFinding.Grid.GenerateMaze();
        }
        if(e.getKeyChar() == 'd') {
            PathFinding.showStat = true;
            d = true;
        }
        if(e.getKeyChar() == '.') {
            PathFinding.speedControl(6);
        }
        if(e.getKeyChar() == ',') {
            PathFinding.speedControl(-6);
        }
        if(e.getKeyChar() == 'f') PathFinding.findPath();
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar() == 'd') {
            d = false;
            PathFinding.showStat = false;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation()>0) {
            PathFinding.addToGrid(50);
        }

        if(e.getWheelRotation()<0) {
            PathFinding.addToGrid(-50);
        }
    }

}
