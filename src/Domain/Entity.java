package Domain;

import java.util.ArrayList;

public class Entity {
    private ArrayList<String> attributes = new ArrayList<>();

    public Entity(ArrayList<String> attributes){
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Entity " +
                "attributes=" + attributes;
    }
}
