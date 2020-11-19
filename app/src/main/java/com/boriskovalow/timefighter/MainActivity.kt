package com.boriskovalow.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var score : Int = 0
    private var gameStarted: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown: Long = 60000
    private val countDownInterval: Long = 1000
    private var timeLeftOnTimer: Long = 60000

    companion object{
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
        private const val GAME_STATE_KEY = "GAME_STATE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState != null)
        {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            gameStarted = savedInstanceState.getBoolean(GAME_STATE_KEY)

            if(gameStarted)
                restoreGame()
            else
                resetGame()
        }
        else {
            resetGame()
        }

        startGame()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.actionAbout){
            showInfo()
        }
        return true
    }

    private fun showInfo(){
        val dialogTitle = getString(R.string.aboutTitle)
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        outState.putBoolean(GAME_STATE_KEY, gameStarted)

        countDownTimer.cancel()
    }

    private fun resetGame(){
        score = 0
        gameScoreTextView.text = getString(R.string.score, score)

        tapMeButton.text = getString(R.string.start)

        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object  : CountDownTimer(initialCountDown, countDownInterval){

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun restoreGame(){
        gameScoreTextView.text = getString(R.string.score, score)
        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object  : CountDownTimer(timeLeftOnTimer, countDownInterval){

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }

    private fun startGame(){

        tapMeButton.setOnClickListener(){

            if(!gameStarted) {
                tapMeButton.text = getString(R.string.tapMe)
                countDownTimer.start()
                gameStarted = true

            }
            else {
                val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
                it.startAnimation(bounceAnimation)
                incrementScore()
            }

        }
    }

    private fun incrementScore(){

        score+= 1
        val newScore = getString(R.string.score, score)
        gameScoreTextView.text = newScore

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        gameScoreTextView.startAnimation(blinkAnimation)
    }

    private fun endGame(){
//        Toast.makeText(this, getString(R.string.gameOverScoreMessage, score), Toast.LENGTH_LONG).show()
//        resetGame()

        val dialogTitle = getString(R.string.gameOverMessage)
        val dialogMessage = getString(R.string.gameOverScoreMessage, score)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
        resetGame()

    }

}
