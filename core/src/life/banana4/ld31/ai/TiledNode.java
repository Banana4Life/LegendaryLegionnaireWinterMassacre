package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;
import life.banana4.ld31.resource.Levels;
import life.banana4.ld31.util.TileType;

public class TiledNode implements IndexedNode<TiledNode>
{
    public final int x;
    public final int y;

    public final TileType type;

    public final int graphHeight;

    protected Array<Connection<TiledNode>> connections = new Array<>(4);

    public TiledNode(int x, int y, TileType type, int graphHeight)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        this.graphHeight = graphHeight;
    }

    @Override
    public Array<Connection<TiledNode>> getConnections()
    {
        return connections;
    }

    @Override
    public int getIndex()
    {
        return x * graphHeight + y;
    }

    public float getTileX()
    {
        return this.x * Levels.TILE_WIDTH;
    }

    public float getTileY()
    {
        return this.y * Levels.TILE_WIDTH;
    }
}
