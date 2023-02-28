package Calculator;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CalculatorDisplay extends Application {

    private TextField textField1;
    private TextArea textField2;
    private Button buttonLeftParenthesis;//(
    private Button buttonRightParenthesis;//)
    private Button buttonC;//C
    private Button buttonCE;//CE
    private Button buttonAdd;//+
    private Button buttonSub;//-
    private Button buttonMul;//x
    private Button buttonDiv;//÷
    private Button buttonEqual;//=
    private Button buttonPoint;//.
    private Button button0;//0
    private Button button1;//1
    private Button button2;//2
    private Button button3;//3
    private Button button4;//4
    private Button button5;//5
    private Button button6;//6
    private Button button7;//7
    private Button button8;//8
    private Button button9;//9

    public CalculatorDisplay() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            AnchorPane root = new AnchorPane();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setWidth(800);
            primaryStage.setHeight(500);
            primaryStage.setResizable(false);
            addComp(root);
            primaryStage.setTitle("My Calculator");
            primaryStage.show();
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("8");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addComp(AnchorPane root) {

        textField1 = new TextField();
        textField1.setLayoutX(50);
        textField1.setLayoutY(15);
        textField1.setPrefSize(260,60);
        textField1.setEditable(true);

        textField2 = new TextArea();
        textField2.setLayoutX(370);
        textField2.setLayoutY(15);
        textField2.setPrefSize(350,420);

        root.getChildren().add(textField1);
        root.getChildren().add(textField2);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(50));
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        root.getChildren().add(gridPane);

        LogicEvent logicEvent = new LogicEvent();
        NumberEvent numberEvent = new NumberEvent();

        //Button 0
        button0 = new MyButton("0");
        button0.setOnMouseClicked(numberEvent);
        GridPane.setConstraints(button0, 1, 6);
        gridPane.getChildren().add(button0);
        //Button 1
        button1 = new MyButton("1");
        button1.setOnMouseClicked(numberEvent);
        GridPane.setConstraints(button1, 0, 5);
        gridPane.getChildren().add(button1);
        //Button 2
        button2 = new MyButton("2");
        button2.setOnMouseClicked(numberEvent);
        GridPane.setConstraints(button2, 1, 5);
        gridPane.getChildren().add(button2);
        //Button 3
        button3 = new MyButton("3");
        button3.setOnMouseClicked(numberEvent);
        GridPane.setConstraints(button3, 2, 5);
        gridPane.getChildren().add(button3);
        //Button 4
        button4 = new MyButton("4");
        GridPane.setConstraints(button4, 0, 4);
        button4.setOnMouseClicked(numberEvent);
        gridPane.getChildren().add(button4);
        //Button 5
        button5 = new MyButton("5");
        GridPane.setConstraints(button5, 1, 4);
        button5.setOnMouseClicked(numberEvent);
        gridPane.getChildren().add(button5);
        //Button 6
        button6 = new MyButton("6");
        GridPane.setConstraints(button6, 2, 4);
        button6.setOnMouseClicked(numberEvent);
        gridPane.getChildren().add(button6);
        //Button 7
        button7 = new MyButton("7");
        GridPane.setConstraints(button7, 0, 3);
        button7.setOnMouseClicked(numberEvent);
        gridPane.getChildren().add(button7);
        //Button 8
        button8 = new MyButton("8");
        button8.setOnMouseClicked(numberEvent);
        GridPane.setConstraints(button8, 1, 3);
        gridPane.getChildren().add(button8);
        //Button 9
        button9 = new MyButton("9");
        button9.setOnMouseClicked(numberEvent);
        GridPane.setConstraints(button9, 2, 3);
        gridPane.getChildren().add(button9);
        //Button .
        buttonPoint = new MyButton(".");
        buttonPoint.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonPoint, 0, 6);
        gridPane.getChildren().add(buttonPoint);
        //Button +
        buttonAdd = new MyButton("+");
        buttonAdd.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonAdd, 3, 6);
        gridPane.getChildren().add(buttonAdd);
        //Button -
        buttonSub = new MyButton("-");
        buttonSub.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonSub, 3, 5);
        gridPane.getChildren().add(buttonSub);
        //Button *
        buttonMul = new MyButton("*");
        buttonMul.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonMul, 3, 4);
        gridPane.getChildren().add(buttonMul);
        //Button /
        buttonDiv = new MyButton("/");
        buttonDiv.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonDiv, 3, 3);
        gridPane.getChildren().add(buttonDiv);
        //Button =
        buttonEqual = new MyButton("=");
        buttonEqual.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonEqual, 2, 6);
        gridPane.getChildren().add(buttonEqual);
        //Button (
        buttonLeftParenthesis = new MyButton("(");
        buttonLeftParenthesis.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonLeftParenthesis, 0, 2);
        gridPane.getChildren().add(buttonLeftParenthesis);
        //Button )
        buttonRightParenthesis = new MyButton(")");
        buttonRightParenthesis.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonRightParenthesis, 1, 2);
        gridPane.getChildren().add(buttonRightParenthesis);
        //Button C
        buttonC = new MyButton("C");
        buttonC.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonC, 2, 2);
        gridPane.getChildren().add(buttonC);
        //Button CE
        buttonCE = new Button("CE");
        buttonCE.setPrefSize(50,50);
        buttonCE.setFont(new Font("Verdana", 18));
        buttonCE.setOnMouseClicked(logicEvent);
        GridPane.setConstraints(buttonCE, 3, 2);
        gridPane.getChildren().add(buttonCE);

    }

    String str = null;
    int decimalPointNum = 0;
    int hisNum = 0;
    int leftParenthesisNum = 0;
    int rightParenthesisNum = 0;
    int equalNum = 0;

    class LogicEvent implements EventHandler<Event> {
        public void handle(Event e) {
            //Press .
            if (e.getSource().equals(buttonPoint)) {
                str = textField1.getText();
                if (str.length() > 15 || hisNum == 1) {
                }
                if (decimalPointNum == 0) {
                    textField1.setText(str + ".");
                    decimalPointNum = 1;
                }
            }
            //Press +
            if (e.getSource().equals(buttonAdd)) {
                str = textField1.getText();
                char ch1[] = str.toCharArray();
                int length1 = str.length() - 1;
                if ((length1 == -1 || ch1[length1] != ')') && (str.equals("0") || str.equals("") || ch1[length1] == '.' || ch1[length1] == '+' || ch1[length1] == '-' || ch1[length1] == '*' || ch1[length1] == '/' || ch1[length1] == '(' || ch1[length1] == ')')) {
                } else {
                    textField1.setText(str + "+");
                }
                decimalPointNum = 0;
            }
            //Press -
            if (e.getSource().equals(buttonSub)) {
                str = textField1.getText();
                char ch1[] = str.toCharArray();
                int length1 = str.length() - 1;
                if ((length1 == -1 || ch1[length1] != ')') && (ch1[length1] == '.' || ch1[length1] == '+' || ch1[length1] == '-' || ch1[length1] == '*' || ch1[length1] == '/' || ch1[length1] == '(' || ch1[length1] == ')')) {
                } else {
                    textField1.setText(str + "-");
                }
                decimalPointNum = 0;
            }
            //Press *
            if (e.getSource().equals(buttonMul)) {
                str = textField1.getText();
                char ch1[] = str.toCharArray();
                int length1 = str.length() - 1;
                if ((length1 == -1 || ch1[length1] != ')') && (str.equals("0") || str.equals("") || ch1[length1] == '.' || ch1[length1] == '+' || ch1[length1] == '-' || ch1[length1] == '*' || ch1[length1] == '/' || ch1[length1] == '(' || ch1[length1] == ')')) {
                } else {
                    textField1.setText(str + "*");
                }
                decimalPointNum = 0;
            }
            //Press /
            if (e.getSource().equals(buttonDiv)) {
                str = textField1.getText();
                char ch1[] = str.toCharArray();
                int length1 = str.length() - 1;
                if ((length1 == -1 || ch1[length1] != ')') && (str.equals("0") || str.equals("") || ch1[length1] == '.' || ch1[length1] == '+' || ch1[length1] == '-' || ch1[length1] == '*' || ch1[length1] == '/' || ch1[length1] == '(' || ch1[length1] == ')')) {
                } else {
                    textField1.setText(str + "/");
                }
                decimalPointNum = 0;
            }
            //Press (
            if (e.getSource().equals(buttonLeftParenthesis)) {
                str = textField1.getText();
                char ch[] = str.toCharArray();
                int length = str.length() - 1;
                if (length == -1 || ch[length] == '+' || ch[length] == '-' || ch[length] == '*' || ch[length] == '/') {
                    textField1.setText(str + '(');
                    leftParenthesisNum++;
                }
                if (length == -1 || ch[length] == '+' || ch[length] == '-' || ch[length] == '*' || ch[length] == '/')
                    decimalPointNum = 0;
                if (length == 0 || ch[length] == 0) {
                    textField1.setText("(");
                    leftParenthesisNum++;
                }
            }
            //Press )
            if (e.getSource().equals(buttonRightParenthesis)) {
                str = textField1.getText();
                char ch[] = str.toCharArray();
                int length = str.length() - 1;
                if (Character.isDigit(ch[length]) && leftParenthesisNum > rightParenthesisNum) {
                    rightParenthesisNum++;
                    textField1.setText(str + ')');
                }
                decimalPointNum = 0;
            }
            //Press C
            if (e.getSource().equals(buttonC)) {
                textField1.setText("0");
                leftParenthesisNum = 0;
                rightParenthesisNum = 0;
                decimalPointNum = 0;
                hisNum = 0;
                textField2.setText(" ");
            }
            //Press CE
            if (e.getSource().equals(buttonCE)) {
                textField1.setText("0");
                decimalPointNum = 0;
            }
            //Press =
            if (e.getSource().equals(buttonEqual)) {
                str = textField1.getText();
                if (leftParenthesisNum != rightParenthesisNum) {
                    textField1.setText("Invalid Equation");
                } else {
                    ans(str);
                }
                String s = str + "=" + textField1.getText();
                textField2.setText(s + "\r\n" + textField2.getText());
                equalNum = 1;
            }
        }
    }
    class NumberEvent implements EventHandler<Event>{
        public void handle(Event e) {
            if (equalNum == 1) {
                textField1.setText("0");
                equalNum = 0;
            }
            //Press 0
            if (e.getSource().equals(button0)) {
                str = textField1.getText();
                if (str.length() > 16 || str.equals("0") || hisNum == 1) {

                } else {
                    textField1.setText(str + "0");
                }
            }
            //Press 1
            if (e.getSource().equals(button1)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {

                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("1");
                } else {
                    textField1.setText(str + "1");
                }
            }
            //Press 2
            if (e.getSource().equals(button2)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("2");
                } else {
                    textField1.setText(str + "2");
                }
            }
            //Press 3
            if (e.getSource().equals(button3)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("3");
                } else {
                    textField1.setText(str + "3");
                }
            }
            //Press 4
            if (e.getSource().equals(button4)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("4");
                } else {
                    textField1.setText(str + "4");
                }
            }
            //Press 5
            if (e.getSource().equals(button5)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("5");
                } else {
                    textField1.setText(str + "5");
                }
            }
            //Press 6
            if (e.getSource().equals(button6)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("6");
                } else {
                    textField1.setText(str + "6");
                }
            }
            //Press 7
            if (e.getSource().equals(button7)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("7");
                } else {
                    textField1.setText(str + "7");
                }
            }
            //Press 8
            if (e.getSource().equals(button8)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("8");
                } else {
                    textField1.setText(str + "8");
                }
            }
            //Press 9
            if (e.getSource().equals(button9)) {
                str = textField1.getText();
                if (str.length() > 16 || hisNum == 1) {
                } else if (str.equals("0") || str.equals("")) {
                    textField1.setText("9");
                } else {
                    textField1.setText(str + "9");
                }
            }
        }
    }


    public void ans(String str) {
        Postfix_machine cc = new Postfix_machine();
        String a = cc.infixToPostfix(str);
        textField1.setText(cc.getResult(a));
    }
}

