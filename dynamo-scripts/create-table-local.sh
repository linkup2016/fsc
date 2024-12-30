aws dynamodb create-table \
    --table-name ScratchCardTable \
    --attribute-definitions AttributeName=scratchCardNumber,AttributeType=S AttributeName=createdDate,AttributeType=S \
    --key-schema AttributeName=scratchCardNumber,KeyType=HASH AttributeName=createdDate,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000

aws dynamodb put-item \
    --table-name ScratchCardTable \
    --item '{
        "id": {"N": "1"},
        "scratchCardNumber": {"S": "SCRATCH12345"},
        "createdDate": {"S": "2024-12-27"},
        "redeemedDate": {"S": ""},
        "isRedeemed": {"BOOL": false},
        "balance": {"N": "100.50"},
        "pin": {"S": "1234"}
    }' \
    --endpoint-url http://localhost:8000
read -p "Press Enter to close..."  //pauses the bash window until enter is pressed
