package net.slimevoid.PutEmThere;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "PutEmThere", name = "PutEmThere", version = "1.0.0")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class PutEmThere {
	@Instance("PutEmThere")
	public static PutEmThere instance;
	private static File configurationFile;
	private static Configuration configuration;
	public static ChunkCoordinates initialSpawn;

	@EventHandler
	public void LittleBlocksInit(FMLPreInitializationEvent event) {
		CommonConfig(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void LittleBlocksInit(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ServerJoinEvent());
	}

	public static void CommonConfig(File configFile) {
		if (configurationFile == null) {
			configurationFile = configFile;
			configuration = new Configuration(configFile);
		}

		configuration.load();

		int x, y, z = 0;
		x = configuration.get("InitialSpawnPoint", "x", 0).getInt();
		y = configuration.get("InitialSpawnPoint", "y", 65).getInt();
		z = configuration.get("InitialSpawnPoint", "z", 0).getInt();
		initialSpawn = new ChunkCoordinates(x, y, z);
		configuration.save();
	}

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
									.getMinecraftServerInstance()
									.getFolderName());
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
}
