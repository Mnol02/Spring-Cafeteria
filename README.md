# Spring-Cafeteria


Member Table
| Column Name      | Data Type                                     | Constraints             |
|------------------|----------------------------------------------|-------------------------|
| `id`             | `bigint`                                     | NOT NULL, PRIMARY KEY   |
| `nickname`       | `varchar(30)`                                | DEFAULT NULL            |
| `profile_image`  | `varchar(255)`                               | DEFAULT NULL            |
| `email`          | `varchar(30)`                                | DEFAULT NULL            |

Menu Table
| Column Name | Data Type | Constraints                               |
|-------------|-----------|-------------------------------------------|
| ID          | int       | NOT NULL, AUTO_INCREMENT, PRIMARY KEY    |
| NAME        | varchar(32) | NOT NULL                                |
| PRICE       | decimal(10,2) | NOT NULL                             |

Cart Table
| Column Name      | Data Type                                     | Constraints                          |
|------------------|----------------------------------------------|--------------------------------------|
| `ID`             | `int`                                       | NOT NULL, AUTO_INCREMENT, PRIMARY KEY |
| `MEMBER_ID`      | `bigint`                                    | NOT NULL                             |

Cart_Item Table
| Column Name | Data Type | Constraints                               |
|-------------|-----------|-------------------------------------------|
| ID          | int       | NOT NULL, AUTO_INCREMENT, PRIMARY KEY    |
| CART_ID     | int       | NOT NULL, KEY                             |
| MENU_ID     | int       | NOT NULL, KEY                             |
| QUANTITY    | int       | NOT NULL                                  |
