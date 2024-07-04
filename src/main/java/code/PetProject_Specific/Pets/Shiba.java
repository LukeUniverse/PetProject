package code.PetProject_Specific.Pets;

import code.PetProject_Specific.Patches.PetsPatches;
import code.util.PetSettings;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shiba extends AbstractPet {
    static String atlasLocation = "PetProjectResources/images/Pets/Shiba_WIP/ShibaProject.atlas";
    static String jsonLocation = "PetProjectResources/images/Pets/Shiba_WIP/ShibaProject.json";
    static String startingAnimationState = "Idle";


    List<Integer> shibaXLocations = new ArrayList<>(Arrays.asList(980, 580, 240));
    List<Integer> shibaYLocations = new ArrayList<>(Arrays.asList(540, 600, 380));
    List<Boolean> shibaInFrontOfPlayer = new ArrayList<Boolean>(Arrays.asList(false, false, true));
    PetSettings ShibaSettings = new PetSettings(shibaXLocations,shibaYLocations,shibaInFrontOfPlayer);


    public void updateAnimation() {
        super.updateAnimation();
    }

    public Shiba() {
        super(atlasLocation, jsonLocation, startingAnimationState, 0.4F);

        List<AbstractPet> pets = (List<AbstractPet>) PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
        int amountOfPetsWeHave = pets.size();
        this.cX = ShibaSettings.PetXLocations.get(amountOfPetsWeHave) * Settings.scale;
        this.cY = ShibaSettings.PetYLocations.get(amountOfPetsWeHave)* Settings.scale;
        this.tX = ShibaSettings.PetXLocations.get(amountOfPetsWeHave)* Settings.scale;
        this.tY = ShibaSettings.PetYLocations.get(amountOfPetsWeHave)* Settings.scale;
        this.BehindOfPlayer = !ShibaSettings.PetInFrontOfPlayer.get(amountOfPetsWeHave);//oops did this opposite, hah.
    }

    public void onEndOfTurn() {
        AbstractDungeon.player.heal(1);
    }

}

