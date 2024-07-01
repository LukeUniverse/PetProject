package code.cards;

import code.PetProject;
import code.PetProject_Specific.Pets.AbstractPet;
import code.PetProject_Specific.Pets.Tentacle;
import code.PetProject_Specific.Actions.ChannelPetAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpawnTentacle extends AbstractEasyCard {
    public static final String ID = PetProject.makeID("SpawnTentacle");

    public SpawnTentacle() {
        super(ID, 1, AbstractCard.CardType.SKILL, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);
        this.color = AbstractCard.CardColor.BLUE;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChannelPetAction(new Tentacle()));
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}
