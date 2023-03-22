package nferno1.homework_13.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MyMainViewModel : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Success)
    val state = _state.asStateFlow()

    private val _error: Channel<String> = Channel<String>()
    val error: Flow<String> = _error.receiveAsFlow()

    fun onSignInClick(textSearch: String) {
        viewModelScope.launch {
            if (textSearch.length <= 3) {
                _state.value = State.Error("недостаточно символов для поиска")
                _error.send("недостаточно символов для поиска")
            } else
                _state.value = State.Loading
            delay(5000)
            _state.value = State.Success
        }
    }
}