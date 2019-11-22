package app;

import java.io.*;
import java.util.*;
import app.binary;
import app.split;

public class Assembler {

    public static void main(String[] args) {
        binary binary = new binary();
        split data0 = new split();
        ArrayList<String> data = new ArrayList<String>();
        int addr = 0;// addr of line
        Map<String, Integer> label = new HashMap<String, Integer>();
        // label.get(arrOfdata[?]) to get address of label
        // mem[address] to get value that store in mem

        // READ ASSEMBLEY FORM INPUT FILE
        try {
            File myObj = new File("assembly/srl.txt");
            Scanner myReader = new Scanner(myObj);
            addr = 0;
            while (myReader.hasNextLine()) {
                data.add(myReader.nextLine());
                addr++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid input file.");
            e.printStackTrace();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }

        // TO STORE LABEL OF EACH LINE
        ArrayList<StringBuffer> mem = new ArrayList<StringBuffer>();
        for (int index = 0; index < data.size(); index++)
            mem.add(new StringBuffer(""));// initial memory
        int r = 0;
        int exten = 0;
        while (r + exten < data.size()) {
            String[] label0 = data0.instruction(new StringBuffer(data.get(r + exten)));
            while (label0[1].isEmpty()) {
                label0 = data0.instruction(new StringBuffer(data.get(r + exten)));
                exten++;
            }
            if (!label0[2].matches("-?(0|[1-9]\\d*)") || !label0[3].matches("-?(0|[1-9]\\d*)")) {
                if (label0[2].equals(".fill") || label0[2].isEmpty() || label0[3].isEmpty()) {

                } else {
                    throw new IllegalArgumentException("exit(1) : Assembly code is error!");
                }
            }
            if (label.containsKey(label0[0])) {
                throw new IllegalArgumentException("exit(1) : Label is duplicate!"); // check same label
            }

            if (!label0[0].equals("")) {
                if (label0[0].length() > 6 || label0[0].substring(0, 1).matches("-?(0|[1-9]\\d*)"))
                    // check label size > 6? and start with number?
                    throw new IllegalArgumentException("exit(1) : Label is undefined!");
            }

            if (label0[1].equals(".fill")) {
                label.put(label0[0], r);
                if (label0[2].matches("-?(0|[1-9]\\d*)")) {
                    mem.set(r, binary.create(Integer.valueOf(label0[2]), 32));
                } else {
                    mem.set(r, binary.create(label.get(label0[2]), 32));
                }
            } else if (!label0[0].equals("")) {
                label.put(label0[0], r); // set label to addr
            }
            r++;
        }
        r = 0;

        // TO CREATE MACHINE CODE
        addr = 0;
        exten = 0;
        try {
            File myObj = new File("output/machineCode.txt");
            if (!myObj.createNewFile()) {
                myObj.delete();
                myObj.createNewFile();
                System.out.println("File already exists.");
            }

            FileWriter myWriter = new FileWriter("output/machineCode.txt");
            while (addr + exten < data.size()) {
                String[] arrOfdata = data0.instruction(new StringBuffer(data.get(addr + exten)));

                while (arrOfdata[1].isEmpty()) {
                    arrOfdata = data0.instruction(new StringBuffer(data.get(addr + exten)));
                    exten++;
                }
                StringBuffer BiCode = new StringBuffer("0000000"); // machinecode binary bit

                if (arrOfdata[1].equals("add")) {
                    BiCode.append("000");
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[2], 10), 3));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[3], 10), 3));
                    BiCode.append(binary.create(0, 13));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[4], 10), 3));
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals("nand")) {
                    BiCode.append( "001");
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[2], 10), 3));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[3], 10), 3));
                    BiCode.append(binary.create(0, 13));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[4], 10), 3));
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals("lw")) {
                    BiCode.append("010");
                    // DETEC ERROR
                    if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                        if (Integer.valueOf(arrOfdata[4], 10) > 32767
                                || Integer.valueOf(arrOfdata[4], 10) < -32768) {
                            // offsetfield >16 bit?
                            throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                        }
                    } else {
                        if (label.get(arrOfdata[4]) > 32767 || (label.get(arrOfdata[4]) < -32768)) {
                            throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                        }
                    }

                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[2], 10), 3));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[3], 10), 3));
                    if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                        BiCode.append(binary.create(Integer.valueOf(arrOfdata[4], 10), 16));
                    } else {
                        BiCode.append(binary.create(label.get(arrOfdata[4]), 16));
                    }
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals("sw")) {
                    BiCode.append("011");
                    // DETEC ERROR
                    if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                        if (Integer.valueOf(arrOfdata[4], 10) > 32767
                                || Integer.valueOf(arrOfdata[4], 10) < -32768) {
                            // offsetfield >16 bit?
                            throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                        }
                    } else {
                        if (label.get(arrOfdata[4]) > 32767 || (label.get(arrOfdata[4]) < -32768)) {
                            throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                        }
                    }

                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[2], 10), 3));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[3], 10), 3));
                    if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                        BiCode.append(binary.create(Integer.valueOf(arrOfdata[4], 10), 16));
                    } else {
                        BiCode.append(binary.create(label.get(arrOfdata[4]), 16));
                    }
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals("beq")) {
                    BiCode.append("100");
                    // DETEC ERROR
                    if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                        if (Integer.valueOf(arrOfdata[4], 10) > 32767
                                || Integer.valueOf(arrOfdata[4], 10) < -32768) {
                            // offsetfield >16 bit?
                            throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                        }
                    } else {
                        if (label.get(arrOfdata[4]) > 32767 || (label.get(arrOfdata[4]) < -32768)) {
                            throw new IllegalArgumentException("exit(1) : Offsetfield more then 16 bit!");
                        }
                    }

                    // TO DO BEQ
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[2], 10), 3));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[3], 10), 3));

                    int dst = 0;
                    if (arrOfdata[4].matches("-?(0|[1-9]\\d*)")) {
                        dst = Integer.valueOf(arrOfdata[4], 10);
                        BiCode.append(binary.create(dst, 16));
                    } else {
                        dst = label.get(arrOfdata[4]);
                        BiCode.append(binary.create(dst - addr - 1, 16));
                    }
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals("jalr")) {
                    BiCode.append("101");
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[2], 10), 3));
                    BiCode.append(binary.create(Integer.valueOf(arrOfdata[3], 10), 3));
                    BiCode.append(binary.create(0, 16));
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals("halt")) {
                    BiCode.append("110");
                    BiCode.append(binary.create(0, 22));
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals("noop")) {
                    BiCode.append("111");
                    BiCode.append(binary.create(0, 22));
                    mem.set(addr, BiCode);
                } else if (arrOfdata[1].equals(".fill")) {
                } else {
                    throw new IllegalArgumentException("exit(1) opcode is undefine.");
                }
                String wr = "Memory[" + addr + "]=" + binary.biToDec(new StringBuffer(mem.get(addr)), 2);
                System.out.println(wr);
                myWriter.write(wr + "\n");
                // System.out.println("Memory[" + addr + "]=" + mem.get(addr));
                addr++;
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("\n");

        // BEGIN Simulator
        int[] reg = new int[8];
        for (int i = 0; i < 8; i++)
            reg[i] = 0; // initialize

        int endf = addr;
        int PC = 0, ins = 0;
        boolean jum = false;
        while (PC < endf) {
            reg[0] = 0;
            if (PC < 0 || mem.size() > 200)
                throw new IllegalArgumentException("exit(1) infiniteloop.");
            jum = false;
            binary.printstate(mem, reg, PC, mem.size());

            if (mem.get(PC).substring(7, 10).equals("000")) {
                // TO DO ADD
                reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(29)), 2)] = binary.add(
                        reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(10, 13)), 2)],
                        reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(13, 16)), 2)]);
            } else if (mem.get(PC).substring(7, 10).equals("001")) {
                // TO DO NAND
                reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(29)), 2)] = binary.nand(
                        reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(10, 13)), 2)],
                        reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(13, 16)), 2)]);
            } else if (mem.get(PC).substring(7, 10).equals("010")) {
                // TO DO LOAD
                int memAddr = binary.biToDec(new StringBuffer(mem.get(PC).substring(16)), 2)
                        + reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(10, 13)), 2)];
                reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(13, 16)), 2)] = binary.biToDec(new StringBuffer(mem.get(memAddr)),
                        2);
            } else if (mem.get(PC).substring(7, 10).equals("011")) {
                // TO DO STORE
                int memAddr = binary.biToDec(new StringBuffer(mem.get(PC).substring(16)), 2)
                        + reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(10, 13)), 2)];
                if (memAddr >= mem.size()) {
                    while (memAddr >= mem.size()) {
                        mem.add(new StringBuffer("0"));
                    }
                }
                mem.set(memAddr, binary.create(reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(13, 16)), 2)], 32));
            } else if (mem.get(PC).substring(7, 10).equals("100")) {
                // TO DO BEQ
                if (reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(10, 13)), 2)] == reg[binary
                        .biToDec(new StringBuffer(mem.get(PC).substring(13, 16)), 2)]) {
                    if (mem.get(PC).substring(16, 17).equals("1")) {
                        StringBuffer p = new StringBuffer(mem.get(PC).substring(16));
                        PC = PC + 1 - binary.biToDec(new StringBuffer(binary.funcTwoCom(p)), 2);
                    } else {
                        PC = PC + 1 + binary.biToDec(new StringBuffer(mem.get(PC).substring(16)), 2);
                    }
                    jum = true;
                } else {
                    jum = false;
                }
            } else if (mem.get(PC).substring(7, 10).equals("101")) {
                // TO DO jalr
                if (mem.get(PC).substring(10, 13).equals(mem.get(PC).substring(13, 16))) {
                    int temp = PC + 1;
                    PC = reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(10, 13)), 2)];
                    reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(13, 16)), 2)] = temp;
                } else {
                    reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(13, 16)), 2)] = PC + 1;
                    PC = reg[binary.biToDec(new StringBuffer(mem.get(PC).substring(10, 13)), 2)];
                }
                jum = true;
            } else if (mem.get(PC).substring(7, 10).equals("110")) {
                // TO DO Halt
                PC++;
                ins++;
                System.out.println("machine halted");
                System.out.println("total of " + ins + " instructions executed");
                System.out.println("final state of machine:");
                break;
            } else if (mem.get(PC).substring(7, 10).equals("111")) {
            }
            if (!jum) {
                PC++;
                jum = false;
            }
            ins++;
        }
        System.out.print("\n");
        binary.printstate(mem, reg, PC, mem.size());
        System.out.println(
                ("--------------------------------\nProcess exited with value 0\nPress any key to continue . . ."));
    }

}
