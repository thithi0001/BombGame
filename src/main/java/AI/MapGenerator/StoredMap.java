package AI.MapGenerator;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import main.Main;

public class StoredMap {
    public  static List<int[]> getMonsterList(int[][] map){
        List<int[]> re = new ArrayList<>();
        for(int i = 0; i < map.length; i++ ){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] == 3){
                    re.add(new int[]{i,j});
                }
            }
        }
        return re;
    }
 
    public  static String toStringMonsterList(List<int[]> monsterList){
        String re = "";
        for(int[]a : monsterList){
            re += String.valueOf(a[0]) + "," + String.valueOf(a[1]) + ";" ;
        }
        if (re.length() > 0) {
            re = re.substring(0, re.length()-1);
        }
        return re.toString();
    }

    public  static void saveMap(int[][] map ) {

        try {
            FileWriter writer = new FileWriter(Main.res + "/maps/levelRandom.txt");
            CreateListItem listItem = new CreateListItem();
            writer.write("grass_2\n");
            writer.write(listItem.getRe() +"\n");

            List<int[]> monterList = new ArrayList<>();
            String stringMap = new String();
            for(int i = 0; i < map.length; i++ ){
                stringMap +="\n";
                for(int j = 0; j < map[0].length; j++){
                    if(map[i][j] == 3){
                        monterList.add(new int[]{i,j});
                        stringMap += String.valueOf(0) + " ";
                    }
                    else{
                        stringMap += String.valueOf(map[i][j]) + " ";
                    }
                }
            }
            
            writer.write(toStringMonsterList(monterList)+ "\n");
            writer.write("1,1");
            writer.write(stringMap);
            writer.close();
            System.out.println("Create map completed");
        } catch (Exception e) {
            System.out.println("Errol save file");
        }
    }
}
