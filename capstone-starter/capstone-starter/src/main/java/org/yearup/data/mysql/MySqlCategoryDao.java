package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()  {
        List<Category> categories = new ArrayList<>();

        String sql = " SELECT * FROM categories";

        try(Connection connection = getConnection()){

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Category category = mapRow(resultSet);
                categories.add(category);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try(Connection connection = getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,categoryId);

            ResultSet row = preparedStatement.executeQuery();

            if (row.next()) {
                return mapRow(row);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String sql = "INSERT INTO categories(category_id,name,description)" + " VALUES (?,?,?);";

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

           int rowsAffected = statement.executeUpdate();

           if(rowsAffected > 0){

               ResultSet gkeys = statement.getGeneratedKeys();

               if(gkeys.next()){

                   int orderId = gkeys.getInt(1);

                   return getById(orderId);
               }
           }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String sql = "UPDATE categories " +
                "SET name = ? " +
                ", description = ? " +
                "WHERE category_id = ? ;";

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,category.getName());
            statement.setString(2,category.getDescription());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(int categoryId)
    {
        String sql = "SELECT FROM category" +
                "WHERE category_id = ? ;";

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,categoryId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return new Category(categoryId,name,description);
    }

}
