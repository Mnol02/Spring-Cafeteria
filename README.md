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

Review Table
# Review Table Creation

```sql
CREATE TABLE `review` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `MENU_ID` int DEFAULT NULL,
  `MEMBER_ID` bigint DEFAULT NULL,
  `RATING` int NOT NULL,
  `COMMENT` text,
  `CREATED_AT` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `MENU_ID` (`MENU_ID`),
  KEY `MEMBER_ID` (`MEMBER_ID`),
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`MENU_ID`) REFERENCES `menu` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`MEMBER_ID`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
