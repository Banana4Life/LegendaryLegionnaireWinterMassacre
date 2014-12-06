package life.banana4.ld31.input;

import java.util.LinkedList;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * This class is a custom input multiplexer that replaces the multiplexer.
 * This multiplexer allows to add input processors in front of the existing processors or after them.
 * In addition to that a fallback processor can be specified which is called if no other processor
 * handled the event.
 *
 * @author Phillip Schichtel
 */
public class InputMultiplexer implements AllThemInputProcessor
{
    private final LinkedList<AllThemInputProcessor> processors = new LinkedList<>();
    private final AllThemInputProcessor fallback;

    public InputMultiplexer()
    {
        this(null);
    }

    public InputMultiplexer(AllThemInputProcessor fallback)
    {
        this.fallback = fallback;
    }

    /**
     * Appends an input processor to the list of processors.
     *
     * @param processor the processor to append
     *
     * @return fluent interface
     */
    public AllThemInputProcessor append(AllThemInputProcessor processor)
    {
        this.processors.addLast(processor);
        return this;
    }

    /**
     * Prepends an input processor to the list of processors.
     *
     * @param processor the processor to prepend
     *
     * @return fluent interface
     */
    public AllThemInputProcessor prepend(AllThemInputProcessor processor)
    {
        this.processors.addLast(processor);
        return this;
    }

    /**
     * Removes an input processor from the list of processors.
     *
     * @param processor the processor to remove
     *
     * @return fluent interface
     */
    public AllThemInputProcessor remove(AllThemInputProcessor processor)
    {
        this.processors.remove(processor);
        return this;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.keyDown(keycode))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.keyUp(keycode))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.keyTyped(character))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.touchDown(screenX, screenY, pointer, button))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.touchUp(screenX, screenY, pointer, button))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.touchDragged(screenX, screenY, pointer))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.mouseMoved(screenX, screenY))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.scrolled(amount))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.scrolled(amount);
    }

    @Override
    public void connected(Controller controller)
    {
        for (AllThemInputProcessor processor : processors)
        {
            processor.connected(controller);
        }
    }

    @Override
    public void disconnected(Controller controller)
    {
        for (AllThemInputProcessor processor : processors)
        {
            processor.disconnected(controller);
        }
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.buttonDown(controller, buttonCode))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.buttonDown(controller, buttonCode);
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.buttonUp(controller, buttonCode))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.buttonUp(controller, buttonCode);
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.axisMoved(controller, axisCode, value))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.axisMoved(controller, axisCode, value);
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.povMoved(controller, povCode, value))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.povMoved(controller, povCode, value);
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.xSliderMoved(controller, sliderCode, value))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.xSliderMoved(controller, sliderCode, value);
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.ySliderMoved(controller, sliderCode, value))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.ySliderMoved(controller, sliderCode, value);
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        for (AllThemInputProcessor processor : processors)
        {
            if (processor.accelerometerMoved(controller, accelerometerCode, value))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.accelerometerMoved(controller, accelerometerCode, value);
    }
}
