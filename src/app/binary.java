package app;

import java.util.ArrayList;

public class binary {

    public void printstate(ArrayList<String> mem,int[] reg,int cur,int endfile) {
        System.out.print("@@@\nstate:\n");
        System.out.println("    PC "+cur);
        System.out.println("    memory:");
            for (int i=0;i<endfile;i++){
                    System.out.println("        mem[" + i + "] " + binaryToDecimal(mem.get(i), 2));
            }
            System.out.println("    registers:");
            for (int i=0;i<8;i++){
                System.out.println("        reg[" + i + "] " + reg[i]);
            }
            System.out.println("end state:\n");
    }

    public String funcTwoCom(StringBuffer str) {
        int n = str.length();

        int i;
        for (i = n - 1; i >= 0; i--)
            if (str.charAt(i) == '1')
                break;

        if (i == -1)
            return "1" + str;

        for (int k = i - 1; k >= 0; k--) {
            if (str.charAt(k) == '1')
                str.replace(k, k + 1, "0");
            else
                str.replace(k, k + 1, "1");
        }

        return str.toString();
    }

    public String create(int dec, int l) {
        String bi = "";
        int i = 0;
        boolean flag = false;
        if (dec < 0) {
            dec = 0-dec;
            flag = true;
        }
        while (i < l) {
            if (dec == 0) {
                bi = "0" + bi;
                dec = dec / 2;
            } else if (dec == 1) {
                bi = "1" + bi;
                dec = dec / 2;
            } else {
                bi = Integer.toString(dec % 2) + bi;
                dec = dec / 2;
            }
            i++;
        }
        if (flag) {
            bi = funcTwoCom(new StringBuffer(bi));
        }
        return bi;
    }

    public int nand(int l, int r){
        StringBuffer str1Buffer = new StringBuffer(create(l, 32));
        StringBuffer str2Buffer = new StringBuffer(create(r, 32));
        String strReturn = "";
        for (int k = 0; k < 32; k++) {
            if (str1Buffer.charAt(k) == '1'){
                if (str2Buffer.charAt(k) == '1'){
                    strReturn += '0';
                }else{strReturn += '1';}
            }else{
                if (str2Buffer.charAt(k) == '1'){
                    strReturn += '1';
                }else{strReturn += '1';}
            }
        }
        return binaryToDecimal(strReturn, 2);
    }

    public int binaryToDecimal(String str, int base) {
        if (base != 2){
            return Integer.parseInt(str);
        }
        int bit = str.length()-1;
        StringBuffer strBitBuffer = new StringBuffer(str);
        int decimal = 0;
        if (strBitBuffer.charAt(0) == '1' && str.length()>3) {
            strBitBuffer = new StringBuffer(funcTwoCom(strBitBuffer));
            for (int i = 0; i <= bit; i++) {
                if (strBitBuffer.charAt(i) == '1') {
                    decimal += Math.pow(2, bit-i);
                }
            }
            return decimal*-1;
        }else{
            for (int i = 0; i <= bit; i++) {
                if (strBitBuffer.charAt(i) == '1') {
                    decimal += Math.pow(2, bit-i);
                }
            }
        }
        return decimal;
    }
}
