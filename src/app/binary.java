package app;

import java.util.ArrayList;

public class binary {

    public void printstate(ArrayList<StringBuffer> mem,int[] reg,int cur,int endfile) {
        System.out.print("@@@\nstate:\n");
        System.out.println("    PC "+cur);
        System.out.println("    memory:");
            for (int i=0;i<endfile;i++){
                    System.out.println("        mem[" + i + "] " + biToDec(new StringBuffer(mem.get(i)), 2));
            }
            System.out.println("    registers:");
            for (int i=0;i<8;i++){
                System.out.println("        reg[" + i + "] " + reg[i]);
            }
            System.out.println("end state:\n");
    }

    public StringBuffer funcTwoCom(StringBuffer str) {
        int n = str.length();
        int i;
        for (i = n - 1; i >= 0; i--)
            if (str.charAt(i) == '1')
                break;
        for (int k = i - 1; k >= 0; k--) {
            if (str.charAt(k) == '1')
                str.replace(k, k + 1, "0");
            else
                str.replace(k, k + 1, "1");
        }
        return str;
    }

    public StringBuffer create(int dec, int l) {
        StringBuffer bi = new StringBuffer("");
        boolean flag = false;
        if (dec < 0) {
            dec = 0-dec;
            flag = true;
        }
        if (dec == -2147483648){
            bi.append("10000000000000000000000000000000");
            return bi;
        }
        for (int i = 0;i<l;i++) {
            if (dec == 0) {
                bi.insert(0, "0");
            } else if (dec == 1) {
                bi.insert(0, "1");
            } else {
                bi.insert(0,dec%2);
            }
            dec = dec/2;
        }
        if (flag)
            bi = funcTwoCom(bi);
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
        return biToDec(new StringBuffer(strReturn), 2);
    }

    public int add(int regA, int regB) {
        StringBuffer addend = new StringBuffer(create(regA, 32));
        StringBuffer adder = new StringBuffer(create(regB, 32));
        char carry = '0';
        String result = "";
        for (int i=31;i>=0;i--){
            if(addend.charAt(i) == '0'){
                if (adder.charAt(i) == '0'){//0+0
                    if (carry == '1'){
                        carry = '0';
                        result = '1'+result;
                    }else{
                        carry = '0';
                        result = '0'+result;
                    }
                }else{// 0+1
                    if (carry == '1'){
                        carry = '1';
                        result = '0'+result;
                    }else{
                        carry = '0';
                        result = '1'+result;
                    }
                }
            }else{//addend.charAt(i) == '1'
                if (adder.charAt(i) == '0'){//1+0
                    if (carry == '1'){
                        carry = '1';
                        result = '0'+result;
                    }else{
                        carry = '0';
                        result = '1'+result;
                    }
                }else{//1+1
                    if (carry == '1'){
                        carry = '1';
                        result = '1'+result;
                    }else{
                        carry = '1';
                        result = '0'+result;
                    }
                }
            }
        }
        if (result.isEmpty()){
            throw new IllegalArgumentException("String is empty!");
        }
        return biToDec(new StringBuffer(result), 2);
    }

    public int biToDec(StringBuffer bi_32, int base) {
        if (base != 2){
            return Integer.valueOf(bi_32.toString());
        }
        
        int bit = bi_32.length()-1;
        int neg = 1;
        StringBuffer strBitBuffer;
        if (bi_32.charAt(0) == '1' && bi_32.length()>=4) {
            if (bi_32.toString().equals("10000000000000000000000000000000"))
                return 2147483647+1;
            neg = -1;
            strBitBuffer = new StringBuffer(funcTwoCom(bi_32));
        }else{
            strBitBuffer = new StringBuffer(bi_32);
        }
        int decimal = 0;
        int[] arrDec = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536,
             131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 
             134217728, 268435456, 536870912, 1073741824, 0};
        for (int i=0;i<=bit;i++) {
            if (strBitBuffer.charAt(bit-i) == '1') {
                decimal += arrDec[i];
            }
        }
        return decimal*neg;
    }
}
