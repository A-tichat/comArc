package app;

public class complier {
    public String[] instruction(StringBuffer ins) {
            boolean islabel = false;
            String []arr = new String[5];
            arr[0] = "";
            int isfill = 0;
            for (int j=0;j<ins.length();j++){
                if (Character.isLetter(ins.charAt(j)) || Character.isDigit(ins.charAt(j))){
                    if (j==0){
                        islabel = true;
                    }
                    String label0 = "";
                    if (j>1 && ins.charAt(j-1) == '.' && ins.charAt(j) == 'f' && ins.charAt(++j) == 'i' && ins.charAt(++j) == 'l' && ins.charAt(++j) == 'l'){
                        arr[1] = ".fill";
                        isfill++;
                        continue;
                    }else{
                        while(ins.charAt(j)!='\t' && ins.charAt(j)!=' '){
                            if (Character.isDigit(ins.charAt(j)) && ins.charAt(j-1) == '-'){
                                label0 += ins.charAt(j-1);
                            }
                            label0 += ins.charAt(j++);
                        }
                    }
                    if (label0.isEmpty()) continue;
                    if(islabel){
                        arr[0] = label0;
                        islabel = false;
                    }else if (label0.equals("lw") || label0.equals("add") || label0.equals("beq") || label0.equals("noop") || label0.equals("halt")){
                        arr[1] = label0;
                        if (label0.equals("noop") || label0.equals("halt")) {
                            for (int x=0;x<5;x++){
                                arr[x] = (arr[x] == null) ? "" :arr[x];
                            }
                            break;
                        }
                    }else if (arr[2] == null){
                        arr[2] = label0;
                        isfill++;
                    }else if (arr[3] == null){
                        arr[3] = label0;
                    }else if (arr[4] == null){
                        arr[4] = label0;
                    }else{
                        break;
                    }
                    if (isfill ==2) break;
                }
            }
            for (int k=0;k<5;k++){
                arr[k] = (arr[k] == null) ? "" :arr[k];
            }
            

        return arr;
    }
}