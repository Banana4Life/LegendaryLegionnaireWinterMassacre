package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import life.banana4.ld31.ai.TiledNode.Type;

import static life.banana4.ld31.ai.TiledNode.Type.TILE_FLOOR;

public class TiledGraph extends DefaultIndexedGraph<TiledNode>
{
    public static int SIZE_Y = 50;
    public static int SIZE_X = 20;

    public void init()
    {
        for (int x = 0; x <= SIZE_X; x++)
        {
            for (int y = 0; y <= SIZE_Y; y++)
            {
                TiledNode node;
                if (x == 0 || y == 0 || x == SIZE_X || y == SIZE_Y)
                {
                    System.out.print("W");
                    node = new TiledNode(x, y, Type.TILE_WALL);
                }
                else
                {
                    System.out.print(" ");
                    node = new TiledNode(x, y, TILE_FLOOR);
                }
                nodes.add(node);
            }
            System.out.println();
        }
        for (int x = 0; x < SIZE_X; x++)
        {
            int idx = x * SIZE_Y;
            for (int y = 0; y < SIZE_Y; y++)
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
                if (x < SIZE_X - 1)
                {
                    addConnection(n, 1, 0);
                }
                if (y < SIZE_Y - 1)
                {
                    addConnection(n, 0, 1);
                }
            }
        }
    }

    private void addConnection(TiledNode node, int x, int y)
    {
        TiledNode target = getNode(node.x + x, node.y + y);
        switch (target.type)
        {
            case TILE_FLOOR:
                node.getConnections().add(new TiledConnection(this, node, target));
        }
    }

    public TiledNode getNode(int x, int y)
    {
        return nodes.get(x * SIZE_Y + y);
    }
}
