package app;

import java.io.*;
import java.util.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        String[] data = new String[100]; // read max 100 line
        int addr = 0;
        Map<String, Integer> label = new HashMap<String, Integer>();

        // read code form file
        try {
            File myObj = new File("assembly/count5to0.txt");
            Scanner myReader = new Scanner(myObj);
            addr = 0;
            while (myReader.hasNextLine()) {
                data[addr++] = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid input file.");
            e.printStackTrace();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }

        // TO DO CODE HERE
        addr = 0;
        while (data[addr] != null) {
            String[] arrOfdata = data[addr].split("	");
            String BinaryCode = "0000000";
            int macCode = 0;
            if (arrOfdata[0] != "") {
                label.put(arrOfdata[0], addr); // set label to addr
                // label.get(arrOfdata[]) to get addr
            }

            if (arrOfdata[1].equals("add")) {
                BinaryCode += "000";
                // TO DO SOMETHING
                //macCode = Integer.parseInt(BinaryCode,2);
            } else if (arrOfdata[1].equals("nand")) {
                BinaryCode += "001";
                // TO DO SOMETHING
                //macCode = Integer.parseInt(BinaryCode,2);
            } else if (arrOfdata[1].equals("lw")) {
                BinaryCode += "010";
                // TO DO SOMETHING
                //macCode = Integer.parseInt(BinaryCode,2);
            } else if (arrOfdata[1].equals("sw")) {
                BinaryCode += "011";
                // TO DO SOMETHING
                //macCode = Integer.parseInt(BinaryCode,2);
            } else if (arrOfdata[1].equals("beq")) {
                BinaryCode += "100";
                // TO DO SOMETHING
                //macCode = Integer.parseInt(BinaryCode,2);
            } else if (arrOfdata[1].equals("jalr")) {
                BinaryCode += "101";
                // TO DO SOMETHING
                //macCode = Integer.parseInt(BinaryCode,2);
            } else if (arrOfdata[1].equals("halt")) {
                BinaryCode += "110";
                // TO DO SOMETHING
                for (int indexz = 0; indexz <= 21; indexz++)
                    BinaryCode = BinaryCode + "0";
                macCode = Integer.parseInt(BinaryCode,2); 
            } else if (arrOfdata[1].equals("noop")) {
                BinaryCode += "111";
                // TO DO SOMETHING
                for (int indexz = 0; indexz <= 21; indexz++)
                    BinaryCode = BinaryCode + "0";
                macCode = Integer.parseInt(BinaryCode,2);  

            } else if (arrOfdata[1].equals(".fill")) {
                if (arrOfdata[2].matches("-?(0|[1-9]\\d*)")) {
                    BinaryCode = Integer.toBinaryString(Integer.parseInt(arrOfdata[2]));
                    for (int indexz = BinaryCode.length(); indexz <= 31; indexz++)
                        BinaryCode = "0" + BinaryCode;
                    macCode = Integer.parseInt(arrOfdata[2]);
                } else {
                    BinaryCode = Integer.toBinaryString(label.get(arrOfdata[2]));
                    for (int indexz = BinaryCode.length(); indexz <= 31; indexz++)
                        BinaryCode = "0" + BinaryCode;
                    macCode = label.get(arrOfdata[2]);
                }
            } else {
                throw new IllegalArgumentException("exit(1)");
            }
            System.out.println(macCode);
            addr++;
        }
    }

}
