package nferno1.homework_13.ui.main

sealed class State {
    object Loading: State()
    object Success: State()
    data class Error(val msg: String): State()
}
