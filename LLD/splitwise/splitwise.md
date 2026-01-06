Step 1 : Requirement Gathering:
-------------------------------
1. add friends capability
2. add / manage group
3. add / manage friends inside a group
4. add / manage expense inside a group / without a group
5. split expense capability (equal, unequal, percentage)
6. balance sheet of each user

Step 2 : Object Modeling:
------------------------
1. Splitwise
2. User
3. Group
4. Expense
5. Split
6. BalanceSheet

Step 3 : Classes and attributes:
--------------------------------
SplitType (Enum):
- EQUAL
- UNEQUAL
- PERCENTAGE

Split (Abstract Class):
- user : User
- amount : Double

User:

Expense:
- id : String
- description : String
- amount : Double
- paidBy : User
- splitType : SplitType
- splits : List<Split>

User:
- id : String
- name : String
- userExpenseBalanceSheet : UserExpenseBalanceSheet

Group:
- id : String
- name : String
- user : List<User>
- expense : List<Expense>
- expenseController : ExpenseController

Step 4 : Identify methods for each class:
------------------------------------------
~~~
expense can be created from two places :
1. from group
2. directly between users

To create an expense we need :
- amount
- paidBy
- splitType
- splits
- description
~~~~

UserController:
- list : List<User>
- // CRUD operations for User

UserExpenseBalanceSheet:
- friendBalance : Map<User, Balance>
- totalExpense : Double
- totalPayments : Double
- totalOwe : Double
- totalGetBack : Double

BalanceSheetController:


Balance:
- amountOwe : Double
- amountGetBack : Double

GroupController:
- list : List<Group>
- // CRUD operations for Group

ExpenseController:
- createExpense (
  String expenseId,
  String description,
  Double expenseAmount,
  List<Split> splitDetails,
  ExpenseSplitType splitType,
  User paidByUser
  )

SplitFactory:
- getSplitObject (ExpenseSplitType splitType)

ExpenseSplit (Interface):
- validateRequest(List<Split> splitList, Double totalAmount)
    - equalSplit()
    - unequalSplit()
    - percentageSplit()

Splitwise:
- userController : UserController
- groupController : GroupController


