package GroupProject;


public class Account {

	
	private String name;
	private double balance;
	
	public Account(String name, double balance) {
		
		this.name=name;
		this.balance=balance;
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void withdraw(double amount) {
		balance-=amount;
		try {
			if (balance<0) {
				System.out.println("The transaction is not completed!");
				System.exit(1);
			}}
			catch(Exception e) {
				System.out.println("Wrong action!");
			}
	}
	public void deposit(double amount) {
		balance +=amount;
		
	}
	public String toString() {
		return "Name: "+name
				+"\nBalance: "+balance;
		
	}
}
