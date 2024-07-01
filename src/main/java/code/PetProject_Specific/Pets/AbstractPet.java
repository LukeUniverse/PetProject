package code.PetProject_Specific.Pets;

import basemod.animations.AbstractAnimation;
import code.PetProject;
import code.PetProject_Specific.Patches.PetsPatches;
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
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.List;

public class AbstractPet {
    public String name;

    public float scale;

    public String ID;

    public float animX;

    public float animY;

    private Texture img;

    private AbstractCreature.CreatureAnimation animation;

    private float animationTimer;

    private float animationTimerStart;

    private TextureAtlas atlas;

    private AnimationStateData stateData;

    private AbstractAnimation animationA;

    public AbstractPlayer p;

    public Skeleton skeleton;

    public AnimationState state;

    public SkeletonMeshRenderer sr;

    public String NAME = "AbstractPet";

    public final String ORB_ID = PetProject.makeID(this.name);

    private float delayTime = 0.0F;

    public boolean BehindOfPlayer = true;

    public float cX = 0.0F;

    public float cY = 0.0F;

    public float tX = 0.0F;

    public float tY = 0.0F;

    public float channelAnimTimer = 0.0F;

    public Color c = Color.TEAL;

    public AbstractPet(String atlasLocation, String jsonLocation, String startingAnimationState, float petScale) {
        int index = -1;
        List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
        for (int countingVar = 0; countingVar < pets.size(); countingVar++) {
            if (pets.get(countingVar) instanceof EmptyPetSlot) {
                index = countingVar;
                break;
            }
        }
        this.scale = 1.0F;
        this.atlas = new TextureAtlas(Gdx.files.internal(atlasLocation));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(petScale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(jsonLocation));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.TEAL);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
        AnimationState.TrackEntry e = this.state.setAnimation(0, startingAnimationState, true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.ID = this.ORB_ID;
        this.name = this.NAME;
        updateDescription();
    }

    public AbstractPet() {}

    public void updateDescription() {}

    public void onEvoke() {}

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

    public void setSlot(int slotNum) {
        List<AbstractPet> pets = (List<AbstractPet>)PetsPatches.PlayerPets.Pets.get(AbstractDungeon.player);
        if (AbstractDungeon.player instanceof com.megacrit.cardcrawl.characters.Defect &&
                slotNum < 4) {
            if (!(AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.MonsterRoom) || !AbstractDungeon.lastCombatMetricKey.equals("Shield and Spear"))
                this.tX =  pets.get(slotNum).tX * Settings.scale;
            this.tY = pets.get(slotNum).tY * Settings.scale;
        }
        pets.set(slotNum, this);
    }

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
        this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
        this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
    }
}
