package Domain;

import java.util.Comparator;

public class Comparators {
    public static final Comparator<Message> TIME = (Message o1, Message o2) -> o1.creation_date_millis.compareTo(o2.creation_date_millis);
}