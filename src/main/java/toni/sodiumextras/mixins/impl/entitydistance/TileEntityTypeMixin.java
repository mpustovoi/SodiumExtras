package toni.sodiumextras.mixins.impl.entitydistance;

import toni.sodiumextras.EmbyConfig;
import toni.sodiumextras.EmbyTools;
import toni.sodiumextras.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;


import static toni.sodiumextras.SodiumExtras.LOGGER;

@Mixin(BlockEntityType.class)
public abstract class TileEntityTypeMixin implements IWhitelistCheck {
    @Unique private static final Marker e$IT = MarkerManager.getMarker("BlockEntityType");
    @Unique private boolean embPlus$checked = false;
    @Unique private boolean embPlus$whitelisted = false;

    @Override
    public boolean embPlus$isAllowed() {
        if (embPlus$checked) return embPlus$whitelisted;

        var resource = getKey(embPlus$cast());
        this.embPlus$whitelisted = EmbyTools.isWhitelisted(resource, EmbyConfig.tileEntityWhitelist);
        this.embPlus$checked = true;

        LOGGER.debug(e$IT,"Whitelist checked for {}", resource.toString());
        return embPlus$whitelisted;
    }

    @Shadow public static ResourceLocation getKey(BlockEntityType<?> pBlockEntityType) {
        throw new UnsupportedOperationException("stub!");
    }

    @Unique
    private BlockEntityType<?> embPlus$cast() {
        return (BlockEntityType<?>) ((Object) this);
    }
}
