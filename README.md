# Spring-Cafeteria


Member Table
| Column Name      | Data Type                                     | Constraints             |
|------------------|----------------------------------------------|-------------------------|
| `ID`             | `bigint`                                     | NOT NULL, PRIMARY KEY   |
| `NICKNAME`       | `varchar(30)`                                | DEFAULT NULL            |
| `PROFILE_IMAGE`  | `varchar(255)`                               | DEFAULT NULL            |
| `EMAIL`          | `varchar(30)`                                | DEFAULT NULL            |

Menu Table
| Column Name | Data Type | Constraints                               |
|-------------|-----------|-------------------------------------------|
| `ID`          | `int`       | NOT NULL, AUTO_INCREMENT, PRIMARY KEY    |
| `NAME`        | `varchar(32)` | NOT NULL                                |
| `PRICE`       | `decimal(10,2)` | NOT NULL                             |

Cart Table
| Column Name      | Data Type                                     | Constraints                          |
|------------------|----------------------------------------------|--------------------------------------|
| `ID`             | `int`                                       | NOT NULL, AUTO_INCREMENT, PRIMARY KEY |
| `MEMBER_ID`      | `bigint`                                    | NOT NULL                             |

Cart_Item Table
| Column Name | Data Type | Constraints                               |
|-------------|-----------|-------------------------------------------|
| `ID`          | `int`       | NOT NULL, AUTO_INCREMENT, PRIMARY KEY    |
| `CART_ID`     | `int`       | NOT NULL, KEY                             |
| `MENU_ID`     | `int`       | NOT NULL, KEY                             |
| `QUANTITY`    | `int`       | NOT NULL                                  |
