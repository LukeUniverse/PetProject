package code.PetProject_Specific.Actions;

import code.PetProject_Specific.Patches.PetsPatches;
import code.PetProject_Specific.Pets.AbstractPet;
import code.PetProject_Specific.Util.PetUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.Iterator;
import java.util.List;

import static code.PetProject_Specific.Patches.PetsPatches.PlayerPets.Pets;

public class ChannelPetAction extends AbstractGameAction {
    private AbstractPet PetToCreate;


    public ChannelPetAction(AbstractPet PetToCreate) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.PetToCreate = PetToCreate;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {

                List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
                int amountOfPetsWeHave = pets.size();
                if(amountOfPetsWeHave >= 3)
                {
                    //For now let's just hard cap at 3 pets and do nothing if trying to summon another.
                    //Obviously we might want that to change though later down the line.
                }
                else{
                        pets.add(PetToCreate);
                        PetsPatches.PlayerPets.Pets.set(AbstractDungeon.player, pets);
                }

            if (Settings.FAST_MODE) {
                this.isDone = true;
                return;
            }
        }
        tickDuration();
    }
}
