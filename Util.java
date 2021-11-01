import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Util {
    //flood fill algorithm to find the total area of a country (used in coloring)
    public static ArrayList<int[]> expandRegion(int x, int y, Color bg){
        BufferedImage copy = Game.mapImage.getSubimage(0, 0, Main.WIDTH, Main.HEIGHT);
        ArrayList<int[]> result = new ArrayList<>();
        int[] first = new int[]{x,y};
        if (copy.getRGB(x,y)==bg.getRGB()) {
            copy.setRGB(x,y,Color.green.getRGB());
        }

        //written with tutorial from google
        ArrayList<int[]> queue = new ArrayList<>();
        queue.add(first);
        result.add(first);
        while(!queue.isEmpty()) {
            int[] current = queue.get(0);
            result.add(current);
            queue.remove(0);
            int j = current[0];
            int i = current[1];

            // 4 neighbours
            for (int[] arr : new int[][]{
                    {j, i - 1},
                    {j, i + 1},
                    {j - 1, i},
                    {j + 1, i}
            }) {
                if (arr[1] >= 0 && arr[1] < Main.HEIGHT && arr[0] >= 0 && arr[0] < Main.WIDTH && copy.getRGB(arr[0],arr[1]) == bg.getRGB()) {
                    copy.setRGB(arr[0],arr[1], Color.blue.getRGB());
                    queue.add(new int[]{arr[0], arr[1]});
                }
            }
        }
        return result;
    }
    //calculating euclidian distance
    public static double dist(double a, double b, double c, double d){
        return Math.sqrt(Math.pow(c-a,2)+Math.pow(d-b,2));
    }
    //saving new score to file
    public static void writeScoresToFile() throws IOException {
        String result = GameController.username+"-"+ GameController.score;
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/scores.txt"));
        bw.write(Collections.list(MainMenu.scoresList.elements()).stream().collect(Collectors.joining("\n"))+"\n"+result);
        bw.close();
    }
}
