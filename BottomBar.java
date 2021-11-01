import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.stream.Collectors;


public class BottomBar extends JMenuBar{
    public static JProgressBar infectedMenu = new JProgressBar();
    public static JLabel upgradePoints = new JLabel("");
    public static JLabel timeLabel = new JLabel("Time: ");
    BottomBar(){
        setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        add(upgradePoints);
        infectedMenu.setMinimum(0);
        infectedMenu.setMaximum(GameController.totalPop);
        infectedMenu.setStringPainted(true);


        add(new JButton("Exit"){{
            addActionListener(l->{
                Main.gameState=0;
                Main.menu.setVisible(true);
                Main.g.setVisible(false);
            });
        }});
        add(new JLabel("Infected in world"));
        add(infectedMenu);

        timeLabel.setMinimumSize(new Dimension(200,50));
        timeLabel.setSize(200,50);
        add(timeLabel);
        add(new JButton("UPGRADES"){{
            addActionListener(e->{
                Game.upgrades.setVisible(true);
            });
        }});
        //different speed options
        add(new JButton("||"){{
            addActionListener(e->{
                GameController.speed = (long) 0;
            });
        }});
        add(new JButton(">"){{
            addActionListener(e->{
                GameController.speed = (long) 1;
            });
        }});
        add(new JButton(">>"){{
            addActionListener(e->{
                GameController.speed = (long) 5;
            });
        }});
        add(new JButton(">>>"){{
            addActionListener(e->{
                GameController.speed = (long) 10;
            });
        }});
    }
}
