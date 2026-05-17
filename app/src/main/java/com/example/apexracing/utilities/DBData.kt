package com.example.apexracing.utilities

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.apexracing.models.Circuit
import com.example.apexracing.models.Constructor
import com.example.apexracing.models.Driver
import com.example.apexracing.models.FlatConstructorStanding
import com.example.apexracing.models.FlatDriverStanding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Date

object DBData {


    var drivers: List<Driver> = emptyList()
        private set
    var teams: List<Constructor> = emptyList()
        private set

    var circuits: List<Circuit> = emptyList()
        private set

    private var teamMap: Map<String, Constructor> = emptyMap()

    fun preloadAndWait(): Task<Void> {
        val db = FirebaseFirestore.getInstance()

        val driversTask = db.collection(Constants.FIRESTORE.SEASONS)
            .document("2026")
            .collection(Constants.FIRESTORE.DRIVERS)
            .get()
            .continueWith { task ->

                val snap = task.result ?: throw task.exception ?: Exception("Drivers task failed")


                drivers = snap.documents.map { doc ->
                    Driver.Builder(
                        id = doc.id,
                        code = doc.getString("code") ?: "",
                        constructor = doc.getDocumentReference("constructor"),
                        familyName = doc.getString("familyName") ?: "",
                        givenName = doc.getString("givenName") ?: "",
                        imgRef = doc.getString("imgRef") ?: "",
                        nationality = doc.getString("nationality") ?: "",
                        permanentNumber = (doc.getLong("permanentNumber") ?: 0L).toInt(),
                        points = (doc.getLong("points") ?: 0L).toInt(),
                        position = (doc.getLong("position") ?: 0L).toInt(),
                        fantasyPrice = (doc.getDouble("fantasyPrice") ?: 0L).toFloat()
                    ).build()

                }.sortedBy { it.position }

            }

        val teamsTask = db.collection(Constants.FIRESTORE.SEASONS)
            .document("2026")
            .collection(Constants.FIRESTORE.CONSTRUCTORS)
            .get()
            .continueWith { task ->
                val snap = task.result ?: throw task.exception ?: Exception("Teams task failed")
                teams = snap.documents.map { doc ->
                    Constructor.Builder(
                        id = doc.id,
                        driver1 = doc.getDocumentReference("driver1"),
                        driver2 = doc.getDocumentReference("driver2"),
                        imgRef = doc.getString("imgRef") ?: "",
                        name = doc.getString("name") ?: "",
                        nationality = doc.getString("nationality") ?: "",
                        points = (doc.getLong("points") ?: 0L).toInt(),
                        position = (doc.getLong("position") ?: 0L).toInt(),
                        fantasyPrice = (doc.getDouble("fantasyPrice") ?: 0L).toFloat()

                    ).build()
                }.sortedBy { it.position }

                teamMap = teams.associateBy { it.id }
            }

        val racesTask = db.collection(Constants.FIRESTORE.SEASONS)
            .document("2026")
            .collection(Constants.FIRESTORE.CIRCUITS)
            .get()
            .continueWith { task ->
                val snap = task.result ?: throw task.exception ?: Exception("Circuit task failed")
                circuits = snap.documents.map { doc ->
                    Circuit.Builder(
                        id = doc.id,
                        circuitName = doc.getString("circuitName"),
                        displayName = doc.getString("displayName"),
                        city = doc.getString("city") ?: "",
                        country = doc.getString("country") ?: "",
                        layoutRef = doc.getString("layoutRef") ?: "",
                        location = doc.getGeoPoint("location"),
                        startTime = (doc.getDate("startTime") ?: Date()),
                        round = (doc.getLong("round") ?: 0L).toInt(),
                        flagRef = (doc.getString("flagRef") ?: ""),
                        skylineRef = (doc.getString("skylineRef") ?: "")

                    ).build()
                }.sortedBy { it.round }

            }

        return Tasks.whenAllSuccess<Any>(driversTask, teamsTask, racesTask)
            .continueWith { t ->
                if (!t.isSuccessful) {
                    Log.e("DBData", "Preload failed", t.exception)
                    throw t.exception ?: Exception("Preload failed")
                }
                null
            }
    }

    fun getNextRace(): Circuit? {
        val now = Date()

        return circuits
            .sortedBy { it.startTime }
            .firstOrNull { it.startTime.after(now) }
    }

    fun updateDriversStatDB(drivers: List<FlatDriverStanding>) {
        //function that update the points & position in firebase

        val db = FirebaseFirestore.getInstance()

        val batch = db.batch()

        val driversRef = db.collection(Constants.FIRESTORE.SEASONS)
            .document("2026")
            .collection(Constants.FIRESTORE.DRIVERS)

        for (driver in drivers) {
            val driverRef = driversRef.document(driver.id)
            batch.update(driverRef, "points", driver.points)
            batch.update(driverRef, "position", driver.position)
        }


        batch.commit()
    }

    fun updateConstructorsStatDB(constructors: List<FlatConstructorStanding>) {


        val db = FirebaseFirestore.getInstance()

        val batch = db.batch()

        val teamsRef = db.collection(Constants.FIRESTORE.SEASONS)
            .document("2026")
            .collection(Constants.FIRESTORE.CONSTRUCTORS)

        for (team in constructors) {
            val teamRef = teamsRef.document(team.id)
            batch.update(teamRef, "points", team.points)
            batch.update(teamRef, "position", team.position)
        }

        batch.commit()

    }

    fun getUserFantasyRank(
        onResult: (rank: Int, totalUsers: Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .orderByChild("fantasyPoints")
            .get()
            .addOnSuccessListener { dataSnapshot ->

                val users = dataSnapshot.children.toList()

                val sortedUsers = users.sortedByDescending {
                    it.child("fantasyPoints").getValue(Int::class.java) ?: 0
                }

                val index = sortedUsers.indexOfFirst {
                    it.key == uid
                }

                if (index != -1) {
                    val rank = index + 1
                    onResult(rank, sortedUsers.size)
                }
            }
            .addOnFailureListener { error ->
                onError(error)
            }

    }

    fun updateFantasyScoresForAllUsers() {

        val usersRef = FirebaseDatabase
            .getInstance()
            .getReference("users")

        usersRef.get()
            .addOnSuccessListener { snapshot ->

                for (userSnapshot in snapshot.children) {

                    var score = 0

                    val driverIds = userSnapshot
                        .child("fantasyDriverIds")
                        .children
                        .mapNotNull { it.getValue(String::class.java) }

                    val constructorIds = userSnapshot
                        .child("fantasyConstructorIds")
                        .children
                        .mapNotNull { it.getValue(String::class.java) }



                    for (driverId in driverIds) {
                        val driver = drivers.find { it.id == driverId }


                        if (driver != null) {
                            score += driver.points
                        }
                    }

                    for (constructorId in constructorIds) {
                        val constructor = teams.find { it.id == constructorId }


                        if (constructor != null) {
                            score += constructor.points
                        }
                    }

                    userSnapshot.ref
                        .child("fantasyPoints")
                        .setValue(score)

                }
            }
    }
}

