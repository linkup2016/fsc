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


aws ecr create-repository --repository-name fsc-container-repo --region us-east-2

# Fetches user name and token from AWS ECR so that Docker can authenticate

aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 593793059395.dkr.ecr.us-east-2.amazonaws.com

docker tag fsc 593793059395.dkr.ecr.us-east-2.amazonaws.com/fsc-container-repo:latest

docker push 593793059395.dkr.ecr.us-east-2.amazonaws.com/fsc-container-repo:latest

docker run -p 8080:8080 fsc




