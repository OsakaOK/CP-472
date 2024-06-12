import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// All testing function is in here
public class BankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingSystem bankingSystem = new BankingSystem();

        System.out.println("Welcome to the banking system!");

        List<BankAccount> accounts = bankingSystem.createAccounts(scanner);
        for (BankAccount account : accounts) {
            System.out.println("Account Number: " + account.accountNumber);
            System.out.println("Account Holder: " + account.accountHolderName);
            System.out.println("Balance: " + account.balance);
            System.out.println();

            // Test different classes
            bankingSystem.testCheckingAccount(scanner, account);
            bankingSystem.testSavingsAccount(scanner, account);
            bankingSystem.testDebitCard(scanner, account);
            bankingSystem.testCreditCard(scanner, account);
            bankingSystem.testATM(scanner, accounts, account);
        }

        scanner.close();
    }

    public List<BankAccount> createAccounts(Scanner scanner) {
        List<BankAccount> accounts = new ArrayList<>();

        boolean createMoreAccounts = true;
        while (createMoreAccounts) {
            System.out.println("Do you want to create an account?");
            System.out.println("1. Yes, create a new account");
            System.out.println("2. No, finish creating accounts");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    BankAccount account = createAccount(scanner);
                    if (account != null) {
                        accounts.add(account);
                        System.out.println("Account created successfully.");
                    }
                    break;
                case 2:
                    System.out.println("Finishing creating accounts.");
                    createMoreAccounts = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }

        return accounts;
    }

    public BankAccount createAccount(Scanner scanner) {
        System.out.println("Choose the type of account you want to create:");
        System.out.println("1. Checking Account");
        System.out.println("2. Savings Account");
        System.out.print("Enter your choice: ");
        int accountChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine();

        if (accountChoice == 1) {
            return new CheckingAccount(accountNumber, accountHolderName, initialBalance);
        } else if (accountChoice == 2) {
            return new SavingsAccount(accountNumber, accountHolderName, initialBalance);
        } else {
            System.out.println("Invalid account type. Creating a Checking Account by default.");
            return new CheckingAccount(accountNumber, accountHolderName, initialBalance);
        }
    }

    public void testCheckingAccount(Scanner scanner, BankAccount account) {
        System.out.println("=== Testing Checking Account ===");
        System.out.print("Enter deposit amount for checking account: ");
        double depositAmount = scanner.nextDouble();
        account.deposit(depositAmount);
        System.out.println("New balance after deposit: " + account.balance);

        System.out.print("Enter withdrawal amount for checking account: ");
        double withdrawalAmount = scanner.nextDouble();
        account.withdraw(withdrawalAmount);
        System.out.println("New balance after withdrawal: " + account.balance);
    }

    public void testSavingsAccount(Scanner scanner, BankAccount account) {
        System.out.println("=== Testing Savings Account ===");
        System.out.print("Enter deposit amount for savings account: ");
        double depositAmount = scanner.nextDouble();
        account.deposit(depositAmount);
        System.out.println("New balance after deposit: " + account.balance);

        System.out.print("Enter withdrawal amount for savings account: ");
        double withdrawalAmount = scanner.nextDouble();
        account.withdraw(withdrawalAmount);
        System.out.println("New balance after withdrawal: " + account.balance);
    }

    public void testDebitCard(Scanner scanner, BankAccount account) {
        System.out.println("=== Testing Debit Card ===");
        System.out.print("Enter debit card number: ");
        int debitCardNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter debit card holder name: ");
        String debitCardHolderName = scanner.nextLine();

        DebitCard debitCard = new DebitCard(debitCardNumber, debitCardHolderName, account);
        System.out.print("Enter withdrawal amount via debit card: ");
        double withdrawalAmount = scanner.nextDouble();
        scanner.nextLine();
        debitCard.swipe(withdrawalAmount);
        System.out.println("New balance after debit card transaction: " + account.balance);
    }

    public void testCreditCard(Scanner scanner, BankAccount account) {
        System.out.println("=== Testing Credit Card ===");
        System.out.print("Enter credit card number: ");
        int creditCardNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter credit card holder name: ");
        String creditCardHolderName = scanner.nextLine();
        System.out.print("Enter credit limit for credit card: ");
        double creditLimit = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        CreditCard creditCard = new CreditCard(creditCardNumber, creditCardHolderName, account, creditLimit);
        System.out.print("Enter transaction amount via credit card: ");
        double transactionAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        creditCard.swipe(transactionAmount);
        System.out.println("New balance after credit card transaction: " + account.balance);
    }

    public void testATM(Scanner scanner, List<BankAccount> accounts, BankAccount account) {
        System.out.println("=== Testing ATM ===");
        System.out.println("Select an option:");
        System.out.println("1. Withdraw cash");
        System.out.println("2. Deposit cash");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                System.out.print("Enter withdrawal amount: ");
                double withdrawalAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                ATM.withdraw(account, withdrawalAmount);
                System.out.println("New balance after ATM withdrawal: " + account.balance);
                break;
            case 2:
                System.out.print("Enter deposit amount: ");
                double depositAmount = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                ATM.deposit(account, depositAmount);
                System.out.println("New balance after ATM deposit: " + account.balance);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
}

class BankAccount {
    protected int accountNumber;
    protected String accountHolderName;
    protected double balance;

    public BankAccount(int accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful.");
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful.");
            return true;
        } else {
            System.out.println("Insufficient balance.");
            return false;
        }
    }

    public void displayAccountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder Name: " + accountHolderName);
        System.out.println("Balance: " + balance);
    }
}

// Use Inheritance
class CheckingAccount extends BankAccount {
    public CheckingAccount(int accountNumber, String accountHolderName, double balance) {
        super(accountNumber, accountHolderName, balance);
    }
}

class SavingsAccount extends BankAccount {
    public SavingsAccount(int accountNumber, String accountHolderName, double balance) {
        super(accountNumber, accountHolderName, balance);
    }
}

// Use Composition
class DebitCard {
    private int cardNumber;
    private String cardHolderName;
    private BankAccount linkedAccount;

    public DebitCard(int cardNumber, String cardHolderName, BankAccount linkedAccount) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.linkedAccount = linkedAccount;
    }

    public void swipe(double amount) {
        if (linkedAccount.withdraw(amount)) {
            System.out.println(
                    "Amount " + amount + " swiped using debit card " + cardNumber + " by " + cardHolderName + ".");
        } else {
            System.out.println("Transaction declined. Insufficient funds.");
        }
    }
}

class CreditCard {
    private int cardNumber;
    private String cardHolderName;
    private BankAccount linkedAccount;
    private double creditLimit;

    public CreditCard(int cardNumber, String cardHolderName, BankAccount linkedAccount, double creditLimit) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.linkedAccount = linkedAccount;
        this.creditLimit = creditLimit;
    }

    public void swipe(double amount) {
        if (amount <= (linkedAccount.balance + creditLimit)) {
            linkedAccount.balance -= amount;
            System.out.println(
                    "Amount " + amount + " swiped using credit card " + cardNumber + " by " + cardHolderName + ".");
        } else {
            System.out.println("Transaction declined. Exceeds credit limit.");
        }
    }
}

class ATM {
    public ATM(BankAccount linkedAccount) {
    }

    public static void withdraw(BankAccount account, double amount) {
        if (amount <= account.balance) {
            account.balance -= amount;
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Withdrawal failed. Insufficient funds.");
        }
    }

    public static void deposit(BankAccount account, double amount) {
        if (amount > 0) {
            account.balance += amount;
            System.out.println("Deposit successful.");
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }
}