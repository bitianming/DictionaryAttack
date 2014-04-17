import java.util.ArrayList;

public class Cracker implements Runnable{
	private  ArrayList<String> hashValueList;
	private ArrayList<String> passwordDict;
	private ArrayList<FormattedHashedString> fhsList;
//	private Thread t;
	
	public Cracker(ArrayList<FormattedHashedString> FormattedHashedStringList,ArrayList<String> passwordDict)
	{
//		this.hashValueList=hashValueList;
		this.passwordDict=passwordDict;
		fhsList = FormattedHashedStringList;
	}

	public ArrayList<String> getHashlist()
	{
		return this.hashValueList;
	}

	@Override
	public void run() {
//		String userNum,salt,hashValue;
		
//		if(hashValueList.size()==0)
//			return;
		
		if(fhsList.size()==0)
			return;
		
		for(String password : passwordDict) {
			
			for(int i = 0; i < fhsList.size(); i++) {
				FormattedHashedString fhs = fhsList.get(i);
				
				if(Crypt.crypt(password, fhs.getMD5nSalt()).equals(fhs.getExpectedHashedValue())) {
					
					System.out.println(fhs.getUser() + " = " + password);
					fhsList.remove(i);
					break;
					
				}
			}
		
//			for(int j = 0;j < hashValueList.size(); j++)
//			{
//				
//				String[] tem=hashValueList.get(j).split("\\$",2);
//				userNum=tem[0];
//				salt="$"+tem[1].substring(0,10);
//				hashValue=tem[1].substring(11);
//				if(Crypt.crypt(password, salt).substring(12).equals(hashValue))
//				{
//					
//					hashValueList.remove(j);
//					System.out.println(userNum+":"+password);
//					break;
//				}
//			}			
		}
	}
	
//	   public void start() {
//	      if (t == null)
//	      {
//	         t = new Thread (this, "0");
//	         t.start();
//	      }
//	   }


}
