package CFG;
import java.util.ArrayList;


public class Node {

    ArrayList<Node> children = new ArrayList<Node>();
    String attribute;
    String value;

    public Node (String Attribute, String Value){
        attribute = Attribute;
        value = Value;
    }

    public void addChild(Node child){
        children.add(child);
    }
    public Node getNode(String attribute){
        if(this.attribute.equals(attribute)){
            return this;
        }
        else{
            for(int i=0;i<children.size();i++){
                Node result = children.get(i).getNode(attribute);
                if(result!=null){
                    return result;
                }
            }
        }
        return null;
    }
 
    public String getAttribute(){
        return attribute;
    }
    public void setValue(String value){
        this.value = value;
    }

    public String findValue(String attribute){
        if(this.attribute.equals(attribute)){
            return value;
        }
        else{
            for(int i=0;i<children.size();i++){
                String result = children.get(i).findValue(attribute);
                if(result!=null){
                    return result;
                }
            }
        }
        return null;
    }

    public String getFilledString(){
        //return the string with all values surrounded by < > replaced with the value
        //we need to watch out because multiple <> can have the same attribute but should be filled with different values
        //luckily all values are added to the tree in order of appearance so we can just replace the first one we find
        //we can also assume that all values are filled in the tree
        String result = value;
        for(int i=0;i<children.size();i++){
            String attribute = children.get(i).getAttribute();
            String replacement = children.get(i).getFilledString();

            result = result.replaceFirst(attribute, replacement);
        }
        return result;

    }

    public void clearChildren(){
        children.clear();
    }



    
}
