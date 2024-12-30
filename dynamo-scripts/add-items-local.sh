# Replace `ScratchCardTable` with your table name
TABLE_NAME="ScratchCardTable"

# Insert each item into DynamoDB
aws dynamodb put-item --table-name $TABLE_NAME --item '{
    "scratchCardNumber": {"S": "8455-5716-0691-3646"},
    "id": {"N": "0"},
    "createdDate": {"S": "2024-12-26 15:12:46"},
    "redeemedDate": {"NULL": true},
    "balance": {"N": "25.0"},
    "pin": {"NULL": true},
    "redeemed": {"BOOL": false}
}' --endpoint-url http://localhost:8000

aws dynamodb put-item --table-name $TABLE_NAME --item '{
    "scratchCardNumber": {"S": "3155-7411-6274-0985"},
    "id": {"N": "0"},
    "createdDate": {"S": "2024-12-26 15:12:46"},
    "redeemedDate": {"NULL": true},
    "balance": {"N": "25.0"},
    "pin": {"NULL": true},
    "redeemed": {"BOOL": false}
}' --endpoint-url http://localhost:8000

aws dynamodb put-item --table-name $TABLE_NAME --item '{
    "scratchCardNumber": {"S": "6667-2302-1422-8753"},
    "id": {"N": "0"},
    "createdDate": {"S": "2024-12-26 15:12:46"},
    "redeemedDate": {"NULL": true},
    "balance": {"N": "25.0"},
    "pin": {"NULL": true},
    "redeemed": {"BOOL": false}
}' --endpoint-url http://localhost:8000

aws dynamodb put-item --table-name $TABLE_NAME --item '{
    "scratchCardNumber": {"S": "2486-2870-5895-7056"},
    "id": {"N": "0"},
    "createdDate": {"S": "2024-12-26 15:12:46"},
    "redeemedDate": {"NULL": true},
    "balance": {"N": "25.0"},
    "pin": {"NULL": true},
    "redeemed": {"BOOL": false}
}' --endpoint-url http://localhost:8000

aws dynamodb put-item --table-name $TABLE_NAME --item '{
    "scratchCardNumber": {"S": "7144-2335-7795-4401"},
    "id": {"N": "0"},
    "createdDate": {"S": "2024-12-26 15:12:46"},
    "redeemedDate": {"NULL": true},
    "balance": {"N": "25.0"},
    "pin": {"NULL": true},
    "redeemed": {"BOOL": false}
}' --endpoint-url http://localhost:8000

aws dynamodb put-item --table-name $TABLE_NAME --item '{
    "scratchCardNumber": {"S": "0997-5775-7181-3289"},
    "id": {"N": "0"},
    "createdDate": {"S": "2024-12-26 15:12:46"},
    "redeemedDate": {"NULL": true},
    "balance": {"N": "25.0"},
    "pin": {"NULL": true},
    "redeemed": {"BOOL": false}
}' --endpoint-url http://localhost:8000
