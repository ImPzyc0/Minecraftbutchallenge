package com.plugin.pzyc0;


import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public final class Main extends JavaPlugin implements Listener {

    private HashMap<EntityType, EntityType> entitymap;
    private HashMap<LivingEntity, LivingEntity> entityCoverMap2;
    private HashMap<LivingEntity, LivingEntity> entityCoverMap;
    private ArrayList<EntityType> allLivingEntityTypes;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this, this);
        entitymap = new HashMap<>();
        entityCoverMap = new HashMap<>();
        allLivingEntityTypes = new ArrayList<>();
        entityCoverMap2 = new HashMap<>();

        for(EntityType ent : EntityType.values()){
            if(ent.isAlive()){
                allLivingEntityTypes.add(ent);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent e) {

        if (entityCoverMap.containsKey(e.getEntity())) {
            entityCoverMap.get(e.getEntity()).teleport(e.getEntity().getLocation());
        }


        if (entitymap.containsKey(e.getEntityType()) && !entityCoverMap.containsKey(e.getEntity()) && !entityCoverMap.containsValue(e.getEntity()) && e.getEntityType() != EntityType.PLAYER) {
            LivingEntity realEntity = e.getEntity();
            realEntity.setInvisible(true);
            realEntity.setInvulnerable(true);
            realEntity.setCollidable(false);
//
            LivingEntity coverEntity = (LivingEntity) e.getEntity().getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), entitymap.get(e.getEntityType()));
            coverEntity.setAI(false);
            coverEntity.setCanPickupItems(false);
            coverEntity.setGravity(false);
            coverEntity.setSilent(true);
            coverEntity.setMaxHealth(realEntity.getMaxHealth());
//
            entityCoverMap.put(realEntity, coverEntity);
            entityCoverMap2.put(coverEntity, realEntity);
        } else if (!entitymap.containsKey(e.getEntityType()) && !entityCoverMap.containsKey(e.getEntity()) && !entityCoverMap.containsValue(e.getEntity())&& e.getEntityType() != EntityType.PLAYER) {
//
            Random random = new Random();
//
            LivingEntity realEntity = e.getEntity();
            realEntity.setInvisible(true);
            realEntity.setInvulnerable(true);
            realEntity.setCollidable(false);
//
            entitymap.put(e.getEntityType(), allLivingEntityTypes.get(random.nextInt(allLivingEntityTypes.size())));
//
            LivingEntity coverEntity = (LivingEntity) e.getEntity().getLocation().getWorld().spawnEntity(e.getEntity().getLocation(), entitymap.get(e.getEntityType()));
            coverEntity.setAI(false);
            coverEntity.setCanPickupItems(false);
            coverEntity.setGravity(false);//
            coverEntity.setSilent(true);
            coverEntity.setMaxHealth(realEntity.getMaxHealth());
//
            entityCoverMap.put(realEntity, coverEntity);
            entityCoverMap2.put(coverEntity, realEntity);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
       if(entityCoverMap.containsValue(e.getEntity())){
           e.getDrops().clear();
           entityCoverMap2.get(e.getEntity()).setHealth(0);
           e.getEntity().setHealth(0);
       }
    }
//

}
