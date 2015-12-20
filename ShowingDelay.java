import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ShowingDelay {
    private static boolean myIntermediatePause = false;
    private static Color ourOuterColor = Color.BLUE;
    private static Color ourInnerColor = Color.GREEN;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JComponent content = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    g.setColor(Color.BLACK);
                    g.drawString("Press 1 re-draw the rectangles", 7, 20);

                    g.setColor(ourOuterColor);
                    g.fillRect(87, 87, 175, 175);

                    if (myIntermediatePause) {
                        pause(1000);
                    }

                    g.setColor(ourInnerColor);
                    g.fillRect(137, 137, 75, 75);
                }
            };
            content.setOpaque(true);
            content.setFocusable(true);
            content.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1) {
                        Color outerColor = ourOuterColor;
                        ourOuterColor = ourInnerColor;
                        ourInnerColor = outerColor;

                        myIntermediatePause = true;

                        content.paintImmediately(0, 0, content.getWidth(), content.getHeight());
                    }
                }
            });

            JFrame frame = new JFrame("Drawing order");
            frame.getContentPane().add(content);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setPreferredSize(new Dimension(350, 350));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void pause(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
