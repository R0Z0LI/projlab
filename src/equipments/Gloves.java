package equipments;
import PropertyHandler.PropertyHandler;
import behaviors.GloveDefBehavior;
import virologist.Virologist;

public class Gloves extends Equipment{
    private int usability;
    //private CapeWithGloveDef capeWithGloveDef;
    private GloveDefBehavior gloveDef;

    public Gloves() {
        super();
    }

    @Override
    public void AddBehToStack(Virologist v) {
        v.setDefenseBeh(new GloveDefBehavior(v));
    }

    @Override
    public void RemoveBehFromStack() {
        //
    }

    @Override
    public void BeCollected(PropertyHandler ph) {
        ph.AddEquipment(this);
    }

    @Override
    public void BeRemoved(PropertyHandler ph) {
        ph.RemoveEqupment(this);
    }
}