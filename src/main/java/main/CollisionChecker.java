package main;

import bomb.Bomb;
import bomb.Flame;
import entity.Entity;
import entity.Item;
import entity.Player;

import java.awt.Rectangle;

import static MenuSetUp.DimensionSize.tileSize;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {

        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entityLeftX + entity.solidArea.width - 2;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBotY = entityTopY + entity.solidArea.height - 2;

        int entityLeftCol = entityLeftX / tileSize;
        int entityRightCol = entityRightX / tileSize;
        int entityTopRow = entityTopY / tileSize;
        int entityBotRow = entityBotY / tileSize;

        int eSpeed = entity.getSpeed();
        int tile1, tile2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopY - eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveUp = false;
                }
                break;
            case "down":
                entityBotRow = (entityBotY + eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityBotRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveDown = false;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftX - eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityLeftCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveLeft = false;
                }
                break;
            case "right":
                entityRightCol = (entityRightX + eSpeed) / tileSize;
                tile1 = gp.map.mapTileNum[entityRightCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveRight = false;
                }
                break;
        }
    }

    public boolean canPlaceBomb(Entity entity) {

        boolean canPlaceBomb = true;
        for (int i = 0; i < gp.bombs.size(); i++) {
            Bomb b = gp.bombs.get(i);
            if (entity.col() == b.col() && entity.row() == b.row()) {
                canPlaceBomb = false;
                break;
            }
        }
        return canPlaceBomb;
    }

    public void checkBombForMoving(Entity entity) {

        if (!entity.collisionOn) return;
        int eSpeed = entity.getSpeed();

        Rectangle solidArea = new Rectangle(entity.solidArea);
        solidArea.x += entity.x;
        solidArea.y += entity.y;
        switch (entity.direction) {
            case "up":
                solidArea.y -= eSpeed;
                gp.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveUp = false;
                    }
                });
                break;
            case "down":
                solidArea.y += eSpeed;
                gp.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveDown = false;
                    }
                });
                break;
            case "left":
                solidArea.x -= eSpeed;
                gp.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveLeft = false;
                    }
                });
                break;
            case "right":
                solidArea.x += eSpeed;
                gp.bombs.forEach((b) -> {
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
        solidArea.x += entity.x;
        solidArea.y += entity.y;
        if (flame.verticalSolidArea.intersects(solidArea)
                || flame.horizontalSolidArea.intersects(solidArea)) {
            entity.beingHit();
        }
    }

    public static void checkItemForFlame(Flame flame, Item item) {

        if (item.state == Item.States.shown && flame.duration > 1
                && (flame.verticalSolidArea.intersects(item.solidArea)
                || flame.horizontalSolidArea.intersects(item.solidArea))) {
            item.beingHit();
        }
    }

    public void checkPlayerForItem(Item item) {

        Player player = gp.player;
        Rectangle solidArea = new Rectangle(player.solidArea);
        solidArea.x += player.x;
        solidArea.y += player.y;
        if (item.state == Item.States.shown && solidArea.intersects(item.solidArea)) {
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
                    player.addSpeed(2);
                    break;

                case "clock":
                    player.setBombType("time");
                    break;

                case "shield":
                    player.setHasShield(true);
                    break;

                case "white":
                    gp.WIN = true;
                    break;

                default:
                    break;
            }
        }
    }
}