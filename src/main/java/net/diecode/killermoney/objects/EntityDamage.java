package net.diecode.killermoney.objects;

import java.math.BigDecimal;
import java.util.UUID;

public class EntityDamage {

    private UUID puuid;
    private BigDecimal damage;
    private BigDecimal calculatedMoney;

    public EntityDamage(UUID puuid, BigDecimal damage) {
        this.puuid = puuid;
        this.damage = damage;
    }

    public EntityDamage(UUID puuid, BigDecimal damage, BigDecimal money) {
        this.puuid = puuid;
        this.damage = damage;
        this.calculatedMoney = money;
    }

    public UUID getPlayerUUID() {
        return puuid;
    }

    public BigDecimal getDamage() {
        return damage;
    }
    
    public void increaseDamage(BigDecimal damage) {
        this.damage = this.damage.add(damage);
    }

    public BigDecimal getCalculatedMoney() {
        return calculatedMoney;
    }

    public void setCalculatedMoney(BigDecimal calculatedMoney) {
        this.calculatedMoney = calculatedMoney;
    }
}
