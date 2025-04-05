package org.a6e3iana.dao;

import jakarta.servlet.ServletException;
import org.a6e3iana.exceptions.ExceptionMessages;
import org.a6e3iana.exceptions.FailedConnectionToDataBaseException;
import org.a6e3iana.exceptions.NoteAlreadyExistException;
import org.a6e3iana.model.Currency;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl implements CurrencyDAO{

    public static final String CURRENCY_ALREADY_EXISTS = "Currency already exists: ";

    private final DataSource dataSource;

    public CurrencyDAOImpl() {
        this.dataSource = DataSourceSingleton.getInstance().getDataSource();
    }

    @Override
    public void delete(Integer integer) throws ServletException {
        throw new ServletException("No implementation method: delete, in Currency DAO");
    }

    @Override
    public List<Currency> findAll() throws ServletException {
        final String query = "SELECT * FROM Currencies;";

        try(Connection connection = this.dataSource.getConnection();
            Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(query);
            List<Currency> currencies = new ArrayList<>();
            while(resultSet.next()){
                currencies.add(getCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e){
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
    }

    @Override
    public Optional<Currency> findByCode(String code) throws ServletException{
        final String query = "SELECT * FROM Currencies WHERE Code=?;";

        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, code);
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                return Optional.of(getCurrency(result));
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
    }

    @Override
    public Optional<Currency> findById(Integer id) throws ServletException{
        final String query = "SELECT * FROM Currencies WHERE ID = ?;";

        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(getCurrency(resultSet));
            }else{
                return Optional.empty();
            }
        } catch (SQLException e){
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
    }

    @Override
    public Currency save(Currency currency) throws ServletException{
        final String query = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?);";

        try (Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            ResultSet generatedKey = statement.getGeneratedKeys();
            currency.setId(generatedKey.getInt(1));
        } catch (SQLException e) {
            if(e.getErrorCode() == 19){
                throw new NoteAlreadyExistException(CURRENCY_ALREADY_EXISTS + currency.getCode());
            }
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
        return currency;
    }

    @Override
    public Optional<Currency> update(Currency currency) throws ServletException {
        final String query = "UPDATE Currencies SET Code = ?, FullName = ?, Sign = ? WHERE ID = ?;";

        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.setInt(4, currency.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
        return findById(currency.getId());
    }

    private Currency getCurrency(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt(1);
        String code = resultSet.getString(2);
        String fullName = resultSet.getString(3);
        String sign = resultSet.getString(4);
        return new Currency(id, code, fullName, sign);
    }

}
