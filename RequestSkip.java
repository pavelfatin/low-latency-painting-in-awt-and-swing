import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestSkip {
    private static volatile int myRequestsToPost;
    private static volatile int myRequestCount;
    private static int myPaintCount;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JComponent content = new JComponent() {
                @Override
                public void paint(Graphics g) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, getWidth(), getHeight());

                    g.setColor(Color.BLACK);
                    g.drawString("Press 1 to request 10 repaints", 7, 20);
                    g.drawString("Requests: " + myRequestCount, 7, 37);
                    g.drawString("Repaints: " + myPaintCount, 7, 54);
                    pause(100);

                    myPaintCount++;
                }
            };
            content.setFocusable(true);
            content.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_1) {
                        myRequestsToPost = 10;
                    }
                }
            });

            ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
            pool.scheduleAtFixedRate(() -> {
                if (myRequestsToPost > 0) {
                    myRequestCount++;
                    content.repaint();
                    myRequestsToPost--;
                }
            }, 0, 50, TimeUnit.MILLISECONDS);

            JFrame frame = new JFrame("Painting request skip");
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    pool.shutdown();
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

    private static void pause(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
