package net.slimevoid.PutEmThere;
import java.io.File;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = CoreLib.MOD_ID, name = CoreLib.MOD_NAME, version = CoreLib.MOD_VERSION)
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class PutEmThere {
	@Instance(CoreLib.MOD_ID)
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
}
