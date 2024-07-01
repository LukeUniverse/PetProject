package code.PetProject_Specific.Pets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EmptyPetSlot extends AbstractPet {
    public static final String PET_ID = "Empty";

    public EmptyPetSlot(float x, float y) {
        this.ID = "Empty";
        this.name = "EMPTY";
        this.cX = x;
        this.cY = y;
        this.tX = x;
        this.tY = y;
        updateDescription();
        this.channelAnimTimer = 0.5F;
    }

    public EmptyPetSlot() {
        this.name = "EMPTY";
        this.cX = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_x;
        this.cY = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_y + AbstractDungeon.player.hb_h / 2.0F;
        updateDescription();
    }

    public void updateAnimation() {
        super.updateAnimation();
    }

    public void render(SpriteBatch sb) {}

    public AbstractPet makeCopy() {
        return new EmptyPetSlot();
    }
}
