package citadelles.districts;

import citadelles.bots.Referee;

/**
 * For the district income, the school of magic is considered as a
 * district of the color of your choice, so it pays you if
 * you are, King, Bishop, Merchant or Condottiere
 */
public class SchoolOfMagic extends District {
    public SchoolOfMagic() {
        super("School of Magic", 6, 5);
    }

    @Override
    public void action() {
        Referee referee = new Referee();
        referee.get1GoldFromMagicSchool(getCurrentBot());
    }

}
