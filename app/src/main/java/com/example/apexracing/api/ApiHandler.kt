package com.example.apexracing.api

class ApiHandler private constructor() {

    companion object {
        @Volatile
        private var instance: ApiHandler? = null

        fun init(): ApiHandler {
            return instance ?: synchronized(this) {
                instance ?: ApiHandler()
                    .also { instance = it }

            }
        }

        fun getInstance(): ApiHandler {
            return instance ?: throw IllegalStateException(
                "ApiHandler must be initialized by calling init() before use."
            )
        }

    }
}