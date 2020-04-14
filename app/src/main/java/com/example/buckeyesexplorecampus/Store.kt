package com.example.buckeyesexplorecampus

class User(val id: String) {
    override fun toString(): String = id
}

class Store {
    // instance
    // TODO
    companion object {
        private var instance: Store? = null
        fun instance(): Store {
            if (instance == null) instance = Store()
            return instance as Store
        }
    }

    // user object

    private var user: User? = null

    fun setUser(id: String) {
        user = User(id)
        // TODO
    }

    fun getUser(): User? { return user }

    // landmarks
    // TODO

}