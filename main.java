package com.kitty;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Main {
	static ArrayList<String> listOfItems = new ArrayList<String>();
	static ArrayList<Item> listOfItemsAsClass = new ArrayList<Item>();
	static ArrayList<Item> masterItemList = new ArrayList<Item>();
	private static String items = "";
	static String username;
	static String password;
	static String pages = "50";
	
	//CREATE CLIENT
	static HttpClient client = HttpClientBuilder.create().build();
	static HttpGet request = new HttpGet("http://forum.toribash.com/tori_market.php?format=json&page=");
    
    public static void main(String[] args) throws IOException, InterruptedException {
    	
    	//ASK FOR USERNAME AND PASSWORD
    	getPlayerInfo();
    	
    	//LOGIN WTIH A POST REQUEST
    	makePostRequest(client, username, password);
    	
    	//LOOP THROUGH MARKET PAGES 1 to 11
	    for(int i = 1; i<Integer.parseInt(pages)+1; i++){	
	    	request = new HttpGet("http://forum.toribash.com/tori_market.php?format=json&page="+i);
	    	
	    	System.out.println("Trying to scan page "+i);
	    	get(client, i);
	    	System.out.println();
	    	System.out.println("Attempting to find deals on page "+i);
	    	
	    	System.out.println("Done scanning page "+i);
	    	
	    	//RESET VARIABLES
	    	System.out.println("Resetting variables for next scan");
	    	listOfItems = new ArrayList<String>();
	    	for(Item item : listOfItemsAsClass)
	    		masterItemList.add(item);
	    	listOfItemsAsClass = new ArrayList<Item>();
	    	items = "";
	    	
	    	//WAIT TO AVOID REACHING LIMITS
	    	System.out.println("Waiting to avoid issues");
	    	Thread.sleep(2000);
	    	System.out.println("Done waiting, attempting to scan page "+(i+1));
	    	System.out.println();
	    }
	    
	    System.out.println("Total Items Scanned: "+masterItemList.size());
	    lookForDeals();
	    lookForUndercuttings();
	    promptEnterKey();
    }
    //Check If You Are Undercut
    private static void lookForUndercuttings(){
    	for(Item i : masterItemList){
    		for(Item o : masterItemList){
    			if(i.getItemUsername().toLowerCase().equals(username.toLowerCase())&&i.getItemName().equals(o.getItemName())&&Integer.parseInt(i.getItemPrice()) < Integer.parseInt(o.getItemPrice())){
    				System.out.print("You are being undercut by "+o.getItemUsername()+" on "+o.getItemName()+" by " + (Integer.parseInt(i.getItemPrice())-Integer.parseInt(o.getItemPrice())));
    			}
    		}
    	}
    }
    
    //LOOK FOR DEALS BASED ON SET CRITERIA
    private static void lookForDeals(){
    	for(Item i : masterItemList){
    		if(i.getItemPrice().equals("1")){
    			Toolkit.getDefaultToolkit().beep();
    			JOptionPane.showMessageDialog(null, ""+i.getItemName()+" is being sold for 1tc!");
    		}
    		if(i.getItemPrice().equals("2")){
    			Toolkit.getDefaultToolkit().beep();
    			JOptionPane.showMessageDialog(null, ""+i.getItemName()+" is being sold for 2tc!");
    		}
    		if(Integer.parseInt(i.getItemPrice()) <= 21000 && i.getItemName().equals("\"Flame\"")){
    			Toolkit.getDefaultToolkit().beep();
    			JOptionPane.showMessageDialog(null, "a "+i.getItemName()+" is being sold for cheap!");
    		}
    	}
    }
    
    //ASK USER TO PRESS ENTER BEFORE CONTINUE
    public static void promptEnterKey(){
    	   System.out.println("Press \"ENTER\" to continue...");
    	   Scanner scanner = new Scanner(System.in);
    	   scanner.nextLine();
    }
    
    //CHANGE THE USELESS STRING TO AN ARRAYLIST OF ITEM OBJECTS
    private static void stringListToClassList(){
    	for(int n = 0; n<listOfItems.size(); n++){
        	String inventid = listOfItems.get(n).substring(listOfItems.get(n).indexOf("\"inventid\":")+11, listOfItems.get(n).indexOf(",\"itemid\""));
        	String itemid = listOfItems.get(n).substring(listOfItems.get(n).indexOf("\"itemid\":")+9, listOfItems.get(n).indexOf("\"price\":"));
        	String price = listOfItems.get(n).substring(listOfItems.get(n).indexOf("\"price\":")+8, listOfItems.get(n).indexOf(",\"userid\""));
        	//broken? String market_date = listOfItems.get(n).substring(listOfItems.get(n).indexOf("\"market_date\":")+14, listOfItems.get(n).indexOf(",\"txt\":null"));
        	String txt = listOfItems.get(n).substring(listOfItems.get(n).indexOf("\"txt\":")+6, listOfItems.get(n).indexOf(",\"username\":"));
        	String username = listOfItems.get(n).substring(listOfItems.get(n).indexOf("\"username\":")+11, listOfItems.get(n).indexOf(",\"name\":"));
        	String name = listOfItems.get(n).substring(listOfItems.get(n).indexOf("\"name\":")+7, listOfItems.get(n).indexOf(",\"image\":"));

        	
        	listOfItemsAsClass.add(new Item(name, price, username));
        	
    		System.out.println(listOfItemsAsClass.get(n).getItemName()+" "+listOfItemsAsClass.get(n).getItemPrice());
    	}
    }
    
    //CONVERT A STRING TO MD5
    private static String MD5(String md5) {
    	   try {
    	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
    	        byte[] array = md.digest(md5.getBytes());
    	        StringBuffer sb = new StringBuffer();
    	        for (int i = 0; i < array.length; ++i) {
    	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
    	       }
    	        return sb.toString();
    	    } catch (java.security.NoSuchAlgorithmException e) {
    	    	
    	    }
    	    return null;
    	}
    
    //ASKS PLAYER FOR USERNAME AN PASSWORD
    private static void getPlayerInfo(){
    	Scanner keyboard = new Scanner(System.in);
    	System.out.println("enter your username");
    	username = keyboard.nextLine();
    	System.out.println("enter your password");
    	password = MD5(keyboard.nextLine());
    }
    
    //MAKE A GET REQUEST FOR THE MARKET PAGE WITH PAGE AND CLIENT PARAMETERS
    private static void get(HttpClient client, int page) throws InterruptedException {

        try {
        		HttpResponse response = client.execute(request);
                int responseCode = response.getStatusLine().getStatusCode();
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	 
	            String line;
				while ((line = rd.readLine()) != null) {
					items = line;

				}
				
				//PREPARE THE STRING TO BE TURNED INTO OBJECTS BY CREATING AN ARRAYLIST OF STRINGS CONTAINING EACH ITEM'S LINE
				List<String> temp = new ArrayList<String>(Arrays.asList(items.split(Pattern.quote("},{"))));
		    	listOfItems = (ArrayList<String>) temp;
		    	listOfItems.set(0, listOfItems.get(0).substring(listOfItems.get(0).indexOf("\"inventid\"")));
		    	
		    	//CREATE ITEM OBJECTS FROM STRING ARRAYLIST AND RESET THE ARRAYLIST OF STRINGS
		    	stringListToClassList();
		    	listOfItems=null;
		    	EntityUtils.consume(response.getEntity());
		    	
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            System.out.println("error");
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            System.out.println("error");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        
    }
    
    //POST REQUEST TO LOGIN
    private static void makePostRequest(HttpClient client, String u, String p){
    	
        HttpPost post = new HttpPost("http://forum.toribash.com/login.php?do=login");

        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("do", "login"));
        arguments.add(new BasicNameValuePair("s", ""));
        arguments.add(new BasicNameValuePair("security_token", "guest"));
        arguments.add(new BasicNameValuePair("vb_login_password", ""));
        arguments.add(new BasicNameValuePair("vb_login_username", u));
        arguments.add(new BasicNameValuePair("vb_login_md5password", p));

        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
