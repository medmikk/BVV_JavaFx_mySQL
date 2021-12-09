package Domain;

import java.util.ArrayList;

public class Entity {
    private ArrayList<String> attributes = new ArrayList<>();

    public Entity(ArrayList<String> attributes){
        this.attributes = attributes;
    }

    public Entity(){
    }

    public void addAttribute(String attr){
        this.attributes.add(attr);
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }

    public void setAttributes(String ... attr){
        if (attr != null) {
            this.attributes.addAll(java.util.Arrays.asList(attr));
        }
    }

    @Override
    public String toString() {
        return "Entity " +
                "attributes=" + attributes;
    }
}
