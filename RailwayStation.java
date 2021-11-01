import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RailwayStation extends TransportNode{
    ArrayList<Voyage> ownVoyages = new ArrayList<>();
    public RailwayStation(int x, int y){
        c = Color.MAGENTA;
        this.x = x;
        this.y = y;
    }
    @Override
    public void show(Graphics2D g2d){
        super.show(g2d);
        //draws all current trains
        for (int i = ownVoyages.size()-1; i >=0 ; i--) {
            ownVoyages.get(i).show(g2d);
        }
    }
    public void launchTransport(RailwayStation destination) {
        Voyage v = new Voyage(this, destination);
        ownVoyages.add(v);
    }

    class Voyage{
        Image img;

        {
            try {
                img = ImageIO.read(new File("src/train.png"));
                img = img.getScaledInstance(20,20,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int x;
        int y;
        int dx;
        int dy;
        float speed;
        RailwayStation from;
        RailwayStation to;
        float time = 0;
        float delay = 15;
        Voyage(RailwayStation from, RailwayStation to){
            this.from = from;
            this.to = to;
            time = 0;
            x = this.from.x;
            y = this.from.y;
            dx = to.x - from.x;
            dy = to.y - from.y;

            speed = 0.25f+new Random().nextFloat();
        }
        public void show(Graphics2D g2d) {
            if (time>=delay){
                time = 0;
                from.findCountry().infect(to.findCountry(),(int) ((100+Math.random()*100)),2);
                ownVoyages.remove(this);
            }
            g2d.setColor(new Color(91, 20, 196, 150));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(from.x, from.y, (int) (from.x + time * dx / delay), (int) (from.y + time * dy / delay));
            AffineTransform tr = g2d.getTransform();
            tr.translate(from.x + time * dx / delay, from.y + time * dy / delay);
            tr.rotate(Math.atan2(to.y - from.y, to.x - from.x));
            tr.translate(-20 / 2d, -20 / 2d);
            g2d.drawImage(img, tr, null);
            tr.translate(-from.x + time * dx / delay + 20 / 2d, -from.y + time * dy / delay + 20 / 2d);
            time+=speed;
        }

    }
}
