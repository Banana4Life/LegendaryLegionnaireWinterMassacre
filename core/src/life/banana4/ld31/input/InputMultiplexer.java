package life.banana4.ld31.input;

import java.util.LinkedList;
import com.badlogic.gdx.InputProcessor;

/**
 * This class is a custom input multiplexer that replaces the multiplexer.
 * This multiplexer allows to add input processors in front of the existing processors or after them.
 * In addition to that a fallback processor can be specified which is called if no other processor
 * handled the event.
 *
 * @author Phillip Schichtel
 */
public class InputMultiplexer implements InputProcessor
{
    private final LinkedList<InputProcessor> processors = new LinkedList<>();
    private final InputProcessor fallback;

    public InputMultiplexer()
    {
        this(null);
    }

    public InputMultiplexer(InputProcessor fallback)
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
    public InputMultiplexer append(InputProcessor processor)
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
    public InputMultiplexer prepend(InputProcessor processor)
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
    public InputMultiplexer remove(InputProcessor processor)
    {
        this.processors.remove(processor);
        return this;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        for (InputProcessor processor : processors)
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
        for (InputProcessor processor : processors)
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
        for (InputProcessor processor : processors)
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
        for (InputProcessor processor : processors)
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
        for (InputProcessor processor : processors)
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
        for (InputProcessor processor : processors)
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
        for (InputProcessor processor : processors)
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
        for (InputProcessor processor : processors)
        {
            if (processor.scrolled(amount))
            {
                return true;
            }
        }
        return this.fallback != null && this.fallback.scrolled(amount);
    }
}
