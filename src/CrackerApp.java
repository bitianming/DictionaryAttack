import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class CrackerApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dicFile="dict.txt";
		String hashFile="hash.txt";
		if(args.length>0)
		{
			hashFile=args[0];
			dicFile=args[1];
		}
		ArrayList<String> passwordDict=new ArrayList<String>();
		ArrayList<String> hashValueList=new ArrayList<String>();
		File 					file = null;
		FileInputStream 		fis = null;
		InputStreamReader		isr = null;
		BufferedReader 			br = null;
		
		//load dictionary into passwordDict
		try{
			file=new File(dicFile);
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);	      
		    br = new BufferedReader(isr);
		    String newLine;
		    while((newLine=br.readLine())!=null)
		    {
		    	passwordDict.add(newLine);
		    }
		    //delete passwords more than 8 characters in dictionary
		    for(int i=0;i<passwordDict.size();i++)
		    {
		    	String temp=passwordDict.get(i);
		    	if(temp.length()>8)
		    		passwordDict.remove(i);
		    }
		}catch(Exception e){}
		
		//load hash into passwordDict
		try{
			file=new File(hashFile);
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);	      
			br = new BufferedReader(isr);
			
			String newLine;
			while((newLine=br.readLine())!=null)
			{
				hashValueList.add(newLine);
			}
			fis.close();
		    isr.close();
		    br.close();	
		    ArrayList<String> temp=new ArrayList<String>(passwordDict);
		    //preparing lists
		    ArrayList<String> combinedPassword=new ArrayList<String>();
		    ArrayList<String> combinedNumPassword=new ArrayList<String>();

		    for(int i=0;i<temp.size();i++)
			{
		    	String password;
				password=temp.get(i);//generate combined password
				for(int k=0;k<temp.size();k++)
				{
					String secPassword=temp.get(k);
					password=password+secPassword;
					if(password.length()>8||password.length()<5)
					{
						password=temp.get(i);
						continue;
					}
					combinedPassword.add(password);
				}
			}
		    temp=passwordDict;
		    for(int i=0;i<temp.size();i++)
			{
		    	String password,passwordTem;
				password=temp.get(i);//generate combined password
				for(int k=0;k<temp.size();k++)
				{
					String secPassword=temp.get(k);
					for(int l=0;l<10;l++)
					{
						passwordTem=password+l+secPassword;
						if(passwordTem.length()>8||passwordTem.length()<5)
						{
							break;
						}
						combinedNumPassword.add(passwordTem);
					}
					
				}
			}
		    temp=passwordDict;
		    ArrayList<String> substitution=new ArrayList<String>(); //Substitute character with numbers
		    for(int i=0;i<temp.size();i++)
			{
		    	String password;
				password=temp.get(i);
				password=password.replace('o', '0');
				substitution.add(password);
			}temp=passwordDict;
		    for(int i=0;i<temp.size();i++)
			{
		    	String password;
				password=temp.get(i);
				password=password.replace('i', '1');
				substitution.add(password);
			}temp=passwordDict;
		    for(int i=0;i<temp.size();i++)
			{
		    	String password;
				password=temp.get(i);
				password=password.replace('e', '3');
				substitution.add(password);
			}temp=passwordDict;
		    for(int i=0;i<temp.size();i++)
			{
		    	String password;
				password=temp.get(i);
				password=password.replace('s', '5');
				substitution.add(password);
			}temp=passwordDict;
		    for(int i=0;i<temp.size();i++)
			{
		    	String password;
				password=temp.get(i);
				password=password.replace('t', '7');
				substitution.add(password);
			}temp=passwordDict;
			ArrayList<String> numberedPassword=new ArrayList<String>();
			//add prefix and suffix to the password
			for(int i=0;i<temp.size();i++)
			{
		    	String password;
				password=temp.get(i);
				for(int k = 0; k <= 99; k++){ 
                    String passwordPre = k + password; 
                    String passwordSuf = password + k;
                    if((5<=passwordPre.length()&&passwordPre.length()<=8)||(passwordSuf.length()>=5&&passwordSuf.length()<=8))
                    {
                    	numberedPassword.add(passwordPre);
                        numberedPassword.add(passwordSuf);
                    }
				}
			}
			
			// formatted hashed strings from hash.txt.
			ArrayList<FormattedHashedString> hpList = new ArrayList<FormattedHashedString>(20);

			for (String item : hashValueList) {
				hpList.add(new FormattedHashedString(item));
			}
			
			//create runnable lists
		    ArrayList<Runnable> splitList=new ArrayList<Runnable>();
		    ArrayList<Runnable> combinedSplitList=new ArrayList<Runnable>();
		    ArrayList<Runnable> combinedNumSplitList=new ArrayList<Runnable>();
		    ArrayList<Runnable> substitutionSplitList=new ArrayList<Runnable>();
		    ArrayList<Runnable> numberedSplitList=new ArrayList<Runnable>();
		    splitList=splitList(passwordDict,hpList);
		    substitutionSplitList=splitList(substitution,hpList);
		    combinedSplitList=splitList(combinedPassword,hpList);
		    numberedSplitList=splitList(numberedPassword,hpList);
		    combinedNumSplitList=splitList(combinedNumPassword,hpList);
		    TimeUnit unit=TimeUnit.MINUTES;
		    //add threads to crack password from original list
		    ExecutorService executor = Executors.newFixedThreadPool(15);
		    for(int i=0;i<splitList.size();i++)
		    {
		    	Cracker cra=(Cracker)splitList.get(i);
		    	executor.execute(cra);
		    }
		    //create threads to crack substitution password
		    for(int i=0;i<substitutionSplitList.size();i++)
		    {
		    	Cracker cra=(Cracker)substitutionSplitList.get(i);
		    	executor.execute(cra);
		    }
		    //create threads to crack password with prefix and suffix
		    for(int i=0;i<numberedSplitList.size();i++)
		    {
		    	Cracker cra=(Cracker)numberedSplitList.get(i);
		    	executor.execute(cra);
		    }
		    //create threads to crack combined password
		    for(int i=0;i<combinedSplitList.size();i++)
		    {
		    	Cracker cra=(Cracker)combinedSplitList.get(i);
		    	executor.execute(cra);
		    }
		    for(int i=0;i<combinedNumSplitList.size();i++)
		    {
		    	Cracker cra=(Cracker)combinedNumSplitList.get(i);
		    	executor.execute(cra);
		    }
		    executor.shutdown();
		    executor.awaitTermination(30, unit);
		    System.out.println(hpList.size());
		    

			
//			Cracker c = new Cracker(hpList, passwordDict);
//			c.start();
			
		}catch(Exception e){}
	}
	
	public static ArrayList<Runnable> splitList(ArrayList<String> passwordDict, ArrayList<FormattedHashedString> hpList)
	{
		ArrayList<Runnable> splitList=new ArrayList<Runnable>();
		if(passwordDict.size()<=50)
		{
			Runnable crac=new Cracker(hpList,passwordDict);
			splitList.add(crac);
		}
		else
		{
			int middle=passwordDict.size()/2;
			ArrayList<String> left=new  ArrayList<String>(passwordDict.subList(0, middle-1));
			ArrayList<String> right=new  ArrayList<String>(passwordDict.subList(middle-1,passwordDict.size()));
			splitList.addAll(splitList(left, hpList));
			splitList.addAll(splitList(right,hpList));
		}
		return splitList;
	}
	
}
