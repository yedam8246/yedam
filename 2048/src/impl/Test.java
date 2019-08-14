package impl;

public class Test {
	
	public static void main(String[] args) {
		boolean test=UsersDAO.getInstance().checkNick("2");
		
		System.out.println(test);
	}

}
