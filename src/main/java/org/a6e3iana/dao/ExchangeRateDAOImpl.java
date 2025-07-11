package org.a6e3iana.dao;

import jakarta.servlet.ServletException;
import org.a6e3iana.exceptions.ExceptionMessages;
import org.a6e3iana.exceptions.FailedConnectionToDataBaseException;
import org.a6e3iana.exceptions.NoteAlreadyExistException;
import org.a6e3iana.model.CrossExchanger;
import org.a6e3iana.model.Currency;
import org.a6e3iana.model.ExchangeRate;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {

    public static final String EXCHANGE_ALREADY_EXISTS = "Exchange rate already exists: ";

    private final DataSource dataSource;

    public ExchangeRateDAOImpl() {
        this.dataSource = DataSourceSingleton.getInstance().getDataSource();
    }

    @Override
    public Optional<ExchangeRate> findById(Integer integer) throws ServletException {
        throw new ServletException("No implemented method: findById in ExchangeRateDAOImpl");
    }

    @Override
    public void delete(Integer integer) throws ServletException {
        throw new ServletException("No implemented method: delete in ExchangeRateDAOImpl");
    }

    @Override
    public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) throws ServletException{
        final String query = "SELECT Ex.ID, Base.ID, Base.Code, Base.fullName, Base.Sign, " +
                        "Target.ID, Target.Code, Target.fullName, Target.Sign, Ex.Rate " +
                        "FROM ExchangeRates AS Ex " +
                        "JOIN Currencies AS Base ON Ex.BaseCurrencyID = Base.ID " +
                        "JOIN Currencies AS Target ON Ex.TargetCurrencyID = Target.ID " +
                        "WHERE Base.Code = ? AND Target.Code = ?;";
        ExchangeRate exchangeRate;

        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                exchangeRate = getExchangeRate(resultSet);
                return Optional.of(exchangeRate);
            } else {
                return Optional.empty();
            }
        }catch(SQLException e){
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
    }

    @Override
    public List<ExchangeRate> findAll() throws ServletException {
        final String query = "SELECT Ex.ID, Base.ID, Base.Code, Base.fullName, Base.Sign, " +
                        "Target.ID, Target.Code, Target.fullName, Target.Sign, Ex.Rate " +
                        "FROM ExchangeRates AS Ex " +
                        "JOIN Currencies AS Base ON Ex.BaseCurrencyID = Base.ID " +
                        "JOIN Currencies AS Target ON Ex.TargetCurrencyID = Target.ID;";

        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try(Connection connection = this.dataSource.getConnection();
            Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                exchangeRates.add(getExchangeRate(resultSet));
            }
        }catch (SQLException e) {
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate save(ExchangeRate exchangeRate) throws ServletException{
        final String query = "INSERT INTO ExchangeRates (BaseCurrencyID, TargetCurrencyID, Rate) " +
                             "VALUES (?, ?, ?);";

        try (Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, exchangeRate.getBaseCurrency().getId());
            statement.setInt( 2, exchangeRate.getTargetCurrency().getId());
            statement.setDouble(3, exchangeRate.getRate());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            exchangeRate.setId(generatedKeys.getInt(1));
        } catch(SQLException e){
            if(e.getErrorCode() == 19){
                throw new NoteAlreadyExistException(EXCHANGE_ALREADY_EXISTS +
                        exchangeRate.getBaseCurrency().getCode() +
                        exchangeRate.getTargetCurrency().getCode());
            }
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
        return exchangeRate;
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate exchangeRateRequest) throws ServletException{
        final String query = "UPDATE ExchangeRates " +
                             "SET rate = ? "+
                             "WHERE BaseCurrencyID = ? AND TargetCurrencyID = ?;";

        Optional<ExchangeRate> exchangeRateResponse;
        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setDouble(1, exchangeRateRequest.getRate());
            statement.setInt(2, exchangeRateRequest.getBaseCurrency().getId());
            statement.setInt(3, exchangeRateRequest.getTargetCurrency().getId());
            statement.executeUpdate();
            String baseCode = exchangeRateRequest.getBaseCurrency().getCode();
            String targetCode = exchangeRateRequest.getTargetCurrency().getCode();
            exchangeRateResponse = findByCodes(baseCode, targetCode);
        } catch(SQLException e){
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
        return exchangeRateResponse;
    }


    public List<CrossExchanger> getCrossExchangeRates (int baseCurrencyId, int targetCurrencyId) throws ServletException{
        final String query = "SELECT f.BaseCurrencyId, f.targetCurrencyId, f.rate, s.baseCurrencyId, s.targetCurrencyId, s.rate " +
                        "FROM ExchangeRates as f JOIN ExchangeRates as s " +
                        "WHERE "+
                        "(f.BaseCurrencyId=? AND ( (s.BaseCurrencyId=? AND f.targetCurrencyId=s.targetCurrencyId) OR " +
                        "(s.targetCurrencyId=? AND f.targetCurrencyId=s.BaseCurrencyId) ) ) " +
                        "OR " +
                        "(f.targetCurrencyId=? AND(  (s.BaseCurrencyId=? AND f.BaseCurrencyId=s.targetCurrencyId) OR " +
                        "(s.targetCurrencyId=? AND f.BaseCurrencyId=s.BaseCurrencyId) ) );";

        List<CrossExchanger> crossExchangers = new ArrayList<>();
        try(Connection connection = this.dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, baseCurrencyId);
            statement.setInt(4, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);
            statement.setInt(3, targetCurrencyId);
            statement.setInt(5, targetCurrencyId);
            statement.setInt(6, targetCurrencyId);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                CrossExchanger crossExchange = makeCrossExchangeRates(resultSet);
                crossExchange.calculateCrossRate(baseCurrencyId, targetCurrencyId);
                crossExchangers.add(crossExchange);
            }
        } catch (SQLException e){
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
        return crossExchangers;
    }

    private CrossExchanger makeCrossExchangeRates(ResultSet resultSet) throws ServletException{
        CrossExchanger crossExchanger = new CrossExchanger();
        try {
            crossExchanger.setFromPairBaseId(resultSet.getInt(1));
            crossExchanger.setFromPairTargetId(resultSet.getInt(2));
            crossExchanger.setFromPairRate(resultSet.getDouble(3));
            crossExchanger.setToPairBaseId(resultSet.getInt(4));
            crossExchanger.setToPairTargetId(resultSet.getInt(5));
            crossExchanger.setToPairRate(resultSet.getDouble(6));
        } catch (SQLException e) {
            throw new FailedConnectionToDataBaseException(ExceptionMessages.FAILED_CONNECTION);
        }
        return crossExchanger;
    }

    private ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException{
        int exchangeId = resultSet.getInt(1);
        int baseId = resultSet.getInt(2);
        String baseCode = resultSet.getString(3);
        String baseFullName = resultSet.getString(4);
        String baseSign = resultSet.getString(5);
        int targetId = resultSet.getInt(6);
        String targetCode = resultSet.getString(7);
        String targetFullName = resultSet.getString(8);
        String targetSign = resultSet.getString(9);
        double exRate = resultSet.getDouble(10);
        Currency base = new Currency(baseId, baseCode, baseFullName, baseSign);
        Currency target = new Currency(targetId, targetCode, targetFullName, targetSign);
        return new ExchangeRate(exchangeId, base, target, exRate);
    }
}
