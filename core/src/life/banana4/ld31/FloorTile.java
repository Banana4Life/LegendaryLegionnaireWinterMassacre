package life.banana4.ld31;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class FloorTile
{
    private Vector2 position;
    private TextureRegion texture;

    public FloorTile(TextureRegion texture)
    {
        this.texture = texture;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public FloorTile setPosition(Vector2 position)
    {
        this.position = position;
        return this;
    }

    public FloorTile draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(texture, position.x, position.y);

        return this;
    }
}
