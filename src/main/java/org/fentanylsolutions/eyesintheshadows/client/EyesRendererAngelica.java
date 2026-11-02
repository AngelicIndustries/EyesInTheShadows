package org.fentanylsolutions.eyesintheshadows.client;

import net.minecraft.client.model.ModelBase;

import com.gtnewhorizons.angelica.api.EyePassRenderer;

/**
 * The eyes renderer marked as one with an eye pass, so Angelica draws the glow with a shader pack's glowing eyes
 * program, as it does vanilla's spider eyes. Without it, packs such as Complementary light the glow like a mob's skin:
 * dull, tinted and with its transparent edge showing. Only made when Angelica has the API (ClientProxy).
 */
public class EyesRendererAngelica extends EyesRenderer implements EyePassRenderer {

    private EyesRendererAngelica(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    /** Typed as the plain renderer, so callers don't load this class (or Angelica's API) until they call it. */
    public static EyesRenderer create(ModelBase model, float shadowSize) {
        return new EyesRendererAngelica(model, shadowSize);
    }
}
