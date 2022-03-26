package equipments;

import PropertyHandler.PropertyHandler;
import virologist.Virologist;

public class Sack extends Equipment{

    public Sack() {
        super();
    }

    @Override
    public void AddBehToStack(Virologist v) {
        v.getPropertyHandler().setMax_material(1);
    }

    @Override
    public void RemoveBehFromStack() {

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
