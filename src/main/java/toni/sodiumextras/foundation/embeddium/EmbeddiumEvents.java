package toni.sodiumextras.foundation.embeddium;

#if FORGE
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import toni.sodiumextras.SodiumExtras;
import toni.sodiumextras.foundation.embeddium.pages.EntityCullingPage;
import toni.sodiumextras.foundation.embeddium.pages.OthersPage;
import toni.sodiumextras.foundation.embeddium.pages.TrueDarknessPage;

@Mod.EventBusSubscriber(modid = SodiumExtras.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EmbeddiumEvents {
    @SubscribeEvent
    public static void onEmbeddiumPagesRegister(OptionGUIConstructionEvent e) {
        var pages = e.getPages();

        pages.add(new TrueDarknessPage());
        pages.add(new EntityCullingPage());
        pages.add(new OthersPage());
    }
}
#endif