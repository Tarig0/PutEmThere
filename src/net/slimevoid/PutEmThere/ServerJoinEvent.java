package net.slimevoid.PutEmThere;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;

public class ServerJoinEvent {
	@ForgeSubscribe
	public void joinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayerMP) {
			EntityPlayer player = (EntityPlayerMP) event.entity;
			File worldDirectory = null;
			if (FMLCommonHandler.instance().getMinecraftServerInstance() instanceof DedicatedServer) {
				worldDirectory = FMLCommonHandler
						.instance()
						.getMinecraftServerInstance()
						.getFile(
								FMLCommonHandler.instance()
										.getMinecraftServerInstance()
										.getFolderName());
			} else {
				worldDirectory = new File(FMLCommonHandler.instance()
						.getMinecraftServerInstance().getFile("saves"),
						FMLCommonHandler.instance()
								.getMinecraftServerInstance().getFolderName());
			}

			File playersDirectory = new File(worldDirectory, "players");
			File playerFile = new File(playersDirectory,
					player.getCommandSenderName() + ".dat");
			if (!playerFile.exists()) {
				System.out.println("!!!!New Player!!!! PutEmThere");
				player.setPosition(PutEmThere.initialSpawn.posX,
						PutEmThere.initialSpawn.posY,
						PutEmThere.initialSpawn.posZ);
				player.setSpawnChunk(PutEmThere.initialSpawn, true);
			}
		}

	}
}
