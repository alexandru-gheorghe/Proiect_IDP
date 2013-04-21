/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author alex
 */
public class Constants {
   
    public static final String IP		= "127.0.0.1";
    public static final int PORT		= 30000;
    
    public static final byte Start          =    127;
    public static final byte End            =    23;
    public static final int  BUF_SIZE       =    1024;
    public static final int  LOGIN          =    1;
    public static final int  OFFREQEUEST    =    2;
    public static final int  OFFSERVICE     =    3;
    public static final int  CANCELREQ      =    4;
    public static final int  OFFACCEPT      =    5;
    public static final int  OFFEXCEED      =    6;
    public static final int  OFFREFUSED     =    7;
    public static final int  LOGINACCEPT    =    8;
    public static final int  DROPREQ        =    9;
    public static final int  OFFMAKE        =    10;
    public static final int  ADDCON         =    12;
    public static final int  OFFDROP        =    13;
    public static final String PROD         =    "Producator";
    public static final String CON          =    "Consumator";
    public static final int EXSTATE         =    14;
    public static final int ACTSTATE        =    15;
    public static final int OFFACCOTHER     =    16;
    public static final int INVALACC        =    17;
    public static final int READOP          =    18;
    public static final int WRITEOP         =    19;
}
