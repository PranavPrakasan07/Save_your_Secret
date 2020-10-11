package com.example.hencr;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;


public class uisql {

//
//    public void create(String a,String b,String c, String d){
//        try{
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://"+sip+":1433/"+databasename,user,password);
//            Statement st=con.createStatement();
//
//            String ena=aesen.encrypt(a);
//            String enb=aesen.encrypt(b);
//            String enc=aesen.encrypt(c);
//            String end=aesen.encrypt(d);
//
//            System.out.println("a="+ena+"\nb="+enb+"\nc="+enc+"\nd="+end+"\n");
//            String up="CREATE TABLE "+ena+" ("+enb+" VARCHAR(310),"+enc+" VARCHAR(310),"+end+" VARCHAR(310));";
//            System.out.println(up);
//            st.execute("CREATE TABLE "+ a +" ("+ b+" VARCHAR(310),"+ c +" VARCHAR(310),"+ d +" VARCHAR(310));");
//            ResultSet rs=st.executeQuery("SELECT * FROM "+"ena"+";");
//
//            encryptedTable=new JFrame();
//            encryptedTable.setSize(640,220);
//            encryptedTable.setLocation(460,0);
//            JTable t1=new JTable(buildTableModel(rs));
//            JScrollPane sp = new JScrollPane(t1);
//            sp.setBounds(460,10,640,210);
//            encryptedTable.add(sp);
//            encryptedTable.setVisible(true);
//            decryptedTable=new JFrame();
//            decryptedTable.setSize(640,220);
//            decryptedTable.setLocation(460,230);
//            JTable t2=new JTable(builddecryptedTableModel(rs));
//            JScrollPane sp1 = new JScrollPane(t2);
//            sp.setBounds(460,10,640,210);
//            decryptedTable.add(sp1);
//            decryptedTable.setVisible(true);
//
//            con.close();
//        }
//        catch(Exception e)
//        {
//            System.out.println("Error in connection" + e);
//        }
//
//    }
//


//
//    public void displaydata(String tname)
//    {
//        try{
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://"+sip+":1433/"+databasename,user,password);
//            Statement st=con.createStatement();
//            String enh=aesen.encrypt(tname);
//            st.executeQuery("SELECT * FROM '"+enh+"'");
//
//            ResultSet rs = st.getResultSet();
//            encryptedTable=new JFrame();
//            encryptedTable.setSize(640,220);
//            encryptedTable.setLocation(460,0);
//            JTable t1=new JTable(buildTableModel(rs));
//            JScrollPane sp = new JScrollPane(t1);
//            sp.setBounds(460,10,640,210);
//            encryptedTable.add(sp);
//            encryptedTable.setVisible(true);
//            decryptedTable=new JFrame();
//            decryptedTable.setSize(640,220);
//            decryptedTable.setLocation(460,230);
//            JTable t2=new JTable(builddecryptedTableModel(rs));
//            JScrollPane sp1 = new JScrollPane(t2);
//            sp.setBounds(460,10,640,210);
//            decryptedTable.add(sp1);
//            decryptedTable.setVisible(true);
//            con.close();
//
//        }catch(Exception e)
//        {
//            System.out.println("Error in connection" + e);
//        }
//    }
//

    /*----------------------------------==============----------------*/


//
//    public void updatedata(String a,String b,String c,String d,String h){
//        try{
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://"+sip+":1433/"+databasename,user,password);
//            Statement st=con.createStatement();
//            String ena=aesen.encrypt(a);
//            String enc=aesen.encrypt(c);
//            BigInteger enb=BigInteger.ZERO;
//            BigInteger end=BigInteger.ZERO;
//            if(isInteger(b)){ //Change this section later as per string value or id, sap BigInteger token val
//
//                BigInteger b1=new BigInteger(b);
//                enb=paillier.Encryption(b1,r);
//            }else{
//                enb=paillier.EncrypStr(b,r);
//            }
//            if(isInteger(d)){ //Change this section later as per string value or id, sap BigInteger token val
//                System.out.println("In d1");
//                BigInteger d1=new BigInteger(d);
//                end=paillier.Encryption(d1,r);
//            }else{
//                end=paillier.EncrypStr(d,r);
//            }
//            System.out.println(a+enb+c+end);
//            String enh=aesen.encrypt(h);
//            String up="UPDATE '"+enh+"' SET '" +ena+ "'= '"+enb+"' WHERE `"+enc+"' = '"+end+"'";
//
//            st.executeUpdate(up);
//            st.executeQuery("SELECT * FROM `"+enh+"`");
//
//            ResultSet rs = st.getResultSet();
//            encryptedTable=new JFrame();
//            encryptedTable.setSize(640,220);
//            encryptedTable.setLocation(460,0);
//            JTable t1=new JTable(buildTableModel(rs));
//            JScrollPane sp = new JScrollPane(t1);
//            sp.setBounds(460,10,640,210);
//            encryptedTable.add(sp);
//            encryptedTable.setVisible(true);
//            decryptedTable=new JFrame();
//            decryptedTable.setSize(640,220);
//            decryptedTable.setLocation(460,230);
//            JTable t2=new JTable(builddecryptedTableModel(rs));
//            JScrollPane sp1 = new JScrollPane(t2);
//            sp.setBounds(460,10,640,210);
//            decryptedTable.add(sp1);
//            decryptedTable.setVisible(true);
//
//            con.close();
//
//        }catch(Exception e)
//        {
//            System.out.println("Error in connection" + e);
//        } }
//    public static boolean isInteger(String s) {
//        try {
//            Integer.parseInt(s);
//        } catch(NumberFormatException e) {
//            return false;
//        } catch(NullPointerException e) {
//            return false;
//        }
//        // only got here if we didn't return false
//        return true;
//    }
//
    /*------------------------------------*/


//
//    public void houpdatedata(String a,String b,String c,String d,String g,String f,String h){
//        try{
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://"+sip+":1433/"+databasename,user,password);
//            Statement st=con.createStatement();		BigInteger enb=BigInteger.ZERO;
//            BigInteger end=BigInteger.ZERO;
//            String enh=aesen.encrypt(h);
//            //hocol1,ndata,col2,odata,sym,data,n4
//            if(isInteger(f)){ //Change this section later as per string value or id, sap BigInteger token val
//                BigInteger b1=new BigInteger(f);
//                enb=paillier.Encryption(b1,r);
//            }else{
//                enb=paillier.EncrypStr(b,r);
//            }
//            if(isInteger(d)){ //Change this section later as per string value or id, sap BigInteger token val
//                BigInteger d1=new BigInteger(d);
//                end=paillier.Encryption(d1,r);
//            }else{
//                end=paillier.EncrypStr(d,r);
//            }
//
//            String up=null;
//            ResultSet rs=null;
//            String enc=aesen.encrypt(c);
//            String ena=aesen.encrypt(a);
//            if(g.equals("+")){
//                String u="SELECT * FROM `"+enh+"` WHERE `"+enc+"` ='"+end+"'";
//                st.executeQuery(u);
//                rs = st.getResultSet();
//                BigInteger m=BigInteger.ZERO;
//                while(rs.next()){
//                    BigInteger em1=new BigInteger(rs.getString(1));
//                    m=em1.multiply(enb).mod(paillier.nsquare);}
//                up="UPDATE `"+enh+"` SET `" +ena+ "`= '"+m+"' WHERE `"+enc+"` = '"+end+"'";
//
//            }else if(g.equals("*")){
//                String u="SELECT * FROM `"+enh+"` WHERE `"+enc+"` ='"+end+"'";
//                st.executeQuery(u);
//                rs = st.getResultSet();
//                BigInteger m=BigInteger.ZERO;
//                while(rs.next()){
//                    BigInteger em1=new BigInteger(rs.getString(1));
//                    m=em1.modPow(new BigInteger(f), paillier.nsquare);
//                    System.out.println(m);
//                    System.out.println("Dec:"+paillier.Decryption(m).toString());}
//                up="UPDATE `"+enh+"` SET `" +ena+ "`= '"+m+"' WHERE `"+enc+"` = '"+end+"'";
//            }
//            st.executeUpdate(up);
//            st.executeQuery("SELECT * FROM `"+enh+"`");
//
//            rs = st.getResultSet();
//            encryptedTable=new JFrame();
//            encryptedTable.setSize(640,220);
//            encryptedTable.setLocation(460,0);
//            JTable t1=new JTable(buildTableModel(rs));
//            JScrollPane sp = new JScrollPane(t1);
//            sp.setBounds(460,10,640,210);
//            encryptedTable.add(sp);
//            encryptedTable.setVisible(true);
//            decryptedTable=new JFrame();
//            decryptedTable.setSize(640,220);
//            decryptedTable.setLocation(460,230);
//            JTable t2=new JTable(builddecryptedTableModel(rs));
//            JScrollPane sp1 = new JScrollPane(t2);
//            sp.setBounds(460,10,640,210);
//            decryptedTable.add(sp1);
//            decryptedTable.setVisible(true);
//            con.close();
//
//        }catch(Exception e)
//        {
//            System.out.println("Error in connection" + e);
//        } }
//
}
