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
For secured endpoints use the `Authorization` header with `Bearer your-generated-token`

### Category

- POST `/api/categories` *(Admin users only)*

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

- GET `/api/locations/user` *(Requires authentication)*

- GET `/api/locations/nearby?lat=42.2&lon=34`

- PATCH `/api/locations` *(Requires authentication)*

  All properties are optional for edition except id since it's used for location referral

    ```json
    {
     "id": 1,
     "name": "new name",
     "categories": [1],
     "description": "better description",
     "accessibility": "public",
     "coordinate": {
       "lat": 13.2,
       "lon": 21.2
     }
    }
    ```

- DELETE `/api/locations/{locationId}` *(Requires authentication)*


### Geo

- GET `/api/geo?lat=51.848637&lon=-0.55462`


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

