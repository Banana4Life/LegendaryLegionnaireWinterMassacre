package life.banana4.ld31.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

public class KeyboardIntentionDetector implements IntentionDetector
{
    private final Map<Integer, Intention> keys = new HashMap<>();
    private final Map<Integer, Intention> buttons = new HashMap<>();

    {
        keys.put(Keys.W, Intention.MOVE_UP);
        keys.put(Keys.S, Intention.MOVE_DOWN);
        keys.put(Keys.A, Intention.MOVE_LEFT);
        keys.put(Keys.D, Intention.MOVE_RIGHT);

        buttons.put(Buttons.LEFT, Intention.PRIMARY_ATTACK);
        buttons.put(Buttons.RIGHT, Intention.SECONDARY_ATTACK);
    }

    @Override
    public Set<Intention> detect()
    {
        Set<Intention> intentions = new HashSet<>();

        for (final Entry<Integer, Intention> entry : keys.entrySet())
        {
            if (Gdx.input.isKeyPressed(entry.getKey()))
            {
                intentions.add(entry.getValue());
            }
        }

        for (final Entry<Integer, Intention> entry : buttons.entrySet())
        {
            if (Gdx.input.isButtonPressed(entry.getKey()))
            {
                intentions.add(entry.getValue());
            }
        }

        return intentions;
    }
}
