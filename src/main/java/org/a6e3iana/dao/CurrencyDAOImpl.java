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

    public static final String SQL_GET_ALL = "SELECT * FROM Currencies;";
    public static final String SQL_PREPARED_GET_BY_CODE = "SELECT * FROM Currencies WHERE Code=?;";
    public static final String SQL_PREPARED_INSERT = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?);";
    public static final String SQL_PREPARED_UPDATE = "UPDATE Currencies SET Code = ?, FullName = ?, Sign = ? WHERE ID = ?;";
    public static final String SQL_PREPARED_GET_BY_ID = "SELECT * FROM Currencies WHERE ID = ?;";

    private final DataSource dataSource;

    public CurrencyDAOImpl() {
        this.dataSource = DataSourceSingleton.getInstance().getDataSource();
    }

    @Override
    public List<Currency> getAll() throws ServletException {
        try(Connection connection = this.dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_GET_ALL);
            List<Currency> currencies = new ArrayList<>();
            int id;
            String code, fullName, sign;
            while(resultSet.next()){
                id = resultSet.getInt(1);
                code = resultSet.getString(2);
                fullName = resultSet.getString(3);
                sign = resultSet.getString(4);
                currencies.add(new Currency(id, code, fullName, sign));
            }
            return currencies;
        } catch (SQLException e){
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
    }

    @Override
    public Optional<Currency> findByCode(String code) throws ServletException{
        try(Connection connection = this.dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL_PREPARED_GET_BY_CODE);
            statement.setString(1, code);
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                int id = result.getInt(1);
                String fullName = result.getString(3);
                String sign = result.getString(4);
                return Optional.of(new Currency(id, code, fullName, sign));
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
    }

    public Optional<Currency> findById(int id) throws ServletException{
        try(Connection connection = this.dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL_PREPARED_GET_BY_ID);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                String code = result.getString(2);
                String fullName = result.getString(3);
                String sign = result.getString(4);
                return Optional.of(new Currency(id, code, fullName, sign));
            }else{
                return Optional.empty();
            }
        } catch (SQLException e){
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
    }

    @Override
    public Currency save(Currency currency) throws ServletException{
        try (Connection connection = this.dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL_PREPARED_INSERT,
                                                                                Statement.RETURN_GENERATED_KEYS);
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
        try(Connection connection = this.dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL_PREPARED_UPDATE);
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

}
