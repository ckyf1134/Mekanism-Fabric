package mekanism.fabric_shim.common.crafting;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.stream.Stream;
import mekanism.fabric_shim.common.util.NeoForgeExtraCodecs;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Matches items that satisfy all child ingredients (stand-in for NeoForge's
 * IntersectionIngredient; same surface and JSON format:
 * {@code {"type": "neoforge:intersection", "children": [...]}}).
 */
public final class IntersectionIngredient implements ICustomIngredient {

    public static final MapCodec<IntersectionIngredient> CODEC =
          NeoForgeExtraCodecs.aliasedFieldOf(CustomIngredients.LIST_CODEC_NONEMPTY, "children", "ingredients")
                .xmap(IntersectionIngredient::new, IntersectionIngredient::children);

    public static final IngredientType<IntersectionIngredient> TYPE = new IngredientType<>(CODEC,
          Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).map(IntersectionIngredient::new, IntersectionIngredient::children));

    private final List<Ingredient> children;

    public IntersectionIngredient(List<Ingredient> children) {
        if (children.isEmpty()) {
            throw new IllegalArgumentException("Intersection ingredient must have at least one child");
        }
        this.children = List.copyOf(children);
    }

    public static Ingredient of(Ingredient... children) {
        return of(List.of(children));
    }

    public static Ingredient of(List<Ingredient> children) {
        if (children.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an intersection ingredient with no children");
        }
        if (children.size() == 1) {
            return children.getFirst();
        }
        return new IntersectionIngredient(children).toVanilla();
    }

    public List<Ingredient> children() {
        return children;
    }

    @Override
    public boolean test(ItemStack stack) {
        for (Ingredient child : children) {
            if (!child.test(stack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Stream<ItemStack> getItems() {
        Stream<ItemStack> items = Stream.of(children.getFirst().getItems());
        for (int i = 1; i < children.size(); i++) {
            Ingredient child = children.get(i);
            items = items.filter(child::test);
        }
        return items;
    }

    @Override
    public boolean isSimple() {
        for (Ingredient child : children) {
            if (!CustomIngredients.isSimple(child)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IngredientType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof IntersectionIngredient other && children.equals(other.children);
    }

    @Override
    public int hashCode() {
        return children.hashCode();
    }
}
