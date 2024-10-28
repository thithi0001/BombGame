package entity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

import static MenuSetUp.DimensionSize.maxScreenCol;
import static MenuSetUp.DimensionSize.maxScreenRow;
import static MenuSetUp.DimensionSize.tileSize;
import MenuSetUp.DimensionSize;


public class MonsterBoss extends Monster{
    private int width,height;
    private int hp;
    public MonsterBoss(GamePanel gp, int x, int y) {
        super(gp, x, y);
        this.hp=3;
        this.width=2*tileSize;
        this.height=2*tileSize;
        this.solidArea = new Rectangle(0,0,width,height);
        this.name="monsterBoss";
        setSpeed(1);
        setDefaultValues();
    }
    public void setSpeed(int newSpeed){
        if(newSpeed>=0){
            this.speed=newSpeed;
        }
    }
    @Override
    public void setDefaultValues() {
        speed = 1;
        direction = "down";
       
        x = (maxScreenCol / 2) * tileSize - width / 2;
        y = (maxScreenRow / 2) * tileSize - height / 2;
    }
    @Override
    public void update(){
        if (random.nextInt(100) < changeDirection) {
            randomMove();
        }

        
        gp.cChecker.checkBombForMoving(this);
        gp.cChecker.checkPlayerForMonster(this);

        move();
    }
    @Override
    
public void move() {
    moved = false;

    
    int mapWidth = DimensionSize.maxScreenCol * tileSize;
    int mapHeight = DimensionSize.maxScreenRow * tileSize;

    switch (direction) {
        case "up":
            if (y - speed >= 0 && canMoveUp) { 
                y -= speed;
                moved = true;
            }
            break;

        case "down":
            if (y + speed + height <= mapHeight && canMoveDown) {  
                y += speed;
                moved = true;
            }
            break;

        case "left":
            if (x - speed >= 0 && canMoveLeft) {  
                x -= speed;
                moved = true;
            }
            break;

        case "right":
            if (x + speed + width <= mapWidth && canMoveRight) {  
                x += speed;
                moved = true;
            }
            break;
    }

    if (!moved) {
        randomMove(); 
    }

    resetCollision(); 
}
@Override
public void draw(Graphics2D g2) {
    g2.setColor(Color.BLUE);
    g2.fillRect( x, y, width, height);
    // Vẽ thanh HP phía trên đầu quái
    g2.setColor(Color.RED);
    g2.fillRect(x, y-10, width*(hp/3), 5);
}
@Override
public void beingHit(){
    super.beingHit();
    hp--;
    if(hp<=0){
        isAlive=false;
    }else{
        setSpeed(speed+1);
    }
}
}
