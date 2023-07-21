package org.example;

import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập số: "); //print thay vì println, nó sẽ in ra, nhưng không xuống dòng

        int tmp = sc.nextInt(); // sc.nextInt() là cách để lấy giá trị từ bàn phím, nó sẽ chờ tới khi chúng ta
        //int tmp = 315;
        if (tmp<10){
            System.out.println("result = " +readUnit(tmp) );

        } else if((tmp>10)&&(tmp<20)){
            System.out.println("result= "+ read20(tmp));
        } else if ((tmp>=20)&&(tmp<100)){
            System.out.println("result= "+ read30(tmp));
        } else {
            System.out.println("result ="+ readHundred(tmp));
        }

    }
    // TH: tmp<10
    public static String readUnit(int tmp){
        String result ="";
            switch (tmp){
            case 1:
                result= "One";
                break;
            case 2:
                result=  "Two";
                break;
            case 3:
                result= "Three";
                break;
            case 4:
                result=  "four";
                break;
            case 5:
                result=  "Five";
                break;

            case 6:
                result=  "Six";
                break;

            case 7:
                result=  "Seven";
                break;

            case 8:
                result=  "Eight";
                break;

            case 9:
                result=  "Nine";
                break;

        }
        return result;
    }
    // TH: tmp <20
    public static String read20(int tmp){
        String result="";
        int rs = tmp%10;
        switch (rs){
            case 0:
                result= "ten";
                break;
            case 1 :
                result="eleven";
                break;
            case 2 :
                result="twelve";
                break;
            case 3 :
                result= "thirteen";
                break;
            case 4 :
                result= "fourteen";
                break;
            case 5 :
                result= "fifteen";
                break;
            case 6 :
                result="sixteen";
                break;
            case 7 :
                result= "seventeen";
                break;
            case 8 :
                result="eighteen";
                break;
            case 9 :
                result="nineteen";
                break;
        }
        return result;
    }
    //Xử lý số hàng chục
    public static String readChuc(int tmp){
        String result="";
        switch (tmp){
            case 2:
                result= "twenty";
            break;
            case 3:
                result= "thirty";
            break;
            case 4:
                result= "forty";
            break;
            case 5:
                result= "fifty";
            break;
            case 6:
                result= "sixty";
            break;
            case 7:
                result= "seventy";
            break;
            case 8:
                result= "eighty";
            break;
            case 9:
                result= "ninety";
            break;
        }
        return result;
    }
//    TH: tmp>=20
    public static String read30(int tmp){
        String result="";
        int donvi = tmp%10;
        int chuc = tmp/10;
        if(donvi==0){
            result=readChuc(chuc);
        } else {
            result= readChuc(chuc)+ readUnit(donvi);
        }
        return result;
    }
//    TH: tmp>=100
    public static String readHundred(int tmp){
        String result= "";
        int tram = tmp/100;
        int flag = tmp%100;
        int chuc = flag/10;
        int donvi = flag%10;
        String hundred = readUnit(tram)+"hundred";
        if (flag==0){
            result= hundred;
        } else if((flag>=10)&&(flag<=19)){
            result = hundred+ read20(flag);
        }else if((flag>=20)){
            result= hundred+ read30(flag);
        }else {
            result = hundred+ readUnit(donvi);
        }
        return result;
    }

}