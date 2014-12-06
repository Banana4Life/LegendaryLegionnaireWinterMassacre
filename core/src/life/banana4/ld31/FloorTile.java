package life.banana4.ld31;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class FloorTile extends TextureRegion
{
    private Vector2 position;

    public Vector2 getPosition()
    {
        return position;
    }

    public FloorTile draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(this, position.x, position.y);

        return this;
    }
}
