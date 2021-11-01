

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Country {
    public int x;
    public int y;
    public String name;
    //all transport nodes insidde this country
    public ArrayList<TransportNode> transportNodes = new ArrayList<>();

    public int population;
    public int infected = 0;

    public int countryGrade;
    public int climate;
    public ArrayList<int[]> land = new ArrayList<>();
    public ArrayList<BonusPopup> bonuses = new ArrayList<>();
    Color bg = Color.white;
    int lifetime = 0;
    int healthyPop = 0;

    public Country(int x, int y, String name,int population, int countryGrade, TransportNode... nodes) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.population = population;
        this.countryGrade = countryGrade;
        transportNodes.addAll(Arrays.asList(nodes));
        //automatically add a train station and starting point for car trips to each country
        transportNodes.add(new CarNode(x,y));
        transportNodes.add(new RailwayStation(x,y));

        land = Util.expandRegion(x, y, Color.lightGray);


        bg = Color.getHSBColor(0.4f-0.4f*infected / population, 0.9f,0.9f);
        for (int[] arr : land) {
            Game.mapImage.setRGB(arr[0], arr[1], bg.getRGB());
        }
        if (y < Main.HEIGHT / 6 || y > Main.HEIGHT * 5 / 6) {
            climate = 0;
        } else if (y < Main.HEIGHT / 3 || y > Main.HEIGHT * 2 / 3) {
            climate = 1;
        }else{
            climate = 2;
        }
    }
    //additional constructor for countries with disconnected land
    //FOR EXAMPLE, USA + Alaska
    public Country(ArrayList<int[]> coords, String name,int population, int countryGrade, TransportNode... nodes) {
        this(coords.get(0)[0],coords.get(0)[1], name, population, countryGrade, nodes);
        for (int i = 1; i < coords.size(); i++) {
            int[] arr = coords.get(i);
            land.addAll(Util.expandRegion(arr[0], arr[1], Color.lightGray));
        }
        for (int[] arr : land) {
            Game.mapImage.setRGB(arr[0], arr[1], bg.getRGB());
        }
    }
    //determining way of infecting other country
    public void transferInfection(Country c, int amount){
        //infecting by land
            if (Math.random() < GameController.v.transportationChances[0]) {
                infect(c, (amount), 0);
                return;
            }
            //infecting by planes
            if (Math.random() < GameController.v.transportationChances[1]) {
                if (transportNodes.size() > 0 && c.transportNodes.size() > 0) {
                    ArrayList<Airport> airportsFrom = transportNodes.stream().filter((t) -> t instanceof Airport).map((o) -> (Airport) o).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<Airport> airportsTo = c.transportNodes.stream().filter((t) -> t instanceof Airport).map((o) -> (Airport) o).collect(Collectors.toCollection(ArrayList::new));
                    if (airportsFrom.size() > 0 && airportsTo.size() > 0) {
                        Airport nodeFrom = airportsFrom.get(new Random().nextInt(airportsFrom.size()));
                        Airport nodeTo = airportsTo.get(new Random().nextInt(airportsTo.size()));
                        nodeFrom.launchTransport(nodeTo);
                    }
                }
                return;
            }

            //infecting by trains
            if (Math.random() < GameController.v.transportationChances[2]) {
                if (transportNodes.size() > 0 && c.transportNodes.size() > 0) {
                    ArrayList<RailwayStation> portsFrom = transportNodes.stream().filter((t) -> t instanceof RailwayStation).map((o) -> (RailwayStation) o).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<RailwayStation> portsTo = c.transportNodes.stream().filter((t) -> t instanceof RailwayStation).map((o) -> (RailwayStation) o).collect(Collectors.toCollection(ArrayList::new));
                    if (portsFrom.size() > 0 && portsTo.size() > 0) {
                        RailwayStation nodeFrom = portsFrom.get(new Random().nextInt(portsFrom.size()));
                        RailwayStation nodeTo = portsTo.get(new Random().nextInt(portsTo.size()));
                        if (Util.dist(nodeFrom.x,nodeFrom.y, nodeTo.x, nodeTo.y)<300) {
                            nodeFrom.launchTransport(nodeTo);
                        }
                    }
                }
            }
            //infecting via cars
            if (Math.random() < GameController.v.transportationChances[3]) {
                if (transportNodes.size() > 0 && c.transportNodes.size() > 0) {
                    ArrayList<CarNode> portsFrom = transportNodes.stream().filter((t) -> t instanceof CarNode).map((o) -> (CarNode) o).collect(Collectors.toCollection(ArrayList::new));
                    ArrayList<CarNode> portsTo = c.transportNodes.stream().filter((t) -> t instanceof CarNode).map((o) -> (CarNode) o).collect(Collectors.toCollection(ArrayList::new));
                    if (portsFrom.size() > 0 && portsTo.size() > 0) {
                        CarNode nodeFrom = portsFrom.get(new Random().nextInt(portsFrom.size()));
                        CarNode nodeTo = portsTo.get(new Random().nextInt(portsTo.size()));
                        if (Util.dist(nodeFrom.x,nodeFrom.y, nodeTo.x, nodeTo.y)<100) {
                            nodeFrom.launchTransport(nodeTo);
                        }
                    }
                }

            }
    }
    //process of infection based on climate, transport and difficulty
    public void infect(Country c, int amount, int way){
        amount = (int) ((amount
                *((6d-c.countryGrade)/2d)
                *GameController.v.climateInfectability[c.climate]
                *GameController.v.transportWayInfectability[way]
                *GameController.difficulty));
        int safeAmount = Math.min(amount, c.healthyPop);
        c.infected += safeAmount;
        c.repaint();
    }
    //every frame update
    public void update(){
        healthyPop = population-infected;
        lifetime++;
        if (lifetime>5) {
            Country tmp = GameController.countries.get(new Random().nextInt(GameController.countries.size()));
            while (tmp==this){
                tmp = GameController.countries.get(new Random().nextInt(GameController.countries.size()));
            }
            transferInfection(tmp, 50 + new Random().nextInt(50));

            double factor = new Random().nextDouble()*((6d-countryGrade)/3d);
            //self-infection
            infected += GameController.v.innerInfectabilityRate * infected;

            //curing
            if (GameController.v.medicineEffect>0) {
                infected -= 50 + (infected * (3 - factor)) * GameController.v.medicineEffect;
            }else{
                infected-=10;
            }
            infected = Math.max(0, Math.min(infected,population));

            bg = Color.getHSBColor(0.4f-0.4f*infected / population, 0.9f,0.9f);
            //spawning bonuses
            if (Math.random() < GameController.v.popupChance && bonuses.size()<5-GameController.difficulty){
                int[] coords = land.get(new Random().nextInt(land.size()));
                if (GameController.v.isAutoPickupEnabled){
                    new BonusPopup(coords[0], coords[1]).activate();
                }else {
                    bonuses.add(new BonusPopup(coords[0], coords[1]));
                }
            }
            if (bonuses.size()>0&&Math.random()<0.02){
                bonuses.remove(0);
            }
            lifetime = 0;

        }
    }

    public void show(Graphics2D g2d){
        for (TransportNode node:transportNodes){
            node.show(g2d);
        }
        for (BonusPopup b: bonuses){
            b.show(g2d);
        }
    }
    //coloring the land of the country progressively (not all cells will be same color)
    public void repaint(){
        for (int[] arr : land) {
            if (Math.random()<0.2) {
                Game.mapImage.setRGB(arr[0], arr[1], bg.getRGB());
            }
        }
    }

    static class BonusPopup {
        int x;
        int y;
        int size = 30;
        int value;
        public static int maxValue = 4;
        Rectangle2D shape;
        Image img;

        {
            try {
                img = ImageIO.read(new File("src/bonus.png"));
                img = img.getScaledInstance(size,size,Image.SCALE_SMOOTH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public BonusPopup(int x, int y){
            this.x = x;
            this.y = y;
            value = 1+new Random().nextInt(maxValue);
            shape = new Rectangle(x-img.getWidth(Main.g)/2,y-img.getHeight(Main.g)/2,img.getWidth(Main.g),img.getHeight(Main.g));
        }
        public void show(Graphics2D g2d){
            g2d.drawImage(img,x-img.getWidth(Main.g)/2,y-img.getHeight(Main.g)/2,null);
        }
        public void activate(){
            GameController.upgradePts+=value;
        }
        public boolean contains(int x, int y){
            return shape.contains(x,y);
        }


    }
}
