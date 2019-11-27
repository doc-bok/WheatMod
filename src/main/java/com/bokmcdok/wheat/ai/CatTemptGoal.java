package com.bokmcdok.wheat.ai;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nullable;

public class CatTemptGoal  extends net.minecraft.entity.ai.goal.TemptGoal {
    @Nullable
    private PlayerEntity temptingPlayer;
    private final CatEntity cat;

    /**
     * Construction
     * @param catIn The cat the goal belongs to.
     * @param speedIn The speed of the cat.
     * @param temptItemsIn A list of items that tempt the cat.
     * @param scaredByPlayerMovement Whether or not the cat is scared by player movement.
     */
    public CatTemptGoal(CatEntity catIn, double speedIn, Ingredient temptItemsIn, boolean scaredByPlayerMovement) {
        super(catIn, speedIn, temptItemsIn, scaredByPlayerMovement);
        cat = catIn;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        super.tick();

        //  Handle player tempting the cat with food
        if (temptingPlayer == null && creature.getRNG().nextInt(600) == 0) {
            temptingPlayer = this.closestPlayer;
        } else if (creature.getRNG().nextInt(500) == 0) {
            temptingPlayer = null;
        }

        //  Play the beg sound if the cat wants food
        if (isRunning() && !cat.isTamed() && cat.ticksExisted % 100 == 0) {
            cat.playSound(SoundEvents.ENTITY_CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }

    }

    /**
     * Check if the cat is scared by player movement
     * @return True if the cat is scared
     */
    protected boolean isScaredByPlayerMovement() {
        return temptingPlayer != null && temptingPlayer.equals(closestPlayer) ? false : super.isScaredByPlayerMovement();
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     * @return True if the goal should execute
     */
    public boolean shouldExecute() { return super.shouldExecute() && !cat.isTamed(); }
}