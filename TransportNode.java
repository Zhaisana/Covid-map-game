import java.awt.*;

public abstract class TransportNode {
    int x;
    int y;
    Color c = Color.blue;
    int size = 6;

    public void show(Graphics2D g2d){
        g2d.setColor(c);
        g2d.fillRect(x-size/2, y-size/2, size, size);
    }
    //method to findd country this transport node is contained in
    public Country findCountry(){
        for (Country c: GameController.countries){
            if (c.transportNodes.contains(this)){
                return c;
            }
        }
        return null;
    }
}
