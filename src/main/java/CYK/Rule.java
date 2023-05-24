package CYK;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;

@Data
public class Rule {
    int id;
    String variable;
    @EqualsAndHashCode.Exclude
    List<String> expressions = new ArrayList<>();
    @EqualsAndHashCode.Exclude
    List<List<String>> splitExpressions = new ArrayList<>();

    public Rule copy(){
        Rule copy = new Rule();
        copy.setId(id);
        copy.setVariable(variable);
        copy.setExpressions(new ArrayList<>(expressions));
        return copy;
    }

    public List<String> getExpressions(){
        return expressions;
    }

    public String getVariable(){
        return variable;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setVariable(String variable){
        this.variable = variable;
    }

    public void setExpressions(List<String> expressions){
        this.expressions = expressions;
    }
}
