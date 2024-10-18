package main;

import bomb.Flame;
import entity.Entity;
import entity.Item;

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

        int tile1, tile2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopY - entity.speed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveUp = false;
                }
                break;
            case "down":
                entityBotRow = (entityBotY + entity.speed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityBotRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveDown = false;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed) / tileSize;
                tile1 = gp.map.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityLeftCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveLeft = false;
                }
                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed) / tileSize;
                tile1 = gp.map.mapTileNum[entityRightCol][entityTopRow];
                tile2 = gp.map.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveRight = false;
                }
                break;
        }
    }

    public void checkBomb(Entity entity) {

        if (!entity.collisionOn) return;

        Rectangle solidArea = new Rectangle(entity.solidArea);
        solidArea.x += entity.x;
        solidArea.y += entity.y;
        switch (entity.direction) {
            case "up":
                solidArea.y -= entity.speed;
                gp.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveUp = false;
                    }
                });
                break;
            case "down":
                solidArea.y += entity.speed;
                gp.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveDown = false;
                    }
                });
                break;
            case "left":
                solidArea.x -= entity.speed;
                gp.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveLeft = false;
                    }
                });
                break;
            case "right":
                solidArea.x += entity.speed;
                gp.bombs.forEach((b) -> {
                    if (solidArea.intersects(b.solidArea) && !b.letPlayerPassThrough) {
                        entity.canMoveRight = false;
                    }
                });
                break;
        }
    }

    public static void checkEntityForFlame(Flame flame, Entity entity) {

        Rectangle solidArea = new Rectangle(entity.solidArea);
        solidArea.x += entity.x;
        solidArea.y += entity.y;
        if (flame.verticalSolidArea.intersects(solidArea)
                || flame.horizontalSolidArea.intersects(solidArea)) {
            entity.isHit = true;
            System.out.println("hit player");
        }
    }

    public static void checkItemForFlame(Flame flame, Item item) {

        if (item.state == Item.States.shown && flame.duration > 1
                && (flame.verticalSolidArea.intersects(item.solidArea)
                || flame.horizontalSolidArea.intersects(item.solidArea))) {
            item.state = Item.States.isHit;
            // do something when the item is hit
            System.out.println("hit " + item.name);
        }
    }

    public void checkPlayerForItem(Item item) {

        Rectangle solidArea = new Rectangle(gp.player.solidArea);
        solidArea.x += gp.player.x;
        solidArea.y += gp.player.y;
        if (item.state == Item.States.shown && solidArea.intersects(item.solidArea)) {
            item.state = Item.States.isPickedUp;
            System.out.println("+1 " + item.name);
        }
    }
}