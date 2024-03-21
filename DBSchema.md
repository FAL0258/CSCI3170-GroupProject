# book
- ISBN
    - CHAR(13)
    - NOT NULL
- bTitle
    - VARCHAR(100)
    - NOT NULL
- price
    - INTEGER
    - NOT NULL
- copies
    - INTEGER
    - NOT NULL
- PK: (ISBN)

# customer
- cID
    - VARCHAR(10)
    - NOT NULL
- cName
    - VARCHAR(50)
    - NOT NULL
- address
    - VARCHAR(200)
    - NOT NULL
- cred_card
    - CHAR(19)
    - NOT NULL
- PK: (cID)

# orders
- oID
    - CHAR(8)
    - NOT NULL
- oDate
    - CHAR(10)
    - NOT NULL
- status
    - CHAR(1)
    - NOT NULL
- charge
    - INTEGER
    - NOT NULL
- cID
    - VARCHAR(10)
    - NOT NULL
- PK: (oID)

# bookOrdered
- oID
    - CHAR(8)
    - NOT NULL
- ISBN
    - CHAR(13)
    - NOT NULL
- quantity
    - INTEGER
    - NOT NULL
- PK: (oID, ISBN)

# authorship
- ISBN
    - CHAR(13)
    - NOT NULL
- aName
    - VARCHAR(50)
    - NOT NULL
- PK: (ISBN, aName)