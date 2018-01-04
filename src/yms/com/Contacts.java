package yms.com;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Contacts {
    private static final String URL = "jdbc:mysql://localhost:3306/Data2017";
    private static final String USER = "root";
    private static final String PASS = "123456";

    private Node head;//List header
    private int length;

    public Contacts() {
        try {
            loadData();
        }catch (Exception e){
            e.printStackTrace();
        }
        this.length = 0;
    }

    /*
            @per Per indicate incoming data
            @explain:method for add Element
         */
    public void addNode(Person per){
        Node newnode = new Node(per);
        if(head == null){ //If the head is empty, the instance of it, to avoid NULLPointerException.
            head = newnode;
            this.length++;
            return;
        }
        Node tmp = head;
        while(tmp.next != null){
            tmp = tmp.next;
        }
        tmp.next = newnode;
        this.length++;
    }
    /*
        @explain:This function enters the data load function
     */
    public void loadData() throws Exception{
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
            String sql = "select * from Contacts";

            conn = DriverManager.getConnection(URL,USER,PASS);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                Person per = new Person(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5));
                addNode(per);
            }
        rs.close();
        pstmt.close();
        conn.close();
        chooseSort();
    }

    /*
        @explain:The method is to sort the Node objects.
     */
    public void chooseSort(){
        Node curNode = head;
        Node nextNode = null;
        Person tmpPer = null;

        while (curNode != null){
            nextNode = curNode.next;
            while (nextNode != null) {
                if (curNode.getData().getName().toLowerCase().compareTo(nextNode.getData().getName().toLowerCase()) > 0) {
                    tmpPer = curNode.getData();
                    curNode.setData(nextNode.getData());
                    nextNode.setData(tmpPer);
                }
                nextNode = nextNode.next;
            }
            curNode = curNode.next;
        }
    }

    /*
        @explain:The function of this method is to print out all contact name information.
        @tmp: A temp variable
     */
    public void printInfo(){
        Node tmp = head;
        char ch1,ch2;
        ch1 = ch2 = '0';
        while(tmp != null){
            ch1 = tmp.getData().getName().toUpperCase().charAt(0);
            if(ch1 != ch2){
                int let1 = (int)ch1;
                if(let1>=97&&let1<=122){
                    let1 = let1 - 32;
                }
                System.out.println((char)let1+":");
            }
            System.out.println("          "+tmp.getData().getName());
            tmp = tmp.next;
            ch2 = ch1;
        }
    }

    public void userIndex(){
        System.out.println("\n                My Friendly"                 );
        System.out.println("--------------------------------------------");
        System.out.println("----------|1.Search for you want!|----------");
        System.out.println("--------------------------------------------");
        System.out.println("       2.Modify    3.Addition    4.Delete   ");
        System.out.println("--------------------------------------------");
        //System.out.println("          "+"");
        printInfo();
        System.out.println("--------------------------------------------");
    }

    /*
       @explain:This is the home page.
     */
    public void programRuning(){
        Scanner sc = new Scanner(System.in);
        while(true){
            userIndex();
            String cmd = sc.nextLine();
            cmdAnalyzer(cmd);
        }
    }

    /*
        @explain:This is a core part of the program, it is a command parser,
         it will parse all the instructions you enter and return to you want.

     */
     public void cmdAnalyzer(String cmd) {
        Scanner sc = new Scanner(System.in);
        final char SEARCH = 1;
         final char MODIFY = 2;
         final char ADD = 3;
         final char DLETE = 4;

         //If the instruction length is 1,
         // it means that it may be executing a first-level instruction or querying the first letter of the name.
         if( (cmd.length() == 1)&&(cmd.charAt(0)>='0'&&cmd.charAt(0)<='9')){
             char ch = cmd.charAt(0);
             System.out.println("ch = "+(int)ch);
            if(((int)ch)>=48 && ((int)ch)<=57 ){
                int choose = (int)ch - 48;

                switch (choose){
                    case SEARCH:mainSearch(cmd);break;
                    case MODIFY:{

                    }break;
                    case ADD:{
                        System.out.println("\n\n\n\n");
                        System.out.println("Please enter your friend's phone number, " +
                                "name, type (for example: family, friends, etc.), email.");
                        System.out.println("--------------------------------------------");
                        System.out.print("Phone:");
                        String tpPhone = sc.nextLine();
                        System.out.print("Name:");
                        String tpName = sc.nextLine();
                        System.out.print("Type:");
                        String tpType = sc.nextLine();
                        System.out.print("E-mail:");
                        String tpEmail = sc.nextLine();

                        Connection conn = null;
                        PreparedStatement pstmt = null;
                        ResultSet rs = null;
                        try{
                            String sql = "insert into Contacts(phone,name,type,email)values(?,?,?,?)";
                            conn = DriverManager.getConnection(URL,USER,PASS);
                            pstmt = conn.prepareStatement(sql);
                            pstmt.setString(1,tpPhone);
                            pstmt.setString(2,tpName);
                            pstmt.setString(3,tpType);
                            pstmt.setString(4,tpEmail);
                            pstmt.executeUpdate();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try{
                            pstmt.close();
                            conn.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        addNode(new Person(tpPhone,tpName,tpType,tpEmail));
                        chooseSort();

                        System.out.println("--------------------------------------------");
                        System.out.println("Add friends Successful!");

                    }break;
                    case DLETE:{
                        System.out.println("\n\n\n\n\n\n\n\n\n\n");
                        System.out.println("Please enter the keyword (name or phone number) you want to delete.");
                        System.out.print("Key:");
                        String key = sc.nextLine();

                        Pattern pattern = Pattern.compile("[0-9]*");
                        Matcher isNum = pattern.matcher(key);

                        ArrayList<String> ls;
                        String sch;//This is a flag, in order to facilitate the deletion of matching.
                        if(isNum.matches()){
                            //If it is all figures are.
                            ls = this.searchPhone(key);
                            sch = "phone";
                        }else {
                            ls = this.searchName(key);
                            sch = "name";
                        }

                        if(ls.size() == 0) {
                            System.out.println("No this");
                            break;
                        }

                        System.out.println("Is that all? please choose!");
                        for (int i=0; i<ls.size(); i++){
                            System.out.println(i+1+"."+(String)ls.get(i));
                        }

                        System.out.print("choose:");
                        int usrchoo = sc.nextInt();//Provide users with choices.

                        if ("name".equals(sch)){
                            String var1 = (String)ls.get(usrchoo-1);
                            Node curNode = this.head;
                            if(this.length == 1){
                                head = head.next;
                            }else {
                                Node nextNode = curNode.next;
                                while (nextNode != null){
                                    if(nextNode.getData().getName().equals(var1)){
                                        System.out.println("ok I in ");
                                        try {
                                            this.deleteDataFromDb(nextNode.getData().getPhone());
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            System.exit(0);
                                        }
                                        curNode.next = nextNode.next;
                                        this.length--;
                                        break;
                                    }
                                    curNode = nextNode;
                                    nextNode = nextNode.next;
                                }
                            }

                        } else if ("phone".equals(sch)) {
                            String var1 = (String)ls.get(usrchoo-1);

                            Node curNode = this.head;
                            if(this.length == 1){
                                head = head.next;
                            }else {
                                Node nextNode = curNode.next;
                                while (nextNode != null){
                                    if(nextNode.getData().getPhone().equals(var1)){
                                        try {
                                            this.deleteDataFromDb(nextNode.getData().getPhone());
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            System.exit(0);
                                        }
                                        curNode.next = nextNode.next;
                                        this.length--;
                                        break;
                                    }
                                    curNode = nextNode;
                                    nextNode = nextNode.next;
                                }
                            }

                        }
                        System.out.println("Delete Successful");
                        chooseSort();
                    }break;
                }
            }
       }else {
             this.mainSearch(cmd);
         }

     }

     public void deleteDataFromDb(String key_phone) throws Exception{
         System.out.println("key: "+key_phone);
         Connection conn = null;
         PreparedStatement pstmt = null;

         String sql = "delete from Contacts where phone=?";
         conn = DriverManager.getConnection(URL,USER,PASS);
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1,key_phone);
         pstmt.executeUpdate();

         pstmt.close();
         conn.close();

     }

     public void mainSearch(String key){

            if(key.length() == 1){
                System.out.println("----------------|搜索如下|-------------");
                System.out.println("--------------------------------------");
                boolean flag = false;
                char ch = key.toUpperCase().charAt(0);
                if((ch>='a'&&ch<='z') || (ch>='A'&&ch<='Z')) {
                    Node tmp = this.head;
                    while (tmp != null) {
                        if(tmp.getData().getName().toUpperCase().charAt(0) == ch){
                            System.out.print(tmp.getData().getName());
                            System.out.print("\t" + tmp.getData().getPhone());
                            System.out.print("\t" + tmp.getData().getType());
                            System.out.println("\t" + tmp.getData().getEmail());
                            flag = true;
                        }
                        tmp = tmp.next;
                    }
                    if (!flag) {
                        System.out.println("\tNo Such it!");
                    }
                }
            }else if(key.length() > 1){
                if(Pattern.compile("[0-9]*").matcher(key).matches()) {
                    ArrayList<String> ls = this.searchPhone(key);
                    boolean flagP = true;
                    if (ls.size() == 0) {
                        System.out.println("\tNo Such it!");
                        flagP = false;
                    }
                    if (flagP) {
                        System.out.println("----------------|搜索如下|-------------");
                        System.out.println("--------------------------------------");
                        for (int i = 0; i < ls.size(); i++) {
                            Node tmp = head;
                            while (tmp != null) {
                                if (tmp.getData().getPhone().equals((String)ls.get(i))) {
                                    System.out.print(tmp.getData().getName());
                                    System.out.print("\t" + tmp.getData().getPhone());
                                    System.out.print("\t" + tmp.getData().getType());
                                    System.out.println("\t" + tmp.getData().getEmail());
                                    break;
                                }
                                tmp = tmp.next;
                            }

                        }
                    }

                } else {
                    ArrayList<String> ls = this.searchName(key);
                    boolean flagN = true;
                    if(ls.size() == 0){
                        System.out.println("\tNo Such it!");
                        flagN = false;
                    }

                    if(flagN) {
                        System.out.println("----------------|搜索如下|-------------");
                        System.out.println("--------------------------------------");
                        for (int i = 0; i < ls.size(); i++) {
                            Node tmp = head;
                            while (tmp != null) {
                                if (tmp.getData().getName().equals((String)ls.get(i))) {
                                    System.out.print(tmp.getData().getName());
                                    System.out.print("\t" + tmp.getData().getPhone());
                                    System.out.print("\t" + tmp.getData().getType());
                                    System.out.println("\t" + tmp.getData().getEmail());
                                    break;
                                }
                                tmp = tmp.next;
                            }

                        }
                    }

                }
            }else {
                System.out.println("No such it!");
            }

         Scanner sc = new Scanner(System.in);
            String wait = sc.nextLine();
     }

     public ArrayList<String> searchPhone(String key){
        ArrayList phone = new ArrayList();
        Node tmp = this.head;

        while(tmp != null){
            if(tmp.getData().getPhone().indexOf(key) >=0){
                phone.add(tmp.getData().getPhone());
            }
            tmp = tmp.next;
        }
        return phone;
     }

     public ArrayList<String> searchName(String key){
         ArrayList name = new ArrayList();
         Node tmp = this.head;

         while(tmp != null){
             if(tmp.getData().getName().indexOf(key) >=0){
                 name.add(tmp.getData().getName());
             }
             tmp = tmp.next;
         }
         return name;
     }
}
