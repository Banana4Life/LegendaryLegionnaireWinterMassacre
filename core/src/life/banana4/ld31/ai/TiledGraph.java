package life.banana4.ld31.ai;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import life.banana4.ld31.Level;
import life.banana4.ld31.ai.TiledNode.Type;
import life.banana4.ld31.entity.Wall;

import static life.banana4.ld31.ai.TiledNode.Type.TILE_FLOOR;

public class TiledGraph extends DefaultIndexedGraph<TiledNode>
{
    public static int SIZE_Y = 50;
    public static int SIZE_X = 20;

    public TiledGraph init(Level level)
    {
        for (int x = 0; x < SIZE_X; x++)
        {
            for (int y = 0; y < SIZE_Y; y++)
            {
                TiledNode node;
                if (x == 0 || y == 0 || x == SIZE_X - 1 || y == SIZE_Y - 1)
                {
                    //System.out.print("W");
                    node = new TiledNode(x, y, Type.TILE_WALL);
                    level.addEntity(new Wall(1, 1).move(x, y));
                }
                else
                {
                    //System.out.print(" ");
                    node = new TiledNode(x, y, TILE_FLOOR);
                }
                nodes.add(node);
            }
//            System.out.println();
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

        return this;
    }

    private void addConnection(TiledNode node, int x, int y)
    {
        if (node.type == TILE_FLOOR)
        {
            TiledNode target = getNode(node.x + x, node.y + y);
            switch (target.type)
            {
                case TILE_FLOOR:
                    //System.out.println(node.x + ":" + node.y + "->" + target.x + ":" + target.y + "|" + x + ":" +y);
                    node.getConnections().add(new TiledConnection(this, node, target));
            }
        }
    }

    public TiledNode getNode(int x, int y)
    {
        return nodes.get(x * SIZE_Y + y);
    }
}
