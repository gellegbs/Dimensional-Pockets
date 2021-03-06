package net.gtn.dimensionalpocket.common.core.utils;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Utils {

    public static ForgeDirection getDirectionFromBitMask(int num) {
        switch (num) {
            case 0:
                return ForgeDirection.SOUTH;
            case 1:
                return ForgeDirection.WEST;
            case 2:
                return ForgeDirection.NORTH;
            case 3:
                return ForgeDirection.EAST;
            default:
                return ForgeDirection.UNKNOWN;
        }
    }

    public static String capitalizeString(String string) {
        if (string == null || string.isEmpty())
            return "";

        String firstLetter = string.substring(0, 1).toUpperCase();
        if (string.length() == 1)
            return firstLetter;

        String rest = string.substring(1).toLowerCase();
        return firstLetter + rest;
    }

    public static NBTTagCompound getPlayerPersistTag(EntityPlayer player) {
        NBTTagCompound tag = player.getEntityData();

        NBTTagCompound persistTag = null;
        if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        } else {
            persistTag = new NBTTagCompound();
            tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
        }

        NBTTagCompound modTag = null;
        String modID = Reference.MOD_ID;

        if (persistTag.hasKey(modID)) {
            modTag = persistTag.getCompoundTag(modID);
        } else {
            modTag = new NBTTagCompound();
            persistTag.setTag(modID, modTag);
        }

        return modTag;
    }

    /**
     * This method will write the given name and lore to the itemstack's "display"-nbt tag. (Thanks to oku)
     *
     * @param stack
     * @param name
     * @param loreStrings
     * @return
     */
    public static ItemStack generateItem(ItemStack stack, String name, boolean forceCleanName, String... loreStrings) {
        NBTTagCompound nbt = stack.getTagCompound();
        NBTTagCompound display;
        if (nbt == null) {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        if (!stack.getTagCompound().hasKey("display")) {
            stack.setTagInfo("display", new NBTTagCompound());
        }

        display = stack.getTagCompound().getCompoundTag("display");

        if (loreStrings != null && loreStrings.length > 0) {
            NBTTagList lore = new NBTTagList();
            for (String s : loreStrings)
                lore.appendTag(new NBTTagString(EnumChatFormatting.GRAY + s));
            display.setTag("Lore", lore);
        }

        if (name != null) {
            StringBuilder sb = new StringBuilder();
            if (forceCleanName)
                sb.append(EnumChatFormatting.RESET);
            sb.append(name);

            display.setString("Name", sb.toString());
        }

        return stack;
    }

    /**
     * Spawns an itemStack in the world.
     */
    public static void spawnItemStack(ItemStack itemStack, World world, float x, float y, float z, int delayBeforePickup) {
        EntityItem entityItem = new EntityItem(world, x, y, z, itemStack);
        entityItem.delayBeforeCanPickup = delayBeforePickup;

        world.spawnEntityInWorld(entityItem);
    }
}
