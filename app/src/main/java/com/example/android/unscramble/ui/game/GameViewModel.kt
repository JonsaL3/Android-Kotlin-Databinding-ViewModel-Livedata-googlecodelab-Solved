package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // variables que necesito para el juego
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    // Dentro del viewmodel, mis datos deben de ser editables, por lo que deben de ser var
    private var _score = MutableLiveData<Int>(0)
    private var _currentWordCount = MutableLiveData<Int>(0)
    private val _currentScrambledWord = MutableLiveData<String>() // la inicio tardiamente porque se asignara según
    // se ejecute getNextWord(), y este se ejecuta en un bloque init asique...

    // pero fuera de este debo de leerlos simplemente, por lo que deben de ser val, por lo que
    // tendré que hacer uso de la propiedad de copia de seguridad

    /*fun setScoreData(score: Int) {
        _score.postValue(score)
    }*/

    val score: LiveData<Int>
        get() = _score

    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    // de esta forma, de puertas para afuera de la clase GameViewModel solo puedo leer, pero dentro
    // de esta puedo leer y escribir

    // ahora para poder ver cuando se crea el view model y cuando muere creo un par de funciones que
    // me lo chiven

    init {
        Log.d("::GameViewModel::", "GameViewModel created!")

        currentWord = ""

        // pido la primera palabra
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("::GameViewModel::", "GameViewModel destroyed!")
    }

    // metodo get next word
    private fun getNextWord() {

        // obtengo la palabra de la lista (esto podría ser una petición a una base de datos)
        currentWord = allWordsList.random()

        // convierto en un array de caracteres esa palabra para poder desordenarlo
        val charArray = currentWord.toCharArray()
        charArray.shuffle()

        // en caso de que la palabra desordenada coincida, la desordeno de nuevo
        while (String(charArray) == currentWord) {
            charArray.shuffle()
        }

        // en caso de que la palabra ya existe, pido otra palabra distinta de forma recursiva
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            // si no la he jugado ya, la añado a la lista de palabras jugadas, e incremento el contador
            _currentScrambledWord.value = String(charArray)
            _currentWordCount.value = _currentWordCount.value?.inc() // patata++ es lo mismo que patata = patata + 1
            wordsList.add(currentWord)
        }

    }

    // esta función será accesible desde fuera y generará la siguiente palabra
    // en caso de que no haya llegado al maximo de palabras por partida.
    fun nextWord(): Boolean {

        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }

    }

    // incremento la puntuación dentro de mi view model
    private fun increaseScore() {
        _score.value = _score.value?.plus(1)
    }

    // compruebo que si la palabra está bien escrita, incremento la puntuación y pido otra palabra
    fun isUserWordCorrect(palabraJugador: String): Boolean {
        if (palabraJugador.equals(currentWord, true)) {
            increaseScore()
            return true
         } else {
             return false
        }
    }

}