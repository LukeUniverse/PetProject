package code.PetProject_Specific.Util;

import code.PetProject_Specific.Patches.PetsPatches;
import code.PetProject_Specific.Pets.*;
import code.PetProject_Specific.Actions.ChannelPetAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static code.PetProject_Specific.Patches.PetsPatches.PlayerPets.Pets;

public class PetUtility {

     public static class TriggerEndOfTurnPetsAction extends AbstractGameAction {
        public void update() {
            List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
            if (!pets.isEmpty()) {
                Iterator<AbstractPet> petsIterator = pets.iterator();
                while (petsIterator.hasNext()) {
                    AbstractPet pet = petsIterator.next();
                    pet.onEndOfTurn();
                }
            }
            this.isDone = true;
        }
    }
}

