import javax.swing.*;
import java.awt.*;

public class Main {
    public static Game g;
    public static MainMenu menu;
    public static int gameState = 0;
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 800;
    public static JFrame f;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            f = new JFrame();
            f.setSize(WIDTH, HEIGHT+20);
            f.setResizable(false);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            g = new Game();
            menu = new MainMenu();

            f.add(menu);
            f.add(g);

            f.setVisible(true);
            menu.setVisible(true);
            g.setVisible(false);

        });
    }

}
