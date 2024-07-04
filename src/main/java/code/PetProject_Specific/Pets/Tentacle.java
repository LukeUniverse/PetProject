package code.PetProject_Specific.Pets;

import code.PetProject_Specific.Patches.PetsPatches;
import code.PetProject_Specific.Util.PetUtility;
import code.util.PetSettings;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tentacle extends AbstractPet {
    static String atlasLocation = "PetProjectResources/images/Pets/Tentacle/skeleton.atlas";

    static String jsonLocation = "PetProjectResources/images/Pets/Tentacle/skeleton.json";

    List<Integer> tentacleXLocations = new ArrayList<>(Arrays.asList(980, 580, 240));
    List<Integer> tentacleYLocations = new ArrayList<>(Arrays.asList(540, 600, 380));
    List<Boolean> tentacleInFrontOfPlayer = new ArrayList<Boolean>(Arrays.asList(false, false, true));
    PetSettings TentacleSettings = new PetSettings(tentacleXLocations,tentacleYLocations,tentacleInFrontOfPlayer);

    static String startingAnimationState = "Idle";

    public void updateAnimation() {
        super.updateAnimation();
    }

    public Tentacle() {
        super(atlasLocation, jsonLocation, startingAnimationState, 0.8F);


        List<AbstractPet> pets = (List<AbstractPet>) PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
        int amountOfPetsWeHave = pets.size();
        this.cX = TentacleSettings.PetXLocations.get(amountOfPetsWeHave) * Settings.scale;
        this.cY = TentacleSettings.PetYLocations.get(amountOfPetsWeHave)* Settings.scale;
        this.tX = TentacleSettings.PetXLocations.get(amountOfPetsWeHave)* Settings.scale;
        this.tY = TentacleSettings.PetYLocations.get(amountOfPetsWeHave)* Settings.scale;
        this.BehindOfPlayer = !TentacleSettings.PetInFrontOfPlayer.get(amountOfPetsWeHave);//oops did this opposite, hah.
    }

    @Override
    public PetSettings GetSettings()
    {
        return TentacleSettings;
    }

    public void onEndOfTurn() {
        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        if (randomMonster != null) {
            int damage = 5;
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Attack", false);
            this.state.setTimeScale(1.3F);
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)randomMonster, new DamageInfo((AbstractCreature)AbstractDungeon.player, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }
}

