package code.PetProject_Specific.Pets;

import code.PetProject;
import code.PetProject_Specific.Patches.PetsPatches;
import code.util.PetSettings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.List;

public class AbstractPet {

    public String NAME = "AbstractPet";
    public final String PET_ID = PetProject.makeID(this.NAME);
    public float scale;
    public float animX;
    public float animY;
    private Texture img;
    private AbstractCreature.CreatureAnimation animation;
    private TextureAtlas atlas;
    private AnimationStateData stateData;
    public Skeleton skeleton;
    public AnimationState state;

    private float delayTime = 0.0F;

    public boolean BehindOfPlayer = true;

    public float cX = 0.0F;
    public float cY = 0.0F;
    public float tX = 0.0F;
    public float tY = 0.0F;
    public float channelAnimTimer = 0.0F;

    public AbstractPet(String atlasLocation, String jsonLocation, String startingAnimationState, float petScale) {
        this.scale = 1.0F;
        this.atlas = new TextureAtlas(Gdx.files.internal(atlasLocation));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(petScale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(jsonLocation));
        this.skeleton = new Skeleton(skeletonData);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, startingAnimationState, true);
        e.setTime(e.getEndTime() * MathUtils.random());
        updateDescription();
    }

    public PetSettings GetSettings(){return new PetSettings();}

    public AbstractPet() {}

    public void updateDescription() {}


    public void UpdateAnimation() {
        this.cX = MathHelper.orbLerpSnap(this.cX, AbstractDungeon.player.animX + this.tX);
        this.cY = MathHelper.orbLerpSnap(this.cY, AbstractDungeon.player.animY + this.tY);
    }

    public void render(SpriteBatch sb) {
        if (this.delayTime > 0.0F) {
            this.delayTime -= Gdx.graphics.getDeltaTime();
        } else if (this.atlas == null) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 2.0F));
            sb.draw(this.img, this.cX - this.img.getWidth() + this.animX * Settings.scale / 2.0F, this.cY + this.animY, this.img.getWidth() * Settings.scale, this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), false, false);
        } else {
            this.state.update(Gdx.graphics.getDeltaTime());
            this.state.apply(this.skeleton);
            this.skeleton.updateWorldTransform();
            this.cX += this.animX;
            this.cY += this.animY;
            this.skeleton.setPosition(this.cX, this.cY);
            this.skeleton.setFlip(true, false);
            sb.end();
            CardCrawlGame.psb.begin();
            AbstractMonster.sr.draw(CardCrawlGame.psb, this.skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
            sb.setBlendFunction(360, 360);
        }
    }

    public void playChannelSFX() {}



    public void onEndOfTurn() {}

    public AbstractPet makeCopy() {
        return new AbstractPet("", "", "", 1.0F);
    }

    public void updateAnimation() {
        this.cX = MathHelper.orbLerpSnap(this.cX, this.tX);
        this.cY = MathHelper.orbLerpSnap(this.cY, this.tY);
        if (this.channelAnimTimer != 0.0F) {
            this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
            if (this.channelAnimTimer < 0.0F)
                this.channelAnimTimer = 0.0F;
        }
        //this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
        this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
    }
}
