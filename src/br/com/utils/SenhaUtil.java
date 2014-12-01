package br.com.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import sun.misc.BASE64Encoder;

public class SenhaUtil {
	
	public String encriptaSenha(String senha){
		try{
			MessageDigest disgest = MessageDigest.getInstance("MD5");
			disgest.update(senha.getBytes());
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(disgest.digest());
		} catch(NoSuchAlgorithmException ns){
			ns.printStackTrace();
			return senha;
		}
	}	
	
	public String gerarSenha(){
		String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ1234567890";  
		Random random = new Random();  
		String armazenaChaves = "";  
		int index = -1;  
		for( int i = 0; i < 5; i++ ) {  
		   index = random.nextInt( letras.length() );  
		   armazenaChaves += letras.substring( index, index + 1 );  
		}  		
		return armazenaChaves.toLowerCase();
	}
	
}
