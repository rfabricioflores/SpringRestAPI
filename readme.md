# Rest API with Spring Boot & MySQL database

## Run the project
1. Clone the project `git clone https://github.com/rfabricioflores/SpringRestAPI.git`
2. Rename `env.example` to `.env` and insert your credentials
3. Run `docker compose up` to start the database
4. Run the project with the dev profile

## Requirements
- Docker
- JDK 21
- Code editor like intellij or vscode

## Endpoints

### Category

- POST `/api/categories`*(Admin users only)*
    ```json
    {
      "name": "Lakes",
      "symbol": "💧",
      "description": "very very big lakes"
    }
    ```
- GET `/api/categories`
- GET `/api/categories/{categoryId}`


### Location
- POST `/api/locations` *(Requires authentication)*

    ```json
    {
     "name": "cool place",
     "categories": [1, 2, 3],
     "description": "yes",
     "accessibility": "private",
     "coordinate": {
       "lat": 43.2,
       "lon": 29
     }
    }
    ```

- GET `/api/locations/public`

- GET `/api/locations/public/{locationId}`

- GET `/api/locations/public?category=1`

**# Not implemented yet**

- GET `/api/locations/user` *(Requires authentication)*

- GET `/api/locations/nearby`

- PUT/PATCH `/api/locations/{locationId}` *(Requires authentication)*

- DELETE `/api/locations/{locationId}` *(Requires authentication)*


### Authentication

- POST `/api/auth/register` *(Creates new user)*
    ```json
    {
      "username": "test",
      "password": "awesome"
    }
    ```

- POST `/api/auth/login` *(Returns JWT token)*
    ```json
    {
      "username": "test",
      "password": "awesome"
    }
    ```

