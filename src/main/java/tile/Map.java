package tile;

import entity.Item;
import main.GamePanel;
import main.Main;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {

    GamePanel gp;
    String mapFileName;
    public int[][] mapTileNum;
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Point> itemPos = new ArrayList<>();

    public ArrayList<Point> monsterSpawnPos = new ArrayList<>();

    String baseTile;
    int baseIndex;
//    Monster[] monsters;

    public Map(GamePanel gp, String mapFileName) {

        this.gp = gp;
        this.mapFileName = mapFileName;
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        loadMap(Main.res + "/maps/" + mapFileName + ".txt");
        placeItem();
    }

    public void placeItem() {

        Random rand = new Random();
        Point p;
        int pIndex;
        for (Item item : items) {
            pIndex = rand.nextInt(itemPos.size());
            p = itemPos.get(pIndex);
            item.x = p.x * gp.tileSize;
            item.y = p.y * gp.tileSize;
            item.solidArea = new Rectangle(item.x, item.y, gp.tileSize, gp.tileSize);
            itemPos.remove(pIndex);
            System.out.println(p.y + "," + p.x);
        }
    }

    public void toBaseTile(int x, int y) {

        mapTileNum[x][y] = baseIndex;

        items.forEach(item -> {
            if (item.col() == x && item.row() == y) {
                item.hidden = false;
            }
        });
    }

    public int findBaseIndex() {
        for (int i = 0; i < gp.tileManager.tile.length; i++) {
            if (gp.tileManager.tile[i].name.equals(baseTile)) return i;
        }
        return 0;
    }

    public void loadMap(String filePath) {

        try {
            InputStream is = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            baseTile = br.readLine();
            baseIndex = findBaseIndex();
            String[] itemList = br.readLine().split(";");
            String[] tmp;
            int quantity;
            for (String item : itemList) {
                tmp = item.split(" ");
                quantity = Integer.parseInt(tmp[0]);
                while (quantity-- > 0)
                    items.add(new Item(gp, tmp[1]));
            }

            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                String line = br.readLine();
                String[] numbers = line.split(" ");

                while (col < gp.maxScreenCol) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    if (gp.tileManager.tile[num].destructible) {
                        itemPos.add(new Point(row, col));
                    }
                    col++;
                }

                if (col == gp.maxScreenCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
