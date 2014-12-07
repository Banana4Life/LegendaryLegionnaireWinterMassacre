package life.banana4.ld31.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import life.banana4.ld31.DrawContext;
import life.banana4.ld31.Entity;
import life.banana4.ld31.entity.collision.CollisionSource;
import life.banana4.ld31.entity.collision.CollisionTarget;

public class Wall extends Entity implements CollisionTarget
{
    private TextureRegion texture;
    public Wall(TextureRegion texture, float width, float height)
    {
        super(width, height);
        this.texture = texture;
    }

    @Override
    public void update(OrthographicCamera camera, float delta)
    {

    }

    @Override
    public void draw(DrawContext ctx)
    {
        SpriteBatch spriteBatch = ctx.getSpriteBatch();
        spriteBatch.begin();
        spriteBatch.draw(texture, getX(), getY(), 32, 32);
        spriteBatch.end();
    }

    @Override
    public void onCollide(CollisionSource source, Vector2 mtv)
    {

    }
}
