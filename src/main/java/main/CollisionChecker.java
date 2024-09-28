package main;

import entity.Entity;

import java.awt.Rectangle;

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

        int entityLeftCol = entityLeftX / gp.tileSize;
        int entityRightCol = entityRightX / gp.tileSize;
        int entityTopRow = entityTopY / gp.tileSize;
        int entityBotRow = entityBotY / gp.tileSize;

        int tile1, tile2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopY - entity.speed) / gp.tileSize;
                tile1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveUp = false;
                }
                break;
            case "down":
                entityBotRow = (entityBotY + entity.speed) / gp.tileSize;
                tile1 = gp.tileManager.mapTileNum[entityLeftCol][entityBotRow];
                tile2 = gp.tileManager.mapTileNum[entityRightCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveDown = false;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftX - entity.speed) / gp.tileSize;
                tile1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tile2 = gp.tileManager.mapTileNum[entityLeftCol][entityBotRow];
                if (gp.tileManager.tile[tile1].collision || gp.tileManager.tile[tile2].collision) {
                    entity.canMoveLeft = false;
                }
                break;
            case "right":
                entityRightCol = (entityRightX + entity.speed) / gp.tileSize;
                tile1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                tile2 = gp.tileManager.mapTileNum[entityRightCol][entityBotRow];
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

}