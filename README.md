# MySQL Data Access Layer - WEBSITE

This module provides the **JDBC-based data access layer** for the DELI-cious POS application. It handles all direct communication with the MySQL database using `PreparedStatement`s, `ResultSet` parsing, and proper exception handling.

---

## 📁 Key Classes

### `MySqlProductDao`
Implements `ProductDao` using raw JDBC to support:

- `search(...)` – Flexible product search using optional filters
- `listByCategoryId(int categoryId)` – Lists products by category
- `getById(int productId)` – Gets a single product
- `create(Product product)` – Inserts a new product
- `update(int productId, Product product)` – Updates product details
- `delete(int productId)` – Removes a product

Includes a reusable `mapRow(ResultSet row)` method for object mapping.

---

### `MySqlCategoryDao`
Implements `CategoryDao` with:

- `getAllCategories()` – Returns all categories
- `getById(int categoryId)` – Retrieves category by ID
- `create(Category category)` – Adds a new category
- `update(int categoryId, Category category)` – Updates category
- `delete(int categoryId)` – Deletes category

---

## 🧠 SQL Features

- Uses **`PreparedStatement`** for safe, secure SQL execution.
- Leverages **try-with-resources** to close all JDBC objects.
- Supports **null-safe search filtering** using default fallbacks:
  - `-1` for integers and decimals
  - `""` for strings

### Search Query Pattern (in `ProductDao`)
```sql
SELECT * FROM products
WHERE (category_id = ? OR ? = -1)
  AND (price <= ? OR ? = -1)
  AND (price >= ? OR ? = -1)
  AND (color = ? OR ? = '')

//Made changes to the frontend. Previously two Minimum range is now one "Minimum" and the other "Maximum".
