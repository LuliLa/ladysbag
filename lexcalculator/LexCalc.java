package ru.lexcalculator;

import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Марат
 * Date: 02.01.14
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
public class LexCalc {
    private static LexAnalizator analizator = new LexAnalizator();
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();
            List<Lexems> result = analizator.parseLine(line);
            if (result==null) {
                System.out.println("result is null");
                continue;
            }
            for (Lexems lex:result) {
                System.out.println("Lexem is "+lex.getLexem()+" and value is "+lex.getValue());
            }
        }
    }
}
