import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipelineFlush {
    private static Optional<Double> ourDelay = Optional.empty();

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.opengl", "true"); // optional
//        System.setProperty("sun.java2d.d3d", "true");
//        System.setProperty("sun.java2d.xrender", "true");

        Robot robot = new Robot();
        SwingUtilities.invokeLater(() -> run(robot));
    }

    private static void run(Robot robot) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        JFrame frame = new JFrame("Rendering pipeline flush");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                executorService.shutdown();
            }
        });

        JComponent content = new JComponent() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.BLACK);
                g.drawString("Press 1 to measure latency without flush", 7, 20);
                g.drawString("Press 2 to measure latency with flush", 7, 37);
                ourDelay.ifPresent(delay -> g.drawString("Delay: " + delay, 7, 54));
            }
        };
        content.setFocusable(true);
        content.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_2) {
                    Rectangle bounds = frame.getBounds();
                    boolean flush = e.getKeyCode() == KeyEvent.VK_2;
                    Point center = new Point((int) bounds.getCenterX(), (int) bounds.getCenterY());
                    double delay = measurePaintingDelay(executorService, content.getGraphics(),
                            content.getSize(), flush, robot, center);
                    ourDelay = Optional.of(delay);
                    content.repaint();
                }
            }
        });

        frame.getContentPane().add(content);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(350, 350));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static double measurePaintingDelay(Executor executor, Graphics graphics,
                                               Dimension size, boolean flush,
                                               Robot robot, Point point) {
        long before = System.nanoTime();

        executor.execute(() -> {
            graphics.setColor(Color.GREEN);
            graphics.fillRect(0, 0, size.width, size.height);

            if (flush) {
                Toolkit.getDefaultToolkit().sync();
            }
        });

        Color color;
        do {
            color = robot.getPixelColor(point.x, point.y);
        } while (!Color.GREEN.equals(color));

        return (double) (System.nanoTime() - before) / 1000000;
    }
}
