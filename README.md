## Cart Service
Cart Service is a Micronaut-based RESTful API that provides basic shopping cart functionality. It allows you to create and manage carts, add and remove items from carts, and calculate the total cost of the items in a cart.

## Features
- Create a new cart
- Add items to an existing cart
- Remove items from an existing cart
- Retrieve the details of an existing cart, including the items and their quantities
- Calculate the total cost of the items in a cart

## Prerequisites
- JDK 17
- Micronaut 3.x
- Gradle 7.x

## Getting Started
To build and run the project, follow these steps:

1. Clone the repository:
```
git clone https://github.com/yourusername/cartservice.git 
cd cartservice 
```
2. Build the project:
```
./gradlew clean build
```

3. Start a Docker mysql container
```
docker run -it --rm \
    -p 3306:3306 \
    -e MYSQL_DATABASE=db \
    -e MYSQL_USER=sherlock \
    -e MYSQL_PASSWORD=elementary \
    -e MYSQL_ALLOW_EMPTY_PASSWORD=true \
    mysql:8
```

4. Run the project:
```
./gradlew run
```
The Cart Service should now be running on http://localhost:8080.



## API Endpoints
Here are the available API endpoints for the Cart Service:

### Create a new cart
- **Endpoint**: /carts
- **Method**: POST
- **Request Body**: JSON object containing a product object with id, name, and price properties,  a quantity property as well as a command type
- **Response**: HTTP 200 with a JSON object containing the cartId

**curl example**:
```
curl -X POST -H "Content-Type: application/json" -d '{"product": {"id": "product1", "name": "Product 1", "price": 10}, "quantity": 1, "command":"ADD"}' http://localhost:8080/carts
```

### Add an item to an existing cart
- **Endpoint**: /carts/{cartId}
- **Method**: POST
- **Path Parameter**: cartId - the ID of the cart to update
- **Request Body**: JSON object containing a product object with id, name, and price properties, a quantity property as well as a command type
- **Response**: HTTP 200

**curl example**:
```
curl -X POST -H "Content-Type: application/json" -d '{"product": {"id": "product2", "name": "Product 2", "price": 5}, "quantity": 2, "command": "ADD"}' http://localhost:8080/carts/{cartId}
```

### Remove an item from an existing cart
- **Endpoint**: /carts/{cartId}
- **Method**: POST
- **Path Parameter**: cartId - the ID of the cart to update
- **Request Body**: JSON object containing a product object with id, name, and price properties, a quantity property as well as a command type
- **Response**: HTTP 200

**curl example**:
```
curl -X POST -H "Content-Type: application/json" -d '{"product": {"id": "product2", "name": "Product 2", "price": 5}, "quantity": 2, "command": "REMOVE"}' http://localhost:8080/carts/{cartId}
```

### Get the details of an existing cart
- **Endpoint**: /carts/{cartId}
- **Method**: GET
- **Path Parameter**: cartId - the ID of the cart to retrieve
- **Response**: HTTP 200 with a JSON object representing the cart, including the id, items, and total properties

**curl example**:
```
curl -X GET http://localhost:8080/carts/{cartId}
```
    
## Testing
To run the tests for the project, execute the following command:

```
./gradlew test
```
This command will run all the unit and integration tests for the Cart Service.

License
This project is licensed under the MIT License.