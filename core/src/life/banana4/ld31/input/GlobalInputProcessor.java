package life.banana4.ld31.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import life.banana4.ld31.Entity;
import life.banana4.ld31.Ld31;
import life.banana4.ld31.entity.enemy.Enemy;

/**
 * This is the global input processor which acts as the fallback of the input multiplexer.
 * It can be used to handle global key bindings
 *
 * @author Phillip Schichtel
 */
public class GlobalInputProcessor extends AllThemInputAdapter
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
            return true;
        }
        else if (keycode == Keys.K)
        {
            for (final Entity entity : game.getLevel().getEntities())
            {
                if (entity instanceof Enemy)
                {
                    entity.kill();
                }
            }
            return true;
        }
        else if (keycode == Keys.ENTER && (Gdx.input.isKeyPressed(Keys.ALT_LEFT) | Gdx.input.isKeyPressed(Keys.ALT_RIGHT)))
        {
            if (Gdx.graphics.supportsDisplayModeChange())
            {
                Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), !Gdx.graphics.isFullscreen());
                return true;
            }
        }
        return false;
    }
}
