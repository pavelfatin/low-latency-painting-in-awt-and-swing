import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;

public class BufferingOverhead {
    private static boolean ourDrawContent = true;
    private static Optional<Double> ourDelay = Optional.empty();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JComponent content = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    if (ourDrawContent) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());

                        g.setColor(Color.BLACK);
                        g.drawString("Press 1 to paint nothing in 10x10 area", 7, 20);
                        g.drawString("Press 2 to paint nothing in 1000x1000 area", 7, 37);
                        ourDelay.ifPresent(delay -> g.drawString("Delay: " + delay, 7, 54));
                    }
                }
            };
            content.setOpaque(true);
            content.setFocusable(true);
            content.setPreferredSize(new Dimension(1000, 1000));
            content.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_2) {
                        long before = System.nanoTime();

                        ourDrawContent = false;
                        if (e.getKeyCode() == KeyEvent.VK_1) {
                            content.paintImmediately(0, 0, 10, 10);
                        } else {
                            content.paintImmediately(0, 0, content.getWidth(), content.getHeight());
                        }
                        ourDrawContent = true;

                        ourDelay = Optional.of((double) (System.nanoTime() - before) / 1000000);

                        content.repaint();
                    }
                }
            });

            JFrame frame = new JFrame("Buffering overhead");
            frame.getContentPane().add(content);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
