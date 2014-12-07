package life.banana4.ld31.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.input.Intention.Type;

public class KeyboardIntentionDetector implements IntentionDetector
{
    private boolean movingIntended = false;
    private final Map<Integer, Intention> keys = new HashMap<>();
    private final Map<Integer, Intention> buttons = new HashMap<>();

    {
        keys.put(Keys.ESCAPE, new Intention(Type.EXIT_GAME));

        buttons.put(Buttons.LEFT, new Intention(Type.PRIMARY_ATTACK));
        buttons.put(Buttons.RIGHT, new Intention(Type.SECONDARY_ATTACK));
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

        Vector2 dir = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Keys.W))
        {
            dir.add(0, -1);
        }
        if (Gdx.input.isKeyPressed(Keys.S))
        {
            dir.add(0, 1);
        }
        if (Gdx.input.isKeyPressed(Keys.A))
        {
            dir.add(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.D))
        {
            dir.add(1, 0);
        }

        if (!dir.equals(Vector2.Zero))
        {
            intentions.add(new Intention(Type.MOVE, dir));
            this.movingIntended = true;
        }
        else if (this.movingIntended)
        {
            intentions.add(new Intention(Type.HALT));
            this.movingIntended = false;
        }

        return intentions;
    }
}
