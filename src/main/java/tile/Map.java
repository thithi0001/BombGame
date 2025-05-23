package tile;

import bomb.Bomb;
import entity.Item;
import entity.Monster;
import main.GamePanel;
import main.Main;
import res.LoadResource;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static MenuSetUp.DimensionSize.*;

public class Map {

    GamePanel gp;
    String mapFileName;
    public int[][] mapTileNum;

    private final ArrayList<Point> newlyAddedObstacles = new ArrayList<>();
    private final ArrayList<Point> newlyRemovedObstacles = new ArrayList<>();

    public ArrayList<Bomb> bombs = new ArrayList<>();

    public ArrayList<Item> items = new ArrayList<>();
    ArrayList<Point> itemPos = new ArrayList<>();

    //    public MonsterBoss boss;
    public ArrayList<Monster> monsters = new ArrayList<>();
    ArrayList<Point> monsterSpawnPos = new ArrayList<>();

    String mode;
    int normalNumMonsters;

    public Point checkPos = new Point();
    String baseTile;
    int baseIndex;
    public int destructibleTiles;

    public Map(GamePanel gp, String mapFileName, String mode) {

        this.gp = gp;
        this.mapFileName = mapFileName;
        this.mode = mode;
        mapTileNum = new int[maxScreenCol][maxScreenRow];

        loadMap(Main.res + "/maps/" + mapFileName + ".txt");
        destructibleTiles = itemPos.size();
        placeItem();
        spawnMonster();
//        boss = new MonsterBoss(gp, 8, 6);
        setupCheckPoint();
    }

    void spawnMonster() {
        monsterSpawnPos.forEach(m -> monsters.add(new Monster(gp, m.x, m.y)));
    }

    public void initMonsterAi() {
        monsters.forEach(Monster::initAI);
    }

    void placeItem() {

        if (itemPos.isEmpty()) return;
        Random rand = new Random();
        Point p;
        int pIndex;
        for (Item item : items) {
            pIndex = rand.nextInt(itemPos.size());
            p = itemPos.get(pIndex);
            item.x = p.x * tileSize;
            item.y = p.y * tileSize;
            item.solidArea = new Rectangle(item.x, item.y, tileSize, tileSize);
            itemPos.remove(pIndex);
//            System.out.println(p.y + "," + p.x);
        }
    }

    public void completeMap() {
        items.forEach(item -> {
            if (item.name.equals("closing_door")) {
                item.name = "opening_door";
                item.itemImg = LoadResource.itemImgMap.get(item.name);
            }
        });
    }

    public void toBaseTile(int x, int y) {

        gp.player.score += gp.tileManager.tile[mapTileNum[x][y]].score;

        mapTileNum[x][y] = baseIndex;
        destructibleTiles--;

        items.forEach(item -> {
            if (item.col() == x && item.row() == y) {
                item.state = Item.States.shown;
            }
        });

        newlyRemovedObstacles.add(new Point(x, y));
    }

    public boolean isObstacle(int x, int y) {
        return gp.tileManager.tile[mapTileNum[x][y]].collision;
    }

    public ArrayList<Point> getNewlyRemovedObstacles() {
        return newlyRemovedObstacles;
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
        newlyAddedObstacles.add(bomb.getPosition());
    }

    public void removeBomb(Bomb bomb) {
        bombs.remove(bomb);
        newlyAddedObstacles.remove(bomb.getPosition());
        newlyRemovedObstacles.add(bomb.getPosition());
    }

    public ArrayList<Point> getNewlyAddedObstacles() {
        return newlyAddedObstacles;
    }

    int findBaseIndex() {
        for (int i = 0; i < gp.tileManager.tile.length; i++) {
            if (gp.tileManager.tile[i].name.equals(baseTile)) return i;
        }
        return 0;
    }

    void setupCheckPoint() {
        Item item = new Item(gp, "closing_door");
        item.state = Item.States.shown;
        item.isCheckPoint = true;
        item.x = checkPos.x * tileSize;
        item.y = checkPos.y * tileSize;
        item.solidArea = new Rectangle(item.x, item.y, tileSize, tileSize);
        items.add(item);
    }

    void getMonsterPos(String[] arr) {
        String[] pos;
        int size = (mode.equals("hard"))? arr.length : normalNumMonsters;
        for (int i = 0; i < size; i++) {
            pos = arr[i].split(",");
            monsterSpawnPos.add(new Point(Integer.parseInt(pos[1]),
                    Integer.parseInt(pos[0])));
        }
    }

    void loadMap(String filePath) {

        try {
            InputStream is = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            // find baseTile
            baseTile = br.readLine();
            baseIndex = findBaseIndex();
            // get items' position
            String[] normalItemList = br.readLine().split(";");
            String[] hardItemList = br.readLine().split(";");
            // get monsters' position
            String[] monsterList = br.readLine().split(";");
            normalNumMonsters = Integer.parseInt(br.readLine());
            getMonsterPos(monsterList);
            // get checkPoint's position
            String[] point = br.readLine().split(",");
            checkPos.x = Integer.parseInt(point[1]);
            checkPos.y = Integer.parseInt(point[0]);
            // get item's quantity
            String[] tmp;
            String[] itemList = (mode.equals("hard"))? hardItemList : normalItemList;
            int quantity;
            for (String item : itemList) {
                tmp = item.split(" ");
                quantity = Integer.parseInt(tmp[0]);
                while (quantity-- > 0)
                    items.add(new Item(gp, tmp[1]));
            }

            while (col < maxScreenCol && row < maxScreenRow) {
                String line = br.readLine();
                String[] numbers = line.split(" ");

                while (col < maxScreenCol) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    if (gp.tileManager.tile[num].destructible) {
                        itemPos.add(new Point(col, row));
                    }
                    col++;
                }

                if (col == maxScreenCol) {
                    col = 0;
                    row++;
                }
            }
            is.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {

        items.forEach(Item::update);
        items.removeIf(item -> item.state == Item.States.isHit || item.state == Item.States.isPickedUp);
        monsters.forEach(Monster::update);
        monsters.removeIf(monster -> !monster.isAlive);
//        if (boss.isAlive)
//            boss.update();
    }

    public void draw(Graphics2D g2) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < maxScreenCol && row < maxScreenRow) {

            int tileNum = mapTileNum[col][row];

            g2.drawImage(gp.tileManager.tile[tileNum].image, x, y, null);
            col++;
            x += tileSize;

            if (col == maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += tileSize;
            }
        }

        items.forEach(item -> {
            if (item.state == Item.States.shown) {
                item.draw(g2);
            }
        });

        monsters.forEach(m -> {
            if (m.isAlive) {
                m.draw(g2);
            }
        });
//        if (boss.isAlive)
//            boss.draw(g2);
    }

}
