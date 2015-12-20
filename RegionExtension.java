import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RegionExtension {
    private static Color mySquareColor = Color.BLUE;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JComponent content = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    Rectangle r = g.getClipBounds();
                    String bounds = String.format("Last painting bounds: (%d, %d, %d, %d)",
                            r.x, r.y, r.width, r.height);

                    g.setColor(Color.BLACK);
                    g.drawString("Press 1 to request repaints of the squares", 7, 20);
                    g.drawString(bounds, 7, 37);

                    g.setColor(mySquareColor);
                    g.fillRect(0, getHeight() - 10, 10, 10);
                    g.fillRect(getWidth() - 10, 0, 10, 10);
                }
            };
            content.setFocusable(true);
            content.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1) {
                        mySquareColor = Color.BLUE.equals(mySquareColor) ? Color.GREEN : Color.BLUE;
                        content.repaint(0, content.getHeight() - 10, 10, 10);
                        content.repaint(content.getWidth() - 10, 0, 10, 10);
                    }
                }
            });

            JFrame frame = new JFrame("Painting area extension");
            frame.getContentPane().add(content);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(350, 350));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
