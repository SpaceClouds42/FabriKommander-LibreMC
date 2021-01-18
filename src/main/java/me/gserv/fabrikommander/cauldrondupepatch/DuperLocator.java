package me.gserv.fabrikommander.cauldrondupepatch;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DuperLocator {
    public static String getDuper(ItemEntity itemEntity) {
        World world = itemEntity.getEntityWorld();
        PlayerEntity duper = world.getClosestPlayer(itemEntity, 10);
        if (duper == null) { return ""; } else { return duper.getEntityName(); }
    }
}
