package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.graphics.Pixmap;

import static life.banana4.ld31.util.TileType.FLOOR;
import static life.banana4.ld31.util.TileType.getType;

public class TiledGraph extends DefaultIndexedGraph<TiledNode>
{
    private final int width;
    private final int height;

    public TiledGraph(Pixmap map)
    {
        this.width = map.getWidth();
        this.height = map.getHeight();

        for (int x = 0; x < map.getWidth(); x++)
        {
            for (int y = 0; y < map.getHeight(); y++)
            {
                nodes.add(new TiledNode(x, y, getType(map.getPixel(x, y)), height));
            }
        }

        for (int x = 0; x < width; x++)
        {
            int idx = x * height;
            for (int y = 0; y < height; y++)
            {
                TiledNode n = nodes.get(idx + y);
                if (x > 0)
                {
                    addConnection(n, -1, 0);
                }
                if (y > 0)
                {
                    addConnection(n, 0, -1);
                }
                if (x < width - 1)
                {
                    addConnection(n, 1, 0);
                }
                if (y < height - 1)
                {
                    addConnection(n, 0, 1);
                }
            }
        }
    }

    private void addConnection(TiledNode node, int x, int y)
    {
        if (node.type == FLOOR)
        {
            TiledNode target = getNode(node.x + x, node.y + y);
            switch (target.type)
            {
                case FLOOR:
                    //System.out.println(node.x + ":" + node.y + "->" + target.x + ":" + target.y + "|" + x + ":" + y);
                    node.getConnections().add(new TiledConnection(this, node, target));
            }
        }
    }

    public TiledNode getNode(int x, int y)
    {
        return nodes.get(x * height + y);
    }
}
