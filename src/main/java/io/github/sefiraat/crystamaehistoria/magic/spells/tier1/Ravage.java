package io.github.sefiraat.crystamaehistoria.magic.spells.tier1;

import io.github.sefiraat.crystamaehistoria.magic.CastInformation;
import io.github.sefiraat.crystamaehistoria.magic.spells.core.Spell;
import io.github.sefiraat.crystamaehistoria.magic.spells.core.SpellCoreBuilder;
import io.github.sefiraat.crystamaehistoria.magic.spells.spellobjects.MagicSummon;
import io.github.sefiraat.crystamaehistoria.slimefun.items.mechanisms.liquefactionbasin.RecipeSpell;
import io.github.sefiraat.crystamaehistoria.stories.definition.StoryType;
import io.github.sefiraat.crystamaehistoria.utils.ParticleUtils;
import io.github.sefiraat.crystamaehistoria.utils.SpellUtils;
import io.github.sefiraat.crystamaehistoria.utils.mobgoals.RidableGroundGoal;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedPotionEffectType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Ravage extends Spell {

    public Ravage() {
        SpellCoreBuilder spellCoreBuilder = new SpellCoreBuilder(5, true, 0, false, 25, true)
            .makeInstantSpell(this::cast)
            .makeEffectingSpell(true, false)
            .addPositiveEffect(VersionedPotionEffectType.RESISTANCE, 1, 300)
            .addPositiveEffect(VersionedPotionEffectType.STRENGTH, 1, 300)
            .addPositiveEffect(PotionEffectType.ABSORPTION, 1, 300);
        setSpellCore(spellCoreBuilder.build());
    }

    @ParametersAreNonnullByDefault
    public void cast(CastInformation castInformation) {
        final UUID caster = castInformation.getCaster();
        final Location location = castInformation.getCastLocation();
        final Location spawnLocation = location.clone().add(
            ThreadLocalRandom.current().nextDouble(-3, 3),
            0,
            ThreadLocalRandom.current().nextDouble(-3, 3)
        );
        final MagicSummon magicSummon = SpellUtils.summonTemporaryMob(
            EntityType.RAVAGER,
            caster,
            spawnLocation,
            new RidableGroundGoal(caster),
            300,
            this::onTick
        );
        applyPositiveEffects(magicSummon.getMob(), castInformation);
    }

    @ParametersAreNonnullByDefault
    public void onTick(MagicSummon magicSummon) {
        // TODO: do I PR to slimefun to include this in the VersionedParticle class or do I make a class
        // in this repo so that it still supports previous versions
        ParticleUtils.displayParticleEffect(magicSummon.getMob(), Particle.ANGRY_VILLAGER, 1, 2);
    }

    @Nonnull
    @Override
    public RecipeSpell getRecipe() {
        return new RecipeSpell(
            1,
            StoryType.MECHANICAL,
            StoryType.ALCHEMICAL,
            StoryType.ANIMAL
        );
    }

    @Nonnull
    @Override
    public String[] getLore() {
        return new String[]{
            "Summons a tame ravager to your side.",
            "This spells effects and multipliers",
            "are applied to the ravager, not the",
            "caster."
        };
    }

    @Nonnull
    @Override
    public String getId() {
        return "RAVAGE";
    }

    @Nonnull
    @Override
    public Material getMaterial() {
        return Material.RAVAGER_SPAWN_EGG;
    }

}
