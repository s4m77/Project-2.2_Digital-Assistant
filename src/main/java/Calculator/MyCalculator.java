package Calculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MyCalculator extends JFrame implements ActionListener {
    MyCalculator() {
        start();
    }

    private JTextField textField1;
    private JTextArea textField2;
    private JButton buttonLeftParenthesis;//(
    private JButton buttonRightParenthesis;//)
    private JButton buttonC;//C
    private JButton buttonCE;//CE
    private JButton buttonDele;//Delete
    private JButton buttonAdd;//+
    private JButton buttonSub;//-
    private JButton buttonMul;//x
    private JButton buttonDiv;//÷
    private JButton buttonEqual;//=
    private JButton buttonPoint;//.
    private JButton button0;//0
    private JButton button1;//1
    private JButton button2;//2
    private JButton button3;//3
    private JButton button4;//4
    private JButton button5;//5
    private JButton button6;//6
    private JButton button7;//7
    private JButton button8;//8
    private JButton button9;//9

    public void start() {
        JFrame frame = new JFrame("My Calculator");
        frame.setLayout(null);

        //Button 0
        button0 = new MyButton("0", 100, 400, 110, 50);
        frame.add(button0);
        //Button 1
        button1 = new MyButton("1", 40, 340, 50, 50);
        frame.add(button1);
        //Button 2
        button2 = new MyButton("2", 100, 340, 50, 50);
        frame.add(button2);
        //Button 3
        button3 = new MyButton("3", 160, 340, 50, 50);
        frame.add(button3);
        //Button 4
        button4 = new MyButton("4", 40, 280, 50, 50);
        frame.add(button4);
        //Button 5
        button5 = new MyButton("5", 100, 280, 50, 50);
        frame.add(button5);
        //Button 6
        button6 = new MyButton("6", 160, 280,50,50);
        frame.add(button6);
        //Button 7
        button7 = new MyButton("7", 40, 220, 50, 50);
        frame.add(button7);
        //Button 8
        button8 = new MyButton("8", 100, 220, 50, 50);
        frame.add(button8);
        //Button 9
        button9 = new MyButton("9", 160, 220, 50, 50);
        frame.add(button9);
        //Button .
        buttonPoint = new MyButton(".", 40, 400, 50, 50);
        frame.add(buttonPoint);
        //Button +
        buttonAdd = new MyButton("+", 220, 400, 50, 50);
        frame.add(buttonAdd);
        //Button -
        buttonSub = new MyButton("-", 220, 340, 50, 50);
        frame.add(buttonSub);
        //Button *
        buttonMul = new MyButton("*", 220, 280, 50, 50);
        frame.add(buttonMul);
        //Button /
        buttonDiv = new MyButton("/", 220, 220, 50, 50);
        frame.add(buttonDiv);
        //Button =
        buttonEqual = new MyButton("=", 280, 340, 110, 110);
        frame.add(buttonEqual);
        //Button B
        buttonDele = new MyButton("B", 280, 220, 110, 110);
        frame.add(buttonDele);
        //Button（
        buttonLeftParenthesis = new MyButton("(", 40, 160, 80, 50);
        frame.add(buttonLeftParenthesis);
        //Button ）
        buttonRightParenthesis = new MyButton(")", 130, 160, 80, 50);
        frame.add(buttonRightParenthesis);
        //Button C  Remove all the calc
        buttonC = new MyButton("C", 220, 160, 80, 50);
        frame.add(buttonC);
        //Button CE Remove the current calc
        buttonCE = new MyButton("CE", 310, 160, 80, 50);
        frame.add(buttonCE);
        //Add a text field to input calc formulae
        textField1 = new JTextField();
        textField1.setBounds(40, 20, 350, 60);
        frame.add(textField1);
        textField2 = new JTextArea();
        textField2.setBounds(400, 20, 280, 430);
        frame.add(textField2);


        button0.addActionListener(this);
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        button6.addActionListener(this);
        button7.addActionListener(this);
        button8.addActionListener(this);
        button9.addActionListener(this);
        buttonPoint.addActionListener(this);
        buttonAdd.addActionListener(this);
        buttonSub.addActionListener(this);
        buttonMul.addActionListener(this);
        buttonDiv.addActionListener(this);
        buttonEqual.addActionListener(this);
        buttonDele.addActionListener(this);
        buttonLeftParenthesis.addActionListener(this);
        buttonRightParenthesis.addActionListener(this);
        buttonC.addActionListener(this);
        buttonCE.addActionListener(this);
        textField1.addActionListener(this);

        frame.setBounds(0, 0, 700, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    String str = null;
    int decimalPointNum = 0;
    int hisNum = 0;
    int leftParenthesisNum = 0;
    int rightParenthesisNum = 0;
    int equalNum = 0;


    public void actionPerformed(ActionEvent e) {
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
        //Press B
        if (e.getSource().equals(buttonDele)) {
            str = textField1.getText();
            char []nums=str.toCharArray();
            if (nums[str.length()-1]=='('){
                leftParenthesisNum--;
            }
            str = str.substring(0, str.length() - 1);
            textField1.setText(str);
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
    public void ans(String str) {
        Postfix_machine cc = new Postfix_machine();
        String a = cc.infixToPostfix(str);
        textField1.setText(cc.getResult(a));

    }

    public static void main(String[] args) {
        MyCalculator cal = new MyCalculator();
    }
}
