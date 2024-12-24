# Spring-Cafeteria
java-programming-application project(solo..).

## :computer: SoftWare
* `JAVA17`
* `STS4`
* `MySql`
* `Spring-Web` `JPA` `Lombok` `Thymeleaf`
* `Bootstrap` `WireShark`

## :memo: Todo

## :open_file_folder: Structure
Member Table
| Column Name | Data Type | Constraints |
|------------------|----------------------------------------------|-------------------------|
| ID | bigint | NOT NULL, PRIMARY KEY |
| NICKNAME | varchar(30) | DEFAULT NULL |
| PROFILE_IMAGE | varchar(255) | DEFAULT NULL |
| EMAIL | varchar(30) | DEFAULT NULL |

Menu Table
| Column Name | Data Type | Constraints |
|-------------|-----------|-------------------------------------------|
| ID | int | NOT NULL, AUTO_INCREMENT, PRIMARY KEY |
| NAME | varchar(32) | NOT NULL |
| PRICE | decimal(10,2) | NOT NULL |

Cart Table
| Column Name | Data Type | Constraints |
|------------------|----------------------------------------------|--------------------------------------|
| ID | int | NOT NULL, AUTO_INCREMENT, PRIMARY KEY |
| MEMBER_ID | bigint | NOT NULL |

Cart_Item Table
| Column Name | Data Type | Constraints |
|-------------|-----------|-------------------------------------------| 
| ID | int | NOT NULL, AUTO_INCREMENT, PRIMARY KEY |
| CART_ID | int | NOT NULL, KEY |
| MENU_ID | int | NOT NULL, KEY |
| QUANTITY | int | NOT NULL |

Review Table
| Column Name | Data Type | Constraints |
|-------------|-----------|-------------------------------------------|
| ID | int | NOT NULL, AUTO_INCREMENT, PRIMARY KEY | 
| MENU_ID | int | NOT NULL, KEY | 
| MEMBER_ID | bigint | NOT NULL, KEY |
| RATING | int | NOT NULL |
| COMMENT | text | | 
| QUANTITY | int | NOT NULL | 
| CREATED_AT| timestamp | DEFAULT CURRENT_TIMESTAMP |

## :wrench: Debug
![image](https://github.com/user-attachments/assets/f1cd002a-eca6-4476-810c-68923fc360f7)

[URL Rewriting 비활성화 -> response.encodeRedirectURL("/");](https://jaimemin.tistory.com/1516)
