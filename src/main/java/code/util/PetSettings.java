package code.util;

import java.util.List;

public class PetSettings {
        public PetSettings(List<Integer> petXLocations, List<Integer> petYLocations, List<Boolean> petInFrontOfPlayer)
        {
            PetXLocations = petXLocations;
            PetYLocations = petYLocations;
            PetInFrontOfPlayer = petInFrontOfPlayer;
        }

        public PetSettings()
        {
            //Only used in AbstractPet.
        }
        public List<Integer> PetXLocations;
        public List<Integer> PetYLocations;
        public List<Boolean> PetInFrontOfPlayer;

}
