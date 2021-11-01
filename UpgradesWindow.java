


import javax.swing.*;
import java.awt.*;

public class UpgradesWindow extends JDialog {

    public UpgradesWindow(){
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(600,600);
        setAutoRequestFocus(true);

        add(new Upgrade(4, "Encourage wearing masks"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.transportationChances[0]-=0.07;
                }
            }

        });
        add(new Upgrade(10, "Provide free masks on street"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.innerInfectabilityRate-=0.001;
                }
            }
        });
        add(new Upgrade(11, "Buy medical equipment to improve curing process"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.medicineEffect+=0.001;
                }
            }

        });
        add(new Upgrade(15, "Increase frequency of popup bonuses"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.popupChance+=0.0005;
                }
            }
        });
        add(new Upgrade(20, "Increase amount of bonus coins per popup"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    Country.BonusPopup.maxValue+=1;
                }
            }
        });
        add(new Upgrade(25, "Discourage travelling"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.transportationChances[0] -= 0.03;
                    GameController.v.transportationChances[1] -= 0.03;
                    GameController.v.transportationChances[2] -= 0.02;
                    GameController.v.transportationChances[3] -= 0.02;
                }
            }
        });
        add(new Upgrade(42, "Pick up bonuses automatically"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.isAutoPickupEnabled = true;
                }
            }
        });
        add(new Upgrade(17, "Reduce Plane traffic"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.transportationChances[1] -= 0.03;
                }
            }
        });
        add(new Upgrade(17, "Reduce Train traffic"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.transportationChances[2] -= 0.03;
                }
            }
        });
        add(new Upgrade(20, "Reduce Car traffic"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.transportationChances[3] -= 0.03;
                }
            }
        });
        add(new Upgrade(30, "Introduce quarantine regime"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.innerInfectabilityRate-=0.0015;
                }
            }
        });
        add(new Upgrade(45, "Start curing the infected people"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.medicineEffect += 0.009;
                }
            }
        });
        add(new Upgrade(50, "Introduce full quarantine"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.innerInfectabilityRate=0.001;
                    GameController.v.transportationChances = new double[]{0.04, 0.04, 0.04, 0.04};
                }
            }
        });
        add(new Upgrade(100, "Vaccinating"){
            @Override
            public void action() {
                if (GameController.upgradePts>=price) {
                    super.action();
                    GameController.v.medicineEffect+=0.01;
                }
            }
        });
        pack();
    }


    static class Upgrade extends JButton{
        public int price;
        public Upgrade(int price, String text){
            this.price = price;
            setText("Ω"+price+": "+text);
            addActionListener(l->action());
        }
        public void action(){

            GameController.upgradePts-=price;
            setEnabled(false);
        }
    }
}
