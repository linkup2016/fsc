aws dynamodb create-table \
    --table-name ScratchCardTable \
    --attribute-definitions AttributeName=scratchCardNumber,AttributeType=S \
    --key-schema AttributeName=scratchCardNumber,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region us-east-2

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
    --region us-east-2
