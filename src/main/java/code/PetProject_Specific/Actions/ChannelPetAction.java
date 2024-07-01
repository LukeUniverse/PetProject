package code.PetProject_Specific.Actions;

import code.PetProject_Specific.Patches.PetsPatches;
import code.PetProject_Specific.Pets.AbstractPet;
import code.PetProject_Specific.Util.PetUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.Iterator;
import java.util.List;

public class ChannelPetAction extends AbstractGameAction {
    private AbstractPet petType;

    private boolean autoEvoke;

    public ChannelPetAction(AbstractPet newPetType) {
        this(newPetType, true);
    }

    public ChannelPetAction(AbstractPet newPetType, boolean autoEvoke) {
        this.autoEvoke = false;
        this.duration = Settings.ACTION_DUR_FAST;
        this.petType = newPetType;
        this.autoEvoke = autoEvoke;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.autoEvoke) {
                PetUtility.ChannelPet(this.petType);
            } else {
                List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
                Iterator<AbstractPet> petsIterator = pets.iterator();
                while (petsIterator.hasNext()) {
                    AbstractPet o = petsIterator.next();
                    if (o instanceof code.PetProject_Specific.Pets.EmptyPetSlot) {
                        PetUtility.ChannelPet(this.petType);
                        break;
                    }
                }
            }
            if (Settings.FAST_MODE) {
                this.isDone = true;
                return;
            }
        }
        tickDuration();
    }
}
