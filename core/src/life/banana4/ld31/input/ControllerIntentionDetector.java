package life.banana4.ld31.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.Array;
import life.banana4.ld31.input.Intention.Type;

import static java.lang.Math.abs;

public class ControllerIntentionDetector implements IntentionDetector
{
    private final Map<Integer, Intention> buttons = new HashMap<>();

    {
        buttons.put(XBox360Pad.BUTTON_LB, new Intention(Type.PRIMARY_ATTACK));
        buttons.put(XBox360Pad.BUTTON_RB, new Intention(Type.SECONDARY_ATTACK));
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

        if (leftX > 0)
        {
            intentions.add(new Intention(Type.MOVE_RIGHT, abs(leftX)));
        }
        if (leftX < 0)
        {
            intentions.add(new Intention(Type.MOVE_LEFT, abs(leftX)));
        }

        if (leftY > 0)
        {
            intentions.add(new Intention(Type.MOVE_DOWN, abs(leftY)));
        }
        if (leftY < 0)
        {
            intentions.add(new Intention(Type.MOVE_UP, abs(leftY)));
        }

        return intentions;
    }

    public static class XBox360Pad
    {
        public static final int BUTTON_X = 2;
        public static final int BUTTON_Y = 3;
        public static final int BUTTON_A = 0;
        public static final int BUTTON_B = 1;
        public static final int BUTTON_BACK = 6;
        public static final int BUTTON_START = 7;
        public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
        public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
        public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
        public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
        public static final int BUTTON_LB = 4;
        public static final int BUTTON_L3 = 8;
        public static final int BUTTON_RB = 5;
        public static final int BUTTON_R3 = 9;
        public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
        public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
        public static final int AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
        public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
        public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
        public static final int AXIS_RIGHT_TRIGGER = 4; //value 0 to -1f
    }
}
