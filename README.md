# Spring-Cafeteria
<img src="https://capsule-render.vercel.app/api?type=waving&color=BDBDC8&height=150&section=header" />

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
| Column Name | Data Type | Constraints                               |
|-------------|-----------|-------------------------------------------|
| `ID`        | `int`     | NOT NULL, AUTO_INCREMENT, PRIMARY KEY    |
| `MENU_ID`   | `int`     | NOT NULL, KEY                             |
| `MEMBER_ID` | `bigint`  | NOT NULL, KEY                             |
| `RATING`    | `int`     | NOT NULL                                  |
| `COMMENT`   | `text`    |                                           |
| `QUANTITY`  | `int`     | NOT NULL                                  |
| `CREATED_AT`| `timestamp` | DEFAULT CURRENT_TIMESTAMP               |

<img src="https://capsule-render.vercel.app/api?type=waving&color=BDBDC8&height=150&section=footer" />
