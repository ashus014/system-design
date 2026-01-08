https://leetcode.com/discuss/post/6017121/coupon-management-system-system-design-i-ld1y/

Problem Statement:
------------------
~~~
Design a coupon generation system. 
- System will allow users to create discount coupons, manage these coupons. 
- Also, system should be able to validate the coupons.
- One user should get single coupn per day. 
- Once a user gets a coupon he/she has to buy something on the website with in 5 mins otherwise coupon will be availbale for other user. 
- Coupons should not redeemed multiple times. 
- System should focus on managing the coupons.
~~~

Functional Requirements:
------------------------
~~~
1. Admin should be able to create 100k coupons daily.
2. User should be able to search coupons.
3. User should be able to get coupon and redeem it.
4. One user should get single coupn per day.
5. Once a user gets a coupon he/she has to buy something on the website with in 5 mins.
6. If user did not use coupon 5 in minute then coupon will be availbale for other user.
7. System should be able to validate the coupons(user can not be redeemed multiple times same coupon)
~~~

Non - Functional Requirements:
------------------------------
~~~
1. The system should have high availability for searching & viewing coupons, 
but should consistant for getting coupons (only one user should get a coupon)
2. The system should have low latency search
3. The system should be scalable and able to handle high throughput in the form of popular coupons(10M users for 100k coupons)
4. The system is read heavy, and thus needs to be able to support high read throughput (100:1)
~~~

Core Entities:
--------------
~~~
1. Coupon: This Entity store essential information about a coupon, including details like the date, description, type. 
It acts as the central point of information for each unique coupon.
2. Redeem: Represents the essential information about redeem.
3. User: Represents the individual interacting with the system.
~~~

APIs:
-----
~~~
1. Create Coupon

POST: /coupons → returns couponId

Who uses it : Usually admin or merchant.
What it does : Creates a new coupon in the system.

Request Body:
POST /coupons
{
  "category": "electronics",
  "description": "10% off",
  "expiryDate": "2026-01-31",
  "type": "PERCENTAGE"
}

Response:
201 Created
{
  "couponId": "COUP123"
}
~~~

~~~
2. Search Coupon

GET: /coupons/search?search_term={search_term}&type={type}

Who uses it : App users browsing available coupons.
What it does : Returns list of coupons matching filters.

Request Body:
/coupons/search?search_term=laptop&type=PERCENTAGE

Response:
[
  {
    "couponId": "COUP123",
    "description": "10% off laptops",
    "type": "PERCENTAGE",
    "category": "electronics"
  }
]
~~~

~~~
3. Get (Locked) Coupon

POST: /coupons/locked/{couponId} → returns Coupon

“Locked coupon” usually means:
- User selects a coupon
- System locks it temporarily
- No one else can redeem it during that time

Why POST instead of GET?
Because something is changing (we are locking it), so POST is reasonable.

Flow:
User clicks “use coupon”
Backend marks coupon status as:
---
LOCKED
lockedByUser = USER123
lockExpiresAt = timestamp
---

Response:
{
  "couponId": "COUP123",
  "status": "LOCKED"
}
~~~

~~~
4. Redeem Coupon

POST: /coupons/redeem/{couponId}

Who uses it : User actually applies coupon at checkout.

What happens
- Validates coupon
- Checks lock
- Marks coupon as REDEEMED
- Prevents reuse (if single-use coupon)

Request Body:
POST /coupons/redeem/COUP123
{
  "userId": "USER123",
  "orderId": "ORDER999"
}

Response:
{
  "status": "SUCCESS",
  "discountApplied": 120,
  "finalPrice": 880
}

Important validations
- coupon is not expired
- coupon belongs to user (if personalized)
- coupon not already redeemed
- coupon was previously locked (optional but good practice- because lock might get released during redeem)
~~~

High Level Design:
------------------

1. Admin can create coupon using POST:/coupons api it will return success or fail. 
Coupon Generation Service will handle the request and it will create coupon based on 
the request body and save it into the Database.
~~~
1️⃣ Admin creates coupon

Admin can create coupon using POST:/coupons api
Coupon Generation Service will handle the request and save it in DB

What this really means:
- Admin hits POST /coupons
- Coupon Generation Service:
    - validates request
    - generates couponId
    - sets initial status = ACTIVE
    - saves coupon in DB


At this stage in DB:
===================
- couponId = C123
- status   = ACTIVE
===================
~~~

2. User can search a coupon based on the some seach term or coupon type. Search Service will handle this request. 
It will search coupon in Database directly and return results to the user. 
Note: This search will be slow because we needs to scan entire database.
~~~
2️⃣ User searches coupon (slow search)

- User can search coupon based on search term or type
- Search Service searches DB directly
Note: This search will be slow because we scan entire DB

What they’re saying:
- User opens app → types something like “electronics” or “10%”
- Search Service runs queries like:

=========================================================
SELECT * FROM coupons WHERE category LIKE '%electronics%'
=========================================================

Why “slow”?
Because:
- No indexes
- Large coupon table
- Full table scan

👉 This is acceptable for basic HLD, but interviewer may later ask:
“How will you optimize search?”

(Answer: indexes, Elasticsearch, cache — but not needed yet.)
~~~

3. Once coupon is presented to user on broser or mobile app user can select coupon, then user will get the couponid or coupon code. 
In the database status of coupon will be locaked so than no other user can get that coupon.
~~~
3️⃣ User selects coupon → coupon gets locked
- Once coupon is presented, user selects coupon
- Coupon status becomes locked so no other user can get it

This is the most important step.
What actually happens:
- User clicks “Apply coupon”
- App sends:
=============================
POST /coupons/{couponId}/lock
=============================

Backend updates DB:
===============================
status        = LOCKED
lockedBy      = USER123
lockExpiresAt = now + 5 minutes
===============================

Meaning:
👉 “This coupon is reserved ONLY for this user for 5 minutes.”

Why?
To prevent:
- two users using same coupon
- race conditions
- double discount
~~~

4. User has to redeem coupon in 5 mins, if coupon will not redeemed then it has to put it back to available state 
in database so that other user can use it. 
~~~
4️⃣ Redeem within 5 minutes or release lock

- User has to redeem in 5 mins
- If not redeemed, put it back to available state

This is lock expiration.
Two possible flows:

✅ Case A: User redeems within 5 min
===============================
POST /coupons/redeem/{couponId}
===============================

System checks:
- coupon is LOCKED
- lockedBy is same user
- lock not expired
=================
status = REDEEMED
=================

❌ Case B: User does nothing (timeout) / payment fails

After 5 minutes:
- background job / scheduler runs
- finds expired locks
- resets coupon
======================
status        = ACTIVE
lockedBy      = null
lockExpiresAt = null
======================

If interviewer pushes further, they may ask
- How do you implement lock expiration?
- What if two users try to lock at same time?
- How do you optimize search?
- Do you use DB lock or application lock?
~~~

Object Modeling:
----------------

Coupons
~~~
- couponId : String
- category : String
- description : String
- dateCreated : Date
- type : Enum (PERCENTAGE, FLAT)
- status : Enum (ACTIVE, LOCKED, REDEEMED, EXPIRED)
- createdTimeStamp : DateTime
- lockedTimeStamp : DateTime
- userId : String
~~~

Redeem
~~~
- redeemId : String
- couponId : String
- userId : String
- orderId : String
- redeemTimeStamp : DateTime
~~~

Users
~~~
- userId : String
- firstName : String
- lastName : String
- email : String
- phoneNumber : String
~~~

Deep Dive
---------
~~~

~~~
