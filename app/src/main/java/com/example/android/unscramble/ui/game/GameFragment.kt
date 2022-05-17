/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment where the game is played, contains the game logic.
 */
class GameFragment : Fragment() {

    // ESTOS DATOS LOS DELEGO AL VIEW MODEL
    /*private var score = 0
    private var currentWordCount = 0
    private var currentScrambledWord = "test"*/

    // IMPLEMENTO MI VIEW MODEL
    private val viewModel: GameViewModel by viewModels()
    // var crea los getter y los setters de manera automatica
    // val crea solo los getter de manera automatica
    // con by puedo delegar los getter y los setter a otra clase
    // si no le indicase el by (y usase el constructor por defecto de GameViewModel)
    // , al rotar el movil se resetearia tod-o, y no queremos que ocurra eso

    // es importante separar los datos de la parte grafica
    // datos == viewmodel
    // grafica == fragments / activities


    // Binding object instance with access to the views in the game_fragment.xml layout
    private lateinit var binding: GameFragmentBinding

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment

    override fun onCreateView( // onCreateView es el metodo que se llama cuando se crea el fragment
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        // CON DATA BINDING UTIL CONSIGO LO CONTRARIO A VIEWbinding, que es acceder al codigo
        // desde el xml, y no al reves como hace view binding
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        // Aqui podré comprobar cada vez cuando se recrea el fragment
        Log.d(":::", "Fragmento del juego recreado!")

        // muestro los datos de la partida cada vez que se ejecuta esta función
        Log.d("GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")

        return binding.root
    }

    // reescribo on detach para ver cuando se destruye el fragment
    override fun onDetach() { // on detach se ejecuta cuando se destruye el fragment
        super.onDetach()
        Log.d(":::", "Fragmento del juego destruido!")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inicializo las variables que he creado en el xml
        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS

        // le especifico al binding que el layout tiene que ser el mismo que el que esta en el xml
        binding.lifecycleOwner = viewLifecycleOwner

        // Setup a click listener for the Submit and Skip buttons.
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }
        // Update the UI
        //updateNextWordOnScreen()
        /*binding.score.text = getString(R.string.score, 0)
        binding.wordCount.text = getString(
                R.string.word_count, 0, MAX_NO_OF_WORDS)*/

        // según cambie mi dato en el viewmodel, se actualizará la interfaz
        /*viewModel.currentScrambledWord.observe(viewLifecycleOwner) { newWord ->
            binding.textViewUnscrambledWord.text = newWord
        }*/

        /*viewModel.score.observe(viewLifecycleOwner) { newScore ->
            binding.score.text = getString(R.string.score, newScore)
        }

        viewModel.currentWordCount.observe(viewLifecycleOwner) { newWordCount ->
            binding.wordCount.text = getString(R.string.word_count, newWordCount, MAX_NO_OF_WORDS)
        }*/

    }

    /*
    * Checks the user's word, and updates the score accordingly.
    * Displays the next scrambled word.
    */
    /*private fun onSubmitWord() {
        currentScrambledWord = getNextScrambledWord()
        currentWordCount++
        score += SCORE_INCREASE
        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
        binding.score.text = getString(R.string.score, score)
        setErrorTextField(false)
        updateNextWordOnScreen()
    }*/

    // Lo mismo que hay comentado pero con viewModel
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (!viewModel.nextWord()) {
                victoriaRoyale()
            }
        } else {
            setErrorTextField(true)
        }
    }

    /*
     * Skips the current word without changing the score.
     * Increases the word count.
     */
    /*private fun onSkipWord() {
        currentScrambledWord = getNextScrambledWord()
        currentWordCount++
        binding.wordCount.text = getString(R.string.word_count, currentWordCount, MAX_NO_OF_WORDS)
        setErrorTextField(false)
        updateNextWordOnScreen()
    }*/

    // lo mismo que hay comentado pero con viewmodel
    private fun onSkipWord() {

        if (viewModel.nextWord()) {
            //updateNextWordOnScreen()
        } else {
            victoriaRoyale()
        }

    }

    /*
     * Gets a random word for the list of words and shuffles the letters in it.
     */
    private fun getNextScrambledWord(): String {
        val tempWord = allWordsList.random().toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }

    /*
     * Re-initializes the data in the ViewModel and updates the views with the new data, to
     * restart the game.
     */
    private fun restartGame() {
        setErrorTextField(false)
        //updateNextWordOnScreen()
    }

    /*
     * Exits the game.
     */
    private fun exitGame() {
        activity?.finish()
    }

    /*
    * Sets and resets the text field error status.
    */

    /*
     * Displays the next scrambled word on screen.
     */
    // YA NO NECESITO ESTA FUNCIÓN PORQUE VOY A USAR LIVEDATA
    /*private fun updateNextWordOnScreen() {     // aqui le pregunto por el dato al viewmodel
        binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
    }*/

    /*
     * Creo el cuadro de diálogo de fin de la partida
     */
    private fun victoriaRoyale() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Fin de la partida")
            .setMessage("Has obtenido una puntuación de ${viewModel.score.value} puntos")
            .setPositiveButton("Salir") { _, _ ->
                restartGame()
            }
            .setNegativeButton("Reintentar") { _, _ ->
                restartGame()
            }
            .show()
    }

    /*
     * Una funciómn que ponga el input field en modo error
     */
    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }


}

