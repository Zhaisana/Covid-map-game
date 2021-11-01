import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class GameController {
    //default username
    public static String username = "Guest";
    public static ArrayList<Country> countries = new ArrayList<>();
    public static Virus v;
    public static double speed = 1;
    //initial money
    public static int upgradePts = 5;
    public static int difficulty = 1; //default = easy
    public static int totalPop;
    public static int score;
    public static int totalInfected = 0;
    public static void initMap(){
        //reading the world_map image and recoloring it
        try{
            Image tmp = ImageIO.read(new File("src/world_map.png"));
            BufferedImage bg = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics tempG = bg.createGraphics();
            tempG.drawImage(tmp, 0, 0, null);
            tempG.dispose();
            for (int i = 0; i < Game.mapImage.getHeight(); i++) {
                for (int j = 0; j < Game.mapImage.getWidth(); j++) {
                    //if its see its recolored to better color
                    if (bg.getRGB(j,i)==new Color(24,0,255).getRGB()) {
                        Game.mapImage.setRGB(j,i, new Color(28, 71,99).getRGB());
                        //if its closer to white than to black its light gray (used to determing land of a country later)
                    } else if (Math.abs(bg.getRGB(j,i)-Color.white.getRGB())<Math.abs(bg.getRGB(j,i)-Color.black.getRGB())) {
                        Game.mapImage.setRGB(j, i, Color.lightGray.getRGB());
                    }else{
                        //else its a country border
                        Game.mapImage.setRGB(j,i,Color.black.getRGB());
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("error");
        }
    }
    //adding all the countries used in the game
    public static void initCountries(){
        GameController.countries.add(new Country(934, 157, "Russia", 1500000, 3,
                                                 new Airport(914, 167),
                                                 new Airport(1224,188),
                                                 new RailwayStation(916, 115)));
        GameController.countries.add(new Country(905, 205, "Ukraine", 400000, 3,
                                                 new Airport(896, 205)));
        GameController.countries.add(new Country(853, 194, "Poland", 380000, 3));
        GameController.countries.add(new Country(886, 188, "Belarus", 95000, 3));
        GameController.countries.add(new Country(791, 221, "France", 670000, 4,
                                                 new Airport(783, 214)));
        GameController.countries.add(new Country(819, 200, "Germany", 830000, 4));
        GameController.countries.add(new Country(765, 254, "Spain", 470000, 4,
                                                 new Airport(765, 251)));
        GameController.countries.add(new Country(825, 235, "Italy", 600000, 4));
        GameController.countries.add(new Country(new ArrayList<int[]>(){{
            add(new int[]{397, 249});
            add(new int[]{270, 133});
        }}, "USA", 3280000, 4,
                                                 new Airport(450, 250),
                                                 new RailwayStation(432, 310)));

        GameController.countries.add(new Country(384, 165, "Canada", 380000, 4,
                                                 new Airport(400, 160)));
        GameController.countries.add(new Country(316, 319, "Mexico", 1260000, 3,
                                                 new RailwayStation(318, 368)));
        GameController.countries.add(new Country(746, 260, "Portugal", 102000, 3));
        GameController.countries.add(new Country(813, 220, "Switzerland", 85000, 4));
        GameController.countries.add(new Country(841, 205, "Czech Republic", 106500, 3));
        GameController.countries.add(new Country(836, 217, "Austria", 106500, 4));
        GameController.countries.add(new Country(854, 211, "Slovakia", 54500, 2));
        GameController.countries.add(new Country(857, 220, "Hungary", 97700, 2));
        GameController.countries.add(new Country(880, 226, "Romania", 194100, 2));
        GameController.countries.add(new Country(894, 219, "Moldova", 35460, 2));
        GameController.countries.add(new Country(883, 239, "Bulgaria", 70000, 2));
        GameController.countries.add(new Country(new ArrayList<int[]>(){{
            add(new int[]{773, 192});
            add(new int[]{755, 183});
        }}, "UK", 666000, 4,
                                                 new Airport(776,195),
                                                 new RailwayStation(772, 165)));

        GameController.countries.add(new Country(871, 131, "Finland", 55000, 4));
        GameController.countries.add(new Country(838, 138, "Sweden", 102300, 4));
        GameController.countries.add(new Country(812, 146, "Norway", 54000, 4));
        GameController.countries.add(new Country(749, 188, "Ireland", 49000, 3));
        GameController.countries.add(new Country(814, 173, "Denmark", 58000, 4));
        GameController.countries.add(new Country(802, 193, "Netherlands", 172800, 4));
        GameController.countries.add(new Country(798, 202, "Belgium", 114600, 4));
        GameController.countries.add(new Country(551, 512, "Brazil", 2090000, 3,
                                                 new Airport(502,490),
                                                 new RailwayStation(615, 480)));
        GameController.countries.add(new Country(445, 516, "Peru", 320000, 2));
        GameController.countries.add(new Country(477, 587, "Chile", 187300, 2));
        GameController.countries.add(new Country(450, 442, "Colombia", 496500, 1));
        GameController.countries.add(new Country(496, 412, "Venezuela", 289000, 1));
        GameController.countries.add(new Country(1129, 257, "China", 13930000, 4,
                                                 new Airport(1258,303),
                                                 new RailwayStation(1294, 343)));
        GameController.countries.add(new Country(1398, 597, "Australia", 250000, 4,
                                                 new Airport(1398,597),
                                                 new RailwayStation(1334, 624)));
        GameController.countries.add(new Country(1108, 319, "India", 13530000, 3,
                                                 new Airport(1124,365),
                                                 new RailwayStation(1124, 418)));
        GameController.countries.add(new Country(913,316, "Africa",12160000,0,
                                                 new Airport(882,433),
                                                 new Airport(886,617),
                                                 new RailwayStation(704, 359),
                                                 new RailwayStation(907, 628)));
        totalPop = countries.stream().mapToInt(c->c.population).sum();
    }
}
