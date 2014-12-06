package life.banana4.ld31.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import life.banana4.ld31.Ld31;
import life.banana4.ld31.entity.Player;

/**
 * This is the global input processor which acts as the fallback of the input multiplexer.
 * It can be used to handle global key bindings
 *
 * @author Phillip Schichtel
 */
public class GlobalInputProcessor implements AllThemInputProcessor
{
    private final Ld31 game;
    private final OrthographicCamera camera;

    public GlobalInputProcessor(Ld31 game, OrthographicCamera camera)
    {
        this.game = game;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if (keycode == Keys.SPACE)
        {
            System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
            System.out.println("Living entities: " + game.getLevel().getEntities().size());
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        Player player = game.getLevel().getPlayer();
        player.lookAt(screenX, screenY);
        player.setMouseControlled(true);
        return true;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

    @Override
    public void connected(Controller controller)
    {

    }

    @Override
    public void disconnected(Controller controller)
    {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        return false;
    }

    @Override
    public boolean axisMoved(Controller c, int axisCode, float value)
    {
        if (Math.abs(value) < Player.MINIMUM_MOVE_MUL)
        {
            return false;
        }
        if (axisCode == XBox360Pad.AXIS_RIGHT_X || axisCode == XBox360Pad.AXIS_RIGHT_Y)
        {
            Vector2 vec = new Vector2(c.getAxis(XBox360Pad.AXIS_RIGHT_X), c.getAxis(XBox360Pad.AXIS_RIGHT_Y));
            game.getLevel().getPlayer().setRotation(vec.angle());
        }
        game.getLevel().getPlayer().setMouseControlled(false);
        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        return false;
    }
}
