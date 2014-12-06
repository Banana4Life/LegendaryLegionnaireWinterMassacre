package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

public class TiledNode implements IndexedNode<TiledNode>
{
    public enum Type
    {
        TILE_EMPTY,
        TILE_FLOOR,
        TILE_WALL
    }

    public final int x;
    public final int y;

    public final Type type;

    protected Array<Connection<TiledNode>> connections = new Array<>(4);

    public TiledNode(int x, int y, Type type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public Array<Connection<TiledNode>> getConnections()
    {
        return connections;
    }

    @Override
    public int getIndex()
    {
        return x * TiledGraph.SIZE_Y + y;
    }
}
