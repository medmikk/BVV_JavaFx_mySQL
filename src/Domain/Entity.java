package Domain;

import java.util.ArrayList;

public class Entity {
    private ArrayList<String> attributes = new ArrayList<>();

    public Entity(ArrayList<String> attributes){
        this.attributes = attributes;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public String[] getAttributesArray() {
        String[] attr = new String[attributes.size()];
        attributes.toArray(attr);
        return attr;
    }

    @Override
    public String toString() {
        return "Entity " +
                "attributes=" + attributes;
    }
}
