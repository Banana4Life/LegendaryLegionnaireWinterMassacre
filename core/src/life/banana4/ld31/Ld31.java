package life.banana4.ld31;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import life.banana4.ld31.input.GlobalInputProcessor;
import life.banana4.ld31.input.InputMultiplexer;
import life.banana4.ld31.resource.LevelLoader;

public class Ld31 extends ApplicationAdapter
{
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    private static boolean DEBUG = System.getProperties().containsKey("debug");

    private Level level;
    private DrawContext drawContext;
    private Ld31Resources resources;
    private InputMultiplexer inputMultiplexer;
    private OrthographicCamera cam;

    private float lastShakeX;
    private float lastShakeY;
    private float shakeX;
    private float shakeY;
    private float shakeDuration;
    private float shakeStrength;

    @Override
    public void create()
    {
        cam = new OrthographicCamera();
        cam.setToOrtho(true);
        inputMultiplexer = new InputMultiplexer(new GlobalInputProcessor(this, cam));
        Gdx.input.setInputProcessor(inputMultiplexer);
        Controllers.addListener(inputMultiplexer);
        Gdx.input.setCursorCatched(true);

        this.resources = new Ld31Resources();
        resources.build();
        this.drawContext = new DrawContext(cam, new SpriteBatch(), new ShapeRenderer(), resources);

        Pixmap cursor = new Pixmap(Gdx.files.internal("textures/cursor.png"));
        Gdx.input.setCursorImage(cursor, cursor.getWidth() / 2, cursor.getHeight() / 2);

        if (!isDebug())
        {
            Music song = resources.songs.main;
            song.setLooping(true);
            song.setVolume(.1f);
            song.play();
        }
    }

    public void shakeScreen(float x, float y, float duration, float strength)
    {
        float len = (float)Math.sqrt(x * x + y * y);
        if (len > 0)
        {
            x /= len;
            y /= len;
        }
        this.shakeX = x;
        this.shakeY = y;
        this.shakeDuration = duration;
        this.shakeStrength = strength;
    }

    private void shake(float delta)
    {
        if (shakeDuration > 0)
        {
            float frame = Gdx.graphics.getFrameId();
            float shake = (float)Math.sin(frame) * shakeStrength * delta;
            float x = shakeX * shake;
            float y = shakeY * shake;
            cam.translate(-lastShakeX, -lastShakeY);
            cam.translate(x, y);
            lastShakeX = x;
            lastShakeY = y;
            shakeDuration -= delta;
            if (shakeDuration <= 0)
            {
                cam.translate(-lastShakeX, -lastShakeY);
                lastShakeX = 0;
                lastShakeY = 0;
            }
        }
    }

    @Override
    public void render()
    {
        final float delta = Gdx.graphics.getDeltaTime();
        if (level == null)
        {
            level = LevelLoader.load(this, resources.textures);
        }
        shake(delta);
        cam.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.level.render(drawContext, delta);
    }

    public Level getLevel()
    {
        return level;
    }

    public DrawContext getDrawContext()
    {
        return drawContext;
    }

    public static boolean isDebug()
    {
        return DEBUG;
    }

    public InputMultiplexer getInputMultiplexer()
    {
        return inputMultiplexer;
    }

    public void reset()
    {
        if (level != null)
        {
            level.shutdown();
        }
        this.level = null;
    }
}
