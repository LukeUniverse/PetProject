package code.PetProject_Specific.Pets;

import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Tentacle extends AbstractPet {
    static String atlasLocation = "PetProjectResources/images/Pets/Tentacle/skeleton.atlas";

    static String jsonLocation = "PetProjectResources/images/Pets/Tentacle/skeleton.json";

    static String startingAnimationState = "Idle";

    public void updateAnimation() {
        super.updateAnimation();
    }

    public Tentacle() {
        super(atlasLocation, jsonLocation, startingAnimationState, 0.8F);
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

