package life.banana4.ld31.input;

import java.util.Collections;
import java.util.Set;
import life.banana4.ld31.input.Intention.Type;

public interface IntentionDetector
{
    static final Set<Intention> NO_INTENTIONS = Collections.emptySet();

    Set<Intention> detect();
}
