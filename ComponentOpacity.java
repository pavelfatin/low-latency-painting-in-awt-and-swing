import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ComponentOpacity {
    private static boolean drawingEnabled = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JComponent content = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    if (drawingEnabled) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());

                        g.setColor(Color.BLACK);
                        g.drawString("Press 1 to paint nothing in internal area", 7, 20);
                        g.drawString("Press 2 to repaint the content", 7, 37);
                    }
                }
            };
            content.setFocusable(true);
            content.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1) {
                        drawingEnabled = false;
                        content.paintImmediately(87, 87, 175, 175);
                        drawingEnabled = true;
                    } else if (e.getKeyCode() == KeyEvent.VK_2) {
                        content.repaint();
                    }
                }
            });

            JPanel parent = new JPanel(new BorderLayout());
            parent.setBackground(Color.GREEN);
            parent.add(content, BorderLayout.CENTER);

            JFrame frame = new JFrame("Component opacity");
            frame.getContentPane().add(parent);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(350, 350));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
