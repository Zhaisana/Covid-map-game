import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MainMenu extends JPanel {
    //list model that backs up the JList with leaderboards
    public static DefaultListModel<String> scoresList = new DefaultListModel<>();
    //background image used in main menu
    BufferedImage bg = Game.mapImage;
    JButton start = new JButton("START");
    JButton tutorial = new JButton("HELP");
    JTextField usernameField = new JTextField();
    public static JList<String> leaderboards = new JList<>();
    JDialog helpDialog = new JDialog(){
        {
            add(new JTextArea("The world is infected by an unknown virus and your aim as a player is to prevent the spread of the disease and elimination of the humanity. The virus is spread with planes, ships and human interaction. The virus mutates with time. However, to counter this you may inflict countermeasures by buying upgrades with OmegaCoins. You may collect them by clicking on the blue bonuses that appear on the map."){{
                setWrapStyleWord(true);
                setLineWrap(true);
                setEditable(false);
                setFont(new Font("Arial", Font.PLAIN, 30));
            }});
        }
    };
    MainMenu(){
        leaderboards.setModel(scoresList);
        //reading scores from scores.txt file and sorting them descending
        File scores = new File("src/scores.txt");
        try {
            scores.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(scores));
            for (String s: br.lines().collect(Collectors.toList())){
                scoresList.add(0,s);
            }
            ArrayList<String> tmp =Collections.list(scoresList.elements());
            tmp.sort((o1, o2) -> {
                String[] elems1 = o1.split("-");
                String[] elems2 = o2.split("-");
                return Integer.parseInt(elems1[1]) - Integer.parseInt(elems2[1]);
            });
            scoresList.clear();
            for (String s : tmp){
                scoresList.add(Math.max(0,scoresList.size()-1), s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        setLayout(null);
        //applying blur to background image (from google tutorial)
        Kernel kernel = new Kernel(3, 3,
                new float[] {
                        1f/27f, 1f/27f, 1f/27f,

                        1f/27f, 1f/27f, 1f/27f,

                        1f/27f, 1f/27f, 1f/27f});
        BufferedImageOp op = new ConvolveOp(kernel);

        bg = op.filter(bg, null);

        setSize(Main.WIDTH, Main.HEIGHT);
        start.setForeground(Color.red);
        start.setSize(300,75);
        start.setFont(new Font("Arial", Font.BOLD, 40));
        start.setBounds(Main.WIDTH/2-start.getWidth()/2, Main.HEIGHT/2-start.getHeight()/2,start.getWidth(),start.getHeight());
        start.addActionListener(e -> {
            //implementing difficulty choice
            Main.menu.setVisible(false);
            JDialog difficulySetting = new JDialog();
            difficulySetting.setContentPane(new JPanel(){{
                JButton easyChoice = new JButton("easy");
                JButton mediumChoice = new JButton("medium");
                JButton hardChoice = new JButton("hard");

                easyChoice.addActionListener(a->{
                    GameController.difficulty = 1;
                    difficulySetting.setVisible(false);
                    Main.g.setVisible(true);
                    Main.gameState = 1;
                    GameController.username = (usernameField.getText().length()>0?usernameField.getText():"Guest");
                });
                mediumChoice.addActionListener(a->{
                    GameController.difficulty = 2;
                    difficulySetting.setVisible(false);
                    Main.g.setVisible(true);
                    Main.gameState = 1;
                    GameController.username = (usernameField.getText().length()>0?usernameField.getText():"Guest");
                });
                hardChoice.addActionListener(a->{
                    GameController.difficulty = 3;
                    difficulySetting.setVisible(false);
                    Main.g.setVisible(true);
                    Main.gameState = 1;
                    GameController.username = (usernameField.getText().length()>0?usernameField.getText():"Guest");
                });

                setLayout(new FlowLayout());
                setSize(300,300);
                add(easyChoice);
                add(mediumChoice);
                add(hardChoice);

            }});
            difficulySetting.setLocation(Main.WIDTH/2-150, Main.HEIGHT/2-150);
            difficulySetting.pack();
            difficulySetting.setVisible(true);
        });

        add(start);
        start.setVisible(true);
        leaderboards.setBorder(BorderFactory.createLineBorder(Color.orange, 5,true));
        leaderboards.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboards.setVisible(true);
        leaderboards.setSize(200,500);
        leaderboards.setBounds(100,Main.HEIGHT/2-leaderboards.getHeight()/2,leaderboards.getWidth(),leaderboards.getHeight());
        add(new JScrollPane(leaderboards){{
            setVisible(true);
            setSize(200,500);
            setBounds(100,Main.HEIGHT/2-getHeight()/2,getWidth(),getHeight());
        }});

        add(new JLabel("LEADERBOARD"){{
            setFont(new Font("Arial", Font.ITALIC, 20));
            setVisible(true);
            setBounds(leaderboards.getX()+20,leaderboards.getY()-50,300,50);
        }});

        helpDialog.setSize(500,500);
        helpDialog.setVisible(false);
        helpDialog.setLocation(Main.WIDTH/2-helpDialog.getWidth()/2, Main.HEIGHT/2-helpDialog.getHeight()/2);

        tutorial.setForeground(Color.red);
        tutorial.setSize(300,75);
        tutorial.setFont(new Font("Arial", Font.BOLD, 40));
        tutorial.setBounds(Main.WIDTH/2-tutorial.getWidth()/2, Main.HEIGHT/2+tutorial.getHeight()/2,tutorial.getWidth(),tutorial.getHeight());
        tutorial.addActionListener(e -> helpDialog.setVisible(true));

        add(tutorial);
        tutorial.setVisible(true);

        usernameField.setSize(300,50);
        usernameField.setBounds(Main.WIDTH/2-usernameField.getWidth()/2,
                Main.HEIGHT/2-start.getHeight()/2-usernameField.getHeight(),
                usernameField.getWidth(),
                usernameField.getHeight());
        usernameField.setVisible(true);
        usernameField.setFont(new Font("Arial", Font.BOLD, 40));
        usernameField.setToolTipText("USERNAME:");
        add(usernameField);

        add(new JLabel("USERNAME:"){{
            setFont(new Font("Arial", Font.ITALIC, 20));
            setVisible(true);
            setBounds(usernameField.getX()+80,usernameField.getY()-50,300,50);
        }});
    }

    @Override
    protected void paintComponent(Graphics g) {
        //only painting main menu if it is visible on screen
        if (isVisible()) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(bg, 0, 0, null);
        }
    }
}

