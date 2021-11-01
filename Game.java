import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Game extends JPanel implements MouseListener {
    //buffered image of a world map
    public static BufferedImage mapImage = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_ARGB);
    public static UpgradesWindow upgrades = new UpgradesWindow();
    public Thread gameThread = new Thread(()->{
        while (true) {
            //different speed settings
                try {
                    if (GameController.speed != 0) {
                        Thread.sleep((long) (42 / GameController.speed));
                    } else {
                        while (GameController.speed == 0) {
                            Thread.sleep(500);
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            if (Main.gameState == 1) {
                //calculating total score
                GameController.score = (int) ((GameController.upgradePts*100+Math.max(0, 25000-GameController.v.time))*(0.5+GameController.difficulty/2));
                //calculating total infected in the worldd
                GameController.totalInfected = GameController.countries.stream().mapToInt(c->c.infected).sum();
                //win condition
                if (GameController.totalInfected<1000&&GameController.v.time>1000){
                    Main.gameState = 0;
                    GameController.speed = 0;
                    JDialog win = new JDialog(Main.f, "Congratulations!", true);
                    win.setLayout(new BorderLayout());
                    win.add(new JTextArea("Congratulations! You have won the game and stopped the infection. Final score: "+GameController.score+""){{
                        setFont(new Font("Arial", Font.PLAIN, 24));
                        setLineWrap(true);
                        setEditable(false);
                        setMinimumSize(new Dimension(600,300));
                    }}, BorderLayout.CENTER);
                    win.setSize(600,300);
                    win.setLocation(Main.WIDTH/2, Main.HEIGHT/2);
                    win.setVisible(true);
                    win.setAutoRequestFocus(true);
                    win.pack();


                    MainMenu.scoresList.add(0, GameController.username+"-"+ GameController.score);
                    ArrayList<String> tmp = Collections.list(MainMenu.scoresList.elements());
                    tmp.sort((o1, o2) -> {
                        String[] elems1 = o1.split("-");
                        String[] elems2 = o2.split("-");

                        return Integer.parseInt(elems1[1]) - Integer.parseInt(elems2[1]);
                    });
                    MainMenu.scoresList.clear();
                    for (String s : tmp){
                        MainMenu.scoresList.add(Math.max(0,MainMenu.scoresList.size()-1), s);
                    }
                    try {
                        Util.writeScoresToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Main.menu.setVisible(true);
                    Main.g.setVisible(false);

                }
                //lose condition
                if (GameController.totalInfected>=GameController.totalPop){
                    Main.gameState = 0;
                    GameController.speed = 0;
                    JDialog win = new JDialog(Main.f, "You lost!", true);
                    win.setLayout(new BorderLayout());
                    win.add(new JTextArea("Game Over! Unfortunately the whole world is infected!"){{
                        setFont(new Font("Arial", Font.PLAIN, 24));
                        setLineWrap(true);
                        setEditable(false);
                        setMinimumSize(new Dimension(600,300));
                    }}, BorderLayout.CENTER);
                    win.setSize(600,300);
                    win.setLocation(Main.WIDTH/2, Main.HEIGHT/2);
                    win.setVisible(true);
                    win.setAutoRequestFocus(true);
                    win.pack();
                    Main.menu.setVisible(true);
                    Main.g.setVisible(false);
                }
                BottomBar.infectedMenu.setValue(GameController.totalInfected);
                BottomBar.upgradePoints.setText("Ω Coins: "+ GameController.upgradePts);
                repaint();
            }
        }
    });
    public Game(){
        this.setSize(Main.WIDTH, Main.HEIGHT);
        setLayout(new BorderLayout());

        GameController.initMap();
        GameController.initCountries();
        add(new BottomBar(), BorderLayout.SOUTH);

        gameThread.start();
        GameController.v = new Virus();
        addMouseListener(this);

    }

    @Override
    public void paintComponent(Graphics g){

        if (isVisible()){
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(mapImage, 0, 0, null);
            if (Main.gameState == 1) {
                for (Country c : GameController.countries) {
                    c.update();
                    c.show(g2d);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //catching mouse clicks on the bonus popups
        if (!GameController.v.isAutoPickupEnabled) {
            Country:
            for (Country c : GameController.countries) {
                for (Country.BonusPopup p : c.bonuses) {
                    if (p.contains(e.getX(), e.getY())) {
                        p.activate();
                        c.bonuses.remove(p);
                        break Country;
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
