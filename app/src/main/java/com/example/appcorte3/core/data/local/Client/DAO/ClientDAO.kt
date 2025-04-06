package com.example.appcorte3.core.data.local.Client.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appcorte3.core.data.local.Client.entities.ClientEntity

@Dao
interface ClientDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity)

    @Query("SELECT * FROM Clients")
    suspend fun getAllClients(): List<ClientEntity>

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Delete
    suspend fun deleteClient(client: ClientEntity)

    @Query("SELECT * FROM Clients where sended = 0")
    suspend fun getNoSendedClients(): List<ClientEntity>

    @Query("UPDATE Clients set sended = 1 where id = :clientId")
    suspend fun markClientAsSended(clientId: String)
}