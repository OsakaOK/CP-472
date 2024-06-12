class BankAccount:
    def __init__(self, accountNumber, accountHolderName, balance=0):
        self.__accountNumber = accountNumber
        self.__accountHolderName = accountHolderName
        self.__balance = balance

    # If negative value then invalid
    def deposit(self, amount):
        if amount > 0:
            self.__balance += amount
            print(f"Deposited {amount} into account {self.__accountNumber}.")
        else:
            print("Invalid deposit amount.")

    def withdraw(self, amount):
        if amount > 0:
            if amount <= self.__balance:
                self.__balance -= amount
                print(f"Withdrew {amount} from account {self.__accountNumber}.")
            else:
                print("Insufficient balance.")
        else:
            print("Invalid withdrawal amount.")

    def displayAccountDetails(self):
        print(f"Account Number: {self.__accountNumber}")
        print(f"Account Holder Name: {self.__accountHolderName}")
        print(f"Balance: {self.__balance}")


# Use Inheritance to extend BankAccount for below Classes
class SavingsAccount(BankAccount):
    def __init__(self, accountNumber, accountHolderName, balance=0, minBalance=0):
        super().__init__(accountNumber, accountHolderName, balance)
        self.__minBalance = minBalance

    def withdraw(self, amount):
        if amount > 0:
            if (self._BankAccount__balance - amount) >= self.__minBalance:
                self._BankAccount__balance -= amount
                print(
                    f"Withdrew {amount} from savings account {self._BankAccount__accountNumber}."
                )
            else:
                print("Withdrawal amount exceeds minimum balance.")
        else:
            print("Invalid withdrawal amount.")


class CheckingAccount(BankAccount):
    def __init__(self, accountNumber, accountHolderName, balance=0):
        super().__init__(accountNumber, accountHolderName, balance)


class CreditCardAccount(BankAccount):
    def __init__(self, accountNumber, accountHolderName, creditLimit=0, balance=0):
        super().__init__(accountNumber, accountHolderName, balance)
        self.__creditLimit = creditLimit

    def available_credit(self):
        return self.__creditLimit - self._BankAccount__balance

    def swipe(self, amount):
        if amount <= self.available_credit():
            self._BankAccount__balance += amount
            print(
                f"Amount {amount} swiped on credit card {self._BankAccount__accountNumber}."
            )
        else:
            print("Transaction declined. Insufficient credit limit.")


class DebitCard(BankAccount):
    def __init__(self, cardNumber, cardHolderName, linkedAccount):
        super().__init__(
            linkedAccount._BankAccount__accountNumber,
            cardHolderName,
            linkedAccount._BankAccount__balance,
        )
        self.__cardNumber = cardNumber

    def swipe(self, amount):
        self.withdraw(amount)


# Use composition to related to BankAccount
class ATM:
    def __init__(self):
        self.__accounts = {}

    def add_account(self, account):
        self.__accounts[account._BankAccount__accountNumber] = account

    def withdraw(self, accountNumber, amount):
        if accountNumber in self.__accounts:
            self.__accounts[accountNumber].withdraw(amount)
        else:
            print("Account not found.")

    def deposit(self, accountNumber, amount):
        if accountNumber in self.__accounts:
            self.__accounts[accountNumber].deposit(amount)
        else:
            print("Account not found.")


# Different function for testing
def create_account():
    acc_type = input("Enter account type (Savings/Checking): ").lower()
    account_number = int(input("Enter account number: "))
    account_holder_name = input("Enter account holder name: ")
    initial_deposit = float(input("Enter initial deposit amount: "))

    if acc_type == "savings":
        min_balance = float(input("Enter minimum balance for savings account: "))
        return SavingsAccount(
            account_number, account_holder_name, initial_deposit, min_balance
        )
    elif acc_type == "checking":
        return CheckingAccount(account_number, account_holder_name, initial_deposit)
    else:
        print("Invalid account type.")
        return None


def test_savings_account(account):
    print("\n=== Testing Savings Account ===")
    withdrawal_amount = float(input("Enter withdrawal amount for savings account: "))
    account.withdraw(withdrawal_amount)
    account.displayAccountDetails()


def test_checking_account(account):
    print("\n=== Testing Checking Account ===")
    withdrawal_amount = float(input("Enter withdrawal amount for checking account: "))
    account.withdraw(withdrawal_amount)
    account.displayAccountDetails()


def test_debit_card(accounts):
    print("\n=== Testing Debit Card ===")
    debit_card_number = input("Enter debit card number: ")
    debit_card_holder_name = input("Enter debit card holder name: ")

    found_account = None
    for account in accounts:
        if account._BankAccount__accountHolderName == debit_card_holder_name:
            found_account = account
            break

    if found_account:
        debit_card = DebitCard(debit_card_number, debit_card_holder_name, found_account)
        withdrawal_amount = float(input("Enter withdrawal amount via debit card: "))
        if found_account.withdraw(withdrawal_amount):
            print(
                f"Withdrew {withdrawal_amount} from account {found_account.getAccountNumber()}."
            )
        found_account.displayAccountDetails()
    else:
        print("No account found for the given debit card holder name.")


def test_credit_card_account(account, savings_account):
    print("\n=== Testing Credit Card Account ===")
    credit_limit = float(input("Enter credit limit for credit card account: "))
    withdrawal_amount = float(
        input("Enter withdrawal amount for credit card account: ")
    )
    account = CreditCardAccount(
        account._BankAccount__accountNumber,
        account._BankAccount__accountHolderName,
        credit_limit,
        account._BankAccount__balance,
    )
    account.swipe(withdrawal_amount)
    account.displayAccountDetails()


def test_atm(accounts):
    print("\n=== Testing ATM ===")
    atm = ATM()
    for account in accounts:
        atm.add_account(account)

    choice = input(
        "Would you like to withdraw or deposit funds? (withdraw/deposit): "
    ).lower()
    if choice == "withdraw":
        account_number = int(input("Enter account number for withdrawal: "))
        amount = float(input("Enter withdrawal amount: "))
        atm.withdraw(account_number, amount)
    elif choice == "deposit":
        account_number = int(input("Enter account number for deposit: "))
        amount = float(input("Enter deposit amount: "))
        atm.deposit(account_number, amount)
    else:
        print("Invalid choice.")

    for account in accounts:
        print("\nAccount Details:")
        account.displayAccountDetails()


# --------------------------------------------#
# When testing if 'no' get input first then it will run the testing afterward
# but nothing is being test, and it will end at ATM
# --------------------------------------------#
if __name__ == "__main__":
    accounts = []
    while True:
        choice = input(
            "Do you want to create an account? (yes/no/q(to quit)): "
        ).lower()
        if choice == "yes":
            account = create_account()
            if account:
                print("Account created successfully!")
                accounts.append(account)
        elif choice == "no":
            break
        elif choice == "q":
            print("Exiting the program.")
            exit()
        else:
            print("Invalid choice. Please enter 'yes' or 'no' or 'q'.")

    # Test account types
    for account in accounts:
        if isinstance(account, SavingsAccount):
            test_savings_account(account)
        elif isinstance(account, CheckingAccount):
            test_checking_account(account)

    # Test debit card
    test_debit_card(accounts)

    # Test credit card account
    for account in accounts:
        if isinstance(account, CreditCardAccount):
            test_credit_card_account(account)

    # Test ATM
    test_atm(accounts)
