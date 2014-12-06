package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class TiledManhattenDistance implements Heuristic<TiledNode>
{

    @Override
    public float estimate(TiledNode node, TiledNode endNode)
    {
        return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
    }
}
