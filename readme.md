# Warehouse Management API

## Project Overview
The Warehouse Management API allows users to manage warehouses and bays while each adhere to a set of constraints and requirements. This API is a take-home coding challenge for Möbel Höffner.

## Setup
This project does not require any third-party plugins. Decisions were made not to use industry standard plugins, such as Lombok, to allow this application to work out-of-the-box when cloned.

### Project Specifications
- Java 17
- Maven
- H2 in-memory database

### Step 1: Clone repository
```http
git clone git@github.com:RyanG702/moebelhoeffner.git
```

### Step 2: Run project locally
- Ensure that your IDE is using Java 17 JDK
- Run the application using your IDE of choice
- Application will run on port `:8080`

### Step 3: Interact with application
- Swagger UI: http://localhost:8080/swagger-ui/index.html#/
- H2 Database Console: http://localhost:8080/h2-console
  - Username: sa
  - Password: *leave blank*

### Step 4: Get started
When you first get started, there will be no `warehouse` or `bay` entities pre-loaded. Use the Swagger UI to add your first warehouse, and then bays to a warehouse.


## API Reference

### Warehouse

#### Create new warehouse

```http
POST /api/v1/warehouses/
```

```json
// Example payload
{
    "name": "Warehouse 1",
    "identifierCode": "111",
    "address": {
        "streetAddress": "Maple Dr",
        "city": "Berlin",
        "state": "GA",
        "postalCode": "63281"
    },
    "maxRows": 3,
    "maxLevels": 3,
    "maxShelves": 3
}
```

#### Get all warehouses

```http
GET /api/v1/warehouses
```

#### Get a warehouse

```http
GET /api/v1/warehouse/{identifierCode}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |

#### Delete a warehouse

```http
GET /api/v1/warehouses
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |


#### Update warehouse name

```http
PUT /api/v1/warehouses/{oldName}/name/{newName}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `oldName` | `string` | **Required**. Current warehouse name. |
| `newName` | `string` | **Required**. New warehouse name. |


#### Get all tags by warehouse

```http
GET /api/v1/warehouse/tags/{identifierCode}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |

### Bays

#### Create new bay in warehouse

```http
POST /api/v1/warehouses/{identifierCode}/bays
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |

```json
// Example payload
{
    "tags": [
        {
            "name": "leather"
        }
    ],
    "bayType": "PALLET",
    "holdingPoints": 4
}
```

#### Get all bays by warehouse

```http
GET /api/v1/warehouses/{identifierCode}/bays
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |

#### Delete a bay in warehouse

```http
DELETE /api/v1/warehouses/{identifierCode}/bays
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |

#### View the status of a bay's holding points

```http
GET /api/v1/warehouses/{identifierCode}/bays/{bayLabel}/holdingPoints/status
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |
| `bayLabel` | `string` | **Required**. The label of the bay. |

#### Mark a bay's holding points as "busy"

```http
POST /api/v1/warehouses/{identifierCode}/bays/{bayLabel}/busyHoldingPoints
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |
| `bayLabel` | `string` | **Required**. The label of the bay. |

```json
// Example payload
{
    "quantity": 1
}
```

#### Remove "busy" holding points from bay

```http
DELETE /api/v1/warehouses/{identifierCode}/bays/{bayLabel}/busyHoldingPoints/{quantity}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |
| `bayLabel` | `string` | **Required**. The label of the bay. |
| `quantity` | `integer` | **Required**. The quantity of holding points that should be marked as "free". |

#### Get all bays by tags

```http
GET /api/v1/warehouses/{identifierCode}/bays/byTags
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `identifierCode` | `string` | **Required**. The three-digit warehouse identifier. |
| `tags` | `array` | **Required**. Unique tags associated with a bay's contents.|

## Design Decisions
Some design decisions were made based on assumptions from reading the project brief:

| Topic | Design Decision                                                                                                                                                                                                                                                            |
| :----- |:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|Warehouse| User does not have control over bay placement. Based on warehouse size (rows, shelves, levels) chosen during warehouse creation, bays will automatically be populated and labeled.                                                                                         |
|Warehouse Removal| Warehouses can only be deleted after all bays have been deleted.                                                                                                                                                                                                           |
|Warehouse Updates| Only the warehouse name can be modified after warehouse creation to prevent changes made to warehouse configurations that would require recalulation of bay sequence.                                                                                                      |
|Bay Sequence| Bays are automatically assigned identifiers based on the next available bay location. Bays first fill all available horizontal space before moving up to the next level. New rows will be created once all available space has been used based on warehouse configuration. |
|Bay Tags| Bays must include at least one tag. Business requirements stated "Each tag is unique per bay, **cannot be empty** and should be all lower case.                                                                                                                            |
|Tag Search| Tags are limiting. The more tags used the fewer results returned.                                                                                                                                                                                                          |
|Bay Removal| Specific bays cannot be removed. Bays will be removed in the order they were added. This allows bays to be torn down the same way they were built up.                                                                                                                      |
|Holding Point Updates| Quanity of holding points is established during bay creation. After bay is created, the only change that can be made are the amounts of holding points that are `free` or `busy` in a bay.                                                                                 |