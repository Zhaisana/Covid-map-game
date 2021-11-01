
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CarNode extends TransportNode{
    ArrayList<Trip> ownTrips = new ArrayList<>();
    public CarNode(int x, int y) {
        c = Color.YELLOW;
        this.x = x;
        this.y = y;
    }
    @Override
    public void show(Graphics2D g2d){
        super.show(g2d);
        for (int i = ownTrips.size()-1; i >=0 ; i--) {
            ownTrips.get(i).show(g2d);
        }
    }
    public void launchTransport(CarNode destination) {
        Trip f = new Trip(this, destination);
        ownTrips.add(f);
    }

    class Trip {
        Image img;

        {
            try {
                img = ImageIO.read(new File("src/car.png"));
                img = img.getScaledInstance(15, 15, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int x;
        int y;
        int dx;
        int dy;
        CarNode from;
        CarNode to;
        float time = 0;
        float delay = 10;

        Trip(CarNode from, CarNode to) {
            this.from = from;
            this.to = to;
            x = this.from.x;
            y = this.from.y;
            dx = to.x - from.x;
            dy = to.y - from.y;
        }

        public void show(Graphics2D g2d) {
            if (time > delay) {
                time = 0;
                from.findCountry().infect(to.findCountry(), (int) ((20 + Math.random() * 20)), 1);
                ownTrips.remove(this);
            }
            g2d.setColor(new Color(255, 200, 0, 150));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(from.x, from.y, (int) (from.x + time * dx / delay), (int) (from.y + time * dy / delay));
            AffineTransform tr = g2d.getTransform();
            tr.translate(from.x + time * dx / delay, from.y + time * dy / delay);
            tr.rotate(Math.atan2(to.y - from.y, to.x - from.x));
            tr.translate(-20 / 2d, -20 / 2d);
            g2d.drawImage(img, tr, null);
            tr.translate(-from.x + time * dx / delay + 20 / 2d, -from.y + time * dy / delay + 20 / 2d);
            time += 0.25;
        }
    }
}
