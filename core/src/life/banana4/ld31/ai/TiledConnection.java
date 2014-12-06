package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.pfa.DefaultConnection;

public class TiledConnection extends DefaultConnection<TiledNode>
{
    TiledGraph worldMap;

    public TiledConnection(TiledGraph worldMap, TiledNode fromNode, TiledNode toNode)
    {
        super(fromNode, toNode);
        this.worldMap = worldMap;
    }

    @Override
    public float getCost()
    {
        return 1;
    }
}
