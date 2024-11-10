import java.io.Serializable;
import java.util.UUID;

public class Character implements Serializable {
    private static final long serialVersionUID = 1L;
    String Name;
    String type;
    double x;
    double y;
    int direction; // 1 for left, 2 for right
    int size = 64;
    int scale = 2;
    int maxHealth;
    double currentHealth;
    int damage;
    int speed;
    int attackRange;  // New field for attack range
    boolean isRanged; // New field to identify ranged units
    boolean isInCombat;
    int towerDamageScale;
    public String id; // Add unique identifier
    
    public Character(String type, int playerId) {
        this.type = type;
        this.id = UUID.randomUUID().toString(); // Generate unique ID
        
        // Set attributes based on character type and player ID
        if (playerId == 1) {
            switch (type) {
                case "CHAR1": // Alice
                    Name = "Alice";
                    maxHealth = 100;
                    damage = 20;
                    speed = 4;
                    attackRange = 50; // Melee range
                    isRanged = false;
                    towerDamageScale = 1;
                    break;
                case "CHAR2": // Mad Hatter (now ranged)
                    Name = "Mad Hatter";
                    maxHealth = 80;
                    damage = 15;
                    speed = 2;
                    attackRange = 300; // Long range
                    isRanged = true;
                    towerDamageScale = 2;
                    break;
                case "CHAR3": // Tweedles
                    Name = "Tweedle Twins";
                    maxHealth = 150;
                    damage = 30;
                    speed = 2;
                    attackRange = 50; // Melee range
                    isRanged = false;
                    towerDamageScale = 2;
                    break;
                case "CHAR4": // Bandersnatch
                    Name = "Bandersnatch";
                    maxHealth = 300;
                    damage = 10;
                    speed = 1;
                    attackRange = 50;
                    isRanged = false;
                    towerDamageScale = 4;
                    break;
            }
        } else {
            switch (type) {
                case "CHAR1": // Guard
                    Name = "Guard";
                    maxHealth = 100;
                    damage = 20;
                    speed = 4;
                    attackRange = 50; // Melee range
                    isRanged = false;
                    towerDamageScale = 1;
                    break;
                case "CHAR2": // Jabberwocky (now ranged)
                    Name = "Jabberwocky";
                    maxHealth = 80;
                    damage = 15;
                    speed = 2;
                    attackRange = 300; // Long range
                    isRanged = true;
                    towerDamageScale = 2;
                    break;
                case "CHAR3": // Queen
                    Name = "Red Queen";
                    maxHealth = 150;
                    damage = 30;
                    speed = 2;
                    attackRange = 50; // Melee range
                    isRanged = false;
                    towerDamageScale = 2;
                    break;
                case "CHAR4": // Knave
                    Name = "Knave";
                    maxHealth = 300;
                    damage = 10;
                    speed = 1;
                    attackRange = 50;
                    isRanged = false;
                    towerDamageScale = 4;
                    break;
            }
        }
        currentHealth = maxHealth;
    }
    
    public void attack(Character target) {
        if (currentHealth > 0 && target.currentHealth > 0) {
            target.currentHealth -= damage;
            if (target.currentHealth < 0) target.currentHealth = 0;
        }
    }
    
    public boolean isDead() {
        return currentHealth <= 0;
    }
    
    public void move() {
        if (!isInCombat) {
            if (direction == 1) { // Moving left
                x -= speed;
            } else { // Moving right
                x += speed;
            }
        }
    }
}