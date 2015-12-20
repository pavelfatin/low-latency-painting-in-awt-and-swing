import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IncrementalPainting {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JComponent content = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    g.setColor(Color.BLACK);
                    g.drawString("Press 1 to draw some text", 7, 20);
                    g.drawString("Press 2 to clone the text area", 7, 37);
                }
            };
            content.setOpaque(true);
            content.setFocusable(true);
            content.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_2) {
                        Graphics g = content.getGraphics();

                        Font font = g.getFont();

                        long before = System.nanoTime();

                        if (e.getKeyCode() == KeyEvent.VK_1) {
                            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
                            g.setColor(Color.RED);
                            g.drawString("Some text here", 20, 100);

                            g.setFont(new Font(Font.SERIF, Font.BOLD, 12));
                            g.setColor(Color.GREEN.darker());
                            g.drawString("Some text here", 20, 130);

                            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
                            g.setColor(Color.BLUE);
                            g.drawString("Some text here", 20, 160);
                        } else {
                            g.copyArea(10, 70, 120, 120, 130, 0);
                        }

                        double delay = (double) (System.nanoTime() - before) / 1000000;

                        g.setFont(font);

                        g.setColor(Color.WHITE);
                        g.fillRect(7, 44, 200, 10);

                        g.setColor(Color.BLACK);
                        g.drawString("Delay: " + delay, 7, 54);
                    }
                }
            });

            JFrame frame = new JFrame("Incremental painting");
            frame.getContentPane().add(content);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(350, 350));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
