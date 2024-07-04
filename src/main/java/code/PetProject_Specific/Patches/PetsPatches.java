package code.PetProject_Specific.Patches;

import code.PetProject_Specific.Pets.AbstractPet;
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



    @SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
    public static class PetOnEndOfTurnPatch {
        @SpireInsertPatch(rloc = 2)
        public static void IncreaseMaxOrbSlots(GameActionManager __instance) {
            __instance.addToBottom((AbstractGameAction)new PetUtility.TriggerEndOfTurnPetsAction());
        }
    }
}
