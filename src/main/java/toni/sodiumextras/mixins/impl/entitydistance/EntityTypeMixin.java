package toni.sodiumextras.mixins.impl.entitydistance;

import toni.sodiumextras.EmbyConfig;
import toni.sodiumextras.EmbyTools;
import toni.sodiumextras.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static toni.sodiumextras.SodiumExtras.LOGGER;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements IWhitelistCheck {
    @Unique private static final Marker e$IT = MarkerManager.getMarker("EntityType");
    @Unique private boolean embPlus$checked = false;
    @Unique private boolean embPlus$whitelisted = false;

    @Override
    @Unique
    public boolean embPlus$isAllowed() {
        if (embPlus$checked) return embPlus$whitelisted;

        final var resource = embPlus$resourceLocation();
        this.embPlus$whitelisted = EmbyTools.isWhitelisted(resource, EmbyConfig.entityWhitelist);
        this.embPlus$checked = true;

        LOGGER.debug(e$IT,"Whitelist checked for {}", resource.toString());
        return embPlus$whitelisted;
    }

    @Unique
    public ResourceLocation embPlus$resourceLocation() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(embPlus$cast());
    }

    @Unique
    private EntityType<?> embPlus$cast() {
        return (EntityType<?>) ((Object) this);
    }
}
