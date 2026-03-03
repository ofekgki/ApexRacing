package com.example.apexracing.utilities

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.apexracing.models.Constructor
import com.example.apexracing.models.Driver

object DBData {


    var drivers: List<Driver> = emptyList()
        private set
    var teams: List<Constructor> = emptyList()
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
                        position = (doc.getLong("position") ?: 0L).toInt()
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
                        position = (doc.getLong("position") ?: 0L).toInt()
                    ).build()
                }.sortedBy { it.position }

                teamMap = teams.associateBy { it.id }
            }

        return Tasks.whenAllSuccess<Any>(driversTask, teamsTask)
            .continueWith { t ->
                if (!t.isSuccessful) {
                    Log.e("DBData", "Preload failed", t.exception)
                    throw t.exception ?: Exception("Preload failed")
                }
                null
            }
    }

}