package app;

public class binary {
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
    public void printstate(String[] mem,int[] reg,int addr) {
        System.out.println("    memory:");
            for (int i=0;i<addr;i++){
                if (mem[i].substring(0, 1).equals("1")) {
                    StringBuffer p = new StringBuffer(mem[i]);
                    System.out.println("        mem[" + i + "] -" + Integer.parseInt(funcTwoCom(p), 2));
                }else {
                    System.out.println("        mem[" + i + "] " + Integer.parseInt(mem[i], 2));
                }
            }
            System.out.println("    registers:");
            for (int i=0;i<=9;i++){
                System.out.println("        reg[" + i + "] " + reg[i]);
            }
            System.out.println("end state:\n");
    }
}
