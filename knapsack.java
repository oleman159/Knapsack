import java.net.*;
import java.util.*;
import java.io.*;
import java.math.*;
import javax.crypto.*;
import java.security.*;

//Name: Muhammad Nuh Bin Norseni
//java version "1.8.0_212"

class Knapsack{
	
	//Used to String to its Binary Form.
	static String strToBinary(String s){ 
        int n = s.length(); 
		binaryMsg = "";
  
        for (int i = 0; i < n; i++){ 
            // convert each char to 
            // ASCII value 
            int val = Integer.valueOf(s.charAt(i)); 
  
            // Convert ASCII value to binary 
            bin = ""; 
            while (val > 0){ 
                if (val % 2 == 1){ 
                    bin += '1'; 
                } 
                else
                    bin += '0';
                val /= 2; 
            } 
            bin = reverse(bin);
			
			if(bin.length()!=8){
				for(int q = 0; q<=8-bin.length(); q++)
					bin = "0" + bin;
			}
			binaryMsg+=bin;
            //System.out.print(bin + " "); 
        } 
		return binaryMsg;
    } 
	
	//Used to reverse the binary order in strToBinary.
    static String reverse(String input){ 
        char[] d = input.toCharArray(); 
        int l, r = 0; 
        r = d.length - 1; 
  
        for (l = 0; l < r; l++, r--){ 
            // Swap values of l and r  
            char temp = d[l]; 
            d[l] = d[r]; 
            d[r] = temp; 
        } 
        return String.valueOf(d); 
    }
	
	//Used to get String from Binary.
	static String binaryToText(String binary){
		return Arrays.stream(binary.split("(?<=\\G.{8})"))/* regex to split the bits array by 8*/
					 .parallel()
					 .map(eightBits -> (char)Integer.parseInt(eightBits, 2))
					 .collect(
									 StringBuilder::new,
									 StringBuilder::append,
									 StringBuilder::append
					 ).toString();
	}
	
	public static int size, w, m, a, gcd, b, sumA, grp, weightC, inverseW, weightP;
	public static String msgPlain, msgCipher, bin, binaryMsg, plainTxt, cipherTxt;
	public static ArrayList<Integer> aPK = new ArrayList<Integer>(); //ArrayList of ai in private key.
	public static ArrayList<Integer> bPK = new ArrayList<Integer>(); //ArrayList of bi in public key.
	public static ArrayList<Integer> cipherT = new ArrayList<Integer>(); //ArrayList of cipher weight from encryption.
	public static ArrayList<Integer> cipherT2 = new ArrayList<Integer>(); //ArrayList of cipher weight from user.
	public static ArrayList<Integer> plainT = new ArrayList<Integer>(); //ArrayList of plaintext weight.
	
	public static void main( String args[] ) throws Exception {
		Scanner scanner = new Scanner(System.in);
		
		//Size input.
		while(true){
			try{
				while(true){
					System.out.printf("\nEnter size of super-increasing knapsack: ");
					size = scanner.nextInt();
					if(size > 0){
						break;
					}
					else{
						System.out.println("Invalid input, Size needs to be more than 0, please re-enter.");
					}
				}
				break;
			}
			catch(InputMismatchException e){
				String badInput = scanner.next();
				System.out.println("Invalid input, Size needs to be an integer.");
				continue;
			}
		}
		
		//First a value input.
		while(true){
			try{
				while(true){
					System.out.printf("Key in first value of a: ");
					a = scanner.nextInt();
					if(a > 0){
						aPK.add(a);
						break;
					}
					else{
						System.out.println("Invalid input, Value of 'a' needs to be more than 0, please re-enter.\n");
					}
				}
				break;
			}
			catch(InputMismatchException e){
				String badInput = scanner.next();
				System.out.println("Invalid input, 'a' needs to be an integer.\n");
				continue;
			}
		}
		
		//Input of other values of a.
		sumA = a; //Sum of first value.
		for(int i = 2;i <= size; i++){
			while(true){
				try{
					while(true){
						System.out.printf("Key in value of a" + i + ": ");
						a = scanner.nextInt();
						if(sumA < a){
							aPK.add(a);
							sumA += a;
							break;
						}
						else{
							System.out.println("Invalid input, Value of a" + i + " is too small, it needs to be bigger than " + sumA + "\n");
						}
					}
					break;
				}
				catch(InputMismatchException e){
					String badInput = scanner.next();
					System.out.println("Invalid input, a" + i + " needs to be an integer.\n");
					continue;
				}
			}
		}
		
		//m value input.
		while(true){
			try{
				while(true){
					System.out.printf("Key in value of modulus, m: ");
					m = scanner.nextInt();
					//m = 110;
					if(m > sumA){
						break;
					}
					else{
						System.out.println("Invalid input, Value of m is too small, it needs to be bigger than " + sumA + "\n");
					}
				}
				break;
			}
			catch(InputMismatchException e){
				String badInput = scanner.next();
				System.out.println("Invalid input, m needs to be an integer.\n");
				continue;
			}
		}
		
		//w value input.
		while(true){
			try{
				while(true){
					System.out.printf("Key in value of multiplier, w: ");
					w = scanner.nextInt();
					scanner.nextLine();
					//w = 31;
					
					for(int i = 1; i <= m && i <= w; i++)
					{
						if(m%i==0 && w%i==0){
							gcd = i;	
						}
					}
					if(gcd == 1){
						break;
					}
					else{
						System.out.println("Invalid input, Value of w is not relatively prime to m. Please re-enter w.\n");
					}
				}
				break;
			}
			catch(InputMismatchException e){
				String badInput = scanner.next();
				System.out.println("Invalid input, w needs to be an integer.\n");
				continue;
			}
		}
		
		//getting inverse of w.
		inverseW = 0;
		int x = 2;
		while(x > 1){
			inverseW += 1;
			x = (w*inverseW)%m;
			
		}
		
		//calculation of Public key.
		for(int i = 0; i < size; i++){
			b = ((w*aPK.get(i))%m);
			bPK.add(b);
		}
		System.out.println("Public key, b = " + Arrays.toString(bPK.toArray()));
		
		//message input.
		System.out.printf("\nMessage: ");
		msgPlain = scanner.nextLine();
		binaryMsg = strToBinary(msgPlain);
		int padding = size-(binaryMsg.length()%size);
		
		//making groups of split msg binary according to the size of the knapsack. 
		if(binaryMsg.length()%size == 0){
			grp = binaryMsg.length()/size;
		}
		else{
			grp = (binaryMsg.length()/size) + 1;
			for(int i = 0; i < binaryMsg.length()%size; i++){
				binaryMsg+="0";
			}
		}
		
		//getting the weight(ciphertext) of each group of the split binary of the msg.
		for(int q = 1; q <= grp; q++){ 
			weightC = 0;
			for(int i = 0-size; i < 0; i++){
				char c = binaryMsg.charAt(i+size*q);
				if(c == '1'){
					weightC+=bPK.get(i+size);
				}
				else{
					weightC+=0;
				}
			}
			cipherT.add(weightC);
		}
		
		//output of the ciphertext
		System.out.printf("Cipher: ");
		for(int i = 0; i<cipherT.size(); i++){
			cipherTxt = String.valueOf(cipherT.get(i));
			System.out.printf(cipherTxt);
			if(i!=cipherT.size()-1)
				System.out.printf( ",");
		}
		
		//inputing a ciphertext that has been encrypted with the public key.
		System.out.printf("\n\nKey in cipher with ',' separating each number: ");
		msgCipher = scanner.nextLine();
		String[] msgCipherArray = msgCipher.split(",");
		for (String number : msgCipherArray) {
			cipherT2.add(Integer.parseInt(number.trim()));
		}
		
		//using inverse w as multiplier and mod m to get weight of plaintext binary of each group.
		for(int i = 0; i < cipherT2.size(); i++){
			weightP = ((cipherT2.get(i)*inverseW)%m);
			plainT.add(weightP);
		}
		
		//output of the decryption of the ciphertext.
		plainTxt = "";
		for(int q = plainT.size()-1; q>=0 ; q--){
			weightP = plainT.get(q);
			if(q==plainT.size()-1&&padding<size){
				for(int i = size-1-padding; i>=0; i--){
					if(weightP>=aPK.get(i)){
						plainTxt = "1" + plainTxt;
						weightP -= aPK.get(i);
					}
					else{
						plainTxt = "0" + plainTxt;
					}
				}
			}
			else{
				for(int i = size-1; i>=0; i--){
					if(weightP>=aPK.get(i)){
						plainTxt = "1" + plainTxt;
						weightP -= aPK.get(i);
					}
					else{
						plainTxt = "0" + plainTxt;
					}
				}
			}
		}
		System.out.println("Decrypted plaintext: " + binaryToText(plainTxt));
	}
}




















