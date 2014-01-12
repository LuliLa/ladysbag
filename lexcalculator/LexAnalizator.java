package ru.lexcalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Марат
 * Date: 02.01.14
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class LexAnalizator {
    private String value = "";
    private States currentState;
    private int braces;
    private Map<Integer, Lexem> operators = new HashMap<Integer, Lexem>();
    private List<Lexems> commands = new ArrayList<Lexems>();

    public LexAnalizator() {
        operators.put((int)'/', Lexem.DIV);
        operators.put((int)'-',Lexem.MINUS);
        operators.put((int)'*',Lexem.MULT);
        operators.put((int)'+',Lexem.PLUS);

        operators.put((int)'(',Lexem.LBRACE);
        operators.put((int)')',Lexem.RBRACE);
    }

    public List<Lexems> parseLine(String line) {
        commands.clear();
        currentState = States.START;
        braces = 0;
        char[] chars = line.toCharArray();
        boolean process = true;
        int index = 0;
        while(process&&index<=line.length()-1) {
            process = analyze(chars[index], index==line.length()-1);
            index++;
        }
        if (commands.size()>0 && process&& braces==0) {
            return commands;
        }
        return null;
    }

    private boolean analyze(char ch, boolean flag){
        switch(currentState) {
            case START: {
                if (Character.isDigit(ch)) {
                    value += ch;
                    if (flag){
                        commands.add(new Lexems(Lexem.NUMBER, Integer.parseInt(value)));
                    }
                    return true;
                }
                if (ch=='.' && !"".equals(value)) {
                    value += ch;
                    currentState = States.DOUBLE_START;
                    return true;
                }
                if (ch == '(') {
                    if (commands.size()==0||operators.containsValue(commands.get(commands.size()-1).getLexem())) {
                        Lexem lexem = whatOperator(ch);
                        commands.add(new Lexems(lexem, (int) ch));
                        braces++;
                        return true;
                    }
                }
                if (ch == ')'&&!"".equals(value)) {
                    commands.add(new Lexems(Lexem.NUMBER, Integer.valueOf(value)));
                    value = "";
                    Lexem lexem = whatOperator(ch);
                    commands.add(new Lexems(lexem, (int) ch));
                    braces--;
                    currentState = States.OPERATOR;
                    return true;
                }
                if (operators.containsKey((int)ch)&&!"".equals(value)) {
                    commands.add(new Lexems(Lexem.NUMBER, Integer.valueOf(value)));
                    value = "";
                    Lexem lexem = whatOperator(ch);
                    commands.add(new Lexems(lexem, (int) ch));
                    return true;
                }
                break;
            }
            case BRACE_START: {
               if (Character.isDigit(ch)) {
                   value += ch;
                   currentState = States.START;
                   return true;
               }
                break;
            }

            case DOUBLE_START: {
                if (Character.isDigit(ch)) {
                    value += ch;
                    currentState = States.DOUBLE_END;
                    return true;
                }
                break;
            }
            case DOUBLE_END: {
                if (Character.isDigit(ch)) {
                    value += ch;
                    return true;
                }
                if (operators.containsKey((int)ch)) {
                    commands.add(new Lexems(Lexem.NUMBER, Integer.valueOf(value)));
                    value = "";
                    Lexem lexem = whatOperator(ch);
                    commands.add(new Lexems(lexem, (int) ch));
                    currentState = States.START;
                    return true;
                }
                break;
            }
            case OPERATOR: {
                if (operators.containsKey((int)ch)) {
                    Lexem lexem = whatOperator(ch);
                    commands.add(new Lexems(lexem, (int) ch));
                    currentState = States.START;
                    return true;
                }
                break;
            }
            default: {
                return false;
            }
        }
        return false;
    }

    private Lexem whatOperator(char ch) {
        return operators.get((int)ch);
    }
}


enum Lexem{NUMBER, PLUS, MINUS, MULT, DIV, LBRACE, RBRACE, SEMIKOLON};

enum States{START, OPERATOR, BRACE_START, BRACE_END, DOUBLE_START,DOUBLE_END, NUMBER_START};

class Lexems {
    private Lexem lexem;
    private int value;
    public Lexems(Lexem token, int value) {
        this.lexem = token;
        this.value = value;
    }

    public Lexem getLexem() {
        return lexem;
    }

    public int getValue() {
        return value;
    }
}
