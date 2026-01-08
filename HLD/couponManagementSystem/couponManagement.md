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
Use of Redis to Handle Lock Mechanism
=====================================
~~~
Why Redis is a good fit for coupon locking
Coupon lock has these properties:
- temporary (5 minutes)
- must auto-expire
- high contention (many users)
- must be fast
- must avoid race conditions
Redis gives you all of this out of the box.

👉 When your service writes to DB, it also updates ES.
Once we have saved to the database and the transaction commits, 
we asynchronously notify the indexer service, and the indexer service updates Elasticsearch.

Basic Redis lock idea (simple)
When user selects a coupon:

======================================
SET coupon:lock:C123 USER123 NX EX 300
======================================

What this means:
- NX → set only if key does NOT exist
- EX 300 → auto-expire after 5 minutes

If Redis returns OK:
👉 lock acquired

If Redis returns null:
👉 someone else already locked it

No cron job needed for expiry

How full flow looks with Redis
1️⃣ User locks coupon
Coupon Redeem Service:
- tries Redis lock
- if success → proceed
- if fail → reject

Redis:
coupon:lock:C123 = USER123 (TTL 300s)

DB:
status = LOCKED (optional, for visibility)

2️⃣ User redeems coupon

Redeem API:
1. Check Redis lock exists
2. Check USER123 matches
3. Apply discount
4. Save Redeem record
5. Mark coupon REDEEMED
6. Delete Redis lock

Redis:
DEL coupon:lock:C123

3️⃣ User abandons checkout

Redis TTL expires automatically after 5 minutes.
Lock is gone.
Coupon becomes available again.
No scheduler. No cleanup job. Clean.
~~~
Use of Elastic Search to Search Coupons
=======================================
~~~
At small scale, DB search is fine.
But when the system grows:
- millions of coupons
- very high read traffic
- free-text search (“10% laptop discount”)
- filters + sorting

DB problems:
- full table scans
- heavy indexes
- high latency
- expensive read replicas

So we introduce Elasticsearch (ES).

Important rule:
👉 Database is the source of truth
👉 Elasticsearch is only for search

===========================================================
Elasticsearch stores data as JSON documents inside indexes, 
not rows in tables.

If DB = Excel sheet
Then ES = Google search index
===========================================================

1️⃣ Index (like a table, but not really)
In Elasticsearch, data lives inside an index.
For coupons, you might have: coupons_index

Think of an index as:
- a collection of similar documents
- optimized for searching, not transactions

2️⃣ Document (like a row)
Each coupon is stored as a document (JSON).
Example document:
{
  "couponId": "C123",
  "category": "electronics",
  "description": "10% off on laptops",
  "type": "PERCENTAGE",
  "status": "ACTIVE",
  "expiryDate": "2026-02-10"
}

Each document:
- is independent
- has its own ID (often couponId)
- is immutable (updates = re-index)

3️⃣ Fields (like columns, but smarter)
Each key in the document is a field.
But here’s the key difference from DB:
👉 Fields are indexed for search

For example:
- description is broken into words
- category is indexed for filtering
- expiryDate is indexed for range queries

4️⃣ Inverted Index (the magic part)
This is the core of Elasticsearch.
Instead of storing like this (DB style):
====================
Coupon → description
====================

Elasticsearch stores:
========================
word → list of documents
========================
"laptop" → [C123, C456, C789]
"discount" → [C123, C999]
========================

So when user searches: 10% laptop discount

Elasticsearch:
- looks up each word
- finds matching documents
- ranks them
- returns results fast
That’s why search is so fast

5️⃣ Shards (how it scales)
At large scale, an index is split into shards.
================
coupons_index
 ├─ shard 1
 ├─ shard 2
 ├─ shard 3
================
Each shard:
- holds part of the data
- can live on different machines
- is searched in parallel
This is how ES handles millions of coupons.

6️⃣ Replicas (for reliability)
Each shard can have replicas.
Purpose:
fault tolerance
more read capacity
So if one node dies:
👉 search still works

7️⃣ What Elasticsearch is NOT good at
Important to say in interviews:
❌ transactions
❌ joins
❌ strict consistency
❌ frequent updates

That’s why:
- DB is source of truth
- ES is search-only
~~~

