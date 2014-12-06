package life.banana4.ld31.input;

public final class Intention
{
    private final Type type;
    private final Object argument;

    public Intention(Type type)
    {
        this(type, null);
    }

    public Intention(Type type, Object argument)
    {
        this.type = type;
        this.argument = argument;
    }

    public Type getType()
    {
        return type;
    }

    public Object getArgument()
    {
        return argument;
    }

    public <T> T getArgumentAs(Class<T> type)
    {
        return type.cast(getArgument());
    }

    @SuppressWarnings("unchecked")
    public <T> T getArgumentOr(T def)
    {
        if (getArgument() == null)
        {
            return def;
        }
        return getArgumentAs((Class<T>)def.getClass());
    }

    public static enum Type
    {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        PRIMARY_ATTACK,
        SECONDARY_ATTACK,
        TERTIARY_ATTACK;

        public boolean isMove()
        {
            switch (this)
            {
                case MOVE_UP:
                case MOVE_DOWN:
                case MOVE_LEFT:
                case MOVE_RIGHT:
                    return true;
                default:
                    return false;
            }
        }
    }
}
