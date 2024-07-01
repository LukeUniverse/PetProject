package code;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import code.cards.AbstractEasyCard;
import code.cards.cardvars.AbstractEasyDynamicVariable;
import code.relics.AbstractEasyRelic;
import code.util.ProAudio;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class PetProject implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber {

    public static final String ModID = "PetProject";

    public static String makeID(String idText) {
        return ModID + ":" + idText;
    }

    public static Color characterColor = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1); // This should be changed eventually

    public static final String SHOULDER1 = makeCharacterPath("mainChar/shoulder.png");
    public static final String SHOULDER2 = makeCharacterPath("mainChar/shoulder2.png");
    public static final String CORPSE = makeCharacterPath("mainChar/corpse.png");
    private static final String ATTACK_S_ART = makeImagePath("512/attack.png");
    private static final String SKILL_S_ART = makeImagePath("512/skill.png");
    private static final String POWER_S_ART = makeImagePath("512/power.png");
    private static final String CARD_ENERGY_S = makeImagePath("512/energy.png");
    private static final String TEXT_ENERGY = makeImagePath("512/text_energy.png");
    private static final String ATTACK_L_ART = makeImagePath("1024/attack.png");
    private static final String SKILL_L_ART = makeImagePath("1024/skill.png");
    private static final String POWER_L_ART = makeImagePath("1024/power.png");
    private static final String CARD_ENERGY_L = makeImagePath("1024/energy.png");
    private static final String CHARSELECT_BUTTON = makeImagePath("charSelect/charButton.png");
    private static final String CHARSELECT_PORTRAIT = makeImagePath("charSelect/charBG.png");

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
    };

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        return "eng";
    }

    public PetProject() {
        BaseMod.subscribe(this);
    }

    public static String makePath(String resourcePath) {
        return ModID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return ModID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return ModID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return ModID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCharacterPath(String resourcePath)
    {
        return ModID + "Resources/images/char/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return ModID + "Resources/images/cards/" + resourcePath;
    }

    public static void initialize() {
        PetProject thismod = new PetProject();
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(ModID)
                .packageFilter(AbstractEasyRelic.class)
                .any(AbstractEasyRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(ModID)
            .packageFilter(AbstractEasyDynamicVariable.class)
            .any(DynamicVariable.class, (info, var) ->
                BaseMod.addDynamicVariable(var));
        new AutoAdd(ModID)
                .packageFilter(AbstractEasyCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, ModID + "Resources/localization/" + getLangString() + "/Cardstrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, ModID + "Resources/localization/" + getLangString() + "/Relicstrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, ModID + "Resources/localization/" + getLangString() + "/Charstrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, ModID + "Resources/localization/" + getLangString() + "/Powerstrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, ModID + "Resources/localization/" + getLangString() + "/UIstrings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, ModID + "Resources/localization/" + getLangString() + "/Orbstrings.json");
        BaseMod.loadCustomStringsFile(StanceStrings.class, ModID + "Resources/localization/" + getLangString() + "/Stancestrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, ModID + "Resources/localization/" + getLangString() + "/Potionstrings.json");
    }

    @Override
    public void receiveAddAudio() {
        for (ProAudio a : ProAudio.values())
            BaseMod.addAudio(makeID(a.name()), makePath("audio/" + a.name().toLowerCase() + ".ogg"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(ModID + "Resources/localization/" + getLangString() + "/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(ModID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }
}
