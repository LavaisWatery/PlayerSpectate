package me.lavaiswatery.playerspectate.util;

import me.lavaiswatery.playerspectate.manager.MessageManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CommonUtility {

    public static boolean isFood(Material material) {
        return (material == Material.COOKED_BEEF || material == Material.COOKED_CHICKEN || material == Material.COOKED_FISH || material == Material.GRILLED_PORK || material == Material.PORK || material == Material.MUSHROOM_SOUP || material == Material.RAW_BEEF || material == Material.RAW_CHICKEN || material == Material.RAW_FISH || material == Material.APPLE || material == Material.GOLDEN_APPLE || material == Material.MELON || material == Material.COOKIE || material == Material.BREAD || material == Material.SPIDER_EYE || material == Material.ROTTEN_FLESH || material == Material.POTATO_ITEM);
    }

    public static boolean canReallySeeEntity(Player player, LivingEntity entity) {
        BlockIterator bl = new BlockIterator(player, 7);
        boolean found = false;
        double md = 1;

        if(entity.getType() == EntityType.WITHER) {
            md = 9;
        }
        else if(entity.getType() == EntityType.ENDERMAN) {
            md = 5;
        }
        else {
            md = md + entity.getEyeHeight();
        }

        while(bl.hasNext()){
            found = true;
            double d = bl.next().getLocation().distanceSquared(entity.getLocation());

            if(d <= md){
                return true;
            }
        }

        bl = null;

        if(!found){
            return true;
        }

        return false;
    }

    public static String formatNumber(String str) {
        int amount = Integer.parseInt(str);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    public static boolean isNumber(String str){
        try {
            int i = Integer.parseInt(str);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    public static int checkSlotsAvailable(Player player) {
        Inventory inv = player.getInventory(); //Inventory of the player
        ItemStack[] items = inv.getContents(); //Contents of player inventory
        int emptySlots = 0;

        for (ItemStack is : items) {
            if (is == null) {
                emptySlots = emptySlots + 1;
            }
        }

        return emptySlots;
    }

    public static void loadChunk(Location loc) {
        if(!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }
    }

    public static boolean isPotion(ItemStack item) {
        if(item.getType() == Material.POTION) {
            return true;
        }
        return false;
    }

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static LivingEntity getTarget(Player player) {
        int range = 8;
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for (Entity e : player.getNearbyEntities(range, range, range)) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }
        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(player, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez, md = Double.MAX_VALUE, d;

        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();

            for (LivingEntity e : livingE) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                d = loc.distanceSquared(player.getLocation());
                if(e.getType() == EntityType.HORSE) {
                    if ((bx - 1.2 <= ex && ex <= bx + 2.2)
                            && (bz - 1.2 <= ez && ez <= bz + 2.2)
                            && (by - 2.5 <= ey && ey <= by + 4.5)) {
                        if(d < md){
                            md = d;
                            target = e;
                        }
                    }
                }
                else {
                    if ((bx - .80 <= ex && ex <= bx + 1.85)
                            && (bz - .80 <= ez && ez <= bz + 1.85)
                            && (by - 2.5 <= ey && ey <= by + 4.5)) {
                        if(d < md) {
                            md = d;
                            target = e;
                        }
                    }
                }
            }
        }
        livingE.clear();

        return target;
    }

    public static void clearAllInventory(Player player) {
        if(player != null) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }
    }

    public static void clearPotions(Player player) {
        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static boolean instantBreak(Material m){
        return m == Material.TORCH || m == Material.FLOWER_POT || m == Material.RED_ROSE || m == Material.YELLOW_FLOWER || m == Material.LONG_GRASS
                || m == Material.RED_MUSHROOM || m == Material.BROWN_MUSHROOM || m == Material.TRIPWIRE || m == Material.TRIPWIRE_HOOK ||
                m == Material.DEAD_BUSH || m == Material.DIODE_BLOCK_OFF || m == Material.DIODE_BLOCK_ON || m == Material.REDSTONE_COMPARATOR_OFF
                || m == Material.REDSTONE_COMPARATOR_OFF || m == Material.REDSTONE_WIRE || m == Material.REDSTONE_TORCH_OFF ||
                m == Material.REDSTONE_TORCH_ON || m == Material.DOUBLE_PLANT || m == Material.SUGAR_CANE_BLOCK;
    }

    public static List<Material> getArmorPieceList() {
        List<Material> list = new ArrayList<Material>();
        list.add(Material.LEATHER_HELMET);
        list.add(Material.LEATHER_CHESTPLATE);
        list.add(Material.LEATHER_LEGGINGS);
        list.add(Material.LEATHER_BOOTS);

        list.add(Material.CHAINMAIL_HELMET);
        list.add(Material.CHAINMAIL_CHESTPLATE);
        list.add(Material.CHAINMAIL_LEGGINGS);
        list.add(Material.CHAINMAIL_BOOTS);

        list.add(Material.GOLD_HELMET);
        list.add(Material.GOLD_CHESTPLATE);
        list.add(Material.GOLD_LEGGINGS);
        list.add(Material.GOLD_BOOTS);

        list.add(Material.IRON_HELMET);
        list.add(Material.IRON_CHESTPLATE);
        list.add(Material.IRON_LEGGINGS);
        list.add(Material.IRON_BOOTS);

        list.add(Material.DIAMOND_HELMET);
        list.add(Material.DIAMOND_CHESTPLATE);
        list.add(Material.DIAMOND_LEGGINGS);
        list.add(Material.DIAMOND_BOOTS);
        return list;
    }

    public boolean isArmorPiece(Material m) {
        if(getArmorPieceList().contains(m)) {
            return true;
        }
        return false;
    }

    public static List<Material> getSwordList() {
        List<Material> list = new ArrayList<Material>();
        list.add(Material.WOOD_SWORD);
        list.add(Material.IRON_SWORD);
        list.add(Material.DIAMOND_SWORD);
        list.add(Material.GOLD_SWORD);
        list.add(Material.STONE_SWORD);
        return list;
    }

    public boolean isSword(Material m) {
        if(getSwordList().contains(m)) {
            return true;
        }
        return false;
    }

    public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
        double gravity = 0.115;
        int endGain = to.getBlockY() - from.getBlockY();

        double horizDist = Math.sqrt(distanceSquared(from, to));
        int gain = heightGain;
        double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);

        double a = -horizDist * horizDist / (4 * maxGain);
        double b = horizDist;
        double c = -endGain;
        double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);

        double vy = Math.sqrt(maxGain * gravity);
        double vh = vy / slope;

        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;

        double vx = vh * dirx;
        double vz = vh * dirz;
        return new Vector(vx, vy, vz);
    }

    public static double distanceSquared(Vector from, Vector to) {
        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();
        return dx * dx + dz * dz;
    }

    public static int getIntBetween(int x, int y) {
        Random r = new Random();
        int num = r.nextInt(y-x) + x;
        return num;
    }

    public static boolean playerDosntExist(CommandSender sender) {
        MessageManager.message(sender, ChatColor.RED + "This player dosn't exist!");
        return true;
    }

    public static String trimList(List<String> list) {
        if(list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator i = list.iterator();
        if(i.hasNext()){
            sb.append(i.next());
            while(i.hasNext()){
                sb.append(',').append(i.next());
            }
        }
        return sb.toString();
    }

    public static List<String> toStringList(String string) {
        if(string == null) {
            return new ArrayList<String>();
        }
        List<String> listString = new ArrayList<String>();
        String[] split = string.split(",");
        for(String temp : split) {
            listString.add(temp);
        }
        return listString;
    }

    public static PluginManager getPluginManager() { return Bukkit.getPluginManager(); }

    @SuppressWarnings("deprecation")
    public static boolean canSeeBlock(Player player, Block block){
        RayTrace rt = RayTrace.eyeTrace(player, ((player.getGameMode() == GameMode.CREATIVE) ? 8 : 6));
        Block s = rt.getBlock();

        if(s != null){
            if(s.getX() == block.getX() && s.getY() == block.getY() && s.getZ() == block.getZ() && s.getType() == block.getType() && s.getData() == block.getData()){
                return true;
            }
        }

        return false;
    }

    public static boolean isClass(String className) {
        boolean exist = true;
        try {
            Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            exist = false;
        }
        return exist;
    }

    public static int getPing(Player player){
        return ((CraftPlayer) player).getHandle().ping;
    }

    public static double getXZDistance(double x1, double x2, double z1, double z2){
        double a1 = (x2 - x1), a2 = (z2 - z1);

        return ((a1 * (a1)) + (a2 * a2));
    }

    public static boolean isOnLadder(Player player){
        return ((CraftPlayer) player).getHandle().h_();
    }

    public static boolean inWater(Player player){
        return (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER);
    }

    public static int getPotionLevel(Player player, PotionEffectType p){
        for(PotionEffect pe : player.getActivePotionEffects()){
            if(pe.getType().getName().equals(p.getName())){
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
}
