package life.banana4.ld31.input;

import java.util.Collections;
import java.util.Set;

public interface IntentionDetector
{
    static final Set<Intention> NO_INTENTIONS = Collections.emptySet();

    Set<Intention> detect();
}
