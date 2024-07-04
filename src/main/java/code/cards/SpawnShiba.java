package code.cards;

import code.PetProject;
import code.PetProject_Specific.Actions.ChannelPetAction;
import code.PetProject_Specific.Pets.Shiba;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpawnShiba extends AbstractEasyCard {
    public static final String ID = PetProject.makeID("SpawnShiba");

    public SpawnShiba() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        this.color = CardColor.BLUE;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChannelPetAction(new Shiba()));
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}
