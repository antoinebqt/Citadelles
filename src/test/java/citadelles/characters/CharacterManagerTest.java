package citadelles.characters;

import citadelles.bots.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CharacterManagerTest {

    private CharacterManager characterManager;
    private ArrayList<Character> listCharacter;
    @BeforeEach
    void setUp() {
        characterManager = new CharacterManager(6);
        listCharacter = new ArrayList<>();
    }

    @Test
    void initWith7Bots(){
        characterManager = new CharacterManager(7);
        characterManager.init();
        assertEquals(7, characterManager.getCharacters().size());
    }

    @Test
    void initWith6Bots(){
        characterManager = new CharacterManager(6);
        characterManager.init();
        assertEquals(7, characterManager.getCharacters().size());
    }

    @Test
    void initWith5Bots(){
        characterManager = new CharacterManager(5);
        characterManager.init();
        assertEquals(6, characterManager.getCharacters().size());
    }

    @Test
    void initWith4Bots(){
        characterManager = new CharacterManager(4);
        characterManager.init();
        assertEquals(5, characterManager.getCharacters().size());
    }

    @Test
    void removeCharacterChosen() {
        characterManager.init();
        Character charToRemove = characterManager.getCharacters().get(2);

        assertTrue(characterManager.getCharacters().contains(charToRemove));
        characterManager.removeCharacterChosen(charToRemove);
        assertFalse(characterManager.getCharacters().contains(charToRemove));
    }

    @Test
    void pickPlayerCharacterSetCharacterToTheBot() {
        characterManager.init();
        Bot bot = new Bot(0, null);
        Character charChosen = characterManager.getCharacters().get(0);

        characterManager.pickPlayerCharacter(bot);
        assertEquals(charChosen, bot.getCharacter());
        assertEquals(charChosen.getCurrentBot(), bot);
    }

    @Test
    void pickPlayerCharacterRemoveCharacterFromList() {
        characterManager.init();
        Bot bot = new Bot(0, null);
        Character charChosen = characterManager.getCharacters().get(0);

        assertTrue(characterManager.getCharacters().contains(charChosen));
        characterManager.pickPlayerCharacter(bot);
        assertFalse(characterManager.getCharacters().contains(charChosen));
    }
}