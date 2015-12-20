import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ComponentReordering {
    private static java.util.List<String> ourPaintingOrder = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Component painting order");

            JComponent left = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.GREEN);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    ourPaintingOrder.add("Left");
                }
            };
            left.setPreferredSize(new Dimension(10, 10));

            JComponent right = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.BLUE);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    ourPaintingOrder.add("Right");
                }
            };
            right.setPreferredSize(new Dimension(10, 10));

            JPanel content = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    g.setColor(Color.BLACK);
                    g.drawString("Press 1 to request repaints: left, right", 17, 20);
                }
            };
            content.setLayout(new BorderLayout());
            content.add(left, BorderLayout.WEST);
            content.add(right, BorderLayout.EAST);
            content.setFocusable(true);
            content.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1) {
                        ourPaintingOrder.clear();

                        left.repaint();
                        right.repaint();

                        SwingUtilities.invokeLater(() -> {
                            Graphics graphics = content.getGraphics();
                            graphics.drawString("First: " + ourPaintingOrder.get(0), 17, 37);
                            graphics.drawString("Second: " + ourPaintingOrder.get(1), 17, 54);
                        });
                    }
                }
            });

            frame.getContentPane().add(content);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(350, 350));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
