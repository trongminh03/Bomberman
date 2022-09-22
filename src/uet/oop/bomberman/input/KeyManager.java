package uet.oop.bomberman.input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

public class KeyManager {
    private HashMap<KeyCode, Boolean> keys;

    public KeyManager() {
        keys = new HashMap<KeyCode, Boolean>();
    }

    public void pressed(KeyEvent e) {
        keys.put(e.getCode(), true);
    }

    public void released(KeyEvent e) {
        keys.put(e.getCode(), false);
    }

    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }


}
