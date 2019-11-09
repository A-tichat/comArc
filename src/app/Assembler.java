package app;

import java.io.*;
import java.util.*;
import app.binary;

public class Assembler {
    public static void main(String[] args) {
        binary binary = new binary();
        String[] data = new String[100]; // read max 100 line
        int addr = 0;// addr of line
        Map<String, Integer> label = new HashMap<String, Integer>(); // store label of each line

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
        while (data[r] != null) {
            String[] arrOfdatat = data[r].split("	");
            if(label.containsKey(arrOfdatat[0])){
                throw new IllegalArgumentException("exit(1) duplicate"); // check same label
            }
            
            if(!arrOfdatat[0].equals("")){
                if(arrOfdatat[0].length()>6 || arrOfdatat[0].substring(0,1).matches("-?(0|[1-9]\\d*)")) //check size >6? and start with letter?
                throw new IllegalArgumentException("exit(1) Undefined");
            }
            if (arrOfdatat[1].equals(".fill")) {
                if (arrOfdatat[2].matches("-?(0|[1-9]\\d*)")) {
                    label.put(arrOfdatat[0], Integer.parseInt(arrOfdatat[2]));
                } else {
                    label.put(arrOfdatat[0], label.get(arrOfdatat[2]));
                }
            } else if (!arrOfdatat[0].equals("")) {
                label.put(arrOfdatat[0], r); // set label to addr
                // label.get(arrOfdata[]) to get addr
            }
            
            r++;
        }

        String[] macCode = new String[r];
        addr = 0;
        while (data[addr] != null) {
            String[] arrOfdata = data[addr].split("	"); // feild of instruction
            String BinaryCode = "0000000"; // machinecode binary bit
            int printBiCode = 0; // machinecode decimal bit

            if (arrOfdata[1].equals("add")) {
                BinaryCode += "000";
                    if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                        if(Integer.parseInt(arrOfdata[4])>32767||Integer.parseInt(arrOfdata[4])<-32768){ // offsetfield >16 bit?
                    throw new IllegalArgumentException("exit(1)");
                        }
                    } else {
                        if(label.get(arrOfdata[4])>32767||(label.get(arrOfdata[4])<-32768)){
                            throw new IllegalArgumentException("exit(1)");
                    }
                }
                
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                for (int indexz = 3; indexz <= 15; indexz++)
                    BinaryCode = BinaryCode + "0";
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[4]), 3);
                printBiCode = Integer.parseInt(BinaryCode, 2);
            } else if (arrOfdata[1].equals("nand")) {
                BinaryCode += "001";
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    if(Integer.parseInt(arrOfdata[4])>32767||Integer.parseInt(arrOfdata[4])<-32768){
                throw new IllegalArgumentException("exit(1)");
                    }
                } else {
                    if(label.get(arrOfdata[4])>32767||(label.get(arrOfdata[4])<-32768)){
                        throw new IllegalArgumentException("exit(1)");
                }
            }
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                BinaryCode += binary.create(0, 13);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[4]), 3);
                printBiCode = Integer.parseInt(BinaryCode, 2);
            } else if (arrOfdata[1].equals("lw")) {
                BinaryCode += "010";
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    if(Integer.parseInt(arrOfdata[4])>32767||Integer.parseInt(arrOfdata[4])<-32768){
                throw new IllegalArgumentException("exit(1)");
                    }
                } else {
                    if(label.get(arrOfdata[4])>32767||(label.get(arrOfdata[4])<-32768)){
                        throw new IllegalArgumentException("exit(1)");
                }
            }
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    BinaryCode += binary.create(Integer.parseInt(arrOfdata[4]), 16);
                } else {
                    BinaryCode += binary.create(label.get(arrOfdata[4]), 16);
                }
                printBiCode = Integer.parseInt(BinaryCode, 2);
            } else if (arrOfdata[1].equals("sw")) {
                BinaryCode += "011";
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    if(Integer.parseInt(arrOfdata[4])>32767||Integer.parseInt(arrOfdata[4])<-32768){
                throw new IllegalArgumentException("exit(1)");
                    }
                } else {
                    if(label.get(arrOfdata[4])>32767||(label.get(arrOfdata[4])<-32768)){
                        throw new IllegalArgumentException("exit(1)");
                }
            }
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[4]), 16);
                printBiCode = Integer.parseInt(BinaryCode, 2);
            } else if (arrOfdata[1].equals("beq")) {
                BinaryCode += "100";
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    if(Integer.parseInt(arrOfdata[4])>32767||Integer.parseInt(arrOfdata[4])<-32768){
                throw new IllegalArgumentException("exit(1)");
                    }
                } else {
                    if(label.get(arrOfdata[4])>32767||(label.get(arrOfdata[4])<-32768)){
                        throw new IllegalArgumentException("exit(1)");
                }
            }
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                int dst = 0;
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    dst = Integer.parseInt(arrOfdata[4]);
                } else {
                    dst = label.get(arrOfdata[4]);
                    if (dst < addr) {
                        dst = dst * (0 - 1);
                    }
                }
                BinaryCode += binary.create(dst, 16);
                printBiCode = Integer.parseInt(BinaryCode, 2);
            } else if (arrOfdata[1].equals("jalr")) {
                BinaryCode += "101";
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BinaryCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                for(int indexz =0; indexz<=15;indexz++)
                    BinaryCode = BinaryCode + "0";
                printBiCode = Integer.parseInt(BinaryCode,2);
            } else if (arrOfdata[1].equals("halt")) {
                BinaryCode += "110";
                for (int indexz = 0; indexz <= 21; indexz++)
                    BinaryCode = BinaryCode + "0";
                printBiCode = Integer.parseInt(BinaryCode, 2);
            } else if (arrOfdata[1].equals("noop")) {
                BinaryCode += "111";
                for (int indexz = 0; indexz <= 21; indexz++)
                    BinaryCode = BinaryCode + "0";
                printBiCode = Integer.parseInt(BinaryCode, 2);

            } else if (arrOfdata[1].equals(".fill")) {
                if (arrOfdata[2].matches("-?(0|[1-9]\\d*)")) {
                    BinaryCode = Integer.toBinaryString(Integer.parseInt(arrOfdata[2]));
                    for (int indexz = BinaryCode.length(); indexz <= 31; indexz++)
                        BinaryCode = "0" + BinaryCode;
                    printBiCode = Integer.parseInt(arrOfdata[2]);
                } else {
                    BinaryCode = Integer.toBinaryString(label.get(arrOfdata[2]));
                    for (int indexz = BinaryCode.length(); indexz <= 31; indexz++)
                        BinaryCode = "0" + BinaryCode;
                    printBiCode = label.get(arrOfdata[2]);
                }
            } else {
                throw new IllegalArgumentException("exit(1) opcode is undefine.");
            }
            macCode[addr] = BinaryCode;
            System.out.println(printBiCode);
            addr++;
        }
        
        Map<String, String> reg = new HashMap<>();
        reg.put("000", "0");
        reg.put("001", "0");
        reg.put("010", "0");
        reg.put("011", "0");
        reg.put("100", "0");
        reg.put("101", "0");
        reg.put("110", "0");
        reg.put("111", "0");

        int PC = 0;
        
        while(!macCode[PC].substring(7, 10).equals("111")){
            if (macCode[PC].substring(7, 10).equals("000")){

            }
            if (macCode[PC].substring(7, 10).equals("001")){

            }
            if (macCode[PC].substring(7, 10).equals("010")){

            }
            if (macCode[PC].substring(7, 10).equals("011")){
            }
            if (macCode[PC].substring(7, 10).equals("100")){

            }
            if (macCode[PC].substring(7, 10).equals("101")){

            }
            if (macCode[PC].substring(7, 10).equals("110")){

            }

            PC++;
        }
        
    }

}
