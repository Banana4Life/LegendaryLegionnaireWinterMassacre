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
    private final Map<Integer, Intention> keys = new HashMap<>();
    private final Map<Integer, Intention> buttons = new HashMap<>();

    {
        //keys.put(Keys.W, new Intention(Type.MOVE));

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

        Vector2 moveDirection = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Keys.W))
        {
            moveDirection.add(0, -1);
        }
        if (Gdx.input.isKeyPressed(Keys.S))
        {
            moveDirection.add(0, 1);
        }
        if (Gdx.input.isKeyPressed(Keys.A))
        {
            moveDirection.add(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.D))
        {
            moveDirection.add(1, 0);
        }

        if (!moveDirection.equals(Vector2.Zero))
        {
            intentions.add(new Intention(Type.MOVE, moveDirection));
        }

        return intentions;
    }
}
