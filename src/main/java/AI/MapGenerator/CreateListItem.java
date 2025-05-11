package AI.MapGenerator;

import java.util.ArrayList;
import java.util.List;

import res.LoadResource;

public class CreateListItem {
    private String re ;
    public CreateListItem(int itemScore){
        re = "";
        int currentItemScore = 0;
        int sizeOfListItem = 0;
        List<String> listItem = new ArrayList<>();
        for (String string : LoadResource.itemScoreMap.keySet()) {
            if(LoadResource.itemScoreMap.get(string) != 0){
                sizeOfListItem++;
                listItem.add(string);
            }
        }

        int[] itemQuantity = new int[sizeOfListItem];
        int rand;
        // limit some item in map.
        while (currentItemScore < itemScore) {
            rand = (int) (Math.random() * sizeOfListItem);
            if(((listItem.get(rand).equals("shoe")|| 
                listItem.get(rand).equals("shield") ||
                listItem.get(rand).equals("clock") ||
                listItem.get(rand).equals("heart"))&& itemQuantity[rand] > 3)
                || listItem.get(rand).equals("coin"))continue;
            itemQuantity[rand]++;
            currentItemScore += LoadResource.itemScoreMap.get(listItem.get(rand));
        }
        rand = (int) (Math.random() * 4 + 1);
        itemQuantity[listItem.indexOf("coin")] = rand;// quantity of coin is not more 5;
        for(int i = 0; i < listItem.size(); i++){
            re += String.valueOf(itemQuantity[i])+ " " +listItem.get(i) + ";";
        }
        re = re.substring(0, re.length()-1);
    }
    public String getRe(){
        return re;
    }
}
