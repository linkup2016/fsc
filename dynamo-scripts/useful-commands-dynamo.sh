//Know which credentials are used for the DynamoDB client
aws sts get-caller-identity

//List all DynamoDB tables
aws dynamodb list-tables

//Describe a DynamoDB table
aws dynamodb describe-table --table-name ScratchCardTable --endpoint-url http://localhost:8000

//Delete a DynamoDB table
aws dynamodb delete-table --table-name ScratchCardTable --endpoint-url http://localhost:8000

//Explore items in a DynamoDB table
aws dynamodb scan --table-name ScratchCardTable --endpoint-url http://localhost:8000

//Delete all items in a DynamoDB table
aws dynamodb batch-write-item --table-name ScratchCardTable --endpoint-url http://localhost:8000 --request-items file://delete-all-items.json