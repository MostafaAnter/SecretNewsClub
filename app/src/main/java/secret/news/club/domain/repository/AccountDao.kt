package secret.news.club.domain.repository

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import secret.news.club.domain.model.account.Account

@Dao
interface AccountDao {

    @Query(
        """
        SELECT * FROM account
        WHERE id = :id
        """
    )
    fun queryAccount(id: Int): Flow<Account?>

    @Query(
        """
        SELECT * FROM account
        """
    )
    suspend fun queryAll(): List<Account>

    @Query(
        """
        SELECT * FROM account
        """
    )
    fun queryAllAsFlow(): Flow<List<Account>>

    @Query(
        """
        SELECT * FROM account
        WHERE id = :id
        """
    )
    suspend fun queryById(id: Int): Account?

    @Insert
    suspend fun insert(account: Account): Long

    @Insert
    suspend fun insertList(accounts: List<Account>): List<Long>

    @Update
    suspend fun update(vararg account: Account)

    @Delete
    suspend fun delete(vararg account: Account)
}
