package entity;

import main.GamePanel;
import main.UtilityTool;

import java.awt.Rectangle;
import static MenuSetUp.DimensionSize.tileSize;

public class QBoss extends Entity{
    public int width, height;
    int hp;
    int invincibleTime = 0;
    int cooldown;// cd boss's skill
    int cooldownTime ;
    int cdSummon;
    int cdSummonTime;
    public boolean isAlive;
    boolean canHit ;
    boolean beFurious;
    boolean beingCooldown = true;
    boolean canSummon;
    QBoss(){
        setDefaultValues();
    }

    QBoss(GamePanel gp, int x, int y){
        this.gp = gp;
        this.name = "monster";
        this.x = x;
        this.y = y;
        this.solidArea = new Rectangle(0, 0, tileSize, tileSize);
        setDefaultValues();
    }
    public void setDefaultValues(){
        this.hp = 10;
        this.cooldownTime = 15;
        this.isAlive = true;
        this.canHit = true;
        this.beFurious = false;
        this.cdSummonTime = 75;
        setSpeed(0);
    }

    public void update(){
        if(canHit == false) enterInvincibleTime();
        
        if(beingCooldown == true) enterCooldown();
        else{
            randomUseSkill();
        }

        if(canSummon == false) enterCooldownSummon();
    }

    public void getHit(){
        if (canHit == true){
            hp --;
            canHit = false;
            invincibleTime = UtilityTool.convertTime(1.0);
            if(hp == 4){
                enterFuriousState();
            }
            if(hp == 0){
                isAlive = false;
                isEndGame();
            }
        }
    }

    void enterInvincibleTime() {
        invincibleTime--;
        if (invincibleTime == 0) {
            canHit = true;
        }
    }

    void enterCooldown() {
        cooldown--;
        if (cooldown == 0) {
            beingCooldown = false;
            cooldown = cooldownTime;
        }
    }

    void enterCooldownSummon() {
        cdSummon--;
        if (cdSummon == 0) {
            canSummon = true;
            cdSummon = cdSummonTime;
        }
    }

    void randomUseSkill(){
        System.out.println("Boss random skill");
        beingCooldown = true;
    }

    void firstSkill(){
        if(beFurious == false)
            System.out.println("Boss use fisrt skill");
        else System.out.println("Furious Boss use fisrt skill");
    }

    void secondSkill(){
        if(beFurious == false)
            System.out.println("Boss use second skill");
        else System.out.println("Furious Boss use second skill");
    }
    void summon(){
        System.out.println("Boss summon block");
        canSummon = false;
    }

    void enterFuriousState(){
        beFurious = true;
        cooldownTime -= 10;
        System.out.println("boss is furious");
    }

    public void isEndGame() {
        if(isAlive == false){
            System.out.println("endGame");
        }
    }
}
