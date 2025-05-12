package main;

import bomb.Bomb;
import bomb.Flame;
import entity.Entity;
import entity.Item;
import entity.Monster;
import entity.Player;

import java.awt.Rectangle;

import static MenuSetUp.DimensionSize.tileSize;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        if (!entity.collisionOn) return;
        int entityLeftX = entity.solidArea.x;
        int entityRightX = entityLeftX + entity.solidArea.width;
        int entityTopY = entity.solidArea.y;
        int entityBotY = entityTopY + entity.solidArea.height;

        int entityLeftCol = entityLeftX / tileSize;
        int entityRightCol = entityRightX / tileSize;
        int entityTopRow = entityTopY / tileSize;
        int entityBotRow = entityBotY / tileSize;

        int eSpeed = entity.getSpeed();
        int tile1, tile2;

        switch (entity.direction) {
            case UP:
                entityTopRow = (entityTopY - eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveUp = false;
                }
                break;
            case DOWN:
                entityBotRow = (entityBotY + eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityBotRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveDown = false;
                }
                break;
            case LEFT:
                entityLeftCol = (entityLeftX - eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityLeftCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveLeft = false;
                }
                break;
            case RIGHT:
                entityRightCol = (entityRightX + eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityRightCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveRight = false;
                }
                break;
        }
    }

    public void checkMapBoundary(Entity entity) {}

    public boolean hasBombHere(int col, int row) {

        for (Bomb bomb: gp.map.bombs) {
            if (col == bomb.col() && row == bomb.row()) {
                return true;
            }
        }
        return false;
    }

    public void checkBombForEntity(Entity entity) {

        if (!entity.collisionOn) return;
        int eSpeed = entity.getSpeed();

        Rectangle solidArea = new Rectangle(entity.solidArea);
        switch (entity.direction) {
            case UP:
                solidArea.y -= eSpeed;
                gp.map.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveUp = false;
                    }
                });
                break;
            case DOWN:
                solidArea.y += eSpeed;
                gp.map.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveDown = false;
                    }
                });
                break;
            case LEFT:
                solidArea.x -= eSpeed;
                gp.map.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveLeft = false;
                    }
                });
                break;
            case RIGHT:
                solidArea.x += eSpeed;
                gp.map.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveRight = false;
                    }
                });
                break;
        }
    }

    public static void checkBombForFlame(Flame flame, Bomb bomb) {

        if ((flame.verticalSolidArea.intersects(bomb.solidArea)
                || flame.horizontalSolidArea.intersects(bomb.solidArea))
                && !bomb.exploding) {
            bomb.setCountDown(1);
        }
    }

    public static void checkEntityForFlame(Flame flame, Entity entity) {

        Rectangle solidArea = new Rectangle(entity.solidArea);
        if (flame.verticalSolidArea.intersects(solidArea)
                || flame.horizontalSolidArea.intersects(solidArea)) {
            entity.getHit();
        }
    }

    public static void checkItemForFlame(Flame flame, Item item) {

        if (item.state == Item.States.shown && flame.getDuration() > 1
                && (flame.verticalSolidArea.intersects(item.solidArea)
                || flame.horizontalSolidArea.intersects(item.solidArea))) {
            item.getHit();
        }
    }

    public void checkPlayerForMonster(Monster monster) {

        Player player = gp.player;
        if (player.solidArea.intersects(monster.solidArea))
            player.getHit();
    }

    public void checkPlayerForItem(Item item) {

        Player player = gp.player;
        if (item.state == Item.States.shown && player.solidArea.intersects(item.solidArea)) {
            item.beingPickedUp();
            player.addScore(item.score);
            switch (item.name) {
                case "plus_flame":
                    player.addMoreFlame(1);
                    break;

                case "plus_bomb":
                    player.addMoreBombs(1);
                    break;

                case "shoe":
                    player.increaseSpeed();
                    break;

                case "clock":
                    player.setBombType("time");
                    break;

                case "shield":
                    player.activateShield();
                    break;

                case "opening_door":
                    gp.WIN = true;
                    break;

                case "glove":
                    player.setHasGlove();
                    break;

                case "heart":
                    player.increaseHp();
                    break;

                default:
                    break;
            }
        }
    }

    public boolean checkTileForBomb(Bomb bomb) {
        int row =  bomb.y / tileSize;
        int col = bomb.x / tileSize;
        switch (bomb.direction) {
            case UP:
                row = (bomb.y - bomb.speed) / tileSize;
                break;
            case DOWN:
                row += 1;
                break;
            case LEFT:
                col = (bomb.x - bomb.speed) / tileSize;
                break;
            case RIGHT:
                col += 1;
                break;
        }
        int tile = gp.map.mapTileNum[col][row];
        return gp.tileManager.tile[tile].collision;
    }

    public boolean checkBombForBomb(Bomb bomb) {
        Rectangle solidArea = new Rectangle(bomb.solidArea);
        switch (bomb.direction) {
            case UP:
                solidArea.y -= bomb.speed;
                solidArea.height = bomb.speed;
                break;
            case DOWN:
                solidArea.y += tileSize;
                solidArea.height = bomb.speed;
                break;
            case LEFT:
                solidArea.x -= bomb.speed;
                solidArea.width = bomb.speed;
                break;
            case RIGHT:
                solidArea.x += tileSize;
                solidArea.width = bomb.speed;
                break;
        }

        for (int i = 0; i < gp.map.bombs.size(); i++)
            if (solidArea.intersects(gp.map.bombs.get(i).solidArea))
                return true;
        return false;
    }

    public boolean checkMonsterForBomb(Bomb bomb) {
        Rectangle solidArea = new Rectangle(bomb.solidArea);
        switch (bomb.direction) {
            case UP:
                solidArea.y -= bomb.speed;
                break;
            case DOWN:
                solidArea.y += bomb.speed;
                break;
            case LEFT:
                solidArea.x -= bomb.speed;
                break;
            case RIGHT:
                solidArea.x += bomb.speed;
                break;
        }

        for (int i = 0; i < gp.map.monsters.size(); i++)
            if (solidArea.intersects(gp.map.monsters.get(i).solidArea))
                return true;
        return false;
    }
}