import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BufferReuse {
    private static boolean drawSquares = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JComponent right = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    if (drawSquares) {
                        g.setColor(Color.GREEN);
                        g.fillRect(0, getHeight() - 10, 10, 10);
                        g.fillRect(getWidth() - 10, 0, 10, 10);
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(0, 0, getWidth(), getHeight());

                        g.setColor(Color.BLUE);
                        Font defaultFont = g.getFont();
                        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
                        g.drawString("Right", 10, getHeight() - 10);
                        g.setFont(defaultFont);
                    }
                }
            };
            right.setOpaque(true);
            right.setPreferredSize(new Dimension(350, 350));

            JComponent left = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    g.setColor(Color.BLUE);
                    Font defaultFont = g.getFont();
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
                    g.drawString("Left", 10, getHeight() - 10);
                    g.setFont(defaultFont);

                    g.setColor(Color.BLACK);
                    g.drawString("Press 1 to paint squares in the right area", 7, 20);
                    g.drawString("Press 2 to repaint the content", 7, 37);
                }
            };
            left.setOpaque(true);
            left.setFocusable(true);
            left.setPreferredSize(new Dimension(350, 350));
            left.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1) {
                        left.paintImmediately(0, 0, left.getWidth(), left.getHeight());

                        drawSquares = true;
                        right.paintImmediately(0, 0, right.getWidth(), right.getHeight());
                        drawSquares = false;
                    } else if (e.getKeyCode() == KeyEvent.VK_2) {
                        left.repaint();
                        right.repaint();
                    }
                }
            });

            JPanel parent = new JPanel();
            parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
            parent.add(left);
            parent.add(right);

            JFrame frame = new JFrame("Buffer reuse");
            frame.getContentPane().add(parent);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
