# Recipe
# Table of contents
<!-- TOC -->
* [Recipe](#recipe)
* [Table of contents](#table-of-contents)
* [Recipe](#recipe-1)
    * [Main Business Features](#main-business-features)
    * [Installing](#installing)
    * [Run with IDE (__Intellij IDEA__)](#run-with-ide-intellij-idea)
  * [Specification](#specification)
    * [Create a user](#create-a-user)
      * [URL](#url)
      * [Description](#description)
      * [Request](#request)
      * [Request example](#request-example)
      * [Response](#response)
      * [Result codes](#result-codes)
    * [Create a recipe](#create-a-recipe)
      * [URL](#url-1)
      * [Description](#description-1)
      * [Request](#request-1)
      * [Request example](#request-example-1)
      * [Response](#response-1)
      * [Response example](#response-example)
      * [Result codes](#result-codes-1)
    * [Find a recipe by id](#find-a-recipe-by-id)
      * [URL](#url-2)
      * [Description](#description-2)
      * [Response](#response-2)
      * [Response example](#response-example-1)
      * [Result codes](#result-codes-2)
    * [Search a recipe by name/category](#search-a-recipe-by-namecategory)
      * [URL](#url-3)
      * [Description](#description-3)
      * [Response](#response-3)
      * [Response example](#response-example-2)
      * [Result codes](#result-codes-3)
    * [Update recipe by specified id](#update-recipe-by-specified-id)
      * [URL](#url-4)
      * [Description](#description-4)
      * [Request](#request-2)
      * [Request example](#request-example-2)
      * [Response](#response-4)
      * [Result codes](#result-codes-4)
    * [Delete recipe by specified id](#delete-recipe-by-specified-id)
      * [URL](#url-5)
      * [Description](#description-5)
      * [Request](#request-3)
      * [Response](#response-5)
      * [Result codes](#result-codes-5)
<!-- TOC -->
# Recipe
Application represents service for managing recipes.
That is REST service.
Each HTTP command returns response in JSON format.

### Main Business Features
1. Register a user
2. Create a recipe
3. Update a recipe (for recipe author only)
4. Delete a recipe (for recipe author only)
5. Find a recipe by id
6. Search list of recipes by category/name

Important: all features except of registration are for authanticated users only (basic authentication used).

### Installing
To install app you need:
- Install jdk 17

### Run with IDE (__Intellij IDEA__)
1. Open the project in IDE
2. Go to Main class
3. IDE Menu -> Run
4. Embedded Tomcat should start
5. Send HTTP requests using way you like (IDE, postman, cURL)
6. Get response

## Specification
Note: for local usage endpoint should start with localhost:8881
### Create a user
#### URL
POST /api/register
#### Description
Creates a new user in recipe app.
#### Request
| Field name | Required | Type   | Description   | Constraints             |
|------------|----------|--------|---------------|-------------------------|
| email      | yes      | String | username      | valid emails only       |
| password   | yes      | String | user password | min length is 8 symbols |

#### Request example
```json
{
  "email": "zheltov_dmitriy4@mail.ru",
  "column": "12345678"
}
```
#### Response
without body

#### Result codes
| resultCode   | Description                                                                                  |
|--------------|----------------------------------------------------------------------------------------------|
| OK           | User successfully created                                                                    |
| BAD_REQUEST  | User with such username already exists / required params are missing or has incorrect format |


### Create a recipe
#### URL
POST /api/recipe/new
#### Description
Creates a new recipe. Requires basic user authentication (login/password).
#### Request
| Field name  | Required | Type                         | Description                         |
|-------------|----------|------------------------------|-------------------------------------|
| name        | yes      | String                       | name (e.g. Cake)                    |
| category    | yes      | String                       | category (e.g. Dessert)             |
| description | yes      | String                       | description (short explanation)     |
| ingredients | yes      | Array of Ingredients objects | recipe ingredients                  |
| directions  | yes      | Array of Directions objects  | directions about cooking the recipe |

#### Request example
```json
{
  "name": "Mint Tea",
  "category": "beverage",
  "description": "Light, aromatic and refreshing beverage, ...",
  "ingredients": ["boiled water", "honey", "fresh mint leaves"],
  "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
#### Response
| Field name | Required | Type                         | Description     |
|------------|----------|------------------------------|-----------------|
| id         | yes      | String                       | Recipe id in DB |

#### Response example
```json
{
  "id": 11
}
```

#### Result codes
| resultCode   | Description                                         |
|--------------|-----------------------------------------------------|
| OK           | Recipe successfully created                         |
| BAD_REQUEST  | Required params are missing or has incorrect format |
| UNAUTHORIZED | Invalid/missing credentials                         |




### Find a recipe by id
#### URL
GET /api/recipe/{id}
#### Description
Retrieves recipe details by specified id. Requires basic user authentication (login/password).

#### Response
| Field name  | Required | Type                         | Description                         |
|-------------|----------|------------------------------|-------------------------------------|
| name        | yes      | String                       | name (e.g. Cake)                    |
| category    | yes      | String                       | category (e.g. Dessert)             |
| description | yes      | String                       | description (short explanation)     |
| ingredients | yes      | Array of Ingredients objects | recipe ingredients                  |
| date        | yes      | Date/Time                    | created date/time                   |
| directions  | yes      | Array of Directions objects  | directions about cooking the recipe |
#### Response example
```json
{
  "name": "Mint Tea",
  "category": "beverage",
  "date": "2023-09-27T17:18:14.817904",
  "description": "Light, aromatic and refreshing beverage, ...",
  "ingredients": [
    "boiled water",
    "honey",
    "fresh mint leaves"
  ],
  "directions": [
    "Boil water",
    "Pour boiling hot water into a mug",
    "Add fresh mint leaves",
    "Mix and let the mint leaves seep for 3-5 minutes",
    "Add honey and mix again"
  ]
}
```

#### Result codes
| resultCode   | Description                   |
|--------------|-------------------------------|
| OK           | Recipe successfully retrieved |
| UNAUTHORIZED | Invalid/missing credentials   |

### Search a recipe by name/category
#### URL
GET /api/recipe/search?name={someName} OR\
GET /api/recipe/search?category={someCategory}
#### Description
Retrieves list of recipes by specified name or category. 
Requires basic user authentication (login/password).

#### Response
Array of Recipe objects
#### Response example
```json
[
  {
    "name": "Warming Dmitrius Rom",
    "category": "drinks",
    "date": "2023-09-26T21:56:16.63452",
    "description": "Ginger tea is a warming drink for cool weather, ...",
    "ingredients": [
      "1 inch ginger root, minced",
      "1/3 lemon, juiced",
      "3/2 teaspoon manuka rom"
    ],
    "directions": [
      "Ok, Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)",
      "Steep for 5-10 minutes",
      "Drink and enjoy"
    ]
  },
  {
    "name": "Warming Maple Tea",
    "category": "beverage",
    "date": "2023-09-02T23:17:12.252406",
    "description": "Ginger tea is a warming drink for cool weather, ...",
    "ingredients": [
      "1 inch ginger root, minced",
      "1/2 lemon, juiced",
      "1/2 teaspoon manuka honey"
    ],
    "directions": [
      "Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)",
      "Steep for 5-10 minutes",
      "Drink and enjoy"
    ]
  },
  {
    "name": "Warming Dmitriy Tea",
    "category": "beverages",
    "date": "2023-09-02T20:50:07.821666",
    "description": "Ginger tea is a warming drink for cool weather, ...",
    "ingredients": [
      "1 inch ginger root, minced",
      "1/3 lemon, juiced",
      "3/2 teaspoon manuka honey"
    ],
    "directions": [
      "Ok, Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)",
      "Steep for 5-10 minutes",
      "Drink and enjoy"
    ]
  }
]
```

#### Result codes
| resultCode   | Description                                         |
|--------------|-----------------------------------------------------|
| OK           | Recipe list successfully retrieved                  |
| UNAUTHORIZED | Invalid/missing credentials                         |
| BAD_REQUEST  | Required params are missing or has incorrect format |


### Update recipe by specified id
#### URL
PUT /api/recipe/{id}
#### Description
Updates recipe details. Requires basic user authentication (login/password).
For recipe authors only
#### Request
| Field name  | Required | Type             | Description                         |
|-------------|----------|------------------|-------------------------------------|
| name        | yes      | String           | name (e.g. Cake)                    |
| category    | yes      | String           | category (e.g. Dessert)             |
| description | yes      | String           | description (short explanation)     |
| ingredients | yes      | Array of Strings | recipe ingredients                  |
| directions  | yes      | Array of Strings | directions about cooking the recipe |

#### Request example
```json
{
  "name": "Mint Tea",
  "category": "beverage",
  "description": "Light, aromatic and refreshing beverage, ...",
  "ingredients": ["boiled water", "honey", "fresh mint leaves"],
  "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}
```
#### Response
without body

#### Result codes
| resultCode   | Description                                         |
|--------------|-----------------------------------------------------|
| NO CONTENT   | Recipe successfully updated                         |
| BAD_REQUEST  | Required params are missing or has incorrect format |
| UNAUTHORIZED | Invalid/missing credentials                         |
| FORBIDDEN    | User is not an author of recipe                     |


### Delete recipe by specified id
#### URL
DELETE /api/recipe/{id}
#### Description
Delete a recipe. Requires basic user authentication (login/password).
For recipe authors only
#### Request
without body

#### Response
without body

#### Result codes
| resultCode   | Description                                         |
|--------------|-----------------------------------------------------|
| NO CONTENT   | Recipe successfully deleted                         |
| BAD_REQUEST  | Required params are missing or has incorrect format |
| UNAUTHORIZED | Invalid/missing credentials                         |
| FORBIDDEN    | User is not an author of recipe                     |
