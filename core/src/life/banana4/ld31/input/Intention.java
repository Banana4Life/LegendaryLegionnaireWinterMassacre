package life.banana4.ld31.input;

public final class Intention
{
    private final Type type;
    private final Object argument;

    public Intention(Type type) {
        this(type, null);
    }

    public Intention(Type type, Object argument)
    {
        this.type = type;
        this.argument = argument;
    }

    public static enum Type
    {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        PRIMARY_ATTACK,
        SECONDARY_ATTACK,
    }
}
