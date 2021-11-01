import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Virus {
    Thread virusThread;
    //are bonuses picked up automatically (after upgrade0
    public boolean isAutoPickupEnabled = false;
    public int time = 0;
    //how fast is virus spread inside one country
    public double innerInfectabilityRate = 0.006;
    //how fast is virus being cured
    public double medicineEffect = 0;

    public double popupChance = 0.0025;
    public double[] transportWayInfectability; //LAND,AIR,Train,CAR
    public double[] climateInfectability; //COLD,MEDIOCRE,HOT
    public double [] transportationChances;
    public Virus(){
        //initiating infection parameters
         transportWayInfectability = new double[]{1,1,1,1};
         climateInfectability = new double[]{1,1,1};
         transportationChances = new double[]{0.5,0.5,0.35,0.55};

        //initial infection
        //infecting random country
        int index = new Random().nextInt(GameController.countries.size());
        GameController.countries.get(index).infected = 50;
        virusThread = new Thread(()->{
            while(true) {

                    try {
                        if (GameController.speed != 0) {
                            Thread.sleep((long) (42 / GameController.speed));
                        } else {
                            while (GameController.speed == 0) {
                                Thread.sleep(500);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (Main.gameState == 1) {
                    time += 1;
                    BottomBar.timeLabel.setText("Time: " + GameController.v.time);
                }
            }
        });
        virusThread.start();
    }
}