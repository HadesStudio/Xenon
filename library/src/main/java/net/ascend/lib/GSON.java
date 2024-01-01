package net.ascend.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ascend.lib.adapter.BlockVectorAdapter;
import net.ascend.lib.adapter.LocationAdapter;
import net.ascend.lib.adapter.PotionEffectAdapter;
import net.ascend.lib.adapter.VectorAdapter;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * @author Maykol Morales Morante (zSirSpectro)
 * Wednesday, July 14, 2021
 */

public class GSON {

    public static final Gson FANCY = new GsonBuilder().registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).setPrettyPrinting().serializeNulls().create();
    public static final Gson PLAIN = new GsonBuilder().registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();

}
