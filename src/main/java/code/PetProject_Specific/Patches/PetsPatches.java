package code.PetProject_Specific.Patches;

import code.PetProject_Specific.Pets.AbstractPet;
import code.PetProject_Specific.Pets.EmptyPetSlot;
import code.PetProject_Specific.Util.PetUtility;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PetsPatches {
    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
    public static class PlayerPets {
        public static SpireField<List<AbstractPet>> Pets = new SpireField(() -> new ArrayList());
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "render")
    public static class DisplayPetPatches {
        @SpireInsertPatch(rloc = 5, localvars = {"sb"})
        public static void InFrontOfPets(AbstractPlayer __instance, SpriteBatch sb) {
            List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(__instance);
            if (!pets.isEmpty()) {
                Iterator<AbstractPet> petsIterator = pets.iterator();
                while (petsIterator.hasNext()) {
                    AbstractPet p = petsIterator.next();
                    if (p.BehindOfPlayer)
                        p.render(sb);
                }
            }
        }

        @SpireInsertPatch(rloc = 32, localvars = {"sb"})
        public static void BehindOfPets(AbstractPlayer __instance, SpriteBatch sb) {
            List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(__instance);
            if (!pets.isEmpty()) {
                Iterator<AbstractPet> petsIterator = pets.iterator();
                while (petsIterator.hasNext()) {
                    AbstractPet p = petsIterator.next();
                    if (!p.BehindOfPlayer)
                        p.render(sb);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "update")
    public static class UpdatePetAnimationPatch {
        @SpireInsertPatch(rloc = 8)
        public static void UpdatePetAnimation() {
            List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
            if ((AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.EVENT)
                for (AbstractPet p : pets)
                    p.updateAnimation();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "increaseMaxOrbSlots")
    public static class GivePlayerDefaultEmptyPets {
        @SpireInsertPatch(rloc = 0, localvars = {"amount", "playSfx"})
        public static void IncreaseMaxOrbSlots(AbstractPlayer __instance, int amount, boolean playSfx) {
            List<AbstractPet> pets = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                float xToUse, yToUse;
                switch (i) {
                    case 0:
                        xToUse = 980.0F;
                        yToUse = 540.0F;
                        break;
                    case 1:
                        xToUse = 580.0F;
                        yToUse = 600.0F;
                        break;
                    case 2:
                        xToUse = 240.0F;
                        yToUse = 380.0F;
                        break;
                    default:
                        xToUse = 0.0F;
                        yToUse = 0.0F;
                        break;
                }
                pets.add(new EmptyPetSlot(xToUse, yToUse));
            }
            PetsPatches.PlayerPets.Pets.set(AbstractDungeon.player, pets);
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class PetOnEndOfTurnPatch {
        @SpireInsertPatch(rloc = 2)
        public static void IncreaseMaxOrbSlots(GameActionManager __instance) {
            __instance.addToBottom((AbstractGameAction)new PetUtility.TriggerEndOfTurnPetsAction());
        }
    }
}
