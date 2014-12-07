package life.banana4.ld31.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import life.banana4.ld31.input.Intention.Type;

import static java.lang.Math.abs;

public class ControllerIntentionDetector implements IntentionDetector
{
    private final Map<Integer, Intention> buttons = new HashMap<>();

    {
        buttons.put(XBox360Pad.BUTTON_LB, new Intention(Type.PRIMARY_ATTACK));
        buttons.put(XBox360Pad.BUTTON_RB, new Intention(Type.SECONDARY_ATTACK));
        buttons.put(XBox360Pad.BUTTON_R3, new Intention(Type.TERTIARY_ATTACK));
    }

    @Override
    public Set<Intention> detect()
    {
        Array<Controller> controllers = Controllers.getControllers();
        if (controllers.size == 0)
        {
            return NO_INTENTIONS;
        }

        Controller mc = controllers.first();
        if (!mc.getName().toLowerCase().contains("xbox") || !mc.getName().contains("360"))
        {
            return NO_INTENTIONS;
        }

        Set<Intention> intentions = new HashSet<>();

        for (final Entry<Integer, Intention> entry : this.buttons.entrySet())
        {
            if (mc.getButton(entry.getKey()))
            {
                intentions.add(entry.getValue());
            }
        }

        float leftX = mc.getAxis(XBox360Pad.AXIS_LEFT_X);
        float leftY = mc.getAxis(XBox360Pad.AXIS_LEFT_Y);

        Vector2 moveDirection = new Vector2(leftX, leftY);
        if (!moveDirection.equals(Vector2.Zero))
        {
            intentions.add(new Intention(Type.MOVE, moveDirection));
        }

        return intentions;
    }
}
