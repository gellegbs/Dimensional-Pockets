package net.gtn.dimensionalpocket.common.core.teleport;

import cpw.mods.fml.client.ExtendedServerListData;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    CoordSet targetSet;
    TeleportType teleportType;

    public PocketTeleporter(WorldServer worldServer, CoordSet targetSet, TeleportType teleportType) {
        super(worldServer);
        this.targetSet = targetSet.copy();
        this.teleportType = teleportType;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {
        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        World world = player.worldObj;

        double posX = targetSet.getX();
        double posY = targetSet.getY();
        double posZ = targetSet.getZ();

        CoordSet airSet = new CoordSet(posX, posY + 1, posZ);

        player.playerNetServerHandler.setPlayerLocation(airSet.getX() + 0.5F, airSet.getY(), airSet.getZ() + 0.5F, player.rotationYaw, player.rotationPitch);
    }

    private boolean isAirBlocks(World world, CoordSet tempSet) {
        if (tempSet.getY() < 0)
            return false;
        return (world.isAirBlock(tempSet.getX(), tempSet.getY(), tempSet.getZ()) && world.isAirBlock(tempSet.getX(), tempSet.getY() + 1, tempSet.getZ()));
    }

    private CoordSet getRelativeTries(int tryCount) {
        int index = 0;

        for (int i = -1; i <= 1; i++)
            for (int j = -2; j <= 3; j++)
                for (int k = -1; k <= 1; k++)
                    if (index++ == tryCount)
                        return new CoordSet(i, j, k);
        return null;
    }

    public static enum TeleportType {
        INWARD, OUTWARD, REBOUND;
    }

    public static PocketTeleporter createTeleporter(int dimID, CoordSet coordSet, TeleportType teleportType) {
        return new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), coordSet, teleportType);
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }
}
