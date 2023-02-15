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



    
}
