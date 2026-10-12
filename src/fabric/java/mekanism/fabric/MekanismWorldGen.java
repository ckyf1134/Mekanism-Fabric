package mekanism.fabric;

import mekanism.common.Mekanism;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Fabric worldgen bridge: mirrors the upstream NeoForge biome modifiers
 * (data/mekanism/neoforge/biome_modifier/*.json) via fabric-biome-api.
 */
public final class MekanismWorldGen {

    private MekanismWorldGen() {
    }

    public static void init() {
        TagKey<Biome> spawnOres = TagKey.create(Registries.BIOME, Mekanism.rl("spawn_ores"));
        addFeatures(spawnOres, "ore_osmium_upper", "ore_osmium_middle", "ore_osmium_small");
        addFeatures(spawnOres, "ore_tin_small", "ore_tin_large");
        addFeatures(spawnOres, "ore_lead_normal");
        addFeatures(spawnOres, "ore_uranium_small", "ore_uranium_buried");
        addFeatures(spawnOres, "ore_fluorite_normal", "ore_fluorite_buried");
        addFeatures(spawnOres, "salt");
    }

    private static void addFeatures(TagKey<Biome> biomes, String... features) {
        for (String feature : features) {
            BiomeModifications.addFeature(BiomeSelectors.tag(biomes), GenerationStep.Decoration.UNDERGROUND_ORES,
                  ResourceKey.create(Registries.PLACED_FEATURE, Mekanism.rl(feature)));
        }
    }
}
