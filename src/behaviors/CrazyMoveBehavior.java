package behaviors;

import Field.Field;
import java.util.ArrayList;

public class CrazyMoveBehavior extends MovementBehavior {
    public CrazyMoveBehavior(){
        super(v);
    }

    /**+
     * A virológust egy tetszőleges szomszédos mezőre lépteti.
     * @param before    a virológus eredeti pozíciója
     * @param after     a mező, amelyre léptetni kell
     */
    @Override
    public void Move(Field before, Field after) {
        ArrayList<Field> neighbours = before.getNeighbours();
        int index = Math.Round(Math.random()* neighbours.size());
        super.Move(before, neighbours.get(index));
    }
}
