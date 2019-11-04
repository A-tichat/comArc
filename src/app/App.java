package app;

import java.io.*;
import java.util.*;
import java.util.Scanner;

public class App {

    public static String funcBinary(int dec, int l){ // 5, 3
        String bi = "";
        int i=0;
        if (dec >= 0){
            while (i<l){
                if (dec == 0){
                    bi = "0"+bi;
                    dec = dec/2; 
                }else if (dec == 1){
                    bi = "1"+bi;
                    dec = dec/2;
                }else{
                    bi = Integer.toString(dec%2)+bi;
                    dec = dec/2;
                }
                i++;
            }
        }else {

        }
        
        return bi;
    }

    public static void main(String[] args) {
        String[] data = new String[100]; // read max 100 line
        int addr = 0;// addr of line
        Map<String, Integer> label = new HashMap<String, Integer>(); //store label of each line

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

        int r = 0;
        while (data[r] != null){
            String[] arrOfdatat = data[r].split("	");
            if (arrOfdatat[1].equals(".fill")) {
                if (arrOfdatat[2].matches("-?(0|[1-9]\\d*)")) {
                    label.put(arrOfdatat[0], Integer.parseInt(arrOfdatat[2]));
                } else {
                    label.put(arrOfdatat[0], label.get(arrOfdatat[2]));
                }
            }else if (arrOfdatat[0] != "") {
                label.put(arrOfdatat[0], r); // set label to addr
                // label.get(arrOfdata[]) to get addr
            }
            r++;
        }

        addr = 0;
        while (data[addr] != null) {
            String[] arrOfdata = data[addr].split("	"); //feild of instruction
            String BinaryCode = "0000000"; //machinecode binary bit
            int macCode = 0; // machinecode decimal bit
            

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
        System.out.println(label);
        System.out.println(funcBinary(55, 4));
    }

}
