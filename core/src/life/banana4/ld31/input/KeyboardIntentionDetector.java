package life.banana4.ld31.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import life.banana4.ld31.input.Intention.Type;

public class KeyboardIntentionDetector implements IntentionDetector
{
    private final Map<Integer, Intention> keys = new HashMap<>();
    private final Map<Integer, Intention> buttons = new HashMap<>();

    {
        keys.put(Keys.W, new Intention(Type.MOVE_UP));
        keys.put(Keys.S, new Intention(Type.MOVE_DOWN));
        keys.put(Keys.A, new Intention(Type.MOVE_LEFT));
        keys.put(Keys.D, new Intention(Type.MOVE_RIGHT));

        buttons.put(Buttons.LEFT,   new Intention(Type.PRIMARY_ATTACK));
        buttons.put(Buttons.RIGHT,  new Intention(Type.SECONDARY_ATTACK));
        buttons.put(Buttons.MIDDLE, new Intention(Type.TERTIARY_ATTACK));
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
