package app;

import java.io.*;
import java.util.*;
import app.binary;

public class Assembler {

    public static void main(String[] args) {
        binary binary = new binary();
        String[] data = new String[100]; // read max 100 line
        int addr = 0;// addr of line
        Map<String, Integer> label = new HashMap<String, Integer>();
        //label.get(arrOfdata[?]) to get address of label
        //mem[address] to get value that store in mem

        // READ ASSEMBLEY FORM INPUT FILE MUST'T READ
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

        // TO STORE LABEL OF EACH LINE
        String[] mem = new String[data.length];
        int r = 0;
        while (data[r] != null) {
            String[] arrOfdatat = data[r].split("	");
            if (label.containsKey(arrOfdatat[0])) {
                throw new IllegalArgumentException("exit(1) : Label is duplicate!"); // check same label
            }

            if (!arrOfdatat[0].equals("")) {
                if (arrOfdatat[0].length() > 6 || arrOfdatat[0].substring(0, 1).matches("-?(0|[1-9]\\d*)")) 
                    // check label size > 6? and start with number?
                    throw new IllegalArgumentException("exit(1) : Label is undefined!");
            }
            
            if (arrOfdatat[1].equals(".fill")) {
                label.put(arrOfdatat[0], r);
                if (arrOfdatat[2].matches("-?(0|[1-9]\\d*)")) {
                    mem[r] = binary.create(Integer.parseInt(arrOfdatat[2]), 32);
                } else {
                    mem[r] = binary.create(label.get(arrOfdatat[2]), 32);
                }
            }else if (!arrOfdatat[0].equals("")){
                label.put(arrOfdatat[0], r); // set label to addr
            }
            r++;
        }
        r=0;

        // TO CREATE MACHINE CODE
        addr = 0;
        while (data[addr] != null) {
            String[] arrOfdata = data[addr].split("	"); // feild of instruction
            String BiCode = "0000000"; // machinecode binary bit

            if (arrOfdata[1].equals("add")) {
                BiCode += "000";
                BiCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BiCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                BiCode += binary.create(0, 13);
                BiCode += binary.create(Integer.parseInt(arrOfdata[4]), 3);
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals("nand")) {
                BiCode += "001";
                BiCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BiCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                BiCode += binary.create(0, 13);
                BiCode += binary.create(Integer.parseInt(arrOfdata[4]), 3);
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals("lw")) {
                BiCode += "010";
                //DETEC ERROR
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    if (Integer.parseInt(arrOfdata[4]) > 32767 || Integer.parseInt(arrOfdata[4]) < -32768) { 
                        // offsetfield >16 bit?
                        throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                    }
                } else {
                    if (label.get(arrOfdata[4]) > 32767 || (label.get(arrOfdata[4]) < -32768)) {
                        throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                    }
                }

                BiCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BiCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    BiCode += binary.create(Integer.parseInt(arrOfdata[4]), 16);
                } else {
                    BiCode += binary.create(label.get(arrOfdata[4]), 16);
                }
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals("sw")) {
                BiCode += "011";
                //DETEC ERROR
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    if (Integer.parseInt(arrOfdata[4]) > 32767 || Integer.parseInt(arrOfdata[4]) < -32768) { 
                        // offsetfield >16 bit?
                        throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                    }
                } else {
                    if (label.get(arrOfdata[4]) > 32767 || (label.get(arrOfdata[4]) < -32768)) {
                        throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                    }
                }

                BiCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BiCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    BiCode += binary.create(Integer.parseInt(arrOfdata[4]), 16);
                } else {
                    BiCode += binary.create(label.get(arrOfdata[4]), 16);
                }
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals("beq")) {
                BiCode += "100";
                //DETEC ERROR
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    if (Integer.parseInt(arrOfdata[4]) > 32767 || Integer.parseInt(arrOfdata[4]) < -32768) { 
                        // offsetfield >16 bit?
                        throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                    }
                } else {
                    if (label.get(arrOfdata[4]) > 32767 || (label.get(arrOfdata[4]) < -32768)) {
                        throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                    }
                }

                //TO DO BEQ
                BiCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BiCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                
                int dst = 0;
                if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                    dst = Integer.parseInt(arrOfdata[4]);
                    BiCode += binary.create(dst, 16);
                } else {
                    dst = label.get(arrOfdata[4]);
                    BiCode += binary.create(dst-addr-1, 16);
                }
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals("jalr")) {
                BiCode += "101";
                BiCode += binary.create(Integer.parseInt(arrOfdata[2]), 3);
                BiCode += binary.create(Integer.parseInt(arrOfdata[3]), 3);
                BiCode += binary.create(0, 16);
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals("halt")) {
                BiCode += "110";
                BiCode += binary.create(0, 22);
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals("noop")) {
                BiCode += "111";
                BiCode += binary.create(0, 22);
                mem[addr] = BiCode;
            } else if (arrOfdata[1].equals(".fill")) {
            } else {
                throw new IllegalArgumentException("exit(1) opcode is undefine.");
            }
            if (mem[addr].substring(0, 1).equals("1")) {
                StringBuffer p = new StringBuffer(mem[addr]);
                System.out.println("Memory[" + addr + "]=-" + Integer.parseInt(binary.funcTwoCom(p), 2));
                //System.out.println("Memory[" + addr + "]=-" + binary.funcTwoCom(p));
            }else {
                System.out.println("Memory[" + addr + "]=" + Integer.parseInt(mem[addr], 2));
                //System.out.println("Memory[" + addr + "]=" + mem[addr]);
            } 
            addr++;
        }
        System.out.println("\n");

        //BEGIN Simulator
        int[] reg = new int[10];
        for(int i=0;i<=9;i++) reg[i]=0; //initialize

        int endf = addr;
        int PC = 0, ins=0;
        boolean jum = false;
        reg[3] = -1;
        reg[4] = -2;
        while (PC>=0 && PC<endf) {
            jum = false;
            if (ins>50){
                break;
            }
            binary.printstate(mem, reg, PC, endf);
            
            if (mem[PC].substring(7, 10).equals("000")) {
                //TO DO ADD
                reg[Integer.parseInt(mem[PC].substring(29,32))] = reg[Integer.parseInt(mem[PC].substring(10,13))]+reg[Integer.parseInt(mem[PC].substring(13,16))];
            }else if (mem[PC].substring(7, 10).equals("001")) {
                //TO DO NAND
                reg[Integer.parseInt(mem[PC].substring(29),2)] = binary.nand(reg[Integer.parseInt(mem[PC].substring(10,13),2)], reg[Integer.parseInt(mem[PC].substring(13,16),2)]);
            }
            else if (mem[PC].substring(7, 10).equals("010")) {
                //TO DO LOAD
            }
            else if (mem[PC].substring(7, 10).equals("011")) {
                //TO DO STORE
            }
            else if (mem[PC].substring(7, 10).equals("100")) {
                //TO DO BEQ
                if(reg[Integer.parseInt(mem[PC].substring(10,13),2)]  == reg[Integer.parseInt(mem[PC].substring(13,16),2)]){
                    if (mem[PC].substring(16, 17).equals("1")){
                        StringBuffer p = new StringBuffer(mem[PC].substring(16));
                        PC = PC+1-Integer.parseInt(binary.funcTwoCom(p), 2);
                    }else{
                        PC = PC+1+Integer.parseInt(mem[PC].substring(16),2);
                        System.out.println("check PC: "+PC);
                    }
                     jum = true;
                }else{
                    jum = false;
                }
            }
            else if (mem[PC].substring(7, 10).equals("101")) {
                //TO DO jalr
                if(mem[PC].substring(10,13).equals(mem[PC].substring(13,16))){
                   int temp = PC+1;
                    PC = reg[Integer.parseInt(mem[PC].substring(10,13),2)];
                    reg[Integer.parseInt(mem[PC].substring(13,16),2)] = temp;
                }
                else{
                    reg[Integer.parseInt(mem[PC].substring(13,16),2)]=PC+1;
                PC=reg[Integer.parseInt(mem[PC].substring(10,13),2)];
                }
                jum=true;
            }
            else if (mem[PC].substring(7, 10).equals("110")) {
                //TO DO Halt
                System.out.println("machine halted");
                System.out.println("total of "+ins+" instructions executed"); 
                System.out.println("final state of machine:");
                PC++;
                break; 
            }
            else if (mem[PC].substring(7, 10).equals("111")) {
            }
            if (!jum) {
                PC++; 
                jum=false;
            }
            ins++;
        }
        System.out.print("\n");
        binary.printstate(mem, reg, PC, endf);
        
    }

}
