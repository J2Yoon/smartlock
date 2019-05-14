package com.example.test1;

/**
 * Created by 또리또리 on 2019-03-21.
 */

public class subNumCheck {
    public String subcheck(String subnum){
        String subnumber="";
        switch (subnum){
            case "1001":
                subnumber="1호선";
                break;
            case "1002":
                subnumber="2호선";
                break;
            case "1003":
                subnumber="3호선";
                break;
            case "1004":
                subnumber="4호선";
                break;
            case "1005":
                subnumber="5호선";
                break;
            case "1006":
                subnumber="6호선";
                break;
            case "1007":
                subnumber="7호선";
                break;
            case "1008":
                subnumber="8호선";
                break;
            case "1009":
                subnumber="9호선";
                break;
            case "1075":
                subnumber="분당선";
                break;
            case "1063":
                subnumber="경의중앙";
                break;
            case "1067":
                subnumber="경춘선";
                break;
            case "1077":
                subnumber="신분당선";
                break;
            case "1065":
                subnumber="공항철도";
                break;
        }
        return subnumber;
    }

    public String revsubcheck(String subnum){
        String subnumber="";
        switch (subnum){
            case "1호선":
                subnumber="1001";
                break;
            case "2호선":
                subnumber="1002";
                break;
            case "3호선":
                subnumber="1003";
                break;
            case "4호선":
                subnumber="1004";
                break;
            case "5호선":
                subnumber="1005";
                break;
            case "6호선":
                subnumber="1006";
                break;
            case "7호선":
                subnumber="1007";
                break;
            case "8호선":
                subnumber="1008";
                break;
            case "9호선":
                subnumber="1009";
                break;
            case "분당선":
                subnumber="1075";
                break;
            case "경의중앙":
                subnumber="1063";
                break;
            case "경춘선":
                subnumber="1067";
                break;
            case "신분당선":
                subnumber="1077";
                break;
            case "공항철도":
                subnumber="1065";
                break;
        }
        return subnumber;
    }
}
